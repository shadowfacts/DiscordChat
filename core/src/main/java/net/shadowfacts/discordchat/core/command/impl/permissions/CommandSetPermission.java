package net.shadowfacts.discordchat.core.command.impl.permissions;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.ILogger;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.api.permission.Permission;
import net.shadowfacts.discordchat.core.command.exception.InvalidUsageException;

import java.io.IOException;

/**
 * @author shadowfacts
 */
public class CommandSetPermission implements ICommand {

	private JDA jda;
	private ILogger logger;
	private IPermissionManager permissionManager;

	public CommandSetPermission(IDiscordChat discordChat) {
		jda = discordChat.getJDA();
		logger = discordChat.getLogger();
		permissionManager = discordChat.getPermissionManager();
	}

	@Override
	public String getName() {
		return "setPermission";
	}

	@Override
	public Permission getMinimumPermission() {
		return Permission.ADMIN;
	}

	@Override
	public void execute(String[] args, User sender, MessageChannel channel) throws CommandException {
		if (args.length != 2) {
			throw new InvalidUsageException(this);
		}
		User user = jda.getUserById(args[0].substring(2, args[0].length() - 1));
		Permission permission = Permission.valueOf(args[1].toUpperCase());
		permissionManager.set(user, permission);

		try {
			permissionManager.save();
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
		return "<user> <permission>";
	}

}
