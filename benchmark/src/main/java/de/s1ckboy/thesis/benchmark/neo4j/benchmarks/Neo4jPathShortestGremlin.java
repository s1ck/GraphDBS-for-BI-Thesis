package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.tinkerpop.blueprints.Vertex;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jGremlinBenchmark;
import de.s1ckboy.thesis.benchmark.queries.PathShortest;

public class Neo4jPathShortestGremlin extends Neo4jGremlinBenchmark implements
	PathShortest {
    /*
     * start node in paths
     */
    private Long fromUserID;
    /*
     * end node in paths
     */
    private Long toUserID;

    @Override
    public void setUp() {
	super.setUp();
	GREMLIN_QUERY = String
		.format("a.both('%s', '%s', '%s').except(visited).store(visited).loop(3){it.object != b && it.loops < 5}.retain([b]).path{it.__id__}",
			Constants.LABEL_EDGE_FRIEND_OF,
			Constants.LABEL_EDGE_REVIEWED_BY,
			Constants.LABEL_EDGE_SIMILAR_TO);
    }

    @Override
    public void beforeRun() {
	super.beforeRun();
	if (doWarmup()) {
	    fromUserID = getRandomUser();
	    toUserID = getRandomUser();
	} else {
	    // TODO
	}
    }

    @SuppressWarnings({ "unchecked", "unused" })
    @Override
    public void run() {
	Vertex a = g.getVertex(fromUserID);
	List<Vertex> visited = new ArrayList<>();
	visited.add(a);
	bindings.put("a", a);
	bindings.put("b", g.getVertex(toUserID));
	bindings.put("visited", visited);

	// execute the query
	super.run();

	for (List<String> path : (Iterable<List<String>>) result) {
	    // System.out.println(StringUtils.join(path, ","));
	}
    }

    @Override
    public String getName() {
	return Constants.NEO4J_PATH_SHORTEST_GREMLIN;
    }

}
