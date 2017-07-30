package net.shadowfacts.discordchat.core.command.impl.meta;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.ICommandManager;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.Permission;
import net.shadowfacts.discordchat.core.command.exception.InvalidUsageException;

import java.util.List;

/**
 * @author shadowfacts
 */
public class CommandHelp implements ICommand {

	private IDiscordChat discordChat;
	private IConfig config;
	private ICommandManager commandManager;

	public CommandHelp(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		this.config = discordChat.getConfig();
		this.commandManager = discordChat.getCommandManager();
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public Permission getMinimumPermission() {
		return config.getMinimumPermission(getName());
	}

	@Override
	public void execute(String[] args, User author, MessageChannel channel) throws CommandException {
		if (args.length < 1) {
			throw new InvalidUsageException(this);
		}
		if (commandManager.exists(args[0])) {
			List<String> lines = commandManager.get(args[0]).handleHelp(author, channel);
			String msg = String.join("\n", lines);
			sendResponse(msg, channel, discordChat);
		} else {
			throw new CommandException("No such command: " + args[0]);
		}
	}

	@Override
	public String getDescription() {
		return "Retrieves help for the given command.";
	}

	@Override
	public String getUsage() {
		return "<command>";
	}

}
