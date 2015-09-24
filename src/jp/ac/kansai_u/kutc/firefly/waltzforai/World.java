package jp.ac.kansai_u.kutc.firefly.waltzforai;

public class World extends Thread{
	private final int energy; // ワールド全体のエネルギー総量
	private float worldWidth, worldHeight; // ワールドの大きさ
	
	public World(int width, int height){
		energy = 100000;
		worldWidth = width;
		worldHeight = height;
	}
}