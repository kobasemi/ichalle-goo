package jp.ac.kansai_u.kutc.firefly.waltzforai;

import processing.core.PApplet;
import processing.core.PVector;

// 描画クラス
public class Display extends PApplet{
	private static final long serialVersionUID = 4638417483316204510L;
	
	private World world;		// 描画スレッド
	private PVector basePos; 	// 現在の描画範囲
	
	public static void main(String args[]){
		PApplet.main(new String[] { "--present", "jp.ac.kansai_u.kutc.firefly.waltzforai.Display" });
	}
	
	@Override
	public void setup(){
		world = new World(1000, 800);
		world.start();
		
		basePos = new PVector();
	}
	
	@Override
	public void draw(){
		// TODO: フレーム毎に描画
	}
	
	// TODO: 基本はマウスのみの操作（拡大・ゲームスピード変更・一時停止）
	// TODO: キーボードによるショートカット操作
}
