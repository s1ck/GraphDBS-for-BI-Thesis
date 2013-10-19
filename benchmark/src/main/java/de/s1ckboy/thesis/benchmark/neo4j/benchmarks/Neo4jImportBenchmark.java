package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.Importer;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jBenchmark;
import de.s1ckboy.thesis.benchmark.queries.Import;

/**
 * Neo4j Importer uses a GraphElementIterator to insert nodes into the database.
 * 
 * See {@link http://docs.neo4j.org/chunked/milestone/batchinsert.html} for
 * details on batch inserting into Neo4j.
 * 
 * Important: The import assumes, that all nodes are inserted before any edge is
 * being inserted into the database.
 * 
 * @author Martin Junghanns
 * 
 */
public class Neo4jImportBenchmark extends Neo4jBenchmark implements Import {
    private Importer importer;
    
    public Neo4jImportBenchmark(Importer importer, int runs) {
	this.setRuns(runs);
	this.importer = importer;
    }

    @Override
    public void setUp() {
	importer.setUp();
    }
    
    @Override
    public void beforeRun() {
	// no transaction needed
    }
    
    @Override
    public void afterRun() {
	// no transaction needed
    }

    @Override
    public void run() {
	importer.run();
    }
    
    @Override
    public void tearDown() {
	importer.tearDown();
    }
    
    @Override
    public void warmup() {
	// no need to warmup
    }

    @Override
    public String getName() {	
	return Constants.IMPORT;
    }
}
