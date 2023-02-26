package net.slayer.rustle.mixin;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LeavesBlock.class)
public class LeafRustling extends Block {

	private static final VoxelShape FALLING_SHAPE = VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.8999999761581421, 1.0);

	public LeafRustling(Settings settings) {
		super(settings);
	}

	public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
		return stateFrom.isOf(this) ? true : super.isSideInvisible(state, stateFrom, direction);
	}

	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
		return VoxelShapes.empty();
	}

	public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context instanceof EntityShapeContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if (entity != null) {
				if (entity.fallDistance > 2.5F) {
					return FALLING_SHAPE;
				}
			}
		}

		return VoxelShapes.empty();
	}

	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"))
	private static AbstractBlock.Settings addNoCollision(AbstractBlock.Settings settings) {
		return settings.noCollision();
	}

	public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (!((double)fallDistance < 4.0) && entity instanceof LivingEntity livingEntity) {
			LivingEntity.FallSounds fallSounds = livingEntity.getFallSounds();
			SoundEvent soundEvent = (double)fallDistance < 7.0 ? fallSounds.small() : fallSounds.big();
			entity.playSound(soundEvent, 1.0F, 1.0F);
		}
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		entity.slowMovement(state, new Vec3d(1, 1.4, 1));
		if (entity instanceof LivingEntity livingEntity && ((entity.lastRenderX != entity.getX() || entity.lastRenderZ != entity.getZ()) || entity.lastRenderY != entity.getY()) && livingEntity.getRandom().nextInt(15) == 0) {
			entity.playSound(SoundEvents.BLOCK_VINE_HIT, 1, 1);
		}
	}
}