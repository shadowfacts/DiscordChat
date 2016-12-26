package net.shadowfacts.discordchat.api.command;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shadowfacts
 */
public interface ICommand {

	String getName();

	Permission getMinimumPermission();

	void execute(String[] args, User sender, TextChannel channel) throws CommandException;

	String getDescription();

	String getUsage();

	default List<String> handleHelp(User sender, TextChannel channel) {
		List<String> list = new ArrayList<>();
		list.add(getName() + ": " + getDescription());
		list.add("Usage: " + getName() + " " + getUsage());
		return list;
	}

}
