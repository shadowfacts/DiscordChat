package net.shadowfacts.discordchat.api;

/**
 * @author shadowfacts
 */
public interface ILogger {

	void debug(String msg, Object... args);

	void info(String msg, Object... args);

	void warn(String msg, Object... args);

	void warn(Throwable t, String msg, Object... args);

	void bigWarning(String msg);

	void error(String msg, Object... args);

	void error(Throwable t, String msg, Object... args);

}
