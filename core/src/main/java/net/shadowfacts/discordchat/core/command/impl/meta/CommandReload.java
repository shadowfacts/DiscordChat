package net.shadowfacts.discordchat.core.command.impl.meta;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.api.permission.Permission;

import java.io.IOException;

/**
 * @author shadowfacts
 */
public class CommandReload implements ICommand {

	private IDiscordChat discordChat;
	private IConfig config;
	private IPermissionManager permissionManager;

	public CommandReload(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		config = discordChat.getConfig();
		permissionManager = discordChat.getPermissionManager();
	}

	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public Permission getMinimumPermission() {
		return config.getMinimumPermission(getName());
	}

	@Override
	public void execute(String[] args, User sender, MessageChannel channel) throws CommandException {
		try {
			config.load();
			permissionManager.load();
			sendResponse("Configuration and permissions reloaded", channel, discordChat);
		} catch (IOException e) {
			throw new CommandException(e);
		}
	}

	@Override
	public String getDescription() {
		return "Reloads configuration and permissions";
	}

	@Override
	public String getUsage() {
		return "";
	}

}
