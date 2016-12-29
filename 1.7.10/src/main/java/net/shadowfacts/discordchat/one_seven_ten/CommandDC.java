package net.shadowfacts.discordchat.one_seven_ten;

import net.dv8tion.jda.core.entities.Role;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.ILogger;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.api.permission.Permission;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author shadowfacts
 */
public class CommandDC extends CommandBase {

	private IDiscordChat discordChat;
	private ILogger logger;
	private IConfig config;
	private IPermissionManager permissionManager;

	public CommandDC(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		logger = discordChat.getLogger();
		config = discordChat.getConfig();
		permissionManager = discordChat.getPermissionManager();
	}

	@Override
	public String getCommandName() {
		return "discordchat";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/discordchat setPermission <discord role name> <permission>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException(getCommandUsage(sender));
		}
		if (!"setPermission".equalsIgnoreCase(args[0])) {
			throw new WrongUsageException(getCommandUsage(sender));
		}

		args = Arrays.copyOfRange(args, 1, args.length);

		if (args.length < 2) {
			throw new WrongUsageException(getCommandUsage(sender));
		}

		Permission permission = Permission.valueOf(args[args.length - 1].toUpperCase());

		String role = String.join(" ", Arrays.copyOfRange(args, 0, args.length - 1));
		List<Role> roles = discordChat.getJDA().getGuildById(config.getServerID()).getRolesByName(role, true);
		if (roles.isEmpty()) {
			sender.addChatMessage(new ChatComponentText("No such role: " + role));
			return;
		}

		permissionManager.set(roles.get(0), permission);

		try {
			permissionManager.save();
			sender.addChatMessage(new ChatComponentText("Permissions updated"));
		} catch (IOException e) {
			logger.error(e, "Unable to save permissions");
		}
	}

}
