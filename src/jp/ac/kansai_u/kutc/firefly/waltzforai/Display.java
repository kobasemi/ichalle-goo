package jp.ac.kansai_u.kutc.firefly.waltzforai;

import java.util.List;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;
import jp.ac.kansai_u.kutc.firefly.waltzforai.frame.MainFrame;
import jp.ac.kansai_u.kutc.firefly.waltzforai.frame.SecondFrame;
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
		setBounds(MainFrame.w/3, 0, (MainFrame.w - (MainFrame.w / 3)), MainFrame.h);
	}

	@Override
	public void draw(){
		background(255);
		
		drawGrid();

		fill(0);
		for(Entity e: entities){
			e.draw(this);
		}
		
		SecondFrame.plant = world.getPlantNum();
		SecondFrame.plantE = world.getPlantEaterNum();
		SecondFrame.fleshE = world.getFleshEaterNum();
		SecondFrame.omni = world.getOmnivorousNum();
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
					strokeWeight((splitLevel-j)/2f);
					break;
				}
			}
			line(i*world.getWidth()/lineNum, 0, i*world.getWidth()/lineNum, world.getHeight());
			line(0, i*world.getHeight()/lineNum, world.getWidth(), i*world.getHeight()/lineNum);
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
