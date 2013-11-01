package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import java.util.Map;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jGremlinBenchmark;
import de.s1ckboy.thesis.benchmark.queries.RandomRead;

public class Neo4jRandomReadGremlin extends Neo4jGremlinBenchmark implements
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
	bindings.put("a", g.getVertex(nextID));

	// execute
	super.run();

	for (Map.Entry<String, Object> e : ((Map<String, Object>) result)
		.entrySet()) {
	    e.getKey();
	    e.getValue();
	    // System.out.println(e.getKey());
	    // System.out.println(e.getValue());
	}
    }

    @Override
    public String getName() {
	return Constants.NEO4J_RANDOM_READ_GREMLIN;
    }

}
