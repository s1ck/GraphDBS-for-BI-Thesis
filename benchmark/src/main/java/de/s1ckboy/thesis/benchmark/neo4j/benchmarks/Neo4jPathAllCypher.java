package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import java.util.Map;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jCypherBenchmark;
import de.s1ckboy.thesis.benchmark.queries.PathAll;

public class Neo4jPathAllCypher extends Neo4jCypherBenchmark implements PathAll {

    private Long userID;
    private Long productID;

    @Override
    public void beforeRun() {
	super.beforeRun();
	if (doWarmup()) {
	    userID = getRandomUser();
	    productID = getRandomProduct();
	} else {
	    // TODO
	}
	CYPHER_QUERY = String
		.format("START a=node(%d), b=node(%d) MATCH p=a-[:%s|%s|%s*..4]-b RETURN length(p) AS length, count(p) AS cnt;",
			userID, productID, Constants.LABEL_EDGE_FRIEND_OF,
			Constants.LABEL_EDGE_SIMILAR_TO,
			Constants.LABEL_EDGE_REVIEWED_BY);
    }

    @Override
    public void run() {
	super.run();
	for (Map<String, Object> e : result) {
	    e.get("length");
	    e.get("cnt");
	}
    }

    @Override
    public String getName() {
	return Constants.NEO4J_PATH_ALL_CYPHER;
    }
}