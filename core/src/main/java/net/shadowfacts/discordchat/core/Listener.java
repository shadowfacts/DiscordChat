package net.shadowfacts.discordchat.core;

import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.IMessageFormatter;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.api.command.ICommandManager;
import net.shadowfacts.discordchat.core.util.NearestMCColor;

import java.awt.*;

/**
 * @author shadowfacts
 */
public class Listener extends ListenerAdapter {

	private IConfig config;
	private ICommandManager commandManager;
	private IMessageFormatter formatter;
	private IMinecraftAdapter minecraftAdapter;

	public Listener(IDiscordChat discordChat) {
		config = discordChat.getConfig();
		commandManager = discordChat.getCommandManager();
		formatter = discordChat.getFormatter();
		minecraftAdapter = discordChat.getMinecraftAdapter();
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		String raw = event.getMessage().getRawContent();
		if (raw.startsWith(config.getCommandPrefix())) {
			commandManager.execute(raw.substring(config.getCommandPrefix().length()), event.getAuthor(), event.getChannel());
		}
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getAuthor().equals(event.getJDA().getSelfUser())) return;
		if (event.getGuild().getIdLong() != config.getServerID()) return;
		if (!config.getChannelIDs().contains(event.getChannel().getIdLong())) return;

		String raw = event.getMessage().getRawContent();
		if (raw.startsWith(config.getCommandPrefix())) {
			commandManager.execute(raw.substring(config.getCommandPrefix().length()), event.getAuthor(), event.getChannel());
		} else {
			String channel = event.getChannel().getName();
			String color = event.getMember().getColor() == null ? config.getDefaultColor() : NearestMCColor.get(event.getMember().getColor().getRGB());
			String author = event.getMember().getEffectiveName().replaceAll("\\.", "_");
			String message = EmojiParser.parseToAliases(event.getMessage().getContent());
			minecraftAdapter.sendMessage(formatter.fromDiscord(channel, color, author, message));
			event.getMessage().getAttachments().forEach(it -> {
				minecraftAdapter.sendMessage("Attachment: " + it.getUrl());
			});
		}
	}

}
