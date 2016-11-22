package net.shadowfacts.discordchat.core;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.api.command.ICommandManager;

/**
 * @author shadowfacts
 */
public class Listener extends ListenerAdapter {

	private IConfig config;
	private ICommandManager commandManager;
	private IMinecraftAdapter minecraftAdapter;

	public Listener(IDiscordChat discordChat) {
		config = discordChat.getConfig();
		commandManager = discordChat.getCommandManager();
		minecraftAdapter = discordChat.getMinecraftAdapter();
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getAuthor().equals(event.getJDA().getSelfUser())) return;
		if (!event.getGuild().getId().equals(config.getServerID())) return;

		String raw = event.getMessage().getRawContent();
		if (raw.startsWith(config.getCommandPrefix())) {
			commandManager.execute(raw.substring(config.getCommandPrefix().length()), event.getAuthor(), event.getChannel());
		} else {
			minecraftAdapter.sendMessage(event.getMessage().getContent());
		}
	}

}
