package net.shadowfacts.discordchat.core.command.impl.minecraft;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.IMessageFormatter;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.Permission;
import net.shadowfacts.discordchat.core.command.exception.InvalidUsageException;

/**
 * @author shadowfacts
 */
public class CommandTell implements ICommand {

	private IConfig config;
	private IMessageFormatter formatter;
	private IMinecraftAdapter minecraftAdapter;

	public CommandTell(IDiscordChat discordChat) {
		config = discordChat.getConfig();
		formatter = discordChat.getFormatter();
		minecraftAdapter = discordChat.getMinecraftAdapter();
	}

	@Override
	public String getName() {
		return "tell";
	}

	@Override
	public Permission getMinimumPermission() {
		return config.getMinimumPermission(getName());
	}

	@Override
	public void execute(String[] args, User sender, MessageChannel channel) throws CommandException {
		if (args.length < 2) throw new InvalidUsageException(this);
		String player = args[0];
		String message = String.join(" ", args);
		minecraftAdapter.sendMessageToPlayer(formatter.fromDiscordPrivate(sender.getName(), message), player);
	}

	@Override
	public String getDescription() {
		return "Sends a private message to a player in MC";
	}

	@Override
	public String getUsage() {
		return "<player> <message>";
	}

}
