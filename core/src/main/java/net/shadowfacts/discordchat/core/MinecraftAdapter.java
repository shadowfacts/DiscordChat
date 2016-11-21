package net.shadowfacts.discordchat.core;

/**
 * @author shadowfacts
 */
public interface MinecraftAdapter {

	void sendMessage(String message);

	float getTPS(int dimension);

}
