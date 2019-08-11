package com.emretech.game.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.emretech.game.entities.Camera;
import com.emretech.game.entities.Entity;
import com.emretech.game.entities.Light;
import com.emretech.game.models.TexturedModel;
import com.emretech.game.shaders.StaticShader;
import com.emretech.game.shaders.TerrainShader;
import com.emretech.game.skybox.SkyboxRenderer;
import com.emretech.game.terrains.Terrain;

public class MasterRenderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private static final float RED = 0.5444f;
	private static final float GREEN = 0.62f;
	private static final float BLUE = 0.69f;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	
	private TerrainShader terrainShader = new TerrainShader();
	private TerrainRenderer terrainRenderer;
	
	private Map<TexturedModel,List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyboxRenderer skyboxRenderer;
	
	public MasterRenderer(Loader loader) {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader,projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader,projectionMatrix);
	}
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void render(List<Light> lights, Camera camera) {
		prepare();
		
		skyboxRenderer.render(camera);
		
		terrainShader.start();
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		shader.start();
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		terrains.clear();
		entities.clear();
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void cleanUp() {
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = ((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = ((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
	}
	
	 
}
