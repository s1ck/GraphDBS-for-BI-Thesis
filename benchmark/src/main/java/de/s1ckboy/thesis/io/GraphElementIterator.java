package de.s1ckboy.thesis.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

import de.s1ckboy.thesis.generic.GraphElement;

public class GraphElementIterator implements Iterator<GraphElement> {

    private BufferedReader reader;

    private FormatParser parser;

    public GraphElementIterator(BufferedReader reader, FormatParser parser) {
	this.reader = reader;
	this.parser = parser;
    }

    @Override
    public boolean hasNext() {
	try {
	    return reader.ready();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return false;
    }

    @Override
    public GraphElement next() {
	try {
	    return parser.parse(reader.readLine());
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return null;
	}
    }

    @Override
    public void remove() {
	throw new UnsupportedOperationException();
    }

}
