package net.shadowfacts.discordchat.one_eleven;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shadowfacts
 */
public class OneElevenAdapter implements IMinecraftAdapter {

	@Override
	public void sendMessage(String message) {
		FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendChatMsg(new TextComponentString(message));
	}

	@Override
	public int[] getAllDimensions() {
		Integer[] boxed = DimensionManager.getIDs();
		int[] array = new int[boxed.length];
		for (int i = 0; i < boxed.length; i++) {
			array[i] = boxed[i];
		}
		return array;
	}

	@Override
	public double getTPS(int dimension) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		long[] tickTimes = server.worldTickTimes.get(dimension);
		long sum = 0;
		for (int i = 0; i < tickTimes.length; i++) {
			sum += tickTimes[i];
		}
		double mean = sum / tickTimes.length * 1.0E-6D;
		return Math.min(1000 / mean, 20);
	}

	@Override
	public Set<String> getOnlinePlayers() {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().stream()
				.map(EntityPlayer::getName)
				.collect(Collectors.toSet());
	}

	@Override
	public void executeCommand(String command) {
		FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(DummySender.INSTANCE, command);
	}

}
