package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import java.util.HashMap;
import java.util.HashSet;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.util.structures.Row;
import com.tinkerpop.pipes.util.structures.Table;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.queries.SimPattern;
import de.s1ckboy.thesis.benchmark.titan.TitanGremlinBenchmark;

public class TitanSimPatternGremlin extends TitanGremlinBenchmark implements
	SimPattern {

    private Long userID;
    
    private Vertex v;

    private Integer minCommonProductsCnt = 1;

    @Override
    public void setUp() {
	super.setUp();
	String commonProductsQuery = String.format("u.both('%s').dedup()"
		+ ".transform({ it.in('%s')"
		+ ".dedup() }).scatter().groupCount(m)" + ""
		+ ".filter({ m[it] > 1 }).fill(common_products);",
		Constants.LABEL_EDGE_FRIEND_OF,
		Constants.LABEL_EDGE_REVIEWED_BY, minCommonProductsCnt);

	String resultingTableQuery = String
		.format("common_products._().as('product').transform({"
			+ "it.out('%s').filter({"
			+ "it.in('%s').retain(common_products)[0..<2].count() >= %d"
			+ "}).as('friend').both('%s').retain([u]).back('friend').toSet()"
			+ "}).as('friends').table().cap().next();",
			Constants.LABEL_EDGE_REVIEWED_BY,
			Constants.LABEL_EDGE_REVIEWED_BY, minCommonProductsCnt,
			Constants.LABEL_EDGE_FRIEND_OF);

	GREMLIN_QUERY = commonProductsQuery + resultingTableQuery;
    }

    @Override
    public void beforeRun() {
	super.beforeRun();
	if (doWarmup()) {
	    userID = getRandomUser();
	} else {
	    // todo
	}
    }

    @SuppressWarnings({ "rawtypes" })
    @Override
    public void run() {
	v = graphDB.getVertex(userID);
	bindings.put("u", v);
	bindings.put("m", new HashMap<>());
	bindings.put("common_products", new HashSet<>());

	// execute the query
	super.run();

	for (Row row : (Table) result) {
//	    row.getColumn("product");
//	    row.getColumn("friends");
	    System.out.println(v);
	    System.out.println(row.getColumn("product"));
	    System.out.println(row.getColumn("friends"));
	}	
    }

    @Override
    public String getName() {
	return Constants.TITAN_SIM_PATTERN_GREMLIN;
    }
}
