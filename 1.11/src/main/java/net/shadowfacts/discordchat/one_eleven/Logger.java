package net.shadowfacts.discordchat.one_eleven;

import net.shadowfacts.discordchat.api.ILogger;
import org.apache.logging.log4j.LogManager;

/**
 * @author shadowfacts
 */
public class Logger implements ILogger {

	private org.apache.logging.log4j.Logger logger = LogManager.getFormatterLogger(OneElevenMod.MOD_ID);

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
	public void error(String msg, Object... args) {
		logger.error(msg, args);
	}

	@Override
	public void error(Throwable t, String msg, Object... args) {
		logger.error(String.format(msg, args), t);
	}

}
