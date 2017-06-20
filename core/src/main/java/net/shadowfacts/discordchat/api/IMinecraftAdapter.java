package net.shadowfacts.discordchat.api;

import java.util.Set;

/**
 * @author shadowfacts
 */
public interface IMinecraftAdapter {

	void sendMessage(String message);

	void sendMessageToPlayer(String message, String player);

	int[] getAllDimensions();

	double getTickTime(int dimension);

	Set<String> getOnlinePlayers();

	void executeCommand(String command);

	String teleportPlayerToSpawn(String username);

	long getWorldTime(int dimension);

}
