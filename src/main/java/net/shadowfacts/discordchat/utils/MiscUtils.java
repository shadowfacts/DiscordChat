package net.shadowfacts.discordchat.utils;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.shadowfacts.discordchat.DCConfig;

import java.util.regex.Pattern;

/**
 * @author shadowfacts
 */
public class MiscUtils {

	private static final Pattern discordMessage = Pattern.compile("MC \u00BB .+");

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

	public static String createDiscordDeathMessage(EntityPlayer player) {
		return "MC \u00BB " + player.func_110142_aN().func_151521_b().getUnformattedText();
	}

	public static String createAchievementMessage(EntityPlayer player, Achievement achievement) {
		IChatComponent achievementComponent = achievement.func_150951_e();
		IChatComponent achievementText = new ChatComponentText("[").appendSibling(achievementComponent).appendText("]");
		return "MC \u00BB " + I18n.format("chat.type.achievement", ScorePlayerTeam.formatPlayerName(player.getTeam(), player.getDisplayName()), achievementText.getUnformattedText());
	}

	public static String createLoggedInMessage(EntityPlayer player) {
		return I18n.format("multiplayer.player.joined", ScorePlayerTeam.formatPlayerName(player.getTeam(), player.getDisplayName()));
	}

	public static String createLoggedOutMessage(EntityPlayer player) {
		return I18n.format("multiplayer.player.left", ScorePlayerTeam.formatPlayerName(player.getTeam(), player.getDisplayName()));
	}

}
