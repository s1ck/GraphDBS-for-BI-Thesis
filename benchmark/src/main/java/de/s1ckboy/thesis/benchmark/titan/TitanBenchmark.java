package de.s1ckboy.thesis.benchmark.titan;

import org.apache.commons.configuration.Configuration;

import com.thinkaurelius.titan.core.TitanGraph;

import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.benchmark.generic.Benchmark;

public abstract class TitanBenchmark extends Benchmark {

    protected TitanGraph graphDB;

    protected static final Configuration cfg = Configs
	    .get(TitanConstants.INSTANCE_NAME);

    @Override
    public void beforeRun() {
	// TODO Auto-generated method stub
    }

    @Override
    public void afterRun() {
	// TODO Auto-generated method stub
    }

    @Override
    public void warmup() {
	// TODO Auto-generated method stub
    }

    @Override
    public void setUp() {
	graphDB = TitanHelper.getGraphDB(cfg);
    }

    @Override
    public String getDatabaseName() {
	return TitanConstants.INSTANCE_NAME;
    }
}
