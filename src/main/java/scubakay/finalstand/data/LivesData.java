package scubakay.finalstand.data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import scubakay.finalstand.config.ModConfig;
import scubakay.finalstand.networking.ModMessages;
import scubakay.finalstand.util.IEntityDataSaver;

import java.util.Random;

public class LivesData {
    public static void addLives(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.fs_getPersistentData();
        int lives = nbt.getInt("lives");

        int maxLives = ModConfig.getMaxLives();
        if(lives + amount >= maxLives) {
            lives = maxLives;
        } else {
            lives += amount;
        }

        nbt.putInt("lives", lives);
        syncLives((ServerPlayerEntity) player);
    }

    public static int removeLives(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.fs_getPersistentData();
        int lives = nbt.getInt("lives");

        lives -= amount;

        if(lives < 0) {
            lives = 0;
        }

        nbt.putInt("lives", lives);
        syncLives((ServerPlayerEntity) player);
        return lives;
    }

    public static int randomizeLives(IEntityDataSaver player) {
        NbtCompound nbt = player.fs_getPersistentData();

        int lives = determineRandomLives();

        nbt.putInt("lives", lives);
        syncLives((ServerPlayerEntity) player);
        return lives;
    }

    public static void setLives(IEntityDataSaver player, int lives) {
        NbtCompound nbt = player.fs_getPersistentData();
        nbt.putInt("lives", lives);
        syncLives((ServerPlayerEntity) player);
    }

    public static void syncLives(ServerPlayerEntity player) {
        NbtCompound nbt = ((IEntityDataSaver) player).fs_getPersistentData();
        int lives = nbt.getInt("lives");
        TeamState.setPlayerTeam(lives, player);

        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(lives);
        ServerPlayNetworking.send(player, ModMessages.LIVES_SYNC, buffer);
    }

    private static int determineRandomLives() {
        int minLives = ModConfig.getMinLives();
        int maxLives = ModConfig.getMaxLives();

        if(minLives > maxLives) {
            minLives = maxLives;
        }

        Random random = new Random();
        int lives = random.nextInt(maxLives - minLives + 1);
        lives += minLives;

        return lives;
    }
}
