package net.shadowfacts.discordchat.discord;

import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.shadowfacts.discordchat.utils.MiscUtils;

/**
 * @author shadowfacts
 */
public class MainListener extends ListenerAdapter {

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (MiscUtils.shouldUseChannel(event.getChannel()) && !MiscUtils.isMessageFromMC(event.getMessage())) {
			MiscUtils.sendMessage(MiscUtils.fromDiscordMessage(event.getMessage()));
		}
	}

}
