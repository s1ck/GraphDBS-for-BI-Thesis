package de.s1ckboy.thesis.tooling;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.helpers.collection.IteratorUtil;

import com.google.gson.Gson;

import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jConstants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jHelper;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jRelationshipTypes;
import de.s1ckboy.thesis.generic.Constants;

/**
 * This class extract subgraphs from the full amazon-pokec-graph.
 * 
 * @author Martin Junghanns
 * 
 */
public class SubgraphExtraction {
    private static Logger log = Logger.getLogger(SubgraphExtraction.class);

    private Configuration cfg;
    private Index<Node> index;
    private GraphDatabaseService graphDb;

    private Set<Node> nodes;
    private Set<Relationship> edges;

    private Gson gson;

    private int groupCnt = 0;
    private int productCnt = 0;
    private int userCnt = 0;

    private int similarCnt = 0;
    private int reviewCnt = 0;
    private int friendCnt = 0;

    private boolean showLog = false;

    /**
     * Graph settings
     */
    /*
     * Maximum number of products inside the graph
     */
    private int nodeLimit = 1000;
    /*
     * k-Products/Users means the k-neighborhood of a node. 2-neighborhood for
     * users is p.e. all friends and their friends
     */
    private int k_Products = 4;
    private int k_Users = 1;

    private Random r;
    long seed = 1337L;

    public SubgraphExtraction() {
	cfg = Configs.get(Neo4jConstants.INSTANCE_NAME);
	graphDb = Neo4jHelper.getGraphDB(cfg);
	// loading idx must happen in tx oO
	Transaction tx = graphDb.beginTx();
	try {
	    index = graphDb.index().forNodes(Neo4jConstants.NODE_IDX_NAME);
	    tx.success();
	} finally {
	    tx.finish();
	}
	r = new Random(seed);
	nodes = new HashSet<Node>();
	edges = new HashSet<Relationship>();
	gson = new Gson();
    }

    public static void main(String[] args) {
	new SubgraphExtraction().run();
    }

    public void run() {
	Transaction tx = graphDb.beginTx();
	try {
	    extractSubgraphs(getGroupCounts());
	    log.info(String
		    .format("\n%d nodes\n%d edges\n---\n%d groups\n%d products\n%d users\n%d similar_to\n%d reviewed_by\n%d friend_of",
			    nodes.size(), edges.size(), groupCnt, productCnt,
			    userCnt, similarCnt, reviewCnt, friendCnt));
	    storeSubgraph();
	    tx.success();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    tx.finish();
	}
    }

    private void extractSubgraphs(int[] groupCounts) {
	// while (nodes.size() < nodeLimit) {
	while (productCnt < nodeLimit) {
	    int n = r.nextInt(getSum(groupCounts) + 1);
	    if (showLog) {
		log.info("random number: " + n);
	    }
	    int groupId = 0;
	    int upperLimit = groupCounts[groupId];
	    // select associated group
	    while (n > upperLimit) {
		upperLimit += groupCounts[++groupId];
	    }
	    // second parameter calculates the relative index inside the group
	    extractSubgraph(groupId, (groupId > 0) ? n
		    % (upperLimit - groupCounts[groupId]) : n);
	    if (showLog) {
		log.info("---");
	    }
	}
    }

    private void extractSubgraph(int groupId, int nodeIdx) {
	Node group = getNode(Constants.VALUE_TYPE_GROUP + "_"
		+ Integer.toString(groupId));
	Node product = null;
	int i = 0;
	for (Relationship e : group.getRelationships(Direction.INCOMING,
		Neo4jRelationshipTypes.BELONGS_TO)) {
	    if (nodeIdx == i++) {
		// it's directed towards the group, so the start node is
		// necessary
		product = e.getStartNode();
		edges.add(e);
	    }
	}

	if (!nodes.contains(group)) {
	    nodes.add(group);
	    groupCnt++;
	}
	if (!nodes.contains(product)) {
	    nodes.add(product);
	    productCnt++;
	    buildProductSubgraph(product, k_Products);
	}
    }

