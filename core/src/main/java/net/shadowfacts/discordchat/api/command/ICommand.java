package net.shadowfacts.discordchat.api.command;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IDiscordChat;
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

	void execute(String[] args, User sender, MessageChannel channel) throws CommandException;

	default void sendResponse(String message, MessageChannel channel, IDiscordChat discordChat) {
		discordChat.sendMessage(discordChat.getFormatter().command(message), channel);
	}

	String getDescription();

	String getUsage();

	default List<String> handleHelp(User sender, MessageChannel channel) {
		List<String> list = new ArrayList<>();
		list.add(getName() + ": " + getDescription());
		list.add("Usage: " + getName() + " " + getUsage());
		return list;
	}

}
