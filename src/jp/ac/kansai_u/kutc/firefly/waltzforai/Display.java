package jp.ac.kansai_u.kutc.firefly.waltzforai;

import java.util.List;

import com.sun.glass.events.KeyEvent;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;
import jp.ac.kansai_u.kutc.firefly.waltzforai.frame.MainFrame;
import processing.core.PApplet;
import processing.core.PVector;

// 描画クラス
public class Display extends PApplet{
	private static final long serialVersionUID = 4638417483316204510L;
	private MainFrame mainFrame;
	private World world;			// 描画スレッド
	private List<Entity> entities;

	private PVector basePos; 		// 現在の描画範囲

	public Display(MainFrame mainFrame){
		this.mainFrame = mainFrame;
		world = new World(this, 800, 600);
		world.start();

		basePos = new PVector();
	}

	@Override
	public void setup(){
		size(mainFrame.getWidth(), mainFrame.getHeight());
	}

	@Override
	public void draw(){
		background(255);
		
		for(int i = 0; i < 65; i++){
			stroke(200);
			strokeWeight(0.25f);
			if(i % 32 == 0){
				stroke(0);
				strokeWeight(5);
			}else if(i % 16 == 0){
				stroke(50);
				strokeWeight(3);
			}else if(i % 8 == 0){
				stroke(100);
				strokeWeight(2);
			}else if(i % 4 == 0){
				stroke(150);
				strokeWeight(1);
			}else if(i % 2 == 0){
				stroke(200);
				strokeWeight(0.5f);
			}
			
			line(i*world.getWidth()/64, 0, i*world.getWidth()/64, world.getHeight());
			line(0, i*world.getHeight()/64, world.getWidth(), i*world.getHeight()/64);
		}
		
		fill(0);
		for(Entity e: entities){
			e.draw(this);
		}
	}

	// TODO: 基本はマウスのみの操作（拡大・ゲームスピード変更・一時停止）
	// TODO: キーボードによるショートカット操作
	
	@Override
	public void keyPressed(processing.event.KeyEvent e) {
		switch(e.getKeyCode()){
		case LEFT: world.addGameSpeed(1); break;
		case RIGHT: world.addLimitFPS(1); break;
		case ' ': world.pause(); break;
		}
	}

	// セッタ
	public void setEntityList(List<Entity> entities){ this.entities = entities; }
}
