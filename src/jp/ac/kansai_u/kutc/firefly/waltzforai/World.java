package jp.ac.kansai_u.kutc.firefly.waltzforai;

// 計算クラス
public class World extends Thread{
	private final int energy; // ワールド全体のエネルギー総量
	private float worldWidth, worldHeight; // ワールドの大きさ
	private SplitMap splitMap; // 4分木分割空間
	
	public World(int width, int height){
		energy = 100000;
		worldWidth = width;
		worldHeight = height;
		splitMap = new SplitMap(this);
	}
	
	@Override
	public void run() {
		// TODO: フレーム毎に計算
	}
}