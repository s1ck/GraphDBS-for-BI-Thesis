package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.queries.FoafReviews;
import de.s1ckboy.thesis.benchmark.titan.TitanGremlinBenchmark;

public class TitanFoafReviewsGremlin extends TitanGremlinBenchmark implements
	FoafReviews {

    @Override
    public void setUp() {
	super.setUp();
	GREMLIN_QUERY = String
		.format("a.both('%s').both('%s').inE('%s').groupBy{it.outV.next().title}{it.rating}{it.sum() * 1.0 / it.size()}.cap.orderMap(T.decr)",
			Constants.LABEL_EDGE_FRIEND_OF,
			Constants.LABEL_EDGE_FRIEND_OF,
			Constants.LABEL_EDGE_REVIEWED_BY);
    }

    @Override
    public void beforeRun() {
	super.beforeRun();
	if (doWarmup()) {
	    nextID = getRandomUser();
	} else {
	    // TODO
	}
    }

    @SuppressWarnings({ "unused", "unchecked" })
    @Override
    public void run() {
	bindings.put("a", graphDB.getVertex(nextID));

	// execute the query
	super.run();

	for (String title : (Iterable<String>) result) {
	}
    }

    @Override
    public String getName() {
	return Constants.TITAN_FOAF_REVIEWS_GREMLIN;
    }
}
