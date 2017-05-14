package net.shadowfacts.discordchat.api;

import net.shadowfacts.discordchat.api.permission.Permission;

import java.io.IOException;
import java.util.List;

/**
 * @author shadowfacts
 */
public interface IConfig {

	void load() throws IOException;

	void save() throws IOException;

	String getToken();

	String getServerID();

	List<String> getChannels();

	String getCommandPrefix();

	Permission getMinimumPermission(String command);

	FilterMode getMCMessageFilterMode();

	String getMCMessageFilter();

	boolean stripFilterPart();

	boolean sendDeathMessages();

	boolean sendAchievementMessages();

	boolean sendJoinLeaveMessages();

	String getFromMCFormat();

	String getFromDiscordFormat();

	String getDeathFormat();

	String getAchievementFormat();

	String getJoinFormat();

	String getLeaveFormat();

	enum FilterMode {
		NONE,
		PREFIX,
		SUFFIX
	}

}
