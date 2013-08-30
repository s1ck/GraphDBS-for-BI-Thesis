package de.s1ckboy.thesis.benchmark.neo4j;

import org.neo4j.graphdb.RelationshipType;

public enum Neo4jRelationshipTypes implements RelationshipType {
    BELONGS_TO, REVIEWED_BY, SIMILAR_TO, FRIEND_OF
}
