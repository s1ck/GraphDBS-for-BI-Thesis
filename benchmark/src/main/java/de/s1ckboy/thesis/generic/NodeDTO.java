package de.s1ckboy.thesis.generic;

import java.util.HashMap;
import java.util.Map;

public class NodeDTO extends AbstractGraphElement {
    private String id;

    public NodeDTO(String id) {
	this(id, new HashMap<String, Object>());
    }

    public NodeDTO(String id, Map<String, Object> properties) {
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
