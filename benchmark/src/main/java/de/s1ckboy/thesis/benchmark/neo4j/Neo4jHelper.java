package de.s1ckboy.thesis.benchmark.neo4j;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Neo4jHelper {

    private static GraphDatabaseService graphDB;

    public static GraphDatabaseService getGraphDB(Configuration cfg) {
	Map<String, String> dbConfig = new HashMap<String, String>();
	dbConfig.put("neostore.nodestore.db.mapped_memory",
		cfg.getString("neostore.nodestore.db.mapped_memory"));
	dbConfig.put("neostore.relationshipstore.db.mapped_memory",
		cfg.getString("neostore.relationshipstore.db.mapped_memory"));
	dbConfig.put("neostore.propertystore.db.mapped_memory",
		cfg.getString("neostore.propertystore.db.mapped_memory"));
	dbConfig.put("neostore.propertystore.db.strings.mapped_memory", cfg
		.getString("neostore.propertystore.db.strings.mapped_memory"));
	dbConfig.put("neostore.propertystore.db.arrays.mapped_memory",
		cfg.getString("neostore.propertystore.db.arrays.mapped_memory"));
	dbConfig.put("cache_type", cfg.getString("cache_type"));

	// init db
	graphDB = new GraphDatabaseFactory()
		.newEmbeddedDatabaseBuilder(cfg.getString("storage.directory"))
		.setConfig(dbConfig).newGraphDatabase();

	registerShutdownHook(graphDB);
	return graphDB;
    }

    public static void closeGraphDB() {
	try {
	    graphDB.shutdown();
	} catch (Exception e) {
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
		Neo4jHelper.closeGraphDB();
	    }
	});
    }
}
