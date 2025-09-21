package net.trique.gemforged.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.trique.gemforged.item.GemforgedItems;

import java.util.List;

public class ZirconPrismEntity extends Entity implements ItemSupplier {
    private int life;
    private ItemStack storedItem = ItemStack.EMPTY;
    private Player owner;

    public ZirconPrismEntity(EntityType<? extends ZirconPrismEntity> type, Level level) {
        super(type, level);
    }

    public ZirconPrismEntity(Level level, double x, double y, double z, Player owner) {
        this(GemforgedEntities.ZIRCON_PRISM.get(), level);
        this.setPos(x, y, z);
        this.owner = owner;
    }

    @Override
    public void tick() {
        super.tick();
        this.life++;

        // 1. Yavaş yukarı süzülme (yaklaşık 5 blok)
        if (life < 60) {
            this.setDeltaMovement(0, 0.08, 0);
        } else {
            this.setDeltaMovement(0, 0, 0);
        }

        // Hareket uygula
        this.move(MoverType.SELF, this.getDeltaMovement());

        // 2. Dönme (40. tick’ten sonra hızlı dönüş)
        if (life > 40) {
            this.setYRot(this.getYRot() + 20);
        }

        // 3. Partiküller
        if (this.level() instanceof ServerLevel server) {
            int count = (life > 40) ? 30 : 10; // daha sonra daha yoğun
            server.sendParticles(ParticleTypes.ENCHANT,
                    this.getX(), this.getY(), this.getZ(),
                    count, 0.6, 0.6, 0.6, 0.05);
            server.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    this.getX(), this.getY(), this.getZ(),
                    count / 2, 0.5, 0.5, 0.5, 0.01);
        }

        // 4. 120. tick → yıldırımlar
        if (life == 120 && !this.level().isClientSide) {
            triggerEffect();
        }

        // 5. 140. tick → yere düşüp item olsun
        if (life > 140 && !this.level().isClientSide) {
            this.discard();
            if (!storedItem.isEmpty()) {
                this.level().addFreshEntity(new ItemEntity(this.level(),
                        this.getX(), this.getY(), this.getZ(), storedItem));
            }
        }
    }

    private void triggerEffect() {
        AABB area = this.getBoundingBox().inflate(16);
        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class, area,
                e -> e.isAlive() && !isAlly(e));

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
        if (owner != null && owner.isAlliedTo(entity)) return true;
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    public ItemStack getItem() {
        return storedItem.isEmpty() ? new ItemStack(GemforgedItems.ZIRCON_PRISM.get()) : storedItem;
    }

    public void setItem(ItemStack stack) {
        this.storedItem = stack;
    }

    public void setOwner(Player player) {
        this.owner = player;
    }
}
