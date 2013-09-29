package de.s1ckboy.thesis.benchmark.titan;

import java.util.ArrayList;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

import de.s1ckboy.thesis.generic.Constants;
import de.s1ckboy.thesis.io.IOHelper;

public class TitanImport extends TitanBenchmark {

    public TitanImport(int runs) {
	this.setRuns(runs);
    }

    @Override
    public void setUp() {
	if (cfg.getBoolean("import.drop.db")) {
	    IOHelper.removeDirectory(cfg.getString("storage.directory"));
	}
	super.setUp(); // starts the graphdb

	createTypes();
    }

    @Override
    public void run() {
	
    }
    
    @Override
    public void tearDown() {

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
	// TODO Auto-generated method stub

    }

    @Override
    public String getName() {
	return "import";
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
	graphDB.makeType().name(Constants.KEY_PRODUCT_SALESRANK)
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
	 * Edge: REVIEWED_BY
	 */
	graphDB.makeType().name(Constants.LABEL_EDGE_FRIEND_OF).makeEdgeLabel();
	/*
	 * Edge: REVIEWED_BY(votes, rating, helpful, date)
	 */
	graphDB.makeType().name(Constants.LABEL_EDGE_REVIEWED_BY).directed()
		.makeEdgeLabel();
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
    }
}
