package de.s1ckboy.thesis.benchmark.neo4j;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Import_Neo4j extends Neo4jBenchmark {

    private String datasetPath;

    public Import_Neo4j(String datasetPath) {
	this.datasetPath = datasetPath;
    }

    @Override
    public void setUp() {
	try {
	    File f = new File(cfg.getPropertyAsString("location"));
	    if (f.exists()) {
		log.info("Deleting database directory");
		FileUtils.deleteDirectory(f);
	    }
	} catch (IOException e) {
	    log.error(e);
	}
	super.setUp();
    }

    @Override
    public void run() {
	// do some import
    }

    @Override
    public String getName() {
	return "import";
    }

}
