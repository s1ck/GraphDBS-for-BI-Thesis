package de.s1ckboy.thesis.benchmark.neo4j;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import de.s1ckboy.thesis.benchmark.Configuration;

public class Neo4jHelper {

    private static Logger log = Logger.getLogger(Neo4jHelper.class);
    private static GraphDatabaseService graphDB;

    public static GraphDatabaseService getConnection(Configuration cfg) {
	Map<String, String> dbConfig = new HashMap<String, String>();
	dbConfig.put("neostore.nodestore.db.mapped_memory",
		cfg.getPropertyAsString("neostore.nodestore.db.mapped_memory"));
	dbConfig.put(
		"neostore.relationshipstore.db.mapped_memory",
		cfg.getPropertyAsString("neostore.relationshipstore.db.mapped_memory"));
	dbConfig.put("neostore.propertystore.db.mapped_memory", cfg
		.getPropertyAsString("neostore.propertystore.db.mapped_memory"));
	dbConfig.put(
		"neostore.propertystore.db.strings.mapped_memory",
		cfg.getPropertyAsString("neostore.propertystore.db.strings.mapped_memory"));
	dbConfig.put(
		"neostore.propertystore.db.arrays.mapped_memory",
		cfg.getPropertyAsString("neostore.propertystore.db.arrays.mapped_memory"));
	dbConfig.put("cache_type", cfg.getPropertyAsString("cache_type"));

	// init db
	graphDB = new GraphDatabaseFactory()
		.newEmbeddedDatabaseBuilder(cfg.getPropertyAsString("location"))
		.setConfig(dbConfig).newGraphDatabase();

	registerShutdownHook(graphDB);
	return graphDB;
    }

    public static void destroyConnection() {
	try {
	    graphDB.shutdown();
	    log.info("Destroy connection successful");
	} catch (Exception e) {
	    log.error("Destroy connection failed");
	    e.printStackTrace();
	}
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
	// Registers a shutdown hook for the Neo4j instance so that it
	// shuts down nicely when the VM exits (even if you "Ctrl-C" the
	// running example before it's completed)
	Runtime.getRuntime().addShutdownHook(new Thread() {
	    @Override
	    public void run() {
		Neo4jHelper.destroyConnection();
	    }
	});
    }
}
