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
				.replaceAll("\\$1", sender)
				.replaceAll("\\$2", message);
	}

	@Override
	public String fromDiscord(String channel, String sender, String message) {
		return config.getFromDiscordFormat()
				.replaceAll("\\$1", channel)
				.replaceAll("\\$2", sender)
				.replaceAll("\\$3", message);
	}

	@Override
	public String death(String player, String message) {
		return config.getDeathFormat()
				.replaceAll("\\$1", player)
				.replaceAll("\\$2", message);
	}

	@Override
	public String achievement(String player, String achievement) {
		return config.getAchievementFormat()
				.replaceAll("\\$1", player)
				.replaceAll("\\$2", achievement);
	}

	@Override
	public String join(String player) {
		return config.getJoinFormat()
				.replaceAll("\\$1", player);
	}

	@Override
	public String leave(String player) {
		return config.getLeaveFormat()
				.replaceAll("\\$1", player);
	}

}
