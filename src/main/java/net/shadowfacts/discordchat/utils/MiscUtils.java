package net.shadowfacts.discordchat.utils;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.stats.Achievement;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.shadowfacts.discordchat.DCConfig;

import java.util.regex.Pattern;

import static net.shadowfacts.discordchat.DCConfig.botId;

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

	public static void sendMessage(String text) {
		sendMessage(new TextComponentString(text));
	}

	public static void sendMessage(ITextComponent chatComponent) {
		FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendChatMsg(chatComponent);
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
		return message.getAuthor().getId().equals(botId);
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
				.replaceAll("\\$2", player.getCombatTracker().getDeathMessage().getUnformattedText());
	}

	public static String createAchievementMessage(EntityPlayer player, Achievement achievement) {
		ITextComponent achievementComponent = achievement.getStatName();
		ITextComponent achievementText = new TextComponentString("[").appendSibling(achievementComponent).appendText("]");
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
		return ScorePlayerTeam.formatPlayerName(player.getTeam(), player.getDisplayName().getUnformattedText());
	}

}
