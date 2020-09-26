package com.beetmacol.dimensionalteleport;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class FakeCoordinatesObject implements Coordinates {
	Vec3 position;
	Vec2 rotation;

	public FakeCoordinatesObject(Vec3 position) {
		this.position = position;
	}

	public FakeCoordinatesObject(Vec2 rotation) {
		this.rotation = rotation;
	}

	@Override
	public Vec3 getPosition(CommandSourceStack commandSourceStack) {
		return position;
	}

	@Override
	public Vec2 getRotation(CommandSourceStack commandSourceStack) {
		return rotation;
	}

	@Override
	public boolean isXRelative() {
		return false;
	}

	@Override
	public boolean isYRelative() {
		return false;
	}

	@Override
	public boolean isZRelative() {
		return false;
	}
}
