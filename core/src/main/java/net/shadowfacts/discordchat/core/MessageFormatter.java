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
//		TODO: configurable
	}

	@Override
	public String message(String sender, String message) {
		return sender + ": " + message;
	}

	@Override
	public String death(String player, String cause) {
		return player + " died from " + cause;
	}

	@Override
	public String achievement(String player, String achievement) {
		return player + " got achievement " + achievement;
	}

	@Override
	public String join(String player) {
		return player + " joined";
	}

	@Override
	public String leave(String player) {
		return player + " left";
	}

}
