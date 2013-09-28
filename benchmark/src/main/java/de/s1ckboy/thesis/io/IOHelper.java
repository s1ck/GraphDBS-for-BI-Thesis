package de.s1ckboy.thesis.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class IOHelper {
    private static Logger log = Logger.getLogger(IOHelper.class);

    public static boolean removeDirectory(String path) {
	boolean result = false;
	try {
	    File f = new File(path);
	    if (f.exists()) {
		log.info("Deleting database directory");
		FileUtils.deleteDirectory(f);
		result = true;
	    }
	    return result;
	} catch (IOException e) {
	    return result;
	}
    }
}
