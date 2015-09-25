package jp.ac.kansai_u.kutc.firefly.waltzforai;

import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.SplitMap;

// 計算クラス
public class World extends Thread{
	private final int energy;		 	// ワールドのエネルギー総量
	private final int width, height; 	// ワールドの大きさ
	private SplitMap splitMap; 			// 4分木分割空間
	
	public static void main(String args[]){
		SplitMap sm = new SplitMap(new World(1000, 1000), 2);
	}
	
	public World(int width, int height){
		energy = 100000;
		this.width = width;
		this.height = height;
		splitMap = new SplitMap(this, 5);
	}
	
	@Override
	public void run() {
		// TODO: フレーム毎に計算
	}
	
	// ゲッタ
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
}