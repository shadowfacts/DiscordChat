package net.shadowfacts.discordchat.core;

import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.IMessageFormatter;

/**
 * @author shadowfacts
 */
public class MessageFormatter implements IMessageFormatter {

	private IConfig config;

	public MessageFormatter(IDiscordChat discordChat) {
		this.config = discordChat.getConfig();
	}

	@Override
	public String fromMC(String sender, String message) {
		return config.getFromMCFormat()
				.replace("$1", sender)
				.replace("$2", message);
	}

	@Override
	public String fromDiscord(String channel, String senderColor, String sender, String message) {
		return config.getFromDiscordFormat()
				.replace("$1", channel)
				.replace("$2", sender)
				.replace("$3", message)
				.replace("$4", senderColor);
	}

	@Override
	public String fromMCPrivate(String sender, String message) {
		return config.getFromMCPrivateFormat()
				.replace("$1", sender)
				.replace("$2", message);
	}

	@Override
	public String fromDiscordPrivate(String sender, String message) {
		return config.getFromDiscordPrivateFormat()
				.replace("$1", sender)
				.replace("$2", message);
	}

	@Override
	public String death(String player, String message) {
		return config.getDeathFormat()
				.replace("$1", player)
				.replace("$2", message);
	}

	@Override
	public String achievement(String player, String achievement) {
		return config.getAchievementFormat()
				.replace("$1", player)
				.replace("$2", achievement);
	}

	@Override
	public String join(String player) {
		return config.getJoinFormat()
				.replace("$1", player);
	}

	@Override
	public String leave(String player) {
		return config.getLeaveFormat()
				.replace("$1", player);
	}

	@Override
	public String command(String message) {
		return config.getCommandFormat()
				.replace("$1", message);
	}
}
