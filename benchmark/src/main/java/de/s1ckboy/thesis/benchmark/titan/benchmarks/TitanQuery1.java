package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import de.s1ckboy.thesis.benchmark.titan.TitanBenchmark;

public class TitanQuery1 extends TitanBenchmark {

    private Long idToRead;

    public TitanQuery1(int runs) {
	this.setRuns(runs);
    }

    @Override
    public void beforeRun() {
	idToRead = getRandomVertexID();
    }

    @Override
    public void run() {
	graphDB.getVertex(idToRead);
    }

    @Override
    public String getName() {
	return "query1";
    }
}
