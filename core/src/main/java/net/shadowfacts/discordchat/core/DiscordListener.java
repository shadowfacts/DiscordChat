package net.shadowfacts.discordchat.core;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * @author shadowfacts
 */
public class DiscordListener extends ListenerAdapter {

	private DiscordConfig config;

	public DiscordListener(DiscordConfig config) {
		this.config = config;
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

	}

}
