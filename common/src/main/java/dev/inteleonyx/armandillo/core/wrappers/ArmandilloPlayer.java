package dev.inteleonyx.armandillo.core.wrappers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

/**
 * @author Inteleonyx. Created on 02/12/2025
 * @project armandillo
 */

public class ArmandilloPlayer {
    private final ServerPlayer player;

    public ArmandilloPlayer(ServerPlayer player) {
        this.player = player;
    }

    public Component get_name() {
        return player.getName();
    }

    public boolean hurt(DamageSource source, float amount) {
        return this.player.hurt(source, amount);
    }

    public void set_player_input(float f, float g, boolean bl, boolean bl2) {
        this.player.setPlayerInput(f, g, bl, bl2);
    }

    public void attack(Entity entity) {
        this.player.attack(entity);
    }

    public boolean is(Entity entity) {
        return this.player.is(entity);
    }

    public void move(MoverType moverType, Vec3 vec3) {
        this.player.move(moverType, vec3);
    }

    public void remove(Entity.RemovalReason reason) {
        this.player.remove(reason);
    }

    public void swing(InteractionHand hand) {
        this.player.swing(hand);
    }

    public void swing(InteractionHand hand, boolean bl) {
        this.player.swing(hand, bl);
    }

    public void take(Entity entity, int i) {
        this.player.take(entity, i);
    }

    public void tick() {
        this.player.tick();
    }

    public void add(CompoundTag compoundTag) {
        this.player.addAdditionalSaveData(compoundTag);
    }

    public BlockPos adjustSpawnLocation(ServerLevel serverLevel, BlockPos blockPos) {
        return this.player.adjustSpawnLocation(serverLevel, blockPos);
    }



    public ServerPlayer get_native_player() {
        return this.player;
    }
}
