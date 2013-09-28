package de.s1ckboy.thesis.generic;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGraphElement implements GraphElement {

    protected Map<String, Object> properties;

    protected AbstractGraphElement() {
	properties = new HashMap<String, Object>();
    }

    @Override
    public boolean hasProperty(String key) {
	return properties.containsKey(key);
    }

    @Override
    public Object getProperty(String key) {
	return properties.get(key);
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
