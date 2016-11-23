package net.shadowfacts.discordchat.api;

/**
 * @author shadowfacts
 */
public interface IMessageFormatter {

	String fromMC(String sender, String message);

	String fromDiscord(String channel, String sender, String message);

	String death(String player, String message);

	String achievement(String player, String achievement);

	String join(String player);

	String leave(String player);

}
