package de.s1ckboy.thesis.benchmark.titan;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.gremlin.groovy.Gremlin;
import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.iterators.SingleIterator;

public abstract class TitanGremlinBenchmark extends TitanBenchmark {
    protected static String GREMLIN_QUERY;

    @SuppressWarnings("rawtypes")
    protected Pipe pipe;

    @Override
    public void beforeRun() {
	pipe = Gremlin.compile(GREMLIN_QUERY);	
    }

    @SuppressWarnings({ "unchecked", "unused" })
    @Override
    public void run() {
	pipe.setStarts(new SingleIterator<Vertex>(graphDB.getVertex(nextID)));
	// just iterate the resulting pipe
	for (Object o : pipe) {
	}
    }
}
