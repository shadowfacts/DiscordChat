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
import net.shadowfacts.discordchat.discord.DiscordThread;

import java.util.regex.Pattern;

/**
 * @author shadowfacts
 */
public class MiscUtils {

	public static Pattern discordMessage;
	public static Pattern mcMessage;
	public static Pattern deathMessage;
	public static Pattern achievementMessage;
	public static Pattern playerJoinMessage;
	public static Pattern playerLeaveMessage;

//	private static final Pattern discordMessage = Pattern.compile("MC \u00BB .+");

	public static void sendMessage(String text) {
		sendMessage(new ChatComponentText(text));
	}

	public static void sendMessage(IChatComponent chatComponent) {
		MinecraftServer.getServer().getConfigurationManager().sendChatMsg(chatComponent);
	}

	public static String fromDiscordMessage(Message message) {
		return DCConfig.discordToMCFormat
				.replaceAll("\\$1", message.getJDA().getTextChannelById(message.getChannelId()).getName())
				.replaceAll("\\$2", message.getAuthor().getUsername())
				.replaceAll("\\$3", message.getContent());
	}

	public static String toDiscordMessage(String author, String message) {
		return DCConfig.mcToDiscordFormat
				.replaceAll("\\$1", author)
				.replaceAll("\\$2", message);
	}

	public static boolean isMessageFromMC(Message message) {
		return isMessageFromMC(message.getContent());
	}

	public static boolean isMessageFromMC(String message) {
		return discordMessage.matcher(message).matches() || isDeathMessage(message) || isAchievementMessage(message) || isPlayerJoinMessage(message) || isPlayerLeaveMessage(message);
	}

	private static boolean isDeathMessage(String message) {
		return deathMessage.matcher(message).matches();
	}

	private static boolean isAchievementMessage(String message) {
		return achievementMessage.matcher(message).matches();
	}

	private static boolean isPlayerJoinMessage(String message) {
		return playerJoinMessage.matcher(message).matches();
	}

	private static boolean isPlayerLeaveMessage(String message) {
		return playerLeaveMessage.matcher(message).matches();
	}

	public static boolean isMessageFromDiscord(String message) {
		return mcMessage.matcher(message).matches();
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
		return DCConfig.deathMessageFormat
				.replaceAll("\\$1", getName(player))
				.replaceAll("\\$2", player.func_110142_aN().func_151521_b().getUnformattedText());
	}

	public static String createAchievementMessage(EntityPlayer player, Achievement achievement) {
		IChatComponent achievementComponent = achievement.func_150951_e();
		IChatComponent achievementText = new ChatComponentText("[").appendSibling(achievementComponent).appendText("]");
		return DCConfig.achievementMessageFormat
				.replaceAll("\\$1", getName(player))
				.replaceAll("\\$2", achievementText.getUnformattedText());

	}

	public static String createLoggedInMessage(EntityPlayer player) {
		return DCConfig.playerJoinMessageFormat
				.replaceAll("\\$1", getName(player));
	}

	public static String createLoggedOutMessage(EntityPlayer player) {
		return DCConfig.playerLeaveMessageFormat
				.replaceAll("\\$1", getName(player));
	}

	private static String getName(EntityPlayer player) {
		return ScorePlayerTeam.formatPlayerName(player.getTeam(), player.getDisplayName());
	}

	public static boolean isMessageFromBot(Message message) {
		return DiscordThread.instance.jda.getSelfInfo().getUsername().equals(message.getAuthor().getUsername());
	}
}
