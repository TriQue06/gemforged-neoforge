package net.trique.gemforged.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.trique.gemforged.item.GemforgedItems;

import java.util.List;
import java.util.UUID;

public class ThunderPrismEntity extends Entity implements ItemSupplier {

    private int life;
    private Player owner;
    private UUID ownerId;

    private double originX, originZ, targetY;
    private double angle;
    private boolean spinning = false;

    public ThunderPrismEntity(EntityType<? extends ThunderPrismEntity> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    public ThunderPrismEntity(Level level, double x, double y, double z, Player owner) {
        this(GemforgedEntities.THUNDER_PRISM.get(), level);
        this.setPos(x, y, z);
        this.owner = owner;
        this.ownerId = owner.getUUID();
    }

    @Override
    public void tick() {
        super.tick();
        life++;

        if (life == 1) {
            originX = getX();
            originZ = getZ();
            targetY = getY() + 4.5;
            if (!level().isClientSide)
                playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 0.9F, 0.7F);
        }

        if (owner == null && ownerId != null && level() instanceof ServerLevel sl) {
            Entity e = sl.getEntity(ownerId);
            if (e instanceof Player p) owner = p;
        }

        double dy = getY() < targetY ? 0.08 : 0.0;
        double speed = getY() < targetY ? 0.35 : 1.0;
        angle += speed;
        double radius = getY() < targetY ? 0.8 : 1.0;

        double desiredX = originX + Math.cos(angle) * radius;
        double desiredZ = originZ + Math.sin(angle) * radius;
        double dx = (desiredX - getX()) * 0.45;
        double dz = (desiredZ - getZ()) * 0.45;

        setDeltaMovement(dx, dy, dz);
        move(MoverType.SELF, getDeltaMovement());

        spinning = (life >= 40 && life < 100);
        if (spinning) setYRot(getYRot() + 25);

        if (level() instanceof ServerLevel server) {
            int count = spinning ? 36 : 16;
            server.sendParticles(
                    ParticleTypes.ENCHANT, getX(), getY(), getZ(),
                    count, 0.6, 0.6, 0.6, 0.05
            );
            server.sendParticles(
                    ParticleTypes.ELECTRIC_SPARK, getX(), getY(), getZ(),
                    count / 2, 0.5, 0.5, 0.5, 0.01
            );
        }

        if (!level().isClientSide && life < 100 && life % 18 == 0) {
            playSound(SoundEvents.AMETHYST_BLOCK_RESONATE, 0.6F,
                    0.9F + level().random.nextFloat() * 0.2F);
        }

        if (life == 100 && !level().isClientSide)
            triggerEffect();

        if (life > 120 && !level().isClientSide)
            discard();
    }

    private void triggerEffect() {
        AABB area = getBoundingBox().inflate(16);
        List<LivingEntity> list = level().getEntitiesOfClass(
                LivingEntity.class, area,
                e -> e.isAlive() && !isAlly(e)
        );

        for (LivingEntity target : list) {
            for (int i = 0; i < 3; i++) {
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level());
                if (bolt != null) {
                    bolt.moveTo(target.getX(), target.getY(), target.getZ());
                    level().addFreshEntity(bolt);
                }
            }
        }

        playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 2.0F, 1.0F);
    }

    private boolean isAlly(LivingEntity e) {
        if (e == owner) return true;
        if (owner != null && owner.isAlliedTo(e)) return true;
        if (e instanceof TamableAnimal t && owner != null && t.isOwnedBy(owner)) return true;
        if (e instanceof AbstractHorse h && owner != null &&
                h.isTamed() && owner.getUUID().equals(h.getOwnerUUID())) return true;

        return e instanceof IronGolem || e instanceof SnowGolem;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("Owner")) ownerId = tag.getUUID("Owner");
        originX = tag.getDouble("OX");
        originZ = tag.getDouble("OZ");
        targetY = tag.getDouble("TY");
        angle = tag.getDouble("Ang");
        life = tag.getInt("Life");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (ownerId != null) tag.putUUID("Owner", ownerId);
        tag.putDouble("OX", originX);
        tag.putDouble("OZ", originZ);
        tag.putDouble("TY", targetY);
        tag.putDouble("Ang", angle);
        tag.putInt("Life", life);
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(GemforgedItems.THUNDER_PRISM.get());
    }
}