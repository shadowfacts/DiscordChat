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
public class CommandTime implements ICommand {

	private IDiscordChat discordChat;
	private IConfig config;
	private IMinecraftAdapter minecraftAdapter;

	public CommandTime(IDiscordChat discordChat) {
		this.discordChat = discordChat;
		config = discordChat.getConfig();
		minecraftAdapter = discordChat.getMinecraftAdapter();
	}

	@Override
	public String getName() {
		return "time";
	}

	@Override
	public Permission getMinimumPermission() {
		return config.getMinimumPermission(getName());
	}

	@Override
	public void execute(String[] args, User sender, MessageChannel channel) throws CommandException {
		int dimension;
		switch (args.length) {
			case 0:
				dimension = 0;
				break;
			case 1:
				dimension = Integer.parseInt(args[0]);
				break;
			default:
				throw new InvalidUsageException(this);
		}

		long time = minecraftAdapter.getWorldTime(dimension) % 24000L;

		String phase = null;
		if (0 <= time && time < 1000) {
			phase = "Dawn";
		} else if (1000 < time && time < 12000) {
			phase = "Day";
		} else if (12000 < time && time < 13000) {
			phase = "Dusk";
		} else if (13000 < time && time < 24000) {
			phase = "Night";
		}

		String response = (phase == null ? "" : ("**" + phase + "**: ")) + time;
		sendResponse(response, channel, discordChat);
	}

	@Override
	public String getDescription() {
		return "Gets the time of day for a dimension.";
	}

	@Override
	public String getUsage() {
		return "[dimension]";
	}

}
