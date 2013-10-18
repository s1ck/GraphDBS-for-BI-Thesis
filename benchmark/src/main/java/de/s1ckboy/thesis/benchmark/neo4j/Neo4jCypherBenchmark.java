package de.s1ckboy.thesis.benchmark.neo4j;

import org.neo4j.cypher.javacompat.ExecutionResult;

/**
 * Abstract Base Class for all Cypher-based benchmarks. It just executes the
 * query and counts the rows in the result to avoid paging effects.
 * 
 * @author Martin 's1ck' Junghanns
 * 
 */
public abstract class Neo4jCypherBenchmark extends Neo4jBenchmark {
    protected static String CYPHER_QUERY;
    
    protected ExecutionResult result;

    /**
     * Execute the prepared statement.
     */
    @Override
    public void run() {
	result = engine.execute(CYPHER_QUERY);
    }
}
