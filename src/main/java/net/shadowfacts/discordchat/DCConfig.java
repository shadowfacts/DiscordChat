package net.shadowfacts.discordchat;

import net.shadowfacts.discordchat.utils.MiscUtils;
import net.shadowfacts.shadowmc.config.Config;
import net.shadowfacts.shadowmc.config.ConfigManager;
import net.shadowfacts.shadowmc.config.ConfigProperty;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author shadowfacts
 */
@Config(name = DiscordChat.modId)
public class DCConfig {

	@ConfigProperty(comment = "Enable DiscordChat")
	public static boolean enabled = true;

	@ConfigProperty(comment = "Send player death messages to Discord")
	public static boolean sendPlayerDeathMessages = true;

	@ConfigProperty(comment = "Send player achievement messages to Discord")
	public static boolean sendPlayerAchievementMessages = true;

	@ConfigProperty(comment = "Send player join/leave messages to Discord")
	public static boolean sendPlayerJoinLeaveMessages = true;

	@ConfigProperty(category = "general.format", comment = "Format for a normal message from MC to Discord.\n$1 will be replaced with the sender's username and $2 will be replaced with the message")
	public static String mcToDiscordFormat = "MC \u00BB <$1> $2";

	@ConfigProperty(category = "general.format", comment = "Format for a normal message from Discord to MC.\n$1 will be replaced with the channel, $2 will be replaced with the sender's username, and $3 will be replaced with the message")
	public static String discordToMCFormat = "$1 \u00BB <$2> $3";

	@ConfigProperty(category = "general.format", comment = "Format for a player death message from MC to Discord.\n$1 will be replaced with the player's username and $2 will be replaced with the death message")
	public static String deathMessageFormat = "MC \u00BB $2";

	@ConfigProperty(category = "general.format", comment = "Format for a player achievement message from MC to Discord.\n$1Will be replaced with the player's username, $2 will be replaced with the achievement")
	public static String achievementMessageFormat = "MC \u00BB $1 has just earned the achievement $2";

	@ConfigProperty(category = "general.format", comment = "Format for a player join message from MC to Discord.\n$1 will be replaced with the player's username")
	public static String playerJoinMessageFormat = "MC \u00BB $1 joined the game";

	@ConfigProperty(category = "general.format", comment = "Format for a player leave message from MC to Discord.\n$1 will be replaced with the player's username")
	public static String playerLeaveMessageFormat = "MC \u00BB $1 left the game";

	@ConfigProperty(category = "discord", comment = "The email used to login to Discord.\nRequired")
	public static String email = "";

	@ConfigProperty(category = "discord", comment = "The password used to login to Discord.\nRequired")
	public static String password = "";

	@ConfigProperty(category = "discord", comment = "The server ID to connect to.")
	public static String serverId = "";

	@ConfigProperty(category = "discord", comment = "Channels that should be forwarded to MC/MC be forwarded to.\n(Without the # at the beginning)")
	public static String[] channels = new String[0];

	public static void init(File configDir) {
		ConfigManager.instance.configDir = configDir;
		ConfigManager.instance.register(DiscordChat.modId, DCConfig.class, DiscordChat.modId);
		load();
	}

	public static void load() {
		ConfigManager.instance.load(DiscordChat.modId);
		if (email.isEmpty() || password.isEmpty() || serverId.isEmpty() || Arrays.equals(channels, new String[0])) {
			DiscordChat.log.warn("Missing required information, disabling DiscordChat");
			DiscordChat.log.warn("Please go to config/shadowfacts/DiscordChat.cfg and fill out the required fields and restart Minecraft to enable DiscordChat");
			enabled = false;
		} else {
			MiscUtils.discordMessage = Pattern.compile(Pattern.quote(mcToDiscordFormat).replaceAll("\\$\\d", "\\\\E.+\\\\Q"));
			MiscUtils.mcMessage = Pattern.compile(Pattern.quote(discordToMCFormat).replaceAll("\\$\\d", "\\\\E.+\\\\Q"));
			MiscUtils.deathMessage = Pattern.compile(Pattern.quote(deathMessageFormat).replaceAll("\\$\\d", "\\\\E.+\\\\Q"));
			MiscUtils.achievementMessage = Pattern.compile(Pattern.quote(achievementMessageFormat).replaceAll("\\$\\d", "\\\\E.+\\\\Q"));
			MiscUtils.playerJoinMessage = Pattern.compile(Pattern.quote(playerJoinMessageFormat).replaceAll("\\$\\d", "\\\\E.+\\\\Q"));
			MiscUtils.playerLeaveMessage = Pattern.compile(Pattern.quote(playerLeaveMessageFormat).replaceAll("\\$\\d", "\\\\E.+\\\\Q"));
		}
	}

}
