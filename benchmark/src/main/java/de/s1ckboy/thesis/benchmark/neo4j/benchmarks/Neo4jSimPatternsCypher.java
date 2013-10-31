package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import java.util.Map;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jCypherBenchmark;
import de.s1ckboy.thesis.benchmark.queries.SimPattern;

public class Neo4jSimPatternsCypher extends Neo4jCypherBenchmark implements
	SimPattern {

    private Long userID;

    private Integer commonProductsCnt = 1;

    @Override
    public void beforeRun() {
	super.beforeRun();
	if (doWarmup()) {
	    userID = getRandomUser();
	} else {
	    // TODO
	}

	CYPHER_QUERY = String
		.format("START user=node(%d) "
			+ "MATCH user-[:%s]-friends1<-[:%s]-products, "
			+ "products-[:%s]->friends2 "
			+ "WITH user, products, collect(distinct friends1.__id__) as f1, "
			+ "collect(distinct friends2.__id__) as f2 "
			+ "WITH user, products, filter(x in f1 : x in f2) as intersect "
			+ "WITH user, products, count(products) as n, intersect, length(intersect) as intersect_cnt "
			+ "WHERE intersect_cnt > 1 and n >= %d "
			+ "RETURN user.__id__ as user_id, n, products.__id__ as product_id, intersect as friends, intersect_cnt;",
			userID, Constants.LABEL_EDGE_FRIEND_OF,
			Constants.LABEL_EDGE_REVIEWED_BY,
			Constants.LABEL_EDGE_REVIEWED_BY, commonProductsCnt);
    }

    public void run() {
	super.run();
	for (Map<String, Object> e : result) {
//	    System.out.println(e.get("user_id"));
//	    System.out.println(e.get("product_id"));
//	    System.out.println(e.get("friends"));
//	    System.out.println(e.get("intersect_cnt"));

	    e.get("user_id");
	    e.get("product_id");
	    e.get("friends");
	    e.get("intersect_cnt");
	}
    }

    @Override
    public String getName() {
	return Constants.NEO4J_SIM_PATTERNS_CYPHER;
    }
}
