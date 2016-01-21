package net.shadowfacts.discordchat.utils;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.shadowfacts.discordchat.DCConfig;

import java.util.regex.Pattern;

/**
 * @author shadowfacts
 */
public class MiscUtils {

	private static final Pattern discordMessage = Pattern.compile("MC \u00BB <.+> .+");

	public static void sendMessage(String text) {
		sendMessage(new ChatComponentText(text));
	}

	public static void sendMessage(IChatComponent chatComponent) {
		MinecraftServer.getServer().getConfigurationManager().sendChatMsg(chatComponent);
	}

	public static String fromDiscordMessage(Message message) {
		return String.format("#%s \u00BB <%s> %s", message.getJDA().getTextChannelById(message.getChannelId()).getName(), message.getAuthor().getUsername(), message.getContent());
	}

	public static String toDiscordMessage(String author, String message) {
		return String.format("MC \u00BB <%s> %s", author, message);
	}

	public static boolean isMessageFromMC(Message message) {
		return isMessageFromMC(message.getContent());
	}

	public static boolean isMessageFromMC(String message) {
		return discordMessage.matcher(message).matches();
	}

	public static boolean shouldUseChannel(TextChannel channel) {
		for (String s : DCConfig.channels) {
			if (channel.getName().equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}
}
