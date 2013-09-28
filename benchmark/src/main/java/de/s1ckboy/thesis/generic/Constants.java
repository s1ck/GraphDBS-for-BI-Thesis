package de.s1ckboy.thesis.generic;

public class Constants {

    /*
     * Data specific
     */
    public static final String KEY_NODE_EDGE_TYPE = "__type__";
    public static final String KEY_NODE_EDGE_ID = "__id__";

    public static final String VALUE_TYPE_PRODUCT = "p";
    public static final String VALUE_TYPE_GROUP = "g";
    public static final String VALUE_TYPE_USER = "u";
    
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

}
