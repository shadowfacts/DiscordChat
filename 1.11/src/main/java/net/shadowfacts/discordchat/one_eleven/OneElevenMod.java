package net.shadowfacts.discordchat.one_eleven;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.shadowfacts.discordchat.api.IDiscordChat;
import net.shadowfacts.discordchat.api.ILogger;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;
import net.shadowfacts.discordchat.core.DiscordChat;

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

	@Mod.Instance(MOD_ID)
	public static OneElevenMod instance;

	public static IMinecraftAdapter minecraftAdapter = new OneElevenAdapter();
	public static ILogger logger = new Logger();
	public static Config config = new Config();
	public static IDiscordChat discordChat;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) throws IOException {
		config.init(new File(event.getModConfigurationDirectory(), "shadowfacts/DiscordChat.conf"));
		discordChat = new DiscordChat(minecraftAdapter, logger, config);

		MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());
	}

}
