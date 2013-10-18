package de.s1ckboy.thesis.benchmark;

public class Constants {
    /*
     * Data specific
     */
    public static final String KEY_NODE_EDGE_TYPE = "__type__";
    public static final String KEY_NODE_EDGE_ID = "__id__";

    public static final String VALUE_TYPE_GROUP = "g";
    public static final String VALUE_TYPE_PRODUCT = "p";
    public static final String VALUE_TYPE_USER = "u";
    public static final String VALUE_TYPE_REVIEW = "r";

    /*
     * Labels
     */
    public static final String LABEL_NODE_PRODUCT = "Product";
    public static final String LABEL_NODE_GROUP = "Group";
    public static final String LABEL_NODE_USER = "User";

    public static final String LABEL_EDGE_BELONGS_TO = "BELONGS_TO";
    public static final String LABEL_EDGE_SIMILAR_TO = "SIMILAR_TO";
    public static final String LABEL_EDGE_REVIEWED_BY = "REVIEWED_BY";
    public static final String LABEL_EDGE_FRIEND_OF = "FRIEND_OF";

    /*
     * Product
     */
    public static final String KEY_PRODUCT_TITLE = "title";
    public static final String KEY_PRODUCT_SALESRANK = "salesrank";
    public static final String KEY_PRODUCT_CATEGORIES = "categories";
    /*
     * Group
     */
    public static final String KEY_GROUP_NAME = "name";
    /*
     * User
     */
    public static final String KEY_USER_AGE = "age";
    public static final String KEY_USER_GENDER = "gender";
    public static final String KEY_USER_EYE_COLOR = "eye_color";
    public static final String KEY_USER_REGION = "region";
    /*
     * Review (edge)
     */
    public static final String KEY_REVIEW_VOTES = "votes";
    public static final String KEY_REVIEW_RATING = "rating";
    public static final String KEY_REVIEW_HELPFUL = "helpful";
    public static final String KEY_REVIEW_DATE = "date";

    /**
     * Benchmark specific
     */
    public static final Integer DEFAULT_BENCHMARK_RUNS = 100;
    
    public static final String IMPORT = "import";
    
    public static final String NEO4J_Q1_CYPHER = "neo4j_q1_cypher";
    public static final String NEO4J_Q2_CYPHER = "neo4j_q2_cypher";
    
    public static final String TITAN_Q1_GREMLIN = "titan_q1_gremlin";
    public static final String TITAN_Q2_GREMLIN = "titan_q2_gremlin";

    /**
     * Import specific
     */
    public static final Integer NODE_LOG_CNT = 100000;
    public static final Integer EDGE_LOG_CNT = 100000;

}
