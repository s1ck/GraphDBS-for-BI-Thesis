package de.s1ckboy.thesis.io;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import de.s1ckboy.thesis.generic.Edge;
import de.s1ckboy.thesis.generic.GraphElement;
import de.s1ckboy.thesis.generic.Node;

public class GeoffParser implements FormatParser {

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

    @SuppressWarnings("serial")
    private Map<String, Object> parseProperties(String jsonString) {
	return gson.fromJson(jsonString, new TypeToken<Map<String, Object>>() {
	}.getType());
    }
}
