package de.s1ckboy.thesis.benchmark.neo4j;

import org.apache.commons.configuration.Configuration;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.tooling.GlobalGraphOperations;

import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.benchmark.generic.Benchmark;

public abstract class Neo4jBenchmark extends Benchmark {
    /*
     * indexes nodes by Constants.KEY_NODE_EDGE_ID
     */
    protected Index<Node> nodeIdx;
    /*
     * Neo4j instance
     */
    protected GraphDatabaseService graphDB;
    /*
     * Needed for embedded Cypher execution
     */
    protected ExecutionEngine engine;
    /*
     * Access to the config file
     */
    protected static Configuration cfg = Configs
	    .get(Neo4jConstants.INSTANCE_NAME);

    @Override
    public void setUp() {
	// graph database instance
	graphDB = Neo4jHelper.getGraphDB(cfg);
	// cypher execution engine
	engine = new ExecutionEngine(graphDB);
	// get index
	nodeIdx = graphDB.index().forNodes(Neo4jConstants.NODE_IDX_NAME);
    }

    @Override
    public void tearDown() {
	Neo4jHelper.closeGraphDB();
    }

    @Override
    public void beforeRun() {
	// TODO Auto-generated method stub
    }

    @Override
    public void afterRun() {
	// TODO Auto-generated method stub
    }

    @SuppressWarnings("unused")
    @Override
    public void warmup() {
	log.info("Warming up the caches ...");
	for (Node v : GlobalGraphOperations.at(graphDB).getAllNodes()) {
	}
	for (Relationship e : GlobalGraphOperations.at(graphDB)
		.getAllRelationships()) {
	}
	log.info("done");
    }

    @Override
    public String getDatabaseName() {
	return Neo4jConstants.INSTANCE_NAME;
    }
}