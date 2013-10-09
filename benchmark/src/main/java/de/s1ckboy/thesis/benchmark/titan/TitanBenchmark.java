package de.s1ckboy.thesis.benchmark.titan;

import java.util.ArrayList;
import java.util.List;

import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.graphdb.relations.RelationIdentifier;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

import de.s1ckboy.thesis.benchmark.Configs;
import de.s1ckboy.thesis.benchmark.generic.Benchmark;
import de.s1ckboy.thesis.generic.Constants;

public abstract class TitanBenchmark extends Benchmark {

    protected TitanGraph graphDB;
    
    protected List<RelationIdentifier> reviewIDs;

    @Override
    public void setUp() {
	cfg = Configs.get(TitanConstants.INSTANCE_NAME);
	graphDB = TitanHelper.getGraphDB(cfg);
	
	reviewIDs = new ArrayList<RelationIdentifier>();
	
	super.setUp();
    }

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
	log.info("Warming up the caches ...");
	String type;
	long cnt = 0L;
	for (Vertex v : graphDB.getVertices()) {
	    type = v.getProperty(Constants.KEY_NODE_EDGE_TYPE);
	    if (type.equals(Constants.VALUE_TYPE_GROUP)) {
		groupIDs.add((Long) v.getId());
	    } else if (type.equals(Constants.VALUE_TYPE_PRODUCT)) {
		productIDs.add((Long) v.getId());
	    } else if (type.equals(Constants.VALUE_TYPE_USER)) {
		userIDs.add((Long) v.getId());
	    }
	    if (++cnt % 10000 == 0) {
		log.info("touched " + cnt + " nodes");
	    }
	}
	cnt = 0L;
	graphDB.commit();
	
	for (Edge e : graphDB.getEdges()) {
	    type = e.getLabel();
	    if (type.equals(Constants.LABEL_EDGE_REVIEWED_BY)) {
		reviewIDs.add((RelationIdentifier) e.getId());
	    }
	    if (++cnt % 10000 == 0) {
		log.info("touched " + cnt + " edges");
	    }
	}
	log.info("Products: " + productIDs.size());
	log.info("Groups: " + groupIDs.size());
	log.info("Users: " + userIDs.size());
	log.info("Reviews: " + reviewIDs.size());
	log.info("done");
    }

    @Override
    public void tearDown() {
	TitanHelper.closeGraphDB();
    }

    @Override
    public String getDatabaseName() {
	return TitanConstants.INSTANCE_NAME;
    }
}
