package net.shadowfacts.discordchat.core.command;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.ICommandManager;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.api.permission.Permission;
import net.shadowfacts.discordchat.core.command.exception.InvalidPermissionException;

import java.util.*;

/**
 * @author shadowfacts
 */
public class CommandManager implements ICommandManager {

	private IDiscordChat discordChat;
	private IConfig config;

	private Map<String, ICommand> commands = new HashMap<>();

	public CommandManager(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		config = discordChat.getConfig();
	}

	@Override
	public IDiscordChat getDiscordChat() {
		return discordChat;
	}

	@Override
	public void register(ICommand command) {
		commands.put(command.getName().toLowerCase(), command);
	}

	@Override
	public boolean exists(String name) {
		return commands.containsKey(name.toLowerCase());
	}

	@Override
	public ICommand get(String name) {
		return commands.get(name.toLowerCase());
	}

	@Override
	public void execute(String message, User sender, MessageChannel channel) {
		String[] bits = message.split(" ");
		try {
			if (exists(bits[0])) {
				ICommand command = get(bits[0]);
				checkPermission(command, sender, discordChat.getJDA().getGuildById(config.getServerID()));
				command.execute(Arrays.copyOfRange(bits, 1, bits.length), sender, channel);
			} else {
				throw new CommandException("Unknown command: " + bits[0]);
			}
		} catch (CommandException e) {
			discordChat.sendMessage(discordChat.getFormatter().command("Error executing command: " + e.getLocalizedMessage()), channel);
		}
	}

	@Override
	public Collection<ICommand> getCommands() {
		return commands.values();
	}

	private void checkPermission(ICommand command, User user, Guild guild) throws InvalidPermissionException {
		Permission min = command.getMinimumPermission();
		Permission actual = discordChat.getPermissionManager().get(user, guild);
		if (!actual.has(min)) {
			throw new InvalidPermissionException(min, actual, command.getName() + " command");
		}
	}

}
