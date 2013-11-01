package de.s1ckboy.thesis.benchmark.neo4j;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngineFactory;

public abstract class Neo4jGremlinBenchmark extends Neo4jBenchmark {
    protected static String GREMLIN_QUERY;

    // needed to execute the Gremlin query
    protected ScriptEngine engine;
    // used for parametrized queries
    protected Bindings bindings;
    // blueprints wrapper for neo4j
    protected Graph g;

    protected Object result;

    @Override
    public void setUp() {
	super.setUp();
	g = new Neo4jGraph(graphDB);
	engine = new GremlinGroovyScriptEngineFactory().getScriptEngine();
    }

    public void run() {
	try {
	    result = engine.eval(GREMLIN_QUERY, bindings);
	} catch (ScriptException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public void beforeRun() {
	bindings = engine.createBindings();
    }

    @Override
    public void afterRun() {
    }
}
