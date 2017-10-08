package net.shadowfacts.discordchat.one_eight_nine;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * @author shadowfacts
 */
public class DummySender implements ICommandSender {

	public static final ICommandSender INSTANCE = new DummySender();

	@Override
	public String getName() {
		return "[DiscordChat]";
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText(getName());
	}

	@Override
	public void addChatMessage(IChatComponent component) {
		OneEightNineMod.discordChat.sendMessage(OneEightNineMod.discordChat.getFormatter().command("```" + component.getUnformattedText() + "```"));
	}

	@Override
	public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
		return true;
	}

	@Override
	public BlockPos getPosition() {
		return BlockPos.ORIGIN;
	}

	@Override
	public Vec3 getPositionVector() {
		return new Vec3(0, 0, 0);
	}

	@Override
	public World getEntityWorld() {
		return MinecraftServer.getServer().worldServerForDimension(0);
	}

	@Override
	public Entity getCommandSenderEntity() {
		return null;
	}

	@Override
	public boolean sendCommandFeedback() {
		return false;
	}

	@Override
	public void setCommandStat(CommandResultStats.Type type, int amount) {
	}

}
