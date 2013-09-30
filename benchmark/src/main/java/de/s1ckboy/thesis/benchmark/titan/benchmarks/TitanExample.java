package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.groovy.jsr223.GremlinGroovyScriptEngine;
import com.tinkerpop.gremlin.java.GremlinPipeline;

import de.s1ckboy.thesis.benchmark.titan.TitanBenchmark;
import de.s1ckboy.thesis.generic.Constants;

public class TitanExample extends TitanBenchmark {

    public TitanExample(int runs) {
	this.setRuns(runs);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void run() {
	Vertex p1 = graphDB.getVertices(Constants.KEY_NODE_EDGE_ID, "ABC1")
		.iterator().next();

	GremlinPipeline pipe = new GremlinPipeline();
	pipe.start(p1).out(Constants.LABEL_EDGE_REVIEWED_BY)
		.property(Constants.KEY_USER_EYE_COLOR);

	log.info(pipe.next());

	for (Vertex v : p1
		.query()
		.direction(Direction.OUT)
		.labels(Constants.LABEL_EDGE_REVIEWED_BY,
			Constants.LABEL_EDGE_BELONGS_TO,
			Constants.LABEL_EDGE_SIMILAR_TO).vertices()) {
	    // for (String k : v.getPropertyKeys()) {
	    // log.info(k + " = " + v.getProperty(k));
	    // }
	}
    }

    @Override
    public String getName() {
	return "example";
    }

}
