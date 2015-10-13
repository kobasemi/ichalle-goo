package jp.ac.kansai_u.kutc.firefly.waltzforai;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

// 描画クラス
public class Display extends PApplet{
	private static final long serialVersionUID = 4638417483316204510L;
	private World world;			// 描画スレッド
	private List<Entity> entities;

	private PVector basePos; 		// 現在の描画範囲
	private PVector dragPos;		// ドラッグ開始地点
	private float scale;			// 画面倍率

	private List<Button> buttons;	// ボタン
	
	public Display(){
		world = new World(this, 8000, 6000);
		
		basePos = new PVector();
		dragPos = new PVector();
		basePos.set(world.getWidth()/2, world.getHeight()/2);
		
		scale = 1.0f;
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height){
		super.setBounds(x, y, width, height);
		
		// ボタンの配置
		buttons = new ArrayList<Button>();
		buttons.addAll(Arrays.asList(new PauseButton(width-50, 20)));
	}
	
	// ワールドのスレッドを開始する
	public void startWorld(){
		world.start();
	}
	
	/*public static void main(String args[]){
		PApplet.main(new String[] { "--present", "jp.ac.kansai_u.kutc.firefly.waltzforai.Display" });
	}*/

	@Override
	public void draw(){
		// 背景色のセット
		background(255);
		
		pushMatrix();
		
		// 画面倍率のセット
		scale(scale);
		moveDisplayArea();
		drawGrid();

		// エンティティの描画
		for(Entity e: entities){
			if(withinDisplayArea(e)){
				e.draw(this);
			}
		}
		
		popMatrix();
		
		for(int i = 0; i < buttons.size(); i++){
			buttons.get(i).draw();
		}
		
		fill(0);
		text(String.format("(%.0f,%.0f)  Speed x%.2f  Zoom x%.2f", basePos.x, basePos.y, world.getGameSpeed(), scale), 10, 20);
		text(String.format("FPS: %.1f", world.getRealFPS()), 10, 40);
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		float cx = e.getX(), cy = e.getY();
		for(int i = 0; i < buttons.size(); i++){
			buttons.get(i).onClicked(cx, cy);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		dragPos.set(mouseX, mouseY);
	}
	
	@Override
	public void mouseDragged(MouseEvent e){
		// マウスドラッグで画面移動
		basePos.add((dragPos.x - mouseX)/scale, (dragPos.y - mouseY)/scale, 0);
		dragPos.set(mouseX, mouseY);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e){
		// マウスホイールで拡大・縮小
		scale = scale+e.getWheelRotation()/100f < 0.01f ? 0.01f : scale+e.getWheelRotation()/100f;
	}
	
	// 表示領域を移動する
	private void moveDisplayArea(){
		translate((width/2f)/scale-basePos.x, (height/2f)/scale-basePos.y);
	}

	// グリッド線を描画する
	private void drawGrid(){
		int splitLevel = world.getSplitMap().getSplitLevel();
		int lineNum = 1<<(splitLevel+1);
		for(int i = 0; i <= lineNum; i++){
			stroke(200);
			strokeWeight(0.25f);
			for(int j = 0; j < splitLevel; j++){
				if(i % (1<<(splitLevel-j)) == 0){
					stroke(0+j*30);
					strokeWeight((splitLevel-j)/4f);
					break;
				}
			}
			line(i*world.getWidth()/lineNum, 0, i*world.getWidth()/lineNum, world.getHeight());
			line(0, i*world.getHeight()/lineNum, world.getWidth(), i*world.getHeight()/lineNum);
		}
	}
	
	@Override
	public void keyPressed(processing.event.KeyEvent e) {
		switch(e.getKeyCode()){
		case LEFT: world.addGameSpeed(-0.1); break;
		case RIGHT: world.addGameSpeed(0.1); break;
		case ' ': world.pause(); break;
		}
	}
	
	// エンティティが画面内にいるか？
	private boolean withinDisplayArea(Entity entity){
		float scaledWidth, scaledHeight;
		if(entity instanceof Animal){
			scaledWidth = (width/2)/scale+((Animal)entity).getSight();
			scaledHeight = (height/2)/scale+((Animal)entity).getSight();
		}else{
			scaledWidth = (width/2)/scale+entity.getSize();
			scaledHeight = (height/2)/scale+entity.getSize();
		}
		if(-scaledWidth <= entity.getX()-basePos.x && entity.getX()-basePos.x <= scaledWidth &&
				-scaledHeight <= entity.getY()-basePos.y && entity.getY()-basePos.y <= scaledHeight){
			return true;
		}
		return false;
	}

	// ゲッタ
	public World getWorld(){ return world; }
	
	// セッタ
	public void setEntityList(List<Entity> entities){ this.entities = entities; }
	
	private abstract class Button{
		protected float x, y, width, height;
		public Button(float x, float y){
			this.x = x;
			this.y = y;
		}
		public abstract void draw();
		public abstract void onClicked(float x, float y);
	}

	private class PauseButton extends Button{
		public PauseButton(float x, float y) {
			super(x, y);
			this.width = 20;
			this.height = 30;
		}
		@Override
		public void draw() {
			noStroke();
			fill(50);
			if(world.isSuspended()){
				triangle(x, y, x, y+height, x+width, y+height/2);
			}else{
				rect(x, y, width/3, height);
				rect(x+width*2/3, y, width/3, height);
			}
		}
		@Override
		public void onClicked(float cx, float cy) {
			if((x < cx && cx < x+width) && (y < cy && cy < y+height)){
				world.pause();
			}
		}
	}
}
