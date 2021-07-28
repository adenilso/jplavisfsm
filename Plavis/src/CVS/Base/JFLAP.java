/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */


package CVS.Base;


import file.Codec;
import file.ParseException;
import file.xml.Transducer;
import file.xml.TransducerFactory;
import gui.action.NewAction;
import gui.action.OpenAction;
import gui.environment.Profile;
import gui.environment.Universe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This is the class that starts JFLAP.
 * 
 * @author Thomas Finley
 * @author Moti Ben-Ari
 *   All code moved to gui.Main
 *   Parameter dontQuit false for command line invocation
 */

public class JFLAP {
	public static void main(String[] args) {
		gui.Main.main(args, false);
	}
}
