package de.s1ckboy.thesis.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import de.s1ckboy.thesis.generic.Constants;
import de.s1ckboy.thesis.generic.Edge;
import de.s1ckboy.thesis.generic.GraphElement;
import de.s1ckboy.thesis.generic.Node;

/**
 * The Geoff-Reader reads nodes and edges encoded in the Geoff format thereby
 * supporting properties on nodes and edges.
 * 
 * The properties are parsed using the gson parser.
 * 
 * @author Martin Junghanns
 * 
 */
public class GeoffReader implements FormatParser {

    private static final Pattern NODE_PATTERN = Pattern
	    .compile("^\\(([^)]+)\\)(\\s(\\{.*?\\}))?");
    private static final Pattern EDGE_PATTERN = Pattern
	    .compile("^\\(([^)]+)\\)-\\[\\:(.*?)\\]->\\(([^)]+)\\)(\\s(\\{.*?\\}))?");

    private Matcher nodeMatcher;
    private Matcher edgeMatcher;

    private String id1, id2, label;

    private Gson gson = new Gson();

    @Override
    public GraphElement parse(String line) {
	nodeMatcher = NODE_PATTERN.matcher(line);
	if (nodeMatcher.matches()) {
	    id1 = nodeMatcher.group(1);
	    if (nodeMatcher.group(3) != null) {
		return new Node(id1, parseProperties(nodeMatcher.group(3)));
	    } else {
		return new Node(id1);
	    }
	} else {
	    edgeMatcher = EDGE_PATTERN.matcher(line);
	    if (edgeMatcher.matches()) {
		id1 = edgeMatcher.group(1);
		label = edgeMatcher.group(2);
		id2 = edgeMatcher.group(3);
		if (edgeMatcher.group(5) != null) {
		    return new Edge(id1, id2, label,
			    parseProperties(edgeMatcher.group(5)));
		} else {
		    return new Edge(id1, id2, label);
		}
	    } else {
		throw new IllegalArgumentException("no match for line: " + line);
	    }
	}
    }

    /**
     * This method is a bit crappy because it ensured that the parsed values
     * have the right type by casting them manually.
     * 
     * TODO: find a nicer solution for that
     * 
     * @param jsonString
     * @return
     */
    @SuppressWarnings({ "serial", "unchecked" })
    private Map<String, Object> parseProperties(String jsonString) {
	Map<String, Object> loadedProperties = gson.fromJson(jsonString,
		new TypeToken<Map<String, Object>>() {
		}.getType());
	Map<String, Object> validProperties = new HashMap<String, Object>();

	try {
	    for (Map.Entry<String, Object> entry : loadedProperties.entrySet()) {
		if (entry.getKey().equals(Constants.KEY_NODE_EDGE_ID)) {
		    validProperties.put(Constants.KEY_NODE_EDGE_ID,
			    (String) entry.getValue());
		} else if (entry.getKey().equals(Constants.KEY_NODE_EDGE_TYPE)) {
		    validProperties.put(Constants.KEY_NODE_EDGE_TYPE,
			    (String) entry.getValue());
		} else if (entry.getKey().equals(Constants.KEY_GROUP_NAME)) {
		    validProperties.put(Constants.KEY_GROUP_NAME,
			    (String) entry.getValue());
		} else if (entry.getKey().equals(Constants.KEY_PRODUCT_TITLE)) {
		    validProperties.put(Constants.KEY_PRODUCT_TITLE,
			    (String) entry.getValue());
		} else if (entry.getKey().equals(
			Constants.KEY_PRODUCT_SALESRANK)) {
		    validProperties.put(Constants.KEY_PRODUCT_SALESRANK,
			    ((Double) entry.getValue()).intValue());
		} else if (entry.getKey().equals(
			Constants.KEY_PRODUCT_CATEGORIES)) {
		    validProperties.put(Constants.KEY_PRODUCT_CATEGORIES,
			    (ArrayList<String>) entry.getValue());
		} else if (entry.getKey().equals(Constants.KEY_USER_AGE)) {
		    validProperties.put(Constants.KEY_USER_AGE,
			    ((Double) entry.getValue()).intValue());
		} else if (entry.getKey().equals(Constants.KEY_USER_GENDER)) {
		    validProperties.put(Constants.KEY_USER_GENDER,
			    ((String) entry.getValue()).equals("1"));
		} else if (entry.getKey().equals(Constants.KEY_USER_EYE_COLOR)) {
		    validProperties.put(Constants.KEY_USER_EYE_COLOR,
			    (String) entry.getValue());
		} else if (entry.getKey().equals(Constants.KEY_USER_REGION)) {
		    validProperties.put(Constants.KEY_USER_REGION,
			    (String) entry.getValue());
		} else if (entry.getKey().equals(Constants.KEY_REVIEW_VOTES)) {
		    validProperties.put(Constants.KEY_REVIEW_VOTES,
			    ((Double) entry.getValue()).intValue());
		} else if (entry.getKey().equals(Constants.KEY_REVIEW_RATING)) {
		    validProperties.put(Constants.KEY_REVIEW_RATING,
			    ((Double) entry.getValue()).intValue());
		} else if (entry.getKey().equals(Constants.KEY_REVIEW_HELPFUL)) {
		    validProperties.put(Constants.KEY_REVIEW_HELPFUL,
			    ((Double) entry.getValue()).intValue());
		} else if (entry.getKey().equals(Constants.KEY_REVIEW_DATE)) {
		    validProperties.put(Constants.KEY_REVIEW_DATE,
			    (String) entry.getValue());
		}
	    }
	} catch (ClassCastException e) {
	    System.out.println(loadedProperties);
	    e.printStackTrace();
	    System.exit(0);
	}
	return validProperties;
    }
}
