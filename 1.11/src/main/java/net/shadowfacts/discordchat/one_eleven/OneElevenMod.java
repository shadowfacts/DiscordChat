package net.shadowfacts.discordchat.one_eleven;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.shadowfacts.discordchat.api.IConfig;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.core.Config;
import net.shadowfacts.discordchat.core.DiscordChat;
import net.shadowfacts.discordchat.core.Logger;

import java.io.File;
import java.io.IOException;

/**
 * @author shadowfacts
 */
@Mod(modid = OneElevenMod.MOD_ID, name = OneElevenMod.NAME, version = OneElevenMod.VERSION)
public class OneElevenMod {

	public static final String MOD_ID = "discordchat";
	public static final String NAME = "Discord Chat";
	public static final String VERSION = "@VERSION@";

	public static IMinecraftAdapter minecraftAdapter = new OneElevenAdapter();
	public static IConfig config;
	public static IDiscordChat discordChat;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		discordChat = new DiscordChat(minecraftAdapter);
		config = discordChat.getConfig();
		config.init(new File(event.getModConfigurationDirectory(), "shadowfacts/DiscordChat.conf"));
		discordChat.connect();

		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
	}

	@Mod.EventHandler
	public void serverStarted(FMLServerStartedEvent event) {
		discordChat.start();
	}

}
