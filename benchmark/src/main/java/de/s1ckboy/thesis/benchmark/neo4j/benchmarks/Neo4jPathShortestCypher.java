package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import java.util.Iterator;
import java.util.List;

import org.neo4j.helpers.collection.IteratorUtil;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jCypherBenchmark;
import de.s1ckboy.thesis.benchmark.queries.PathShortest;

public class Neo4jPathShortestCypher extends Neo4jCypherBenchmark implements
	PathShortest {
    private Long fromUserID;
    private Long toUserID;
    private Integer pathLength = 4;

    @Override
    public void beforeRun() {
	super.beforeRun();
	if (doWarmup()) {
	    fromUserID = getRandomUser();
	    toUserID = getRandomUser();
	} else {
	    // TODO
	}
	CYPHER_QUERY = String
		.format("START a=node(%d), b=node(%d) MATCH p=shortestPath(a-[:%s|:%s|:%s*..%d]-b) RETURN EXTRACT(n in NODES(p): n.__id__) AS path;",
			fromUserID, toUserID, Constants.LABEL_EDGE_FRIEND_OF,
			Constants.LABEL_EDGE_SIMILAR_TO,
			Constants.LABEL_EDGE_REVIEWED_BY,
			pathLength);
    }
    
    @SuppressWarnings("unused")
    @Override
    public void run() {
	super.run(); 
	Iterator<List<String>> paths = result.columnAs("path");
	for (List<String> path : IteratorUtil.asIterable(paths)) {
//	    System.out.println(StringUtils.join(path, ','));
	}
    }

    @Override
    public String getName() {
	// TODO Auto-generated method stub
	return Constants.NEO4J_PATH_SHORTEST_CYPHER;
    }
}
