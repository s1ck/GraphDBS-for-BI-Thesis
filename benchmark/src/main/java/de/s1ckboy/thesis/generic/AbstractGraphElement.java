package de.s1ckboy.thesis.generic;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGraphElement implements GraphElement {

    protected Map<String, Object> properties;

    protected AbstractGraphElement() {
	properties = new HashMap<String, Object>();
    }

    @Override
    public Object getProperty(String key) {
	if (properties.containsKey(key)) {
	    return properties.get(key);
	}
	return null;
    }

    @Override
    public Map<String, Object> getProperties() {
	return properties;
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
	this.properties = properties;
    }
}
