package net.shadowfacts.discordchat.utils;

import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.shadowfacts.discordchat.DCConfig;
import net.shadowfacts.discordchat.commands.ChatCommands;

import java.lang.reflect.*;

/**
 * @author Naosyth
 */
public class CommandUtils {
	public static boolean processCommand(GuildMessageReceivedEvent event) {
		String message = event.getMessage().getContent().trim();

		if (message.charAt(0) != DCConfig.chatCommandPrefix) {
			return false;
		}

		String[] components = message.split(" ", 2);
		String command = components[0].substring(1).toLowerCase();

		String args = "";
		if (components.length > 1) {
			args = components[1];
		}

		ChatCommands chatCommands = new ChatCommands();
		Class<?>[] paramTypes = { User.class, String.class };
		try {
			Method m = ChatCommands.class.getDeclaredMethod("cc" + command, paramTypes);
			return (Boolean)m.invoke(chatCommands, event.getAuthor(), args);
		}
		catch (Exception e) { return false; }
	}
}
