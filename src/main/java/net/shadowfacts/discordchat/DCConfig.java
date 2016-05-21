package net.shadowfacts.discordchat;

import net.minecraftforge.common.config.Configuration;
import net.shadowfacts.config.Config;
import net.shadowfacts.config.ConfigManager;
import net.shadowfacts.discordchat.utils.MiscUtils;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author shadowfacts
 */
@Config(name = DiscordChat.modId)
public class DCConfig {

	public static Configuration config;

	@Config.Prop(description = "Enable DiscordChat")
	public static boolean enabled = true;

	@Config.Prop(description = "Send player death messages to Discord")
	public static boolean sendPlayerDeathMessages = true;

	@Config.Prop(description = "Send player achievement messages to Discord")
	public static boolean sendPlayerAchievementMessages = true;

	@Config.Prop(description = "Send player join/leave messages to Discord")
	public static boolean sendPlayerJoinLeaveMessages = true;

	@Config.Prop(category = "general.format", description = "Format for a normal message from MC to Discord.\n$1 will be replaced with the sender's username and $2 will be replaced with the message")
	public static String mcToDiscordFormat = "MC \u00BB <$1> $2";

	@Config.Prop(category = "general.format", description = "Format for a normal message from Discord to MC.\n$1 will be replaced with the channel, $2 will be replaced with the sender's username, and $3 will be replaced with the message")
	public static String discordToMCFormat = "$1 \u00BB <$2> $3";

	@Config.Prop(category = "general.format", description = "Format for a player death message from MC to Discord.\n$1 will be replaced with the player's username and $2 will be replaced with the death message")
	public static String deathMessageFormat = "MC \u00BB $2";

	@Config.Prop(category = "general.format", description = "Format for a player achievement message from MC to Discord.\n$1Will be replaced with the player's username, $2 will be replaced with the achievement")
	public static String achievementMessageFormat = "MC \u00BB $1 has just earned the achievement $2";

	@Config.Prop(category = "general.format", description = "Format for a player join message from MC to Discord.\n$1 will be replaced with the player's username")
	public static String playerJoinMessageFormat = "MC \u00BB $1 joined the game";

	@Config.Prop(category = "general.format", description = "Format for a player leave message from MC to Discord.\n$1 will be replaced with the player's username")
	public static String playerLeaveMessageFormat = "MC \u00BB $1 left the game";

	@Config.Prop(category = "discord", description = "The token used to identify your bot to Discord.\nRequired")
	public static String botToken = "";

	@Config.Prop(category = "discord", description = "The server ID to connect to.")
	public static String serverId = "";

	@Config.Prop(category = "discord", description = "Channels that should be forwarded to MC/MC be forwarded to.\n(Without the # at the beginning)")
	public static String[] channels = new String[0];

	public static void init(File configDir) {
		config = new Configuration(new File(configDir, "shadowfacts/DiscordChat.cfg"));
		load();
	}

	public static void load() {

		ConfigManager.load(DCConfig.class, Configuration.class, config);

		if (config.hasChanged()) config.save();

		if (botToken.isEmpty() || serverId.isEmpty() || Arrays.equals(channels, new String[0])) {
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
