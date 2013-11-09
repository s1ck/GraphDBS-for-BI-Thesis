package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import java.util.Map;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jCypherBenchmark;
import de.s1ckboy.thesis.benchmark.queries.FoafReviews;

public class Neo4jFoafReviewsCypher extends Neo4jCypherBenchmark implements
	FoafReviews {
    
    private int[] resCnt = new int[1000];

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
	int i = 0;
	for (Map<String, Object> e : result) {
	    e.get("weight");
	    e.get("title");
	    // System.out.println(e.get("weight"));
	    // System.out.println(e.get("title"));
	    i++;
	}
	resCnt[getCurrentRun()] = i;
    }
    
    public void tearDown() {
	int min = Integer.MAX_VALUE;
	int max = Integer.MIN_VALUE;
	int maxIdx = -1;
	int sum = 0;
	
	for (int i = 0; i < 1000; i++) {
	    sum += resCnt[i];
	    if (resCnt[i] < min) min = resCnt[i];
	    if (resCnt[i] > max) {
		max = resCnt[i];
		maxIdx = i;
	    }
	}
	
	System.out.println("min: " + min);
	System.out.println("max: " + max);
	System.out.println("maxIdx: " + maxIdx);
	System.out.println("avg: " + (sum / 1000.0));
	
	super.tearDown();
    }

    @Override
    public String getName() {
	return Constants.NEO4J_FOAF_REVIEWS_CYPHER;
    }
}
