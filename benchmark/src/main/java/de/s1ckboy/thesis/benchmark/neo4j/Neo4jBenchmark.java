package de.s1ckboy.thesis.benchmark.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.tooling.GlobalGraphOperations;

import de.s1ckboy.thesis.benchmark.Benchmark;
import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.benchmark.Constants;

public abstract class Neo4jBenchmark extends Benchmark {
    /**
     * Indexes nodes by Constants.KEY_NODE_EDGE_ID
     */
    protected Index<Node> nodeIdx;
    /**
     * Neo4j instance
     */
    protected GraphDatabaseService graphDB;
    /**
     * Needed for embedded Cypher execution
     */
    protected ExecutionEngine engine;
    /**
     * Used during warmup and for random selection
     */
    protected List<Long> reviewIDs;
    /**
     * Used during runs
     */
    protected Transaction tx;

    @Override
    public void setUp() {
	cfg = Configs.get(Neo4jConstants.INSTANCE_NAME);
	// graph database instance
	graphDB = Neo4jHelper.getGraphDB(cfg);
	// cypher execution engine
	engine = new ExecutionEngine(graphDB);
	// get index (must happen in tx)
	Transaction tx = null;
	try {
	    tx = graphDB.beginTx();
	    nodeIdx = graphDB.index().forNodes(Neo4jConstants.NODE_IDX_NAME);
	    tx.success();
	} finally {
	    if (tx != null) {
		tx.finish();
	    }
	}
	reviewIDs = new ArrayList<Long>();
	super.setUp();
    }

    @Override
    public void tearDown() {
	Neo4jHelper.closeGraphDB();
    }

    @Override
    public void beforeRun() {
	tx = graphDB.beginTx();
    }

    @Override
    public void afterRun() {
	tx.finish();
    }

    @Override
    public void warmup() {
	log.info("Warming up the caches ...");
	String type = null;
	Transaction tx = null;
	try {
	    tx = graphDB.beginTx();
	    for (Node v : GlobalGraphOperations.at(graphDB).getAllNodes()) {
		if (v.hasProperty(Constants.KEY_NODE_EDGE_TYPE)) {
		    type = (String) v.getProperty(Constants.KEY_NODE_EDGE_TYPE);
		    if (type.equals(Constants.VALUE_TYPE_GROUP)) {
			groupIDs.add(v.getId());
		    } else if (type.equals(Constants.VALUE_TYPE_PRODUCT)) {
			productIDs.add(v.getId());
		    } else if (type.equals(Constants.VALUE_TYPE_USER)) {
			userIDs.add(v.getId());
		    }
		}
	    }
	    for (Relationship e : GlobalGraphOperations.at(graphDB)
		    .getAllRelationships()) {
		type = e.getType().name();
		if (type.equals(Constants.LABEL_EDGE_REVIEWED_BY)) {
		    reviewIDs.add(e.getId());
		}
	    }
	    tx.success();
	} finally {
	    tx.finish();
	}
	log.info("Products: " + productIDs.size());
	log.info("Groups: " + groupIDs.size());
	log.info("Users: " + userIDs.size());
	log.info("Reviews: " + reviewIDs.size());

	log.info("done");
    }

    @Override
    public String getDatabaseName() {
	return Neo4jConstants.INSTANCE_NAME;
    }
}