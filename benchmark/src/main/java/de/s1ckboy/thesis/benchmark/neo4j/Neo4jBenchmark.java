package de.s1ckboy.thesis.benchmark.neo4j;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.tooling.GlobalGraphOperations;

import de.s1ckboy.thesis.benchmark.Configuration;
import de.s1ckboy.thesis.benchmark.generic.Benchmark;

public abstract class Neo4jBenchmark extends Benchmark {

    protected Index<Node> index;
    protected GraphDatabaseService graphDb;
    protected ExecutionEngine engine;

    protected static Configuration cfg = Configuration
	    .getInstance(Neo4jConstants.INSTANCE_NAME);

    @Override
    public void setUp() {
	// graph database instance
	graphDb = Neo4jHelper.getConnection(cfg);
	// cypher execution engine
	engine = new ExecutionEngine(graphDb);
	// get index
	index = graphDb.index().forNodes(Neo4jConstants.NODE_IDX_NAME);
    }

    @Override
    public void tearDown() {
	Neo4jHelper.destroyConnection();
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
	for (Node v : GlobalGraphOperations.at(graphDb).getAllNodes()) {
	}
	for (Relationship e : GlobalGraphOperations.at(graphDb)
		.getAllRelationships()) {
	}
	log.info("done");
    }

    @Override
    public String getDatabaseName() {
	return "neo4j";
    }
}