package de.s1ckboy.thesis.benchmark.generic;

public class ImportBenchmark extends Benchmark {

	private Importer importer;

	public ImportBenchmark(Importer importer) {
		this.importer = importer;
	}

	@Override
	public void setUp() {
		importer.setUp();
	}

	@Override
	public void run() {
		importer.importData();
	}

	@Override
	public void tearDown() {
		// close connection
		importer.tearDown();
	}

	@Override
	public void beforeRun() {
	}

	@Override
	public String getName() {
		return String.format("%s Import", importer.getName());
	}

	@Override
	public void afterRun() {
	}

	@Override
	public void warmup() {
	}
	
	@Override
	public String getDatabaseName() {
		return importer.getName();
	}
}