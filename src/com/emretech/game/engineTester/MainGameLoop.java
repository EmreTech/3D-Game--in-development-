package com.emretech.game.engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.emretech.game.entities.Camera;
import com.emretech.game.entities.Entity;
import com.emretech.game.entities.Light;
import com.emretech.game.entities.Player;
import com.emretech.game.guis.GuiRenderer;
import com.emretech.game.guis.GuiTexture;
import com.emretech.game.models.RawModel;
import com.emretech.game.models.TexturedModel;
import com.emretech.game.objConverter.ModelData;
import com.emretech.game.objConverter.OBJFileLoader;
import com.emretech.game.renderEngine.DisplayManager;
import com.emretech.game.renderEngine.Loader;
import com.emretech.game.renderEngine.MasterRenderer;
import com.emretech.game.renderEngine.OBJLoader;
import com.emretech.game.terrains.Terrain;
import com.emretech.game.textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		RawModel treeModel = OBJLoader.loadObjModel("tree", loader);
		TexturedModel texturedTreeModel = new TexturedModel(treeModel,new ModelTexture(loader.loadTexture("tree")));
		texturedTreeModel.getTexture().setHasTransparency(false);
		RawModel fernModel = OBJLoader.loadObjModel("fern", loader);
		TexturedModel texturedFernModel = new TexturedModel(fernModel,new ModelTexture(loader.loadTexture("fernAtlas")));
		texturedFernModel.getTexture().setNumberOfRows(2);
		texturedFernModel.getTexture().setHasTransparency(true);
		texturedFernModel.getTexture().setUseFakeLightning(true);
		RawModel playerModel = OBJLoader.loadObjModel("person", loader);
		TexturedModel texturedPlayerModel = new TexturedModel(playerModel,new ModelTexture(loader.loadTexture("playerTexture")));
		texturedPlayerModel.getTexture().setHasTransparency(false);
		ModelData lampData = OBJFileLoader.loadOBJ("lamp");
		RawModel lampModel = loader.loadToVAO(lampData.getVertices(), lampData.getTextureCoords(), lampData.getNormals(), lampData.getIndices());
		TexturedModel texturedLampModel = new TexturedModel(lampModel,new ModelTexture(loader.loadTexture("lamp")));
		texturedLampModel.getTexture().setHasTransparency(false);
		RawModel cubeModel = OBJLoader.loadObjModel("box", loader);
		TexturedModel texturedCubeModel = new TexturedModel(cubeModel,new ModelTexture(loader.loadTexture("box")));
		Entity box = new Entity(texturedCubeModel, new Vector3f(178,5,96), 0,0,0,5);
		
		Player player = new Player(texturedPlayerModel, new Vector3f(178,5,93), 0,0,0,0.5f);
		Terrain terrain = new Terrain(0,0,loader, new ModelTexture(loader.loadTexture("grass")), "heightmap");
		
		List<Entity>entities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			if (i % 20 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * 800;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(texturedFernModel,random.nextInt(4),new Vector3f(x, y, z), 0,0,0,0.6f));
			}
			if (i % 5 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * 800;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(texturedTreeModel,new Vector3f(x, y, z), 0,0,0,3));
				/*x = random.nextFloat() * 800;
				z = random.nextFloat() * 800;
				y = terrain.getHeightOfTerrain(x, z);*/
				
			}
		}
		 
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0,1000,-7000), new Vector3f(1f,1f,1f)));
		lights.add(new Light(new Vector3f(178,5,93), new Vector3f(2,0,0), new Vector3f(1,0.01f,0.002f)));
		
		float y = terrain.getHeightOfTerrain(178, 93);
		
		Entity lamp = new Entity(texturedLampModel,new Vector3f(178,y,93),0,0,0,0.5f);
		
		Camera camera = new Camera(player);
	
		List <GuiTexture> guis = new ArrayList<GuiTexture>();
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		MasterRenderer renderer = new MasterRenderer(loader);
		
		while (!Display.isCloseRequested()) {
			box.increaseRotation(1, 1, 0);
			player.move(terrain);
			camera.move();
			renderer.processTerrain(terrain);
			for (Entity entity:entities) {
				renderer.processEntity(entity);
			}
			renderer.processEntity(lamp);
			renderer.processEntity(box);
			renderer.processEntity(player);
			renderer.render(lights, camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
