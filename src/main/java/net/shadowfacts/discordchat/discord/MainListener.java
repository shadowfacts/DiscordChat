package net.shadowfacts.discordchat.discord;

import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.shadowfacts.discordchat.DCCommands;
import net.shadowfacts.discordchat.DCConfig;
import net.shadowfacts.discordchat.utils.MiscUtils;

/**
 * @author shadowfacts
 */
public class MainListener extends ListenerAdapter {

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getMessage().getContent().startsWith(DCConfig.commandPrefix)) {
			DCCommands.handle(event.getChannel(), event.getAuthor(), event.getMessage().getContent().substring(1));
		} else if (MiscUtils.shouldUseChannel(event.getChannel()) && event.getGuild().getId().equals(DCConfig.serverId) && !MiscUtils.isMessageFromMC(event.getMessage())) {
			MiscUtils.sendMessage(MiscUtils.fromDiscordMessage(event.getMessage()));
		}
	}

}
