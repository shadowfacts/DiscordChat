package net.shadowfacts.discordchat.discord;

import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.shadowfacts.discordchat.DCConfig;
import net.shadowfacts.discordchat.utils.MiscUtils;
import net.shadowfacts.discordchat.utils.CommandUtils;

/**
 * @author shadowfacts
 */
public class MainListener extends ListenerAdapter {

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (shouldForwardMessage(event)) {
			MiscUtils.sendMessage(MiscUtils.fromDiscordMessage(event.getMessage()));
		}
	}

	private boolean shouldForwardMessage(GuildMessageReceivedEvent event) {
		return (MiscUtils.shouldUseChannel(event.getChannel()) &&
						event.getGuild().getId().equals(DCConfig.serverId) &&
						!MiscUtils.isMessageFromMC(event.getMessage()) &&
						!CommandUtils.processCommand(event));
	}

}
