package de.s1ckboy.thesis.benchmark.titan;

import org.apache.commons.configuration.Configuration;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;

public class TitanHelper {
    private static TitanGraph graphDB;

    public static TitanGraph getGraphDB(Configuration cfg) {
	if (graphDB == null) {
	    graphDB = TitanFactory.open(cfg);
	}
	return graphDB;
    }

    public static void closeGraphDB() {
	if (graphDB != null) {
	    graphDB.shutdown();
	    graphDB = null;
	}
    }
}
