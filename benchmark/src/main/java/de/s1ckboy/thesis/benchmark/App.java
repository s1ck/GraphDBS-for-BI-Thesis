package de.s1ckboy.thesis.benchmark;

import de.s1ckboy.thesis.benchmark.hypergraphdb.HGDBSuite;
import de.s1ckboy.thesis.benchmark.neo4j.Neo4jSuite;
import de.s1ckboy.thesis.benchmark.orientdb.OrientSuite;
import de.s1ckboy.thesis.benchmark.titan.TitanSuite;

public class App {
    public static void main(String[] args) {
	boolean neo4j = true;
	boolean titan = false;
	boolean orientdb = false;
	boolean hgdb = false;

	
	
	if (neo4j)
	    new Neo4jSuite().execute();
	if (titan)
	    new TitanSuite().execute();
	if (orientdb)
	    new OrientSuite().execute();
	if (hgdb)
	    new HGDBSuite().execute();
    }
}
