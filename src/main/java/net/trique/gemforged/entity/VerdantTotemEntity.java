package net.trique.gemforged.entity;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.trique.gemforged.item.GemforgedItems;
import org.joml.Vector3f;

import java.awt.Color;
import java.util.List;

public class VerdantTotemEntity extends Entity implements ItemSupplier {

    private int lifeTicks;
    private Player owner;
    private double originX, originZ, targetY, angle;

    public VerdantTotemEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    public VerdantTotemEntity(Level level, double x, double y, double z, Player owner) {
        this(GemforgedEntities.VERDANT_TOTEM.get(), level);
        this.setPos(x, y, z);
        this.owner = owner;
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public void tick() {
        super.tick();
        this.lifeTicks++;

        if (this.lifeTicks == 1) {
            this.originX = this.getX();
            this.originZ = this.getZ();
            this.targetY = this.getY() + 4.0;
            if (!this.level().isClientSide) {
                this.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 1.0F, 0.7F);
            }
        }

        double dy = this.getY() < this.targetY ? 0.05 : 0.0;
        this.angle += 0.35;
        double desiredX = this.originX + Math.cos(this.angle) * 0.8;
        double desiredZ = this.originZ + Math.sin(this.angle) * 0.8;
        double dx = (desiredX - this.getX()) * 0.4;
        double dz = (desiredZ - this.getZ()) * 0.4;

        this.setDeltaMovement(dx, dy, dz);
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setYRot(this.getYRot() + 8f);

        if (this.level() instanceof ServerLevel server) {
            if (lifeTicks % 4 == 0) {
                server.sendParticles(
                        new DustParticleOptions(new Vector3f(0.2F, 0.9F, 0.2F), 1.0F),
                        this.getX(), this.getY() + 0.5, this.getZ(),
                        4, 0.35, 0.35, 0.35, 0.01);
            }
            for (int i = 0; i < 6; i++) {
                double a = (Math.PI * 2 * i) / 6.0;
                double px = this.getX() + Math.cos(a) * 1.5;
                double pz = this.getZ() + Math.sin(a) * 1.5;
                double py = this.getY() + 0.1 * Math.sin((lifeTicks + i * 5) * 0.2);
                server.sendParticles(ParticleTypes.COMPOSTER, px, py, pz, 1, 0, 0, 0, 0);
            }
            if (lifeTicks % 20 == 0) {
                AABB box = new AABB(getX() - 6, getY() - 6, getZ() - 6, getX() + 6, getY() + 6, getZ() + 6);
                List<LivingEntity> nearby = level().getEntitiesOfClass(LivingEntity.class, box);
                for (LivingEntity e : nearby) {
                    if (owner != null && (e.isAlliedTo(owner) || e == owner)) {
                        e.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 1, false, true, true));
                    }
                }
            }
        }

        if (lifeTicks == 200 && !this.level().isClientSide) {
            explode();
        }
    }

    private void explode() {
        if (!(level() instanceof ServerLevel server)) return;

        server.playSound(null, getX(), getY(), getZ(),
                SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 4.0F, 1.0F);

        for (int i = 0; i < 120; i++) {
            server.sendParticles(
                    new DustParticleOptions(new Vector3f(0.4F, 1.0F, 0.4F), 1.5F),
                    getX(), getY(), getZ(),
                    1, random.nextGaussian(), random.nextGaussian(), random.nextGaussian(), 0.04);
        }

        AABB area = new AABB(getX() - 6, getY() - 6, getZ() - 6,
                getX() + 6, getY() + 6, getZ() + 6);

        List<LivingEntity> list = server.getEntitiesOfClass(LivingEntity.class, area);
        for (LivingEntity e : list) {
            boolean isFriendly = owner != null && (e.isAlliedTo(owner) || e == owner);
            if (isFriendly || isUndead(e)) {
                e.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 4, false, true, true));
                e.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1, false, true, true));
            }
        }

        this.discard();
    }

    private boolean isUndead(LivingEntity e) {
        return e instanceof Zombie || e instanceof Skeleton || e instanceof WitherSkeleton ||
                e instanceof Stray || e instanceof Drowned || e instanceof ZombifiedPiglin ||
                e instanceof Husk || e instanceof Phantom || e instanceof WitherBoss ||
                e instanceof Zoglin;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    public ItemStack getItem() {
        return new ItemStack(GemforgedItems.VERDANT_TOTEM.get());
    }

    public void setOwner(Player player) {
        this.owner = player;
    }
}