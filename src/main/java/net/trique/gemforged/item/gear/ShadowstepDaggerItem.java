package net.trique.gemforged.item.gear;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import net.trique.gemforged.item.GemforgedItems;

public class ShadowstepDaggerItem extends SwordItem {

    public static final String TAG_COMBO = "onyx_combo";
    public static final String TAG_ACTIVE_UNTIL = "onyx_active_until";

    public static final int MAX_COMBO = 6;
    public static final int COMBO_ACTIVE_TICKS = 20 * 5;
    public static final int COOLDOWN_TICKS = 20 * 30;

    public static final ResourceLocation MOD_DAMAGE_ID =
            ResourceLocation.parse("gemforged:onyx_combo_damage");
    public static final ResourceLocation MOD_SPEED_ID =
            ResourceLocation.parse("gemforged:onyx_combo_speed");

    private static final double TP_MIN = 2.0;
    private static final double TP_MAX = 3.0;

    private static final Vector3f SHADOW_PURPLE =
            new Vector3f(0.2627f, 0.1569f, 0.3843f);
    private static final Vector3f SHADOW_LIGHT =
            new Vector3f(0.4745f, 0.3294f, 0.6118f);

    public ShadowstepDaggerItem(Item.Properties props) {
        super(Tiers.DIAMOND,
                props.attributes(
                        SwordItem.createAttributes(Tiers.DIAMOND, 2, -2.0f)
                ));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof Player player)) return true;
        Level level = player.level();
        if (level.isClientSide) return true;

        CustomData cd = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = cd.copyTag();

        int combo = tag.getInt(TAG_COMBO);
        long now = level.getGameTime();

        if (combo == 0) {
            if (!player.getAbilities().instabuild && findChargeResource(player).isEmpty()) {
                return true;
            }
            addComboModifiers(player);
        }

        if (player instanceof ServerPlayer sp && target.isAlive()) {
            tryTeleportAround((ServerLevel) level, sp, target);
            sp.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
        }

        combo++;
        tag.putInt(TAG_COMBO, combo);
        tag.putLong(TAG_ACTIVE_UNTIL, now + COMBO_ACTIVE_TICKS);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        if (combo >= MAX_COMBO) {
            if (player.getAbilities().instabuild || !findChargeResource(player).isEmpty()) {
                if (!player.getAbilities().instabuild)
                    findChargeResource(player).shrink(1);
                player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            }
            clearCombo(player, stack);
        }

        return true;
    }

    public void clearCombo(Player player, ItemStack stack) {
        CustomData cd = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = cd.copyTag();
        tag.putInt(TAG_COMBO, 0);
        tag.remove(TAG_ACTIVE_UNTIL);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        removeComboModifiers(player);
    }

    private ItemStack findChargeResource(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack s = player.getInventory().getItem(i);
            if (s.is(GemforgedItems.NYXITE.get())) return s;
        }
        return ItemStack.EMPTY;
    }

    private void addComboModifiers(Player player) {
        AttributeInstance dmg = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance spd = player.getAttribute(Attributes.ATTACK_SPEED);

        if (dmg != null && dmg.getModifier(MOD_DAMAGE_ID) == null) {
            dmg.addTransientModifier(
                    new AttributeModifier(MOD_DAMAGE_ID, 2.0,
                            AttributeModifier.Operation.ADD_VALUE));
        }
        if (spd != null && spd.getModifier(MOD_SPEED_ID) == null) {
            spd.addTransientModifier(
                    new AttributeModifier(MOD_SPEED_ID, 10.0,
                            AttributeModifier.Operation.ADD_VALUE));
        }
    }

    private void removeComboModifiers(Player player) {
        AttributeInstance dmg = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance spd = player.getAttribute(Attributes.ATTACK_SPEED);
        if (dmg != null) dmg.removeModifier(MOD_DAMAGE_ID);
        if (spd != null) spd.removeModifier(MOD_SPEED_ID);
    }

    private void tryTeleportAround(ServerLevel level, ServerPlayer player, LivingEntity target) {
        Vec3 c = target.position();

        for (int i = 0; i < 10; i++) {
            double d = Mth.nextDouble(level.random, TP_MIN, TP_MAX);
            double a = level.random.nextDouble() * Math.PI * 2;

            BlockPos guess = BlockPos.containing(
                    c.x + d * Math.cos(a),
                    c.y,
                    c.z + d * Math.sin(a));

            BlockPos safe = findStandable(level, guess, 6);
            if (safe != null) {
                Vec3 from = player.position();
                spawnShadowSmoke(level, from);
                playShadowTeleportSound(level, from);

                player.teleportTo(level,
                        safe.getX() + 0.5,
                        safe.getY(),
                        safe.getZ() + 0.5,
                        player.getYRot(),
                        player.getXRot());

                Vec3 to = player.position();
                spawnShadowSmoke(level, to);
                playShadowTeleportSound(level, to);
                return;
            }
        }
    }

    private void spawnShadowSmoke(ServerLevel level, Vec3 pos) {
        level.sendParticles(new DustParticleOptions(SHADOW_PURPLE, 1.5f),
                pos.x, pos.y + 1, pos.z, 20, 0.6, 0.25, 0.6, 0.02);
        level.sendParticles(new DustParticleOptions(SHADOW_LIGHT, 1.5f),
                pos.x, pos.y + 1, pos.z, 20, 0.6, 0.25, 0.6, 0.02);
        level.sendParticles(ParticleTypes.LARGE_SMOKE,
                pos.x, pos.y + 1, pos.z, 20, 0.6, 0.25, 0.6, 0.02);
        level.sendParticles(ParticleTypes.ASH,
                pos.x, pos.y + 1, pos.z, 10, 0.5, 0.2, 0.5, 0.01);
    }

    private void playShadowTeleportSound(ServerLevel level, Vec3 pos) {
        level.playSound(null, pos.x, pos.y, pos.z,
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS,
                1.0f, 0.6f + level.random.nextFloat() * 0.2f);
        level.playSound(null, pos.x, pos.y, pos.z,
                SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS,
                0.8f, 0.9f + level.random.nextFloat() * 0.1f);
    }

    private BlockPos findStandable(ServerLevel lvl, BlockPos pos, int vRange) {
        BlockPos.MutableBlockPos m = pos.mutable();
        for (int dy = 0; dy <= vRange; dy++) {
            if (isStandable(lvl, m.set(pos.getX(), pos.getY() + dy, pos.getZ())))
                return m.immutable();
            if (isStandable(lvl, m.set(pos.getX(), pos.getY() - dy, pos.getZ())))
                return m.immutable();
        }
        return null;
    }

    private boolean isStandable(ServerLevel lvl, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState sBelow = lvl.getBlockState(below);
        return !sBelow.getCollisionShape(lvl, below).isEmpty()
                && lvl.isEmptyBlock(pos)
                && lvl.isEmptyBlock(pos.above());
    }
}