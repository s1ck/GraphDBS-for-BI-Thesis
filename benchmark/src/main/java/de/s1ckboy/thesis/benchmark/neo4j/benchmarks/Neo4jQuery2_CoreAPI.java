package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import de.s1ckboy.thesis.benchmark.generic.benchmarks.Query2;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jBenchmark;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jRelationshipTypes;
import de.s1ckboy.thesis.generic.Constants;

public class Neo4jQuery2_CoreAPI extends Neo4jBenchmark implements Query2 {

    private Long nextID;

    @Override
    public void beforeRun() {
	super.beforeRun();
	nextID = getRandomProduct();
    }

    @Override
    public void run() {
	// TODO Auto-generated method stub
	Transaction tx = graphDB.beginTx();
	try {
	    runInternal();
	    tx.success();
	} finally {
	    tx.finish();
	}
    }

    private void runInternal() {
	Node v = graphDB.getNodeById(nextID);
	for (Relationship e1 : v.getRelationships(
		Neo4jRelationshipTypes.SIMILAR_TO, Direction.BOTH)) {
	    log.info(e1.getStartNode().getProperty(Constants.KEY_PRODUCT_TITLE));
	    log.info(e1.getEndNode().getProperty(Constants.KEY_PRODUCT_TITLE));
	    for (Relationship e2 : e1.getEndNode().getRelationships(
		    Neo4jRelationshipTypes.SIMILAR_TO, Direction.OUTGOING)) {
		log.info(e2.getEndNode().getProperty(
			Constants.KEY_PRODUCT_TITLE));
	    }
	}
    }

    @Override
    public String getName() {
	return "q2_coreapi";
    }
}
