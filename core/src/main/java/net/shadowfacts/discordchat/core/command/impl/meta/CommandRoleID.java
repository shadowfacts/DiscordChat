package net.shadowfacts.discordchat.core.command.impl.meta;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;

import java.util.List;

/**
 * @author shadowfacts
 */
public class CommandRoleID implements ICommand {

	private IDiscordChat discordChat;

	public CommandRoleID(IDiscordChat discordChat) {
		this.discordChat = discordChat;
	}

	@Override
	public String getName() {
		return "roleid";
	}

	@Override
	public void execute(String[] args, User sender, TextChannel channel) throws CommandException {
		String role = String.join(" ", args);
		List<Role> roles = channel.getGuild().getRolesByName(role, true);
		if (roles.isEmpty()) {
			discordChat.sendMessage("No such role: " + role);
		} else {
			discordChat.sendMessage("ID for role '" + roles.get(0).getName() + "' is " + roles.get(0).getId(), channel);
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
