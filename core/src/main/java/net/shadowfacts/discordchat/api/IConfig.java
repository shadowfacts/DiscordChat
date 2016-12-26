package net.shadowfacts.discordchat.api;

import net.shadowfacts.discordchat.api.permission.Permission;

import java.io.IOException;

/**
 * @author shadowfacts
 */
public interface IConfig {

	void load() throws IOException;

	void save() throws IOException;

	String getToken();

	String getServerID();

	String getChannel();

	String getCommandPrefix();

	Permission getMinimumPermission(String command);

	boolean sendDeathMessages();

	boolean sendAchievementMessages();

	boolean sendJoinLeaveMessages();

	String getFromMCFormat();

	String getFromDiscordFormat();

	String getDeathFormat();

	String getAchievementFormat();

	String getJoinFormat();

	String getLeaveFormat();

}
