package de.s1ckboy.thesis.generic;

import java.util.HashMap;
import java.util.Map;

public class EdgeDTO extends AbstractGraphElement {
    private String fromId;
    private String toId;
    private String label;

    public EdgeDTO(String fromId, String toId, String label) {
	this(fromId, toId, label, new HashMap<String, Object>());
    }

    public EdgeDTO(String fromId, String toId, String label,
	    Map<String, Object> properties) {
	this.fromId = fromId;
	this.toId = toId;
	this.label = label;
	this.properties = properties;
    }

    @Override
    public String getId() {
	return String.format("%s_%s_%s", fromId, label, toId);
    }

    public String getFromId() {
	return fromId;
    }

    public String getToId() {
	return toId;
    }

    public String getLabel() {
	return label;
    }

    @Override
    public boolean isNode() {
	return false;
    }
}
