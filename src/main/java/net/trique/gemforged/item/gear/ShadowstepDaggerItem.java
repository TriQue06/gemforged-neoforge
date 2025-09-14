package net.trique.gemforged.item.gear;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ShadowstepDaggerItem extends SwordItem {

    private static final String TAG_COMBO = "onyx_combo";
    private static final String TAG_LASTHIT = "onyx_last_hit";

    private static final int MAX_COMBO = 6;
    private static final int COMBO_TIMEOUT_TICKS = 60;
    private static final int COOLDOWN_TICKS = 20 * 20;
    private static final double TP_MIN = 2.0, TP_MAX = 3.0;

    private static final ResourceLocation MOD_DAMAGE_ID = ResourceLocation.parse("gemforged:onyx_combo_damage");
    private static final ResourceLocation MOD_SPEED_ID = ResourceLocation.parse("gemforged:onyx_combo_speed");

    public ShadowstepDaggerItem(Tier tier, Properties props) {
        super(tier, props);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!(attacker instanceof Player player)) return true;
        Level level = player.level();
        if (level.isClientSide) return true;

        CustomData cd = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = cd.copyTag();

        long now = level.getGameTime();
        int combo = tag.getInt(TAG_COMBO);
        long last = tag.getLong(TAG_LASTHIT);

        if (combo > 0 && (now - last) > COMBO_TIMEOUT_TICKS) {
            combo = 0;
            removeComboModifiers(player);
        }
        if (player.getCooldowns().isOnCooldown(this)) {
            removeComboModifiers(player);
            return true;
        }

        if (player instanceof ServerPlayer sp && target.isAlive()) {
            tryTeleportAround((ServerLevel) level, sp, target);
            sp.lookAt(EntityAnchorArgument.Anchor.EYES, target.position());
        }

        combo++;
        tag.putInt(TAG_COMBO, combo);
        tag.putLong(TAG_LASTHIT, now);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        if (combo == 1) {
            addComboModifiers(player);
        }

        if (combo >= MAX_COMBO) {
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            tag.putInt(TAG_COMBO, 0);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            removeComboModifiers(player);
        }

        return true;
    }

    private void addComboModifiers(Player player) {
        AttributeInstance dmg = player.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance spd = player.getAttribute(Attributes.ATTACK_SPEED);
        if (dmg != null && dmg.getModifier(MOD_DAMAGE_ID) == null) {
            dmg.addTransientModifier(new AttributeModifier(MOD_DAMAGE_ID, 2.0, AttributeModifier.Operation.ADD_VALUE));
        }
        if (spd != null && spd.getModifier(MOD_SPEED_ID) == null) {
            spd.addTransientModifier(new AttributeModifier(MOD_SPEED_ID, 10.0, AttributeModifier.Operation.ADD_VALUE));
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
                    c.z + d * Math.sin(a)
            );
            BlockPos safe = findStandable(level, guess, 6);
            if (safe != null) {
                Vec3 from = player.position();
                spawnShadowSmoke(level, from.x, from.y + 1.0, from.z);
                playShadowTeleportSound(level, from.x, from.y, from.z);

                player.teleportTo(level, safe.getX() + 0.5, safe.getY(), safe.getZ() + 0.5,
                        player.getYRot(), player.getXRot());

                Vec3 to = player.position();
                spawnShadowSmoke(level, to.x, to.y + 1.0, to.z);
                playShadowTeleportSound(level, to.x, to.y, to.z);
                return;
            }
        }
    }

    private void spawnShadowSmoke(ServerLevel level, double x, double y, double z) {
        level.sendParticles(ParticleTypes.LARGE_SMOKE, x, y, z, 40, 0.6, 0.25, 0.6, 0.02);
        level.sendParticles(ParticleTypes.ASH, x, y, z, 20, 0.5, 0.2, 0.5, 0.01);
    }

    private void playShadowTeleportSound(ServerLevel level, double x, double y, double z) {
        level.playSound(null, x, y, z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 0.6f + level.random.nextFloat() * 0.2f);
        level.playSound(null, x, y, z, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.8f, 0.9f + level.random.nextFloat() * 0.1f);
    }

    private BlockPos findStandable(ServerLevel lvl, BlockPos origin, int vRange) {
        BlockPos.MutableBlockPos m = origin.mutable();
        for (int dy = 0; dy <= vRange; dy++) {
            if (isStandable(lvl, m.set(origin.getX(), origin.getY() + dy, origin.getZ()))) return m.immutable();
            if (isStandable(lvl, m.set(origin.getX(), origin.getY() - dy, origin.getZ()))) return m.immutable();
        }
        return null;
    }

    private boolean isStandable(ServerLevel lvl, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState sBelow = lvl.getBlockState(below);
        boolean solidBelow = !sBelow.getCollisionShape(lvl, below).isEmpty();
        boolean airFeet = lvl.isEmptyBlock(pos);
        boolean airHead = lvl.isEmptyBlock(pos.above());
        return solidBelow && airFeet && airHead;
    }
}
