package de.s1ckboy.thesis.benchmark;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import de.s1ckboy.thesis.generic.EdgeDTO;
import de.s1ckboy.thesis.generic.GraphElement;
import de.s1ckboy.thesis.generic.NodeDTO;
import de.s1ckboy.thesis.io.GeoffReader;
import de.s1ckboy.thesis.io.GraphElementIterator;
import de.s1ckboy.thesis.io.IOHelper;

public abstract class AbstractImporter implements Importer {
    protected Logger log = Logger.getLogger(AbstractImporter.class);

    protected long nodeCnt = 0L;

    protected long edgeCnt = 1L;

    protected long missingEndNodeCnt = 0L;

    protected Configuration cfg;

    @Override
    public void setUp() {
	// drop database directory if necessary
	log.info("Starting import into " + cfg.getString("storage.directory"));
	if (cfg.getBoolean("import.drop.db")) {
	    IOHelper.removeDirectory(cfg.getString("storage.directory"));
	}
    }

    @Override
    public void tearDown() {
	log.info(String.format(
		"Imported %d nodes, %d edges. Got %d missing end nodes",
		nodeCnt, edgeCnt, missingEndNodeCnt));
    }

    @Override
    public void run() {
	try {
	    GraphElementIterator it = new GraphElementIterator(
		    new BufferedReader(new FileReader(
			    cfg.getString("import.dataset.path"))),
		    new GeoffReader());

	    GraphElement element;
	    while (it.hasNext()) {
		element = it.next();
		if (element.isNode()) {
		    storeNode((NodeDTO) element);
		} else {
		    storeEdge((EdgeDTO) element);
		}
		// some logging
		if (nodeCnt % Constants.NODE_LOG_CNT == 0) {
		    log.info(String.format("Stored %d nodes", nodeCnt));
		}
		if (edgeCnt % Constants.EDGE_LOG_CNT == 0) {
		    log.info(String.format("Stored %d edges", edgeCnt));
		}
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	}

    }

    protected abstract void storeNode(NodeDTO node);

    protected abstract void storeEdge(EdgeDTO edge);

}
