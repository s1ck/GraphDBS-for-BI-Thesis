package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngineFactory;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.queries.PathAll;
import de.s1ckboy.thesis.benchmark.titan.TitanGremlinBenchmark;

public class TitanPathAllGremlin extends TitanGremlinBenchmark implements
	PathAll {

    private ScriptEngine engine;

    private Bindings bindings;

    @Override
    public void setUp() {
	super.setUp();
	engine = new GremlinGroovyScriptEngineFactory().getScriptEngine();
	GREMLIN_QUERY = String
		.format("a.both('%s','%s','%s').loop(1){it.object != b && it.loops < 5}.retain([b]).path._().transform{it.size() - 1}.groupCount().cap();",
			Constants.LABEL_EDGE_FRIEND_OF,
			Constants.LABEL_EDGE_REVIEWED_BY,
			Constants.LABEL_EDGE_SIMILAR_TO);
    }

    @Override
    public void beforeRun() {
	bindings = engine.createBindings();
    }

    @SuppressWarnings({ "unused", "unchecked" })
    @Override
    public void run() {
	// this has to happen inside the run methode because in cypher loading
	// the start and end node is part of the query
	Vertex a = graphDB.getVertex(getRandomUser());
	List<Vertex> visited = new ArrayList<>();
	visited.add(a);
	// bindings.put("g", graphDB);
	bindings.put("a", a);
	bindings.put("b", graphDB.getVertex(getRandomProduct()));
	bindings.put("visited", visited);

	try {
	    for (Map<Integer, Integer> o : (Iterable<Map<Integer, Integer>>) engine
		    .eval(GREMLIN_QUERY, bindings)) {
		for (Map.Entry<Integer, Integer> e : o.entrySet()) {
		    e.getKey();
		    e.getValue();
		}
	    }
	} catch (ScriptException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public String getName() {
	return Constants.TITAN_PATH_ALL_GREMLIN;
    }
}
