package net.shadowfacts.discordchat.one_seven_ten;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.shadowfacts.discordchat.api.IMinecraftAdapter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shadowfacts
 */
public class OneSevenTenAdapter implements IMinecraftAdapter {

	@Override
	public void sendMessage(String message) {
		MinecraftServer server = MinecraftServer.getServer();
		if (server != null) {
			server.getConfigurationManager().sendChatMsg(ForgeHooks.newChatWithLinks(message));
		}
	}

	@Override
	public void sendMessageToPlayer(String message, String player) {
		MinecraftServer server = MinecraftServer.getServer();
		EntityPlayer mcPlayer = server.getConfigurationManager().getPlayerByUsername(player);
		mcPlayer.addChatMessage(ForgeHooks.newChatWithLinks(message));
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



	@Override
	public String teleportPlayerToSpawn(String username) {
		MinecraftServer server = MinecraftServer.getServer();
		WorldServer world = server.worldServerForDimension(0);
		ChunkCoordinates spawn = world.getSpawnPoint();
		ServerConfigurationManager configManager = server.getConfigurationManager();
		EntityPlayerMP player = configManager.getPlayerByUsername(username);
		if (player != null) {
			if (player.dimension != 0) {
				int original = player.dimension;
				configManager.transferPlayerToDimension(player, 0, new DummyTeleporter(world));

				if (original == 1 && player.isEntityAlive()) {
					world.spawnEntityInWorld(player);
					world.updateEntityWithOptionalForce(player, false);
				}
			}
			player.setPositionAndUpdate(spawn.posX + 0.5d, spawn.posY, spawn.posZ + 0.5d);
		} else {
			GameProfile profile = server.getPlayerProfileCache().getGameProfileForUsername(username);

			if (profile != null && profile.isComplete()) {
				FakePlayer fakePlayer = FakePlayerFactory.get(world, profile);
				IPlayerFileData saveHandler = world.getSaveHandler().getPlayerNBTManager();
				NBTTagCompound tag = saveHandler.readPlayerData(fakePlayer);

				if (tag == null) {
					return "Unknown player: " + username;
				}

				fakePlayer.dimension = 0;
				fakePlayer.posX = spawn.posX + 0.5d;
				fakePlayer.posY = spawn.posY;
				fakePlayer.posZ = spawn.posZ + 0.5d;

				saveHandler.writePlayerData(fakePlayer);
			} else {
				return "Unknown player: " + username;
			}
		}
		return null;
	}

	@Override
	public long getWorldTime(int dimension) {
		return MinecraftServer.getServer().worldServerForDimension(dimension).getWorldTime();
	}

}
