package de.s1ckboy.thesis.benchmark.neo4j.benchmarks;

import java.util.Random;

import de.s1ckboy.thesis.benchmark.neo4j.Neo4jBenchmark;

public class Neo4jQuery1 extends Neo4jBenchmark {
    private Random r;
    
    private long sleep;
    
    public Neo4jQuery1() {}
    
    public Neo4jQuery1(int runs) {
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
