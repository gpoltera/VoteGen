/*
 * Copyright (c) 2012 Berner Fachhochschule, Switzerland.
 * Bern University of Applied Sciences, Engineering and Information Technology,
 * Research Institute for Security in the Information Society, E-Voting Group,
 * Biel, Switzerland.
 *
 * Project UniVote.
 *
 * Distributable under GPL license.
 * See terms of license at gnu.org.
 */
package ch.hsr.univote.unigen.helper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Log helper class.
 *
 * @author Stephan Fischli &lt;stephan.fischli@bfh.ch&gt;
 */
public class LogHelper {

	private static final String LOG_FILE = ConfigHelper.getLogfilePath();
	private static final Logger logger = Logger.getAnonymousLogger();

	static {
		try {
			logger.setUseParentHandlers(false);
			FileHandler handler = new FileHandler(LOG_FILE, true);
			handler.setEncoding(ConfigHelper.getCharEncoding());
			handler.setFormatter(new SimpleFormatter());
			logger.addHandler(handler);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void log(String message) {
		logger.info(message);
	}

	public static void log(Exception exception) {
		StringWriter message = new StringWriter();
		exception.printStackTrace(new PrintWriter(message));
		logger.severe(message.toString());
	}

	public static void close() {
		for (Handler handler : logger.getHandlers()) {
			handler.close();
		}
	}
}