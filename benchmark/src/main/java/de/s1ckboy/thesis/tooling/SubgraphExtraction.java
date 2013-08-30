package de.s1ckboy.thesis.tooling;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.helpers.collection.IteratorUtil;

import de.s1ckboy.thesis.benchmark.Configuration;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jConstants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jHelper;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jRelationshipTypes;

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
    private ExecutionEngine engine;

    private Set<Node> nodes;
    private Set<Relationship> edges;

    private int groupCnt = 0;
    private int productCnt = 0;
    private int userCnt = 0;

    private int similarCnt = 0;
    private int reviewCnt = 0;
    private int friendCnt = 0;

    /**
     * k is regarding to the k-neighborhood of a node
     * 
     * k_Users = 2 means that the user, his friends and friends of friends are
     * collected
     */
    private int k_Products = 1;
    private int k_Users = 0;

    private Random r;
    long seed = 1337L;

    public SubgraphExtraction() {
	cfg = Configuration.getInstance("neo4j");
	graphDb = Neo4jHelper.getConnection(cfg);
	engine = new ExecutionEngine(graphDb);
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
			    nodes.size(), edges.size(), groupCnt, productCnt, userCnt,
			    similarCnt, reviewCnt, friendCnt));
	    tx.success();
	} finally {
	    tx.finish();
	}
    }

    private void extractSubgraphs(int[] groupCounts) {
	while (nodes.size() < 100) {
	    int n = r.nextInt(getSum(groupCounts) + 1);
	    log.info("random number: " + n);
	    int groupId = 0;
	    int upperLimit = groupCounts[groupId];
	    // select associated group
	    while (n > upperLimit) {
		upperLimit += groupCounts[++groupId];
	    }
	    // second parameter calculates the relative index inside the group
	    extractSubgraph(groupId, (groupId > 0) ? n
		    % (upperLimit - groupCounts[groupId]) : n);
	    log.info("---");
	}
    }

    private void extractSubgraph(int groupId, int nodeIdx) {
	Node group = getNode(Integer.toString(groupId));
	Node product = null;
	int i = 0;
	for (Relationship e : group.getRelationships(Direction.INCOMING,
		Neo4jRelationshipTypes.BELONGS_TO)) {
	    if (nodeIdx == i++) {
		// it's directed towards the group, so the start node is
		// necessary
		product = e.getStartNode();
	    }
	}
	// log.info("associated group: " + group.getProperty("name"));
	// log.info("node idx: " + nodeIdx);

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
	// String tabs = buildTabString(k_Products - depth);
	// log.info(tabs + "product: " + product);
	// log.info(tabs + "product title: " + product.getProperty("title"));
	// log.info(tabs
	// + "similar count: "
	// + IteratorUtil.count(product.getRelationships(
	// Direction.OUTGOING, Neo4jRelationshipTypes.SIMILAR_TO)));
	// log.info(tabs
	// + "review count: "
	// + IteratorUtil.count(product.getRelationships(
	// Direction.OUTGOING, Neo4jRelationshipTypes.REVIEWED_BY)));

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
	String tabs = buildTabString(k_Products - depth);
	log.info(tabs + "user: " + user);
	log.info(tabs
		+ "friend_of count: "
		+ IteratorUtil.count(user.getRelationships(Direction.OUTGOING,
			Neo4jRelationshipTypes.FRIEND_OF)));

	if (depth > 0) {
	    Node endNode = null;
	    for (Relationship e : user.getRelationships(Direction.BOTH,
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
	return index.get(Neo4jConstants.NODE_IDX_ID_KEY, id).getSingle();
    }

    private int[] getGroupCounts() {
	int[] groupCounts = new int[10];
	Node groupNode;
	// iterate all groups (0-9)
	for (int i = 0; i < 10; i++) {
	    // get the group from the index
	    groupNode = getNode(Integer.toString(i));
	    // count the number of associated products
	    groupCounts[i] = IteratorUtil.count(groupNode.getRelationships(
		    Direction.INCOMING, Neo4jRelationshipTypes.BELONGS_TO));
	    log.info(String.format("%s (%d)", groupNode.getProperty("name"),
		    groupCounts[i]));
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
}