    private void buildProductSubgraph(Node product, int depth) {
	if (showLog) {
	    String tabs = buildTabString(k_Products - depth);
	    log.info(tabs + "product: " + product);
	    log.info(tabs + "product title: " + product.getProperty("title"));
	    log.info(tabs
		    + "similar count: "
		    + IteratorUtil.count(product.getRelationships(
			    Direction.OUTGOING,
			    Neo4jRelationshipTypes.SIMILAR_TO)));
	    log.info(tabs
		    + "review count: "
		    + IteratorUtil.count(product.getRelationships(
			    Direction.OUTGOING,
			    Neo4jRelationshipTypes.REVIEWED_BY)));
	}

	Node endNode = null;
	if (depth > 0) {

	    // build product similarity subgraph
	    for (Relationship e : product.getRelationships(Direction.OUTGOING,
		    Neo4jRelationshipTypes.SIMILAR_TO)) {
		edges.add(e);
		similarCnt++;
		endNode = e.getEndNode();
		if (!nodes.contains(endNode)) { // avoid cycles
		    // store neighbor
		    nodes.add(endNode);
		    productCnt++;
		    // go deeper into the graph
		    buildProductSubgraph(endNode, depth - 1);
		}
	    }
	}

	// build user subtree
	for (Relationship e : product.getRelationships(Direction.OUTGOING,
		Neo4jRelationshipTypes.REVIEWED_BY)) {
	    // store review edge
	    edges.add(e);
	    reviewCnt++;
	    endNode = e.getEndNode();
	    if (!nodes.contains(endNode)) {
		// store neighbor
		nodes.add(endNode);
		userCnt++;
		// build user subgraph
		buildUserSubgraph(endNode, k_Users);
	    }
	}
    }

    private void buildUserSubgraph(Node user, int depth) {
	if (showLog) {
	    String tabs = buildTabString(k_Products - depth);
	    log.info(tabs + "user: " + user);
	    log.info(tabs
		    + "friend_of count: "
		    + IteratorUtil.count(user.getRelationships(
			    Direction.OUTGOING,
			    Neo4jRelationshipTypes.FRIEND_OF)));
	}

	if (depth > 0) {
	    Node endNode = null;
	    for (Relationship e : user.getRelationships(Direction.OUTGOING,
		    Neo4jRelationshipTypes.FRIEND_OF)) {
		edges.add(e);
		friendCnt++;
		endNode = (e.getEndNode().equals(user)) ? e.getStartNode() : e
			.getEndNode();
		if (!nodes.contains(endNode)) {
		    nodes.add(endNode);
		    userCnt++;
		    buildUserSubgraph(endNode, depth - 1);
		}
	    }
	}
    }

    private String buildTabString(int cnt) {
	String s = "";
	for (int i = 0; i < cnt; i++) {
	    s += "\t";
	}
	return s;
    }

    private Node getNode(String id) {
	return index.get(Constants.KEY_NODE_EDGE_ID, id).getSingle();
    }

    private int[] getGroupCounts() {
	int[] groupCounts = new int[10];
	Node groupNode;
	// iterate all groups (0-9)
	for (int i = 0; i < 10; i++) {
	    // get the group from the index
	    groupNode = getNode(Constants.VALUE_TYPE_GROUP + "_"
		    + Integer.toString(i));
	    // count the number of associated products
	    groupCounts[i] = IteratorUtil.count(groupNode.getRelationships(
		    Direction.INCOMING, Neo4jRelationshipTypes.BELONGS_TO));

	    if (showLog) {
		log.info(String.format("%s (%d)",
			groupNode.getProperty("name"), groupCounts[i]));
	    }
	}
	return groupCounts;
    }

    private int getSum(int[] f) {
	int sum = 0;
	for (int i = 0; i < f.length; i++) {
	    sum += f[i];
	}
	return sum;
    }

    private void storeSubgraph() throws IOException {
	String graphName = String.format("graph_%d_%d_%d.geoff", nodeLimit,
		k_Products, k_Users);

	BufferedWriter bw = null;
	bw = new BufferedWriter(new FileWriter(graphName));

	for (Node n : nodes) {
	    bw.write(getGeoffNodeString(n) + "\n");
	}

	for (Relationship e : edges) {
	    bw.write(getGeoffEdgeString(e) + "\n");
	}

	bw.close();
    }

    private String getGeoffNodeString(Node n) {
	return String.format("(%s) %s",
		n.getProperty(Constants.KEY_NODE_EDGE_ID),
		getPropertiesString(n));
    }

    private String getGeoffEdgeString(Relationship e) {
	return String.format("(%s)-[:%s]->(%s) %s", e.getStartNode()
		.getProperty(Constants.KEY_NODE_EDGE_ID), e.getType().name(), e
		.getEndNode().getProperty(Constants.KEY_NODE_EDGE_ID),
		getPropertiesString(e));
    }

    private String getPropertiesString(PropertyContainer e) {
	Map<String, Object> props = new HashMap<String, Object>();

	for (String key : e.getPropertyKeys()) {
	    props.put(key, e.getProperty(key));
	}
	return gson.toJson(props);
    }
}
