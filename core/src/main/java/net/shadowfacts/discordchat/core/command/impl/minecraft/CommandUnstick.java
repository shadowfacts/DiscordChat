package net.shadowfacts.discordchat.core.command.impl.minecraft;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;
import net.shadowfacts.discordchat.core.command.exception.InvalidUsageException;

/**
 * @author shadowfacts
 */
public class CommandUnstick implements ICommand {

	private IDiscordChat discordChat;
	private IMinecraftAdapter minecraftAdapter;

	public CommandUnstick(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		this.minecraftAdapter = discordChat.getMinecraftAdapter();
	}

	@Override
	public String getName() {
		return "unstick";
	}

	@Override
	public void execute(String[] args, User sender, TextChannel channel) throws CommandException {
		if (args.length < 1) throw new InvalidUsageException(this);
		String error = minecraftAdapter.teleportPlayerToSpawn(args[0]);
		if (error == null) { // success
			discordChat.sendMessage("Unstuck player: " + args[0]);
		} else {
			discordChat.sendMessage("Unable to unstick player: " + error);
		}
	}

	@Override
	public String getDescription() {
		return "Teleports the player with the given name to spawn";
	}

	@Override
	public String getUsage() {
		return "<player>";
	}

}
