package jp.ac.kansai_u.kutc.firefly.waltzforai;

import java.util.List;

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
		world = new World(this, 10000, 10000);
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
		fill(0);
		for(Entity e: entities){
			e.draw(this);
		}
	}

	// TODO: 基本はマウスのみの操作（拡大・ゲームスピード変更・一時停止）
	// TODO: キーボードによるショートカット操作

	// セッタ
	public void setEntityList(List<Entity> entities){ this.entities = entities; }
}
