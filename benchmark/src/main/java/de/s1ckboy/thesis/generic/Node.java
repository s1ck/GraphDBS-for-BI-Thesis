package de.s1ckboy.thesis.generic;

import java.util.HashMap;
import java.util.Map;

public class Node extends AbstractGraphElement {
    private String id;

    public Node(String id) {
	this(id, new HashMap<String, Object>());
    }

    public Node(String id, Map<String, Object> properties) {
	this.id = id;
	this.properties = properties;
    }

    @Override
    public String getId() {
	return id;
    }
    @Override 
    public boolean isNode() {
	return true;
    }
}
