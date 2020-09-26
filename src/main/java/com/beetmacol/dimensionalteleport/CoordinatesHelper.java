package com.beetmacol.dimensionalteleport;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec3;

public class CoordinatesHelper {

	public static Coordinates getDimensionCorrectFakeCoordinates(ServerLevel level, CommandSourceStack source) {
		double d = DimensionType.getTeleportationScale(source.getLevel().dimensionType(), level.dimensionType());
		return new FakeCoordinatesObject(new Vec3(source.getPosition().x * d, source.getPosition().y, source.getPosition().z * d));
	}

	public static Coordinates getDimensionCorrectFakeCoordinates(ServerLevel level, CommandContext<CommandSourceStack> commandContext, String vec3Argument) {
		double d = DimensionType.getTeleportationScale(commandContext.getSource().getLevel().dimensionType(), level.dimensionType());
		Coordinates originalCoordinates = Vec3Argument.getCoordinates(commandContext, vec3Argument);
		// FIXME coordinates are multiplied after relative coordinates are added and not before.
		double xMultiplier = (originalCoordinates.isXRelative())?(d):(1);
		double zMultiplier = (originalCoordinates.isZRelative())?(d):(1);
		return new FakeCoordinatesObject(new Vec3(originalCoordinates.getPosition(commandContext.getSource()).x * xMultiplier,
				originalCoordinates.getPosition(commandContext.getSource()).y, originalCoordinates.getPosition(commandContext.getSource()).z * zMultiplier));
	}
}
