package net.shadowfacts.discordchat.one_seven_ten;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class DummySender implements ICommandSender {

	public static final ICommandSender INSTANCE = new DummySender();

	@Override
	public String getCommandSenderName() {
		return "[DiscordChat]";
	}

	@Override
	public IChatComponent getFormattedCommandSenderName() {
		return new ChatComponentText(getCommandSenderName());
	}


	@Override
	public void addChatMessage(IChatComponent component) {
		OneSevenTenMod.discordChat.sendMessage(OneSevenTenMod.discordChat.getFormatter().command("```" + component.getUnformattedText() + "```"));
	}

	@Override
	public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
		return true;
	}

	@Override
	public ChunkCoordinates getCommandSenderPosition() {
		return new ChunkCoordinates(0, 0, 0);
	}

	@Override
	public World getEntityWorld() {
		return MinecraftServer.getServer().worldServerForDimension(0);
	}

}
