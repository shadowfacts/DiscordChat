package net.shadowfacts.discordchat.api;

/**
 * @author shadowfacts
 */
public interface IMessageFormatter {

	String message(String sender, String message);

	String death(String player, String cause);

	String achievement(String player, String achievement);

	String join(String player);

	String leave(String player);

}
