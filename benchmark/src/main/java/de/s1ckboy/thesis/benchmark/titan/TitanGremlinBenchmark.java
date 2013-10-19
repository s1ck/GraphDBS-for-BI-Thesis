package de.s1ckboy.thesis.benchmark.titan;

import com.tinkerpop.gremlin.groovy.Gremlin;
import com.tinkerpop.pipes.Pipe;

public abstract class TitanGremlinBenchmark extends TitanBenchmark {
    protected static String GREMLIN_QUERY;

    @SuppressWarnings("rawtypes")
    protected Pipe pipe;

    @Override
    public void beforeRun() {
	pipe = Gremlin.compile(GREMLIN_QUERY);
    }
}
