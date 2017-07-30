package net.shadowfacts.discordchat.core.command.impl.permissions;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.api.permission.Permission;

/**
 * @author shadowfacts
 */
public class CommandPermission implements ICommand {

	private IDiscordChat discordChat;
	private IConfig config;
	private IPermissionManager permissionManager;

	public CommandPermission(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		this.config = discordChat.getConfig();
		permissionManager = discordChat.getPermissionManager();
	}

	@Override
	public String getName() {
		return "permission";
	}

	@Override
	public Permission getMinimumPermission() {
		return config.getMinimumPermission(getName());
	}

	@Override
	public void execute(String[] args, User sender, MessageChannel channel) throws CommandException {
		sendResponse("Permission level for " + sender.getName() + ": " + permissionManager.get(sender, discordChat.getJDA().getGuildById(config.getServerID())), channel, discordChat);
	}

	@Override
	public String getDescription() {
		return "Retrieves the caller's permission";
	}

	@Override
	public String getUsage() {
		return "";
	}

}
