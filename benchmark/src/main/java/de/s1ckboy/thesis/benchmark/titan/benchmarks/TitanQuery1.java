package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import de.s1ckboy.thesis.benchmark.titan.TitanBenchmark;

public class TitanQuery1 extends TitanBenchmark {

    private Long nextID;

    public TitanQuery1(int runs) {
	this.setRuns(runs);
    }

    @Override
    public void beforeRun() {
	nextID = getRandomVertexID();
    }

    @Override
    public void run() {
	graphDB.getVertex(nextID);
	graphDB.commit();
    }

    @Override
    public String getName() {
	return "query1";
    }
}
