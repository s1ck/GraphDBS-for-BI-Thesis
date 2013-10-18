package de.s1ckboy.thesis.benchmark.titan.benchmarks;

import de.s1ckboy.thesis.benchmark.Constants;
import de.s1ckboy.thesis.benchmark.Importer;
import de.s1ckboy.thesis.benchmark.queries.Import;
import de.s1ckboy.thesis.benchmark.titan.TitanBenchmark;

public class TitanImportBenchmark extends TitanBenchmark implements Import {

    private Importer importer;

    public TitanImportBenchmark(Importer importer) {
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
	return Constants.IMPORT;
    }
}
