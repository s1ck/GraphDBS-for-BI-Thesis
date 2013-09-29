package de.s1ckboy.thesis.benchmark.titan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

import de.s1ckboy.thesis.benchmark.generic.AbstractImporter;
import de.s1ckboy.thesis.generic.Constants;
import de.s1ckboy.thesis.generic.Edge;
import de.s1ckboy.thesis.generic.Node;

public class TitanImporter extends AbstractImporter {

    private TitanGraph graphDB;

    private Map<String, Long> nodeCache;

    public TitanImporter(Configuration cfg) {
	this.cfg = cfg;
	this.nodeCache = new HashMap<String, Long>();
    }

    @Override
    public void setUp() {
	super.setUp();
	this.graphDB = TitanHelper.getGraphDB(cfg);
	createTypes();
    }

    @Override
    protected void storeNode(Node node) {
	Vertex v = graphDB.addVertex(null);
	nodeCache.put((String) node.getProperty(Constants.KEY_NODE_EDGE_ID),
		(Long) v.getId());
	for (Map.Entry<String, Object> entry : node.getProperties().entrySet()) {
	    v.setProperty(entry.getKey(), entry.getValue());
	}
	nodeCnt++;
    }

    @Override
    protected void storeEdge(Edge edge) {
	// TODO Auto-generated method stub

    }

    private void createTypes() {
	/*
	 * Property Keys
	 */

	// __id__, __type__
	graphDB.makeType().name(Constants.KEY_NODE_EDGE_ID)
		.unique(Direction.IN).dataType(String.class)
		.indexed(Vertex.class).makePropertyKey();
	graphDB.makeType().name(Constants.KEY_NODE_EDGE_TYPE)
		.unique(Direction.OUT).dataType(String.class).makePropertyKey();
	/*
	 * Node: Product(title, salesrank, categories)
	 */
	graphDB.makeType().name(Constants.KEY_PRODUCT_TITLE)
		.dataType(String.class).unique(Direction.OUT).makePropertyKey();
	graphDB.makeType().name(Constants.KEY_PRODUCT_SALESRANK)
		.dataType(Integer.class).unique(Direction.OUT)
		.makePropertyKey();
	graphDB.makeType().name(Constants.KEY_PRODUCT_CATEGORIES)
		.dataType(ArrayList.class).makePropertyKey();
	/*
	 * Node: Group(name)
	 */
	graphDB.makeType().name(Constants.KEY_GROUP_NAME)
		.dataType(String.class).unique(Direction.OUT).makePropertyKey();

	/*
	 * Node: User(age, gender, eye_color, region)
	 */
	graphDB.makeType().name(Constants.KEY_USER_AGE).dataType(Integer.class)
		.unique(Direction.OUT).makePropertyKey();
	graphDB.makeType().name(Constants.KEY_USER_GENDER)
		.dataType(Boolean.class).unique(Direction.OUT)
		.makePropertyKey();
	graphDB.makeType().name(Constants.KEY_USER_EYE_COLOR)
		.dataType(String.class).unique(Direction.OUT).makePropertyKey();
	graphDB.makeType().name(Constants.KEY_USER_REGION)
		.dataType(String.class).unique(Direction.OUT).makePropertyKey();

	/*
	 * Edge Labels
	 */

	/*
	 * Edge: BELONGS_TO
	 */
	graphDB.makeType().name(Constants.LABEL_EDGE_BELONGS_TO)
		.unique(Direction.OUT).makeEdgeLabel();
	/*
	 * Edge: SIMILAR_TO
	 */
	graphDB.makeType().name(Constants.LABEL_EDGE_SIMILAR_TO)
		.makeEdgeLabel();
	/*
	 * Edge: FRIEND_OF
	 */
	graphDB.makeType().name(Constants.LABEL_EDGE_FRIEND_OF).makeEdgeLabel();
	/*
	 * Edge: REVIEWED_BY(votes, rating, helpful, date)
	 */
	graphDB.makeType().name(Constants.KEY_REVIEW_VOTES)
		.dataType(Integer.class).unique(Direction.OUT)
		.makePropertyKey();
	graphDB.makeType().name(Constants.KEY_REVIEW_RATING)
		.dataType(Integer.class).unique(Direction.OUT)
		.makePropertyKey();
	graphDB.makeType().name(Constants.KEY_REVIEW_HELPFUL)
		.dataType(Integer.class).unique(Direction.OUT)
		.makePropertyKey();
	graphDB.makeType().name(Constants.KEY_REVIEW_DATE)
		.dataType(String.class).unique(Direction.OUT).makePropertyKey();

	graphDB.makeType().name(Constants.LABEL_EDGE_REVIEWED_BY).directed()
		.makeEdgeLabel();
    }
}
