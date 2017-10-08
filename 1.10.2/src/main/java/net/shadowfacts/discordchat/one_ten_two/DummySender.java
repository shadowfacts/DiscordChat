package net.shadowfacts.discordchat.one_ten_two;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nullable;

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
	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}

	@Override
	public void sendMessage(ITextComponent component) {
		OneTenTwoMod.discordChat.sendMessage(OneTenTwoMod.discordChat.getFormatter().command("```" + component.getUnformattedText() + "```"));
	}

	@Override
	public boolean canUseCommand(int permLevel, String commandName) {
		return true;
	}

	@Override
	public BlockPos getPosition() {
		return BlockPos.ORIGIN;
	}

	@Override
	public Vec3d getPositionVector() {
		return Vec3d.ZERO;
	}

	@Override
	public World getEntityWorld() {
		return getServer().worldServerForDimension(0);
	}

	@Nullable
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

	@Nullable
	@Override
	public MinecraftServer getServer() {
		return FMLCommonHandler.instance().getMinecraftServerInstance();
	}

}
