package com.emretech.game.engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import com.emretech.game.entities.Camera;
import com.emretech.game.entities.Entity;
import com.emretech.game.entities.Light;
import com.emretech.game.models.RawModel;
import com.emretech.game.models.TexturedModel;
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
		
		List<Entity>entities = new ArrayList<Entity>();
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			entities.add(new Entity(texturedTreeModel,new Vector3f(random.nextFloat() * 800, 5, random.nextFloat() * 800), 0,0,0,3));
		}
		
		Light light = new Light(new Vector3f(3000,2000,2000), new Vector3f(1,1,1));
		Terrain terrain = new Terrain(0,0,loader, new ModelTexture(loader.loadTexture("grass")));
		Camera camera = new Camera();
		MasterRenderer renderer = new MasterRenderer();
		while (!Display.isCloseRequested()) {
			//entity.increaseRotation(0, 1, 0);
			camera.move();
			for (Entity entity:entities) {
				renderer.processEntity(entity);
			}
			renderer.processTerrain(terrain);
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
