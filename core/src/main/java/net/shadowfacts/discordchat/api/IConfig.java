package net.shadowfacts.discordchat.api;

import net.shadowfacts.discordchat.api.permission.Permission;

import java.util.Map;

/**
 * @author shadowfacts
 */
public interface IConfig {

	String getToken();

	String getServerID();

	String getChannelID();

	String getCommandPrefix();

	Map<String, Permission> getPermissions();

	void setPermissions(Map<String, Permission> permissions);

}
