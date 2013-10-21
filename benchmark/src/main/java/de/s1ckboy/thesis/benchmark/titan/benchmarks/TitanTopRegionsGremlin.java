package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import com.tinkerpop.blueprints.Vertex;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.queries.TopRegions;
import de.s1ckboy.thesis.benchmark.titan.TitanGremlinBenchmark;

public class TitanTopRegionsGremlin extends TitanGremlinBenchmark implements
	TopRegions {

    private Vertex group;

    @Override
    public void setUp() {
	super.setUp();
	group = graphDB.getVertices(Constants.KEY_NODE_EDGE_ID, "g_0")
		.iterator().next();
	GREMLIN_QUERY = String
		.format("group.in('%s').filter{it.salesrank < 500000}.out('%s').groupCount{it.region}.cap().orderMap(T.decr)[0..9]",
			Constants.LABEL_EDGE_BELONGS_TO,
			Constants.LABEL_EDGE_REVIEWED_BY);
    }

    @SuppressWarnings({ "unchecked", "unused" })
    @Override
    public void run() {
	bindings.put("group", group);

	// execute the query
	super.run();

	for (String region : (Iterable<String>) result) {
	    // System.out.println(region);
	}
    }

    @Override
    public String getName() {
	return Constants.TITAN_TOP_REGIONS_GREMLIN;
    }
}
