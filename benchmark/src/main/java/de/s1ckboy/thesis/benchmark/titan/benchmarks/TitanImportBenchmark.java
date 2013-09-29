package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import de.s1ckboy.thesis.benchmark.generic.Importer;
import de.s1ckboy.thesis.benchmark.titan.TitanBenchmark;

public class TitanImportBenchmark extends TitanBenchmark {

    private Importer importer;
    
    public TitanImportBenchmark(Importer importer, int runs) {
	this.setRuns(runs);
	this.importer = importer;
    }

    @Override
    public void setUp() {
	importer.setUp();
    }

    @Override
    public void run() {
	importer.run();
    }

    @Override
    public void tearDown() {
	importer.tearDown();
    }
   
    @Override
    public void warmup() {
	// no need for warmup
    }

    @Override
    public String getName() {
	return "import";
    }
}
