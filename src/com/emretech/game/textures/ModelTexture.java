package com.emretech.game.textures;

public class ModelTexture {
	
	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	private int numberOfRows = 1;
	
	public ModelTexture(int id) {
		this.textureID = id;
	}
	
	
	public int getNumberOfRows() {
		return numberOfRows;
	}



	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}



	public boolean isUseFakeLightning() {
		return useFakeLighting;
	}

	public void setUseFakeLightning(boolean useFakeLightning) {
		this.useFakeLighting = useFakeLightning;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	public int getID() {
		return this.textureID;
	}

	public int getTextureID() {
		return textureID;
	}

	public void setTextureID(int textureID) {
		this.textureID = textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	
}
