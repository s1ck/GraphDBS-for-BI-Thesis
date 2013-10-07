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

    /**
     * Used during warmup and for random selection
     */
    protected List<Long> groupIDs;
    protected List<Long> productIDs;
    protected List<Long> userIDs;
    protected List<RelationIdentifier> reviewIDs;

    @Override
    public void setUp() {
	cfg = Configs.get(TitanConstants.INSTANCE_NAME);
	graphDB = TitanHelper.getGraphDB(cfg);

	groupIDs = new ArrayList<Long>();
	productIDs = new ArrayList<Long>();
	userIDs = new ArrayList<Long>();
	reviewIDs = new ArrayList<RelationIdentifier>();
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
	for (Vertex v : graphDB.getVertices()) {
	    type = v.getProperty(Constants.KEY_NODE_EDGE_TYPE);
	    if (type.equals(Constants.VALUE_TYPE_GROUP)) {
		groupIDs.add((Long) v.getId());
	    } else if (type.equals(Constants.VALUE_TYPE_PRODUCT)) {
		productIDs.add((Long) v.getId());
	    } else if (type.equals(Constants.VALUE_TYPE_USER)) {
		userIDs.add((Long) v.getId());
	    }
	}
	graphDB.commit();
	for (Edge e : graphDB.getEdges()) {
	    type = e.getLabel();
	    if (type.equals(Constants.LABEL_EDGE_REVIEWED_BY)) {
		reviewIDs.add((RelationIdentifier) e.getId());
	    }
	}
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

    /**
     * Picks a random class and inside the class a random node id.
     * 
     * @return a vertex identifier
     */
    protected Long getRandomVertexID() {
	List<Long> l = null;

	/*
	 * Set ranges so that the probability to pick a class depends on the
	 * number of its instances.
	 */
	int b1 = groupIDs.size();
	int b2 = b1 + productIDs.size();
	int total = b2 + userIDs.size();

	int i = r.nextInt(total);

	if (i >= 0 && i < b1) {
	    l = groupIDs;
	} else if (i >= b1 && i < b2) {
	    l = productIDs;
	} else {
	    l = userIDs;
	}
	return l.get(r.nextInt(l.size()));
    }
}
