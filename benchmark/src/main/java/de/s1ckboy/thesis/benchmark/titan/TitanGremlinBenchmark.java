package de.s1ckboy.thesis.benchmark.titan;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngineFactory;

/**
 * 
 * Base class for gremlin scripts. This is using the GremlinGroovyScriptEngine
 * with bindings for better performance.
 * 
 * @see https://groups.google.com/forum/#!searchin/gremlin-users/gremlin$20java/
 *      gremlin-users/d3Jem-PfrMo/c7xcb_XZRVMJ
 * 
 * @author Martin Junghanns
 * 
 */
public abstract class TitanGremlinBenchmark extends TitanBenchmark {
    protected static String GREMLIN_QUERY;

    // needed to execute the Gremlin query
    protected ScriptEngine engine;
    // used for parametrized queries
    protected Bindings bindings;

    protected Object result;

    @Override
    public void setUp() {
	super.setUp();
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
}
