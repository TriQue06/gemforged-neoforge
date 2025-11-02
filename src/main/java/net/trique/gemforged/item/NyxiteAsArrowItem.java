package net.trique.gemforged.item;

import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.trique.gemforged.entity.GemforgedEntities;
import net.trique.gemforged.entity.GhostArrowEntity;

public class NyxiteAsArrowItem extends ArrowItem {
    public NyxiteAsArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new GhostArrowEntity(GemforgedEntities.GHOST_ARROW.get(), shooter, level, ammo.copyWithCount(1), weapon);
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        return new GhostArrowEntity(GemforgedEntities.GHOST_ARROW.get(), pos.x(), pos.y(), pos.z(), level, stack.copyWithCount(1), null);
    }

    @Override
    public boolean isInfinite(ItemStack ammo, ItemStack bow, LivingEntity livingEntity) {
        return false;
    }
}