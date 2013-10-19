package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.util.iterators.SingleIterator;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.queries.Query2;
import de.s1ckboy.thesis.benchmark.titan.TitanGremlinBenchmark;

public class TitanSimProductsGremlin extends TitanGremlinBenchmark implements
	Query2 {
    private Long nextID;

    @Override
    public void beforeRun() {
	GREMLIN_QUERY = String.format(
		"_().both('%s').loop(1){it.loops <= 2}.dedup().title",
		Constants.LABEL_EDGE_SIMILAR_TO);
	super.beforeRun();

	if (doWarmup()) {
	    nextID = getRandomProduct();
	} else {
	    // TODO
	}
    }

    @SuppressWarnings({ "unchecked", "unused" })
    @Override
    public void run() {
	pipe.setStarts(new SingleIterator<Vertex>(graphDB.getVertex(nextID)));
	// just iterate the resulting pipe
	for (Object o : pipe) {
	}
    }

    @Override
    public String getName() {
	return Constants.TITAN_SIM_PROUCTS_GREMLIN;
    }

    // @Override
    // public void tearDown() {
    // int min = Integer.MAX_VALUE;
    // int max = Integer.MIN_VALUE;
    // int sum = 0;
    // for (int e : resultSetSizes) {
    // if (e < min) min = e;
    // if (e > max) max = e;
    // sum += e;
    // }
    // System.out.println(min);
    // System.out.println(max);
    // System.out.println(sum * 1. / resultSetSizes.size());
    // }
}
