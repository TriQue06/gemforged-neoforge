package net.trique.gemforged.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
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
    private ItemStack storedItem = ItemStack.EMPTY;
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
        this.life++;

        if (this.life == 1) {
            this.originX = this.getX();
            this.originZ = this.getZ();
            this.targetY = this.getY() + 4.5;
            if (!this.level().isClientSide) this.playSound(SoundEvents.ENCHANTMENT_TABLE_USE, 0.9F, 0.7F);
        }

        if (this.owner == null && this.ownerId != null && this.level() instanceof ServerLevel sl) {
            Entity e = sl.getEntity(this.ownerId);
            if (e instanceof Player p) this.owner = p;
        }

        double dy = this.getY() < this.targetY ? 0.08 : 0.0;
        double speed = this.getY() < this.targetY ? 0.35 : 1.0;
        this.angle += speed;
        double radius = this.getY() < this.targetY ? 0.8 : 1.0;
        double desiredX = this.originX + Math.cos(this.angle) * radius;
        double desiredZ = this.originZ + Math.sin(this.angle) * radius;
        double dx = (desiredX - this.getX()) * 0.45;
        double dz = (desiredZ - this.getZ()) * 0.45;

        this.setDeltaMovement(dx, dy, dz);
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (life >= 40 && life < 100) {
            spinning = true;
            this.setYRot(this.getYRot() + 25);
        } else {
            spinning = false;
        }

        if (this.level() instanceof ServerLevel server) {
            int count = spinning ? 36 : 16;
            server.sendParticles(ParticleTypes.ENCHANT,
                    this.getX(), this.getY(), this.getZ(),
                    count, 0.6, 0.6, 0.6, 0.05);
            server.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    this.getX(), this.getY(), this.getZ(),
                    count / 2, 0.5, 0.5, 0.5, 0.01);
        }

        if (!this.level().isClientSide && this.life < 100 && this.life % 18 == 0) {
            this.playSound(SoundEvents.AMETHYST_BLOCK_RESONATE, 0.6F, 0.9F + this.level().random.nextFloat() * 0.2F);
        }

        if (life == 100 && !this.level().isClientSide) {
            triggerEffect();
        }

        if (life > 120 && !this.level().isClientSide) {
            this.discard();
            if (!storedItem.isEmpty()) {
                ItemStack drop = storedItem.copy();
                drop.setCount(1);
                int newDamage = drop.getDamageValue() + 1;
                if (newDamage < drop.getMaxDamage()) {
                    drop.setDamageValue(newDamage);
                    this.level().addFreshEntity(new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), drop));
                }
            }
        }
    }

    private void triggerEffect() {
        AABB area = this.getBoundingBox().inflate(16);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, area, e -> e.isAlive() && !isAlly(e));
        for (LivingEntity target : targets) {
            for (int i = 0; i < 3; i++) {
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.level());
                if (bolt != null) {
                    bolt.moveTo(target.getX(), target.getY(), target.getZ());
                    this.level().addFreshEntity(bolt);
                }
            }
        }
        this.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 2.0F, 1.0F);
    }

    private boolean isAlly(LivingEntity entity) {
        if (entity == this.owner) return true;
        if (this.owner != null && this.owner.isAlliedTo(entity)) return true;
        if (entity instanceof TamableAnimal t && this.owner != null && t.isOwnedBy(this.owner)) return true;
        if (entity instanceof AbstractHorse h && this.owner != null && h.isTamed() && this.owner.getUUID().equals(h.getOwnerUUID())) return true;
        if (entity instanceof IronGolem) return true;
        if (entity instanceof SnowGolem) return true;
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Item", 10)) {
            this.storedItem = ItemStack.parse(this.registryAccess(), tag.getCompound("Item")).orElse(ItemStack.EMPTY);
        }
        if (tag.hasUUID("Owner")) this.ownerId = tag.getUUID("Owner");
        this.originX = tag.getDouble("OX");
        this.originZ = tag.getDouble("OZ");
        this.targetY = tag.getDouble("TY");
        this.angle = tag.getDouble("Ang");
        this.life = tag.getInt("Life");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (!this.storedItem.isEmpty()) tag.put("Item", this.storedItem.save(this.registryAccess()));
        if (this.ownerId != null) tag.putUUID("Owner", this.ownerId);
        tag.putDouble("OX", this.originX);
        tag.putDouble("OZ", this.originZ);
        tag.putDouble("TY", this.targetY);
        tag.putDouble("Ang", this.angle);
        tag.putInt("Life", this.life);
    }

    @Override
    public ItemStack getItem() {
        return this.storedItem.isEmpty() ? new ItemStack(GemforgedItems.THUNDER_PRISM.get()) : this.storedItem;
    }

    public void setItem(ItemStack stack) {
        this.storedItem = stack;
    }

    public void setOwner(Player player) {
        this.owner = player;
        this.ownerId = player == null ? null : player.getUUID();
    }
}