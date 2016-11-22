package net.shadowfacts.discordchat.core.command.impl.permissions;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.api.permission.Permission;
import net.shadowfacts.discordchat.core.command.exception.InvalidPermissionException;
import net.shadowfacts.discordchat.core.command.exception.InvalidUsageException;

/**
 * @author shadowfacts
 */
public class CommandSetPermission implements ICommand {

	private JDA jda;
	private IPermissionManager permissionManager;

	public CommandSetPermission(IDiscordChat discordChat) {
		jda = discordChat.getJDA();
		permissionManager = discordChat.getPermissionManager();
	}

	@Override
	public String getName() {
		return "setPermission";
	}

	@Override
	public void execute(String[] args, User sender, MessageChannel channel) throws CommandException {
		if (args.length != 2) {
			throw new InvalidUsageException(this);
		}
		checkPermission(sender);
		User user = jda.getUserById(args[0].substring(2, args[0].length() - 1));
		Permission permission = Permission.valueOf(args[1].toUpperCase());
		permissionManager.set(user, permission);
	}

	@Override
	public String getDescription() {
		return "Sets the permission of the given user";
	}

	@Override
	public String getUsage() {
		return "<user> <permission>";
	}

	private void checkPermission(User user) throws InvalidPermissionException {
		Permission min = Permission.ADMIN;
		Permission actual = permissionManager.get(user);
		if (!actual.has(min)) {
			throw new InvalidPermissionException(min, actual, getName() + " command");
		}
	}

}
