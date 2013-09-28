package de.s1ckboy.thesis.io;

import de.s1ckboy.thesis.generic.GraphElement;

/**
 * Can be implemented to parse graph data line-wise from different formats.
 * 
 * @author Martin Junghanns
 * 
 */
public interface FormatParser {
    GraphElement parse(String line);
}
