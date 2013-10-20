package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import java.util.Map;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.queries.RandomRead;
import de.s1ckboy.thesis.benchmark.titan.TitanGremlinBenchmark;

public class TitanRandomReadGremlin extends TitanGremlinBenchmark implements
	RandomRead {
    private Long nextID;

    @Override
    public void setUp() {
	super.setUp();
	GREMLIN_QUERY = "a.map()";
    }

    @Override
    public void beforeRun() {
	super.beforeRun();
	if (doWarmup()) {
	    if (r.nextBoolean()) {
		nextID = getRandomProduct();
	    } else {
		nextID = getRandomUser();
	    }
	} else {
	    nextID = ids[getCurrentRun()];
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
	bindings.put("a", graphDB.getVertex(nextID));

	// execute
	super.run();

	for (Map.Entry<String, Object> e : ((Map<String, Object>) result)
		.entrySet()) {
	    e.getKey();
	    e.getValue();
	}
    }

    @Override
    public String getName() {
	return Constants.TITAN_RANDOM_READ_GREMLIN;
    }
}
