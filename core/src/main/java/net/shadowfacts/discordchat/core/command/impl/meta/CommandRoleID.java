package net.shadowfacts.discordchat.core.command.impl.meta;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.Permission;

import java.util.List;

/**
 * @author shadowfacts
 */
public class CommandRoleID implements ICommand {

	private IDiscordChat discordChat;
	private IConfig config;

	public CommandRoleID(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		this.config = discordChat.getConfig();
	}

	@Override
	public String getName() {
		return "roleid";
	}

	@Override
	public Permission getMinimumPermission() {
		return config.getMinimumPermission(getName());
	}

	@Override
	public void execute(String[] args, User sender, MessageChannel channel) throws CommandException {
		String role = String.join(" ", args);
		List<Role> roles = discordChat.getJDA().getGuildById(config.getServerID()).getRolesByName(role, true);
		if (roles.isEmpty()) {
			sendResponse("No such role: " + role, channel, discordChat);
		} else {
			sendResponse("ID for role '" + roles.get(0).getName() + "' is " + roles.get(0).getId(), channel, discordChat);
		}
	}

	@Override
	public String getDescription() {
		return "Retrieves the ID for the role with the given name";
	}

	@Override
	public String getUsage() {
		return "<role>";
	}

}
