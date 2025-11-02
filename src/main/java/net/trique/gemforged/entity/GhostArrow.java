package net.trique.gemforged.entity;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;

public class GhostArrow extends AbstractArrow {
    private static final int MAX_LIFETIME_TICKS = 20 * 3;
    private static final float EXPLOSION_POWER = 1.0F;
    private int lifetime;

    public GhostArrow(EntityType<? extends GhostArrow> type, Level level) {
        super(type, level);
        this.pickup = Pickup.DISALLOWED;
    }

    public GhostArrow(EntityType<? extends GhostArrow> type, LivingEntity owner, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(type, owner, level, pickupItemStack, firedFromWeapon);
        this.pickup = Pickup.DISALLOWED;
    }

    public GhostArrow(EntityType<? extends GhostArrow> type, double x, double y, double z, Level level, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(type, x, y, z, level, pickupItemStack, firedFromWeapon);
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    public void tick() {
        super.tickCount++;
        Vec3 motion = this.getDeltaMovement();

        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = motion.horizontalDistance();
            this.setYRot((float)(Mth.atan2(motion.x, motion.z) * 180.0F / Math.PI));
            this.setXRot((float)(Mth.atan2(motion.y, d0) * 180.0F / Math.PI));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        BlockPos blockpos = this.blockPosition();
        if (this.isInWaterOrRain() || this.level().getBlockState(blockpos).is(Blocks.POWDER_SNOW) || this.isInFluidType((f, h) -> this.canFluidExtinguish(f))) {
            this.clearFire();
        }

        this.inGround = false;
        this.inGroundTime = 0;

        Vec3 startPos = this.position();
        Vec3 endPos = startPos.add(motion);

        EntityHitResult entityHit = this.findHitEntity(startPos, endPos);

        if (entityHit != null && !this.isRemoved()) {
            this.onHit(entityHit);
            this.hasImpulse = true;
        }

        motion = this.getDeltaMovement();
        double dx = motion.x;
        double dy = motion.y;
        double dz = motion.z;

        if (this.isCritArrow()) {
            for (int i = 0; i < 4; i++) {
                this.level().addParticle(ParticleTypes.CRIT, this.getX() + dx * i / 4.0D, this.getY() + dy * i / 4.0D, this.getZ() + dz * i / 4.0D, -dx, -dy + 0.2D, -dz);
            }
        }

        double nx = this.getX() + dx;
        double ny = this.getY() + dy;
        double nz = this.getZ() + dz;
        double d4 = motion.horizontalDistance();

        this.setYRot((float)(Mth.atan2(dx, dz) * 180.0F / Math.PI));
        this.setXRot((float)(Mth.atan2(dy, d4) * 180.0F / Math.PI));
        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));

        float friction = 0.99F;
        if (this.isInWater()) {
            for (int j = 0; j < 4; j++) {
                this.level().addParticle(ParticleTypes.BUBBLE, nx - dx * 0.25D, ny - dy * 0.25D, nz - dz * 0.25D, dx, dy, dz);
            }
            friction = this.getWaterInertia();
        }

        this.setDeltaMovement(this.getDeltaMovement().scale(friction));
        this.applyGravity();

        this.setPos(nx, ny, nz);
        this.checkInsideBlocks();

        if (!this.level().isClientSide && ++this.lifetime >= MAX_LIFETIME_TICKS) {
            Entity source = this.getOwner() != null ? this.getOwner() : this;
            this.level().explode(source, this.getX(), this.getY(), this.getZ(), EXPLOSION_POWER, ExplosionInteraction.NONE);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {}

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.AIR);
    }
}