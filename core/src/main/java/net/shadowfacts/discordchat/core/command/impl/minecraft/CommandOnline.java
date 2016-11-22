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
public class CommandOnline implements ICommand {

	private IDiscordChat discordChat;
	private IMinecraftAdapter minecraftAdapter;

	public CommandOnline(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		this.minecraftAdapter = discordChat.getMinecraftAdapter();
	}

	@Override
	public String getName() {
		return "online";
	}

	@Override
	public void execute(String[] args, User sender, MessageChannel channel) throws CommandException {
		discordChat.sendMessage(String.join(", ", minecraftAdapter.getOnlinePlayers()), channel);
	}

	@Override
	public String getDescription() {
		return "Lists all online players";
	}

	@Override
	public String getUsage() {
		return "";
	}

}
