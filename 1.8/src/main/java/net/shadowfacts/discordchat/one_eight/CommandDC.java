package net.shadowfacts.discordchat.one_eight;

import com.google.common.collect.ImmutableList;
import net.dv8tion.jda.core.entities.Role;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.ILogger;
import net.shadowfacts.discordchat.api.permission.IPermissionManager;
import net.shadowfacts.discordchat.api.permission.Permission;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author shadowfacts
 */
public class CommandDC extends CommandBase {

	private IDiscordChat discordChat;
	private ILogger logger;
	private IConfig config;
	private IPermissionManager permissionManager;

	private Map<String, BiConsumer<ICommandSender, String[]>> subcommands = new HashMap<>();

	public CommandDC(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		logger = discordChat.getLogger();
		config = discordChat.getConfig();
		permissionManager = discordChat.getPermissionManager();

		subcommands.put("setpermission", this::setPermission);
		subcommands.put("tell", this::tell);
	}

	@Override
	public String getCommandName() {
		return "discordchat";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/discordchat <setpermission|tell>";
	}

	@Override
	public List<String> getCommandAliases() {
		return ImmutableList.of("dc");
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length < 1) {
			throw new WrongUsageException(getCommandUsage(sender));
		}

		String command = args[0].toLowerCase();
		args = Arrays.copyOfRange(args, 1, args.length);

		if (!subcommands.containsKey(command)) {
			throw new WrongUsageException(getCommandUsage(sender));
		}

		subcommands.get(command).accept(sender, args);
	}

	private void setPermission(ICommandSender sender, String[] args) {
		if (args.length < 2) {
			wrongUsage(sender, "/discordchat setPermission <discord role name> <permission>");
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

	private void tell(ICommandSender sender, String[] args) {
		if (args.length < 2) {
			wrongUsage(sender, "/discordchat tell <user> <message>");
		} else {
			String user = args[0];
			String message = "";
			for (int i = 1; i < args.length; i++) {
				message += args[i];
				if (i != args.length - 1) message += " ";
			}
			discordChat.sendPrivateMessage(sender.getCommandSenderName(), message, user);
		}
	}

	private void wrongUsage(ICommandSender sender, String usage) {
		sender.addChatMessage(new ChatComponentTranslation("commands.generic.usage", usage));
	}

}
