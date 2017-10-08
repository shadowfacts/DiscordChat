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

	Long getServerID();

	List<Long> getChannelIDs();

	String getCommandPrefix();

	Permission getMinimumPermission(String command);

	FilterMode getMCMessageFilterMode();

	String getMCMessageFilter();

	boolean stripFilterPart();

	boolean sendDeathMessages();

	boolean sendAchievementMessages();

	boolean sendJoinLeaveMessages();

	boolean sendServerOnlineOfflineMessages();

	String getDefaultColor();

	String getFromMCFormat();

	String getFromDiscordFormat();

	String getFromMCPrivateFormat();

	String getFromDiscordPrivateFormat();

	String getDeathFormat();

	String getAchievementFormat();

	String getJoinFormat();

	String getLeaveFormat();

	String getCommandFormat();

	enum FilterMode {
		NONE,
		PREFIX,
		SUFFIX
	}

}
