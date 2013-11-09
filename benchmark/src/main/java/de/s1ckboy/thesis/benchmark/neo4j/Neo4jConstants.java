package de.s1ckboy.thesis.benchmark.neo4j;

import org.neo4j.graphdb.Label;

import de.s1ckboy.thesis.benchmark.Constants;

//import org.neo4j.graphdb.Label;

public class Neo4jConstants {
    /**
     * General
     */
    public static final String INSTANCE_NAME = "neo4j";
    /**
     * Schema constants
     */
    public static final Label PRODUCT_LABEL = new Label() {
	@Override
	public String name() {
	    return Constants.LABEL_NODE_PRODUCT;
	}
    };
    public static final Label GROUP_LABEL = new Label() {
	@Override
	public String name() {
	    return Constants.LABEL_NODE_GROUP;
	}
    };
    public static final Label USER_LABEL = new Label() {
	@Override
	public String name() {
	    return Constants.LABEL_NODE_USER;
	}
    };

    /**
     * Import specific settings
     */
    public static final String NODE_IDX_NAME = "nodes";
}
