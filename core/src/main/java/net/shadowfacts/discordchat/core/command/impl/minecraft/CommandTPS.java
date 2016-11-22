package net.shadowfacts.discordchat.core.command.impl.minecraft;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.api.command.ICommand;
import net.shadowfacts.discordchat.api.command.exception.CommandException;

/**
 * @author shadowfacts
 */
public class CommandTPS implements ICommand {

	private IMinecraftAdapter minecraftAdapter;

	public CommandTPS(IDiscordChat discordChat) {
		minecraftAdapter = discordChat.getMinecraftAdapter();
	}

	@Override
	public String getName() {
		return "tps";
	}

	@Override
	public void execute(String[] args, User sender, MessageChannel channel) throws CommandException {
		if (args.length == 0) {
			for (int dim : minecraftAdapter.getAllDimensions()) {
				double tps = minecraftAdapter.getTPS(dim);
				channel.sendMessage(String.format("TPS in dimension %d: %.0f", dim, tps));
			}
		} else {
			int dim;
			try {
				dim = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				throw new CommandException(e);
			}
			double tps = minecraftAdapter.getTPS(dim);
			channel.sendMessage(String.format("TPS in dimension %d: %.0f", dim, tps));
		}
	}

	@Override
	public String getDescription() {
		return "Displays TPS for a given dimension (or all dimensions, if none is specified)";
	}

	@Override
	public String getUsage() {
		return "[dimension]";
	}

}
