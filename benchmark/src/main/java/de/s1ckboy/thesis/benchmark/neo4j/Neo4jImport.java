package de.s1ckboy.thesis.benchmark.neo4j;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Label;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.index.lucene.unsafe.batchinsert.LuceneBatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserterIndex;
import org.neo4j.unsafe.batchinsert.BatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import de.s1ckboy.thesis.generic.Constants;
import de.s1ckboy.thesis.generic.Edge;
import de.s1ckboy.thesis.generic.GraphElement;
import de.s1ckboy.thesis.generic.Node;
import de.s1ckboy.thesis.io.GeoffReader;
import de.s1ckboy.thesis.io.GraphElementIterator;
import de.s1ckboy.thesis.io.IOHelper;

/**
 * Neo4j Importer uses a GraphElementIterator to insert nodes into the database.
 * 
 * See {@link http://docs.neo4j.org/chunked/milestone/batchinsert.html} for
 * details on batch inserting into Neo4j.
 * 
 * Important: The import assumes, that all nodes are inserted before any edge is
 * being inserted into the database.
 * 
 * @author Martin Junghanns
 * 
 */
public class Neo4jImport extends Neo4jBenchmark {
    private BatchInserter inserter;

    private BatchInserterIndexProvider indexProvider;
    /**
     * Indexes created and populated using BatchInserterIndexs from this
     * provider are compatible with the normal Indexes.
     */
    private BatchInserterIndex nodeIdx;
    /**
     * Used to map the original ids to the created node ids. This is needed for
     * edge creation after inserting all nodes.
     */
    private Map<String, Long> nodeCache;

    private long nodeCnt = 0L;

    private long edgeCnt = 1L;

    private long missingEndNodeCnt = 0L;

    public Neo4jImport(int runs) {
	this.setRuns(runs);
    }

    @Override
    public void setUp() {
	if (cfg.getBoolean("import.drop.db")) {
	    IOHelper.removeDirectory(cfg.getString("location"));
	}

	/*
	 * Batch Inserter has to be initialized separately. This is why I
	 * reconfigure.
	 */
	Map<String, String> importCfg = new HashMap<String, String>();
	importCfg.put("neostore.nodestore.db.mapped_memory",
		cfg.getString("neostore.nodestore.db.mapped_memory"));
	importCfg.put("neostore.relationshipstore.db.mapped_memory",
		cfg.getString("neostore.relationshipstore.db.mapped_memory"));
	importCfg.put("neostore.propertystore.db.mapped_memory",
		cfg.getString("neostore.propertystore.db.mapped_memory"));
	importCfg.put("neostore.propertystore.db.strings.mapped_memory", cfg
		.getString("neostore.propertystore.db.strings.mapped_memory"));
	importCfg
		.put("neostore.propertystore.db.arrays.mapped_memory",
			cfg.getString("neostore.propertystore.db.arrays.mapped_memory"));

	// setup node cache for
	nodeCache = new HashMap<String, Long>();
	// setup batch inserter
	inserter = BatchInserters
		.inserter(cfg.getString("location"), importCfg);
	// setup batch inserter index provider
	indexProvider = new LuceneBatchInserterIndexProvider(inserter);
	// setup index to store the original node ids
	nodeIdx = indexProvider.nodeIndex(Neo4jConstants.NODE_IDX_NAME,
		MapUtil.stringMap("type", "exact"));
    }

    @Override
    public void run() {
	try {
	    GraphElementIterator it = new GraphElementIterator(
		    new BufferedReader(new FileReader(
			    cfg.getString("import.dataset.path"))), new GeoffReader());

	    GraphElement element;
	    while (it.hasNext()) {
		element = it.next();
		if (element.isNode()) {
		    storeNode((Node) element);
		} else {
		    storeRelationship((Edge) element);
		}
		// some logging
		if (nodeCnt % Neo4jConstants.NODE_LOG_CNT == 0) {
		    log.info(String.format("Stored %d nodes", nodeCnt));
		}
		if (edgeCnt % Neo4jConstants.EDGE_LOG_CNT == 0) {
		    log.info(String.format("Stored %d edges", edgeCnt));
		}
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Store a node including label and properties in Neo4j.
     * 
     * @param node
     */
    @SuppressWarnings("unchecked")
    private void storeNode(Node node) {
	Map<String, Object> properties = node.getProperties();
	if (properties.containsKey(Constants.KEY_PRODUCT_CATEGORIES)) {
	    // neo4j doesnt support ArrayList as a property Type, so I have to
	    // convert it to an array of string
	    ArrayList<String> list = (ArrayList<String>) properties
		    .get(Constants.KEY_PRODUCT_CATEGORIES);
	    properties.put(Constants.KEY_PRODUCT_CATEGORIES,
		    list.toArray(new String[list.size()]));
	}

	// get corresponding node label
	Label l = null;
	String typeValue = (String) properties
		.get(Constants.KEY_NODE_EDGE_TYPE);
	if (typeValue.equals(Constants.VALUE_TYPE_GROUP)) {
	    l = Neo4jConstants.GROUP_LABEL;
	} else if (typeValue.equals(Constants.VALUE_TYPE_PRODUCT)) {
	    l = Neo4jConstants.PRODUCT_LABEL;
	} else if (typeValue.equals(Constants.VALUE_TYPE_USER)) {
	    l = Neo4jConstants.USER_LABEL;
	}
	// remove type attribute from properties
	if(cfg.getBoolean("import.drop.type")) {
	    properties.remove(Constants.KEY_NODE_EDGE_TYPE);
	}
	// and create the node
	long nodeId = inserter.createNode(properties, l);
	nodeCache.put(node.getId(), nodeId);
	nodeIdx.add(nodeId,
		MapUtil.map(Constants.KEY_NODE_EDGE_ID, node.getId()));
	nodeCnt++;
    }

    /**
     * Stores an edge with corresponding label and properties in Neo4j.
     * 
     * @param edge
     */
    private void storeRelationship(Edge edge) {
	if (nodeCache.containsKey(edge.getToId())) {
	    inserter.createRelationship(nodeCache.get(edge.getFromId()),
		    nodeCache.get(edge.getToId()),
		    DynamicRelationshipType.withName(edge.getLabel()),
		    edge.getProperties());
	    edgeCnt++;
	} else {
	    missingEndNodeCnt++;
	}
    }

    @Override
    public String getName() {
	return "import";
    }

    @Override
    public void warmup() {
	// no need to do a warmup
    }

    @Override
    public void tearDown() {
	// flush the index to make changes visible for reads
	nodeIdx.flush();

	inserter.shutdown();

	log.info(String.format(
		"Imported %d nodes, %d edges. Got %d missing end nodes",
		nodeCnt, edgeCnt, missingEndNodeCnt));
    }
}
