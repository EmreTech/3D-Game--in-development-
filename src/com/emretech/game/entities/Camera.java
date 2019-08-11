package com.emretech.game.entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distanceFromPlayer = 40;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(400,20,400);
	private float pitch = 20;
	private float yaw;
	private float roll;
	
	private Player player;
	
	public Camera(Player player) {
		this.player = player;
	}
	
	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance,verticalDistance);
		this.yaw = 180 - (player.getRoty() + angleAroundPlayer);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float xDistance, float yDistance) {
		float theta = player.getRoty() + angleAroundPlayer;
		float offsetX = (float) (xDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (xDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + yDistance;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		if (distanceFromPlayer < 15) {
			distanceFromPlayer = 15;
		} else if (distanceFromPlayer > 215) {
			distanceFromPlayer = 215;
		}
		
	}
	
	private void calculatePitch() {
		if (Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		}
		if (pitch > 90) {
			pitch = 90;
		} else if (pitch < 8) {
			pitch = 8;
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if (Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
		if (angleAroundPlayer <= -360) {
			angleAroundPlayer = 0;
		} else if (angleAroundPlayer >= 360) {
			angleAroundPlayer = 0;
		}
	}
	
}
