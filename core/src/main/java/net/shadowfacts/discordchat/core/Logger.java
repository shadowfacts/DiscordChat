package net.shadowfacts.discordchat.core;

import net.shadowfacts.discordchat.api.ILogger;
import org.apache.logging.log4j.LogManager;

import java.util.Collections;

/**
 * @author shadowfacts
 */
public class Logger implements ILogger {

	private org.apache.logging.log4j.Logger logger = LogManager.getFormatterLogger("discordchat");

	@Override
	public void debug(String msg, Object... args) {
		logger.debug(msg, args);
	}

	@Override
	public void info(String msg, Object... args) {
		logger.info(msg, args);
	}

	@Override
	public void warn(String msg, Object... args) {
		logger.warn(msg, args);
	}

	@Override
	public void warn(Throwable t, String msg, Object... args) {
		logger.warn(String.format(msg, args), t);
	}

	@Override
	public void bigWarning(String msg) {
		String sep = String.join("", Collections.nCopies(msg.length(), "*"));
		warn(sep);
		warn(msg);
		warn(sep);
	}

	@Override
	public void error(String msg, Object... args) {
		logger.error(msg, args);
	}

	@Override
	public void error(Throwable t, String msg, Object... args) {
		logger.error(String.format(msg, args), t);
	}

}
