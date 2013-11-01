package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import java.util.Map;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jCypherBenchmark;
import de.s1ckboy.thesis.benchmark.queries.FoafReviews;

public class Neo4jFoafReviewsCypher extends Neo4jCypherBenchmark implements
	FoafReviews {

    @Override
    public void beforeRun() {
	super.beforeRun();
	if (doWarmup()) {
	    nextID = getRandomUser();
	} else {
	    // TODO
	}
	CYPHER_QUERY = String
		.format("START n=node(%d) MATCH n-[:%s*1..2]-()<-[r:%s]-p RETURN p.title AS title, avg(r.rating) AS weight ORDER BY weight DESC;",
			nextID, Constants.LABEL_EDGE_FRIEND_OF,
			Constants.LABEL_EDGE_REVIEWED_BY);
    }

    @Override
    public void run() {
	super.run();
	for (Map<String, Object> e : result) {
	    // e.get("weight");
	    // e.get("title");
	    System.out.println(e.get("weight"));
	    System.out.println(e.get("title"));
	}
    }

    @Override
    public String getName() {
	return Constants.NEO4J_FOAF_REVIEWS_CYPHER;
    }
}
