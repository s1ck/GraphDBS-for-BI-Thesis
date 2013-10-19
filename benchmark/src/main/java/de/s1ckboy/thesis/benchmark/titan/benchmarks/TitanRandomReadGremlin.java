package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import java.util.Map;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.util.iterators.SingleIterator;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.queries.Query1;
import de.s1ckboy.thesis.benchmark.titan.TitanGremlinBenchmark;

public class TitanRandomReadGremlin extends TitanGremlinBenchmark implements
	Query1 {
    private Long nextID;

    @Override
    public void beforeRun() {
	GREMLIN_QUERY = "_().map()";
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
	pipe.setStarts(new SingleIterator<Vertex>(graphDB.getVertex(nextID)));
	for (Object o : pipe) {
	    for (Map.Entry<String, Object> e : ((Map<String, Object>) o)
		    .entrySet()) {
		e.getKey();
		e.getValue();
	    }
	}
    }

    @Override
    public String getName() {
	return Constants.TITAN_RANDOM_READ_GREMLIN;
    }
}
