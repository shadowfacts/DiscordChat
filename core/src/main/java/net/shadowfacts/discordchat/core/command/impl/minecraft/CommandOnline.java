package net.shadowfacts.discordchat.core.command.impl.minecraft;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.Permission;

import java.util.Set;

/**
 * @author shadowfacts
 */
public class CommandOnline implements ICommand {

	private IDiscordChat discordChat;
	private IConfig config;
	private IMinecraftAdapter minecraftAdapter;

	public CommandOnline(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		this.config = discordChat.getConfig();
		this.minecraftAdapter = discordChat.getMinecraftAdapter();
	}

	@Override
	public String getName() {
		return "online";
	}

	@Override
	public Permission getMinimumPermission() {
		return config.getMinimumPermission(getName());
	}

	@Override
	public void execute(String[] args, User sender, MessageChannel channel) throws CommandException {
		Set<String> players = minecraftAdapter.getOnlinePlayers();
		int count = players.size();
		String s = count == 1 ? "player" : "players";
		sendResponse(count + " " + s + " online: " + String.join(", ", players), channel, discordChat);
	}

	@Override
	public String getDescription() {
		return "Lists all online players";
	}

	@Override
	public String getUsage() {
		return "";
	}

}
