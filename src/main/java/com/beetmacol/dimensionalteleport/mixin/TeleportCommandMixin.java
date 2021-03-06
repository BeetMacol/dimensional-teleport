package com.beetmacol.dimensionalteleport.mixin;

import com.beetmacol.dimensionalteleport.CoordinatesHelper;
import com.beetmacol.dimensionalteleport.DimensionalTeleport;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Collections;

@Mixin(TeleportCommand.class)
abstract
class TeleportCommandMixin {

	@Shadow
	protected static int teleportToPos(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, ServerLevel serverLevel, Coordinates coordinates, @Nullable Coordinates coordinates2, @Nullable TeleportCommand.LookAt lookAt) {
		try {
			throw new IllegalAccessException();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Inject(method = "register", at = @At(value = "TAIL", target = "Lnet/minecraft/server/commands/TeleportCommand;register(Lcom/mojang/brigadier/CommandDispatcher;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CallbackInfo ci, LiteralCommandNode<CommandSourceStack> literalCommandNode) {
		// TODO LookAt argument when dimension argument is present.
		// TODO add rotation argument to `/teleport <location>`.
		//  In vanilla `/teleport <target> <location> [<rotation>]` is possible but only with the `<target>` argument provided.
		literalCommandNode.getChild("targets").addChild(Commands.argument("dimension", DimensionArgument.dimension()).executes(commandContext -> {
			ServerLevel level = DimensionArgument.getDimension(commandContext, "dimension");
			return teleportToPos(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), level, CoordinatesHelper.getDimensionCorrectFakeCoordinates(level, commandContext.getSource()), null, null);
		}).then(Commands.argument("location", Vec3Argument.vec3()).executes(commandContext -> {
			ServerLevel level = DimensionArgument.getDimension(commandContext, "dimension");
			return teleportToPos(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), level, CoordinatesHelper.getDimensionCorrectFakeCoordinates(level, commandContext, "location"), null, null);
		}).then(Commands.argument("rotation", RotationArgument.rotation()).executes(commandContext -> {
			ServerLevel level = DimensionArgument.getDimension(commandContext, "dimension");
			return teleportToPos(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), level, CoordinatesHelper.getDimensionCorrectFakeCoordinates(level, commandContext, "location"), RotationArgument.getRotation(commandContext, "rotation"), null);
		}))).build());
		literalCommandNode.addChild(Commands.argument("dimension", DimensionArgument.dimension()).executes(commandContext -> {
			ServerLevel level = DimensionArgument.getDimension(commandContext, "dimension");
			return teleportToPos(commandContext.getSource(), Collections.singleton((commandContext.getSource()).getEntityOrException()), level, CoordinatesHelper.getDimensionCorrectFakeCoordinates(level, commandContext.getSource()), null, null);
		}).then(Commands.argument("location", Vec3Argument.vec3()).executes(commandContext -> {
			ServerLevel level = DimensionArgument.getDimension(commandContext, "dimension");
			return teleportToPos(commandContext.getSource(), Collections.singleton((commandContext.getSource()).getEntityOrException()), level, CoordinatesHelper.getDimensionCorrectFakeCoordinates(level, commandContext, "location"), null, null);
		}).then(Commands.argument("rotation", RotationArgument.rotation()).executes(commandContext -> {
			ServerLevel level = DimensionArgument.getDimension(commandContext, "dimension");
			return teleportToPos(commandContext.getSource(), Collections.singleton((commandContext.getSource()).getEntityOrException()), level, CoordinatesHelper.getDimensionCorrectFakeCoordinates(level, commandContext, "location"), RotationArgument.getRotation(commandContext, "rotation"), null);
		}))).build());
		DimensionalTeleport.logger.info("Added <dimension> argument to the `/teleport` command.");
		DimensionalTeleport.logger.warn("Please note that the Dimensional Teleport mod is in an alpha phase and may contain bugs.");
	}
}
