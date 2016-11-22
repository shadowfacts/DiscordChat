package net.shadowfacts.discordchat.api;

import java.util.Set;

/**
 * @author shadowfacts
 */
public interface IMinecraftAdapter {

	void sendMessage(String message);

	float getTPS(int dimension);

	Set<String> getOnlinePlayers();
}
