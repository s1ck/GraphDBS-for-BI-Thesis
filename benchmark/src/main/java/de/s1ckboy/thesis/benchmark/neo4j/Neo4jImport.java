package de.s1ckboy.thesis.benchmark.neo4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.neo4j.graphdb.DynamicRelationshipType;
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
import de.s1ckboy.thesis.io.GeoffParser;
import de.s1ckboy.thesis.io.GraphElementIterator;

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

    private String datasetPath;

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

    public Neo4jImport(String datasetPath) {
	this.datasetPath = datasetPath;
	this.nodeCache = new HashMap<String, Long>();
    }

    @Override
    public void setUp() {
	try {
	    File f = new File(cfg.getPropertyAsString("location"));
	    if (f.exists()) {
		log.info("Deleting database directory");
		FileUtils.deleteDirectory(f);
	    }
	} catch (IOException e) {
	    log.error(e);
	}
	// load config
	Map<String, String> importCfg = new HashMap<String, String>();
	importCfg.put("neostore.nodestore.db.mapped_memory",
		cfg.getPropertyAsString("neostore.nodestore.db.mapped_memory"));
	importCfg
		.put("neostore.relationshipstore.db.mapped_memory",
			cfg.getPropertyAsString("neostore.relationshipstore.db.mapped_memory"));
	importCfg
		.put("neostore.propertystore.db.mapped_memory",
			cfg.getPropertyAsString("neostore.propertystore.db.mapped_memory"));
	importCfg
		.put("neostore.propertystore.db.strings.mapped_memory",
			cfg.getPropertyAsString("neostore.propertystore.db.strings.mapped_memory"));
	importCfg
		.put("neostore.propertystore.db.arrays.mapped_memory",
			cfg.getPropertyAsString("neostore.propertystore.db.arrays.mapped_memory"));

	// setup batch inserter
	inserter = BatchInserters.inserter(cfg.getPropertyAsString("location"),
		importCfg);
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
		    new BufferedReader(new FileReader(datasetPath)),
		    new GeoffParser());

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

    @SuppressWarnings("unchecked")
    private void storeNode(Node node) {
	Map<String, Object> properties = node.getProperties();
	if (properties.containsKey(Constants.PRODUCT_CATEGORIES)) {
	    // neo4j doesnt support ArrayList as a property Type, so I have to
	    // convert it to an array of string
	    ArrayList<String> list = (ArrayList<String>) properties
		    .get(Constants.PRODUCT_CATEGORIES);
	    properties.put(Constants.PRODUCT_CATEGORIES,
		    list.toArray(new String[list.size()]));
	}
	long nodeId = inserter.createNode(node.getProperties());
	nodeCache.put(node.getId(), nodeId);
	nodeIdx.add(nodeId,
		MapUtil.map(Neo4jConstants.NODE_IDX_ID_KEY, node.getId()));
	nodeCnt++;
    }

    private void storeRelationship(Edge edge) {
	// log.info(String.format("(%s)-[:%s]->(%s)", edge.getFromId(),
	// edge.getLabel(), edge.getToId()));
	// log.info(nodeIdx == null);
	// log.info(edge == null);
	// log.info("fromId: " + edge.getFromId());
	// log.info("toId: " + edge.getToId());
	// log.info("label: " + edge.getLabel());
	// log.info(nodeIdx.get(edge.getFromId()) == null);
	// log.info(nodeIdx.get(edge.getToId()) == null);
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
