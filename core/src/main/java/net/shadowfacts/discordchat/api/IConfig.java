package net.shadowfacts.discordchat.api;

import net.shadowfacts.discordchat.api.permission.Permission;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author shadowfacts
 */
public interface IConfig {

	void init(File file) throws IOException;

	void save() throws IOException;

	String getToken();

	String getServerID();

	String getChannel();

	String getCommandPrefix();

	Map<String, Permission> getPermissions();

	void setPermissions(Map<String, Permission> permissions);

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
