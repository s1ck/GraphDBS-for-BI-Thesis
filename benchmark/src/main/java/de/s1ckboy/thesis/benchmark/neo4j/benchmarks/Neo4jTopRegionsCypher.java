package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import java.util.Iterator;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jCypherBenchmark;
import de.s1ckboy.thesis.benchmark.queries.TopRegions;

public class Neo4jTopRegionsCypher extends Neo4jCypherBenchmark implements
	TopRegions {

    private Node group;

    @Override
    public void setUp() {
	super.setUp();

	Transaction tx = null;
	try {
	    tx = graphDB.beginTx();
	    group = nodeIdx.get(Constants.KEY_NODE_EDGE_ID, "g_0").getSingle();
	    tx.success();
	} finally {
	    tx.finish();
	}

	CYPHER_QUERY = String
		.format("START g=node(%d) MATCH g<-[:%s]-p-[r:%s]->u WHERE p.salesrank < 500000 RETURN u.region AS region, count(u) AS cnt ORDER BY cnt DESC LIMIT 10;",
			group.getId(), Constants.LABEL_EDGE_BELONGS_TO,
			Constants.LABEL_EDGE_REVIEWED_BY);

    }

    @SuppressWarnings("unused")
    @Override
    public void run() {
	super.run();
	Iterator<String> regions = result.columnAs("region");
	for (String region : IteratorUtil.asIterable(regions)) {
//	    System.out.println(region);
	}
    }

    @Override
    public String getName() {
	return Constants.NEO4J_TOP_REGIONS_CYPHER;
    }

}
