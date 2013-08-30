package de.s1ckboy.thesis.generic;

import java.util.Map;

public interface GraphElement {
    public static final String ID_KEY = "__id__";
    public static final String TYPE_KEY = "__type__";
    
    String getId();
    
    Object getProperty(String key);
    
    Map<String, Object> getProperties();
    
    void setProperties(Map<String, Object> properties);
    
    // ugly, but faster than instanceof
    boolean isNode();
}
