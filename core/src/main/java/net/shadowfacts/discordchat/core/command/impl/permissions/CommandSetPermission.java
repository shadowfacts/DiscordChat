package net.shadowfacts.discordchat.core.command.impl.permissions;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.ILogger;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.api.permission.Permission;
import net.shadowfacts.discordchat.core.command.exception.InvalidUsageException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author shadowfacts
 */
public class CommandSetPermission implements ICommand {

	private IDiscordChat discordChat;
	private ILogger logger;
	private IConfig config;
	private IPermissionManager permissionManager;

	public CommandSetPermission(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		logger = discordChat.getLogger();
		config = discordChat.getConfig();
		permissionManager = discordChat.getPermissionManager();
	}

	@Override
	public String getName() {
		return "setPermission";
	}

	@Override
	public Permission getMinimumPermission() {
		return config.getMinimumPermission(getName());
	}

	@Override
	public void execute(String[] args, User sender, MessageChannel channel) throws CommandException {
		if (args.length < 2) {
			throw new InvalidUsageException(this);
		}

		Permission permission = Permission.valueOf(args[args.length - 1].toUpperCase());
		String role = String.join(" ", Arrays.copyOfRange(args, 0, args.length - 1));
		List<Role> roles = discordChat.getJDA().getGuildById(config.getServerID()).getRolesByName(role, true);
		if (roles.isEmpty()) {
			sendResponse("No such role: " + role, channel, discordChat);
			return;
		}

		permissionManager.set(roles.get(0), permission);

		try {
			permissionManager.save();
			sendResponse("Permissions updated", channel, discordChat);
		} catch (IOException e) {
			logger.error(e, "Unable to save permissions");
		}
	}

	@Override
	public String getDescription() {
		return "Sets the permission of the given user";
	}

	@Override
	public String getUsage() {
		return "<role> <permission>";
	}

}
