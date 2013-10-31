package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.tinkerpop.blueprints.Vertex;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.queries.PathAll;
import de.s1ckboy.thesis.benchmark.titan.TitanGremlinBenchmark;

public class TitanPathAllGremlin extends TitanGremlinBenchmark implements
	PathAll {
    /*
     * start node in paths
     */
    private Long userID;
    /*
     * end node in paths
     */
    private Long productID;

    @Override
    public void setUp() {
	super.setUp();
	GREMLIN_QUERY = String
		.format("a.both('%s','%s','%s').loop(1){it.object != b && it.loops < 5}.retain([b]).path._().transform{it.size() - 1}.groupCount().cap();",
			Constants.LABEL_EDGE_FRIEND_OF,
			Constants.LABEL_EDGE_REVIEWED_BY,
			Constants.LABEL_EDGE_SIMILAR_TO);
    }

    @Override
    public void beforeRun() {
	super.beforeRun();
	if (doWarmup()) {
	    userID = getRandomUser();
	    productID = getRandomProduct();
	} else {
	    // TODO
	}
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public void run() {
	// fetch start and end vertex inside the run method (cypher does the
	// same)
	Vertex a = graphDB.getVertex(userID);
	List<Vertex> visited = new ArrayList<>();
	visited.add(a);
	bindings.put("a", a);
	bindings.put("b", graphDB.getVertex(productID));
	bindings.put("visited", visited);

	// execute the query
	super.run();

	for (Map<Integer, Integer> o : (Iterable<Map<Integer, Integer>>) result) {
	    for (Map.Entry<Integer, Integer> e : o.entrySet()) {
		e.getKey();
		e.getValue();
//		System.out.println(e.getKey() + " => " + e.getValue());
	    }
	}
    }

    @Override
    public String getName() {
	return Constants.TITAN_PATH_ALL_GREMLIN;
    }
}
