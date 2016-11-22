package net.shadowfacts.discordchat.api.command;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IDiscordChat;

import java.util.Collection;

/**
 * @author shadowfacts
 */
public interface ICommandManager {

	IDiscordChat getDiscordChat();

	void register(ICommand command);

	boolean exists(String name);

	ICommand get(String name);

	void execute(String message, User sender, TextChannel channel);

	Collection<ICommand> getCommands();

}
