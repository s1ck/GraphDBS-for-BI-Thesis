package de.s1ckboy.thesis.benchmark.titan;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.batch.BatchGraph;
import com.tinkerpop.blueprints.util.wrappers.batch.VertexIDType;

import de.s1ckboy.thesis.benchmark.generic.AbstractImporter;
import de.s1ckboy.thesis.generic.Constants;
import de.s1ckboy.thesis.generic.EdgeDTO;
import de.s1ckboy.thesis.generic.NodeDTO;

/**
 * Imports the graph into Titan using the Blueprints BatchImporter described
 * here: https://github.com/tinkerpop/blueprints/wiki/Batch-Implementation
 * 
 * @author Martin Junghanns
 * 
 */
public class TitanImporter extends AbstractImporter {

    private BatchGraph<TitanGraph> graphDB;

    public TitanImporter(Configuration cfg) {
	this.cfg = cfg;
    }

    @Override
    public void setUp() {
	super.setUp();
	this.graphDB = new BatchGraph<TitanGraph>(TitanHelper.getGraphDB(cfg),
		VertexIDType.STRING, cfg.getInt("import.tx.batchsize"));
	graphDB.setVertexIdKey(Constants.KEY_NODE_EDGE_ID);
	graphDB.setEdgeIdKey(Constants.KEY_NODE_EDGE_ID);

	createTypes();
    }

    @Override
    public void tearDown() {
	graphDB.shutdown();
	super.tearDown();
    }

    @Override
    protected void storeNode(NodeDTO node) {
	Vertex v = graphDB.addVertex(node.getId());

	for (Map.Entry<String, Object> entry : node.getProperties().entrySet()) {
	    v.setProperty(entry.getKey(), entry.getValue());
	}
	nodeCnt++;
    }

    @Override
    protected void storeEdge(EdgeDTO edge) {
	Vertex from = graphDB.getVertex(edge.getFromId());
	Vertex to = graphDB.getVertex(edge.getToId());
	if (from != null && to != null) {
	    Edge e = graphDB.addEdge(null, from, to, edge.getLabel());
	    for (Map.Entry<String, Object> entry : edge.getProperties()
		    .entrySet()) {
		e.setProperty(entry.getKey(), entry.getValue());
	    }
	    edgeCnt++;
	} else {
	    missingEndNodeCnt++;
	}
    }

    private void createTypes() {
	/*
	 * Property Keys
	 */

	// __id__, __type__
	graphDB.getBaseGraph().makeType().name(Constants.KEY_NODE_EDGE_ID)
		.unique(Direction.BOTH).indexed(Vertex.class)
		.dataType(String.class).makePropertyKey();
	graphDB.getBaseGraph().makeType().name(Constants.KEY_NODE_EDGE_TYPE)
		.unique(Direction.OUT).dataType(String.class).makePropertyKey();
	/*
	 * Node: Product(title, salesrank, categories)
	 */
	graphDB.getBaseGraph().makeType().name(Constants.KEY_PRODUCT_TITLE)
		.dataType(String.class).unique(Direction.OUT).makePropertyKey();
	graphDB.getBaseGraph().makeType().name(Constants.KEY_PRODUCT_SALESRANK)
		.dataType(Integer.class).unique(Direction.OUT)
		.makePropertyKey();
	graphDB.getBaseGraph().makeType()
		.name(Constants.KEY_PRODUCT_CATEGORIES)
		.dataType(ArrayList.class).unique(Direction.OUT)
		.makePropertyKey();
	/*
	 * Node: Group(name)
	 */
	graphDB.getBaseGraph().makeType().name(Constants.KEY_GROUP_NAME)
		.dataType(String.class).unique(Direction.OUT).makePropertyKey();

	/*
	 * Node: User(age, gender, eye_color, region)
	 */
	graphDB.getBaseGraph().makeType().name(Constants.KEY_USER_AGE)
		.dataType(Integer.class).unique(Direction.OUT)
		.makePropertyKey();
	graphDB.getBaseGraph().makeType().name(Constants.KEY_USER_GENDER)
		.dataType(Boolean.class).unique(Direction.OUT)
		.makePropertyKey();
	graphDB.getBaseGraph().makeType().name(Constants.KEY_USER_EYE_COLOR)
		.dataType(String.class).unique(Direction.OUT).makePropertyKey();
	graphDB.getBaseGraph().makeType().name(Constants.KEY_USER_REGION)
		.dataType(String.class).unique(Direction.OUT).makePropertyKey();

	/*
	 * Edge Labels
	 */

	/*
	 * Edge: BELONGS_TO
	 */
	graphDB.getBaseGraph().makeType().name(Constants.LABEL_EDGE_BELONGS_TO)
		.unique(Direction.OUT).makeEdgeLabel();
	/*
	 * Edge: SIMILAR_TO
	 */
	graphDB.getBaseGraph().makeType().name(Constants.LABEL_EDGE_SIMILAR_TO)
		.makeEdgeLabel();
	/*
	 * Edge: FRIEND_OF
	 */
	graphDB.getBaseGraph().makeType().name(Constants.LABEL_EDGE_FRIEND_OF)
		.makeEdgeLabel();
	/*
	 * Edge: REVIEWED_BY(votes, rating, helpful, date)
	 */
	graphDB.getBaseGraph().makeType().name(Constants.KEY_REVIEW_VOTES)
		.dataType(Integer.class).unique(Direction.OUT)
		.makePropertyKey();
	graphDB.getBaseGraph().makeType().name(Constants.KEY_REVIEW_RATING)
		.dataType(Integer.class).unique(Direction.OUT)
		.makePropertyKey();
	graphDB.getBaseGraph().makeType().name(Constants.KEY_REVIEW_HELPFUL)
		.dataType(Integer.class).unique(Direction.OUT)
		.makePropertyKey();
	graphDB.getBaseGraph().makeType().name(Constants.KEY_REVIEW_DATE)
		.dataType(String.class).unique(Direction.OUT).makePropertyKey();

	graphDB.getBaseGraph().makeType()
		.name(Constants.LABEL_EDGE_REVIEWED_BY).directed()
		.makeEdgeLabel();
    }
}
