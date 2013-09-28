package de.s1ckboy.thesis.benchmark.neo4j;

import java.util.Random;

public class Neo4jQ1 extends Neo4jBenchmark {
    private Random r;
    
    private long sleep;
    
    public Neo4jQ1() {}
    
    public Neo4jQ1(int runs) {
	this.setRuns(runs);
    }

    @Override
    public void setUp() {
	super.setUp();
	r = new Random();
    }

    @Override
    public void beforeRun() {
	sleep = r.nextInt(1000);
    }

    @Override
    public void run() {
	try {
	    Thread.sleep(sleep);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    public String getName() {
	return "query1";
    }

}
