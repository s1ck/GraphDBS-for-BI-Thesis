package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import de.s1ckboy.thesis.benchmark.generic.benchmarks.Query1;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jBenchmark;

public class Neo4jQuery1_CoreAPI extends Neo4jBenchmark implements Query1 {

    private long nextID;
    
    private Node v;
    
    @SuppressWarnings("unused")
    private Object o;

    @Override
    public void beforeRun() {
	super.beforeRun();
	if (r.nextBoolean()) {
	    nextID = getRandomProduct();
	} else {
	    nextID = getRandomUser();
	}
    }

    @Override
    public void run() {
	Transaction tx = null;
	try {
	    tx = graphDB.beginTx();
	    // read node
	    v = graphDB.getNodeById(nextID);
	    // read properties
	    for (String propKey : v.getPropertyKeys()) {
		o = v.getProperty(propKey);
	    }
	    tx.success();
	} finally {
	    tx.finish();
	}
    }

    @Override
    public String getName() {
	return "q1_coreapi";
    }
}
