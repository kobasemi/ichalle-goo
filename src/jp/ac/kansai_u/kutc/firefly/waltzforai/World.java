package jp.ac.kansai_u.kutc.firefly.waltzforai;

import processing.core.PApplet;

public class World extends PApplet{
	private static final long serialVersionUID = 4638417483316204510L;
	
	public static void main(String args[]){
		PApplet.main(new String[] { "--present", "jp.ac.kansai_u.kutc.firefly.waltzforai.World" });
	}
	
	@Override
	public void setup(){
		size(displayWidth, displayHeight);
	}
	
	@Override
	public void draw(){
		
	}
}