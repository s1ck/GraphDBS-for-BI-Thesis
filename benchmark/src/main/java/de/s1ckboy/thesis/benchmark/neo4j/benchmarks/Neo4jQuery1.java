package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import org.neo4j.graphdb.Transaction;

import de.s1ckboy.thesis.benchmark.neo4j.Neo4jBenchmark;

public class Neo4jQuery1 extends Neo4jBenchmark {

    private long nextID;

    public Neo4jQuery1(int runs) {
	this.setRuns(runs);
    }

    @Override
    public void beforeRun() {
	nextID = getRandomVertexID();
    }

    @Override
    public void run() {
	Transaction tx = null;
	try {
	    tx = graphDB.beginTx();
	    graphDB.getNodeById(nextID);
	    tx.success();
	} finally {
	    tx.finish();
	}
    }

    @Override
    public String getName() {
	return "query1";
    }
}
