package net.shadowfacts.discordchat.core.command.impl.minecraft;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.Permission;
import net.shadowfacts.discordchat.core.command.exception.InvalidUsageException;

/**
 * @author shadowfacts
 */
public class CommandExecute implements ICommand {

	private IConfig config;
	private IMinecraftAdapter minecraftAdapter;

	public CommandExecute(IDiscordChat discordChat) {
		config = discordChat.getConfig();
		minecraftAdapter = discordChat.getMinecraftAdapter();
	}

	@Override
	public String getName() {
		return "exec";
	}

	@Override
	public Permission getMinimumPermission() {
		return config.getMinimumPermission(getName());
	}

	@Override
	public void execute(String[] args, User sender, MessageChannel channel) throws CommandException {
		if (args.length == 0) throw new InvalidUsageException(this);

		String cmd = String.join(" ", args);
		minecraftAdapter.executeCommand(cmd);
	}

	@Override
	public String getDescription() {
		return "Executes the given command in Minecraft";
	}

	@Override
	public String getUsage() {
		return "<command>";
	}

}
