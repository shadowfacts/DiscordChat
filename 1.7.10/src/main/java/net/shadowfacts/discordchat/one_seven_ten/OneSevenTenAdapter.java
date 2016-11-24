package net.shadowfacts.discordchat.one_seven_ten;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.DimensionManager;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shadowfacts
 */
public class OneSevenTenAdapter implements IMinecraftAdapter {

	@Override
	public void sendMessage(String message) {
		MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(message));
	}

	@Override
	public int[] getAllDimensions() {
		Integer[] boxed = DimensionManager.getIDs();
		int[] unboxed = new int[boxed.length];
		for (int i = 0; i < boxed.length; i++) {
			unboxed[i] = boxed[i];
		}
		return unboxed;
	}

	@Override
	public double getTickTime(int dimension) {
		MinecraftServer server = MinecraftServer.getServer();
		long[] tickTimes = server.worldTickTimes.get(dimension);
		long sum = 0;
		for (int i = 0; i < tickTimes.length; i++) {
			sum += tickTimes[i];
		}
		return sum / tickTimes.length * 1.0E-6D;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<String> getOnlinePlayers() {
		return (Set<String>)MinecraftServer.getServer().getConfigurationManager().playerEntityList.stream()
				.map(it -> ((EntityPlayer)it).getDisplayName())
				.collect(Collectors.toSet());
	}

	@Override
	public void executeCommand(String command) {
		MinecraftServer.getServer().getCommandManager().executeCommand(DummySender.INSTANCE, command);
	}

}
