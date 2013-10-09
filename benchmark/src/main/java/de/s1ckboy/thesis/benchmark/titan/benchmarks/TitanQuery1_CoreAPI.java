package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import com.tinkerpop.blueprints.Vertex;

import de.s1ckboy.thesis.benchmark.generic.benchmarks.Query1;
import de.s1ckboy.thesis.benchmark.titan.TitanBenchmark;

public class TitanQuery1_CoreAPI extends TitanBenchmark implements Query1 {

    private Long nextID;

    private Vertex v;

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
	// read node
	v = graphDB.getVertex(nextID);
	// read vertex
	for (String propKey : v.getPropertyKeys()) {
	    o = v.getProperty(propKey);
	}
	graphDB.commit();
    }

    @Override
    public String getName() {
	return "q1_coreapi";
    }
}
