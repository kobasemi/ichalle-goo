package jp.ac.kansai_u.kutc.firefly.waltzforai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.SplitMap;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.TreeObject;

// 計算クラス
public class World extends Thread{
	private Display display;
	
	private final List<Entity> entities;	// ワールド内に存在する全てのエンティティのリスト
	private final int energy;				// ワールドのエネルギー総量
	private final int width, height;		// ワールドの大きさ
	private SplitMap splitMap;				// 4分木分割空間
	
	private int idealFPS;					// 目標のFPS
	private LinkedList<Long> processTime;	// 過去数フレームの実行時間
	private double realFPS;					// 実際のFPS
	private boolean suspended;				// 一時停止中か？
	private boolean running;				// 実行中か？
	
	public World(Display display, int width, int height){
		this.display = display;
		
		this.width = width;
		this.height = height;
		splitMap = new SplitMap(this, 1);
		
		entities = new ArrayList<Entity>();
		energy = 10001;
		randomCreateEntity(energy);
		display.setEntityList(new ArrayList<Entity>(entities));
		
		realFPS = idealFPS = 60;
		processTime = new LinkedList<Long>();
		for(int i = 0; i < 30; i++){	// 過去30フレームを記録する
			processTime.add(1000L/idealFPS);
		}
		suspended = false;
		running = true;
	}
	
	private void randomCreateEntity(int energy){
		int spend = 1000;
		while((energy -= spend) > 0){
			Animal animal = new Animal(this, (float)Math.random()*(width-100)+50, (float)Math.random()*(width-100)+50, (float)Math.random()*50, spend);
			entities.add(animal);
			splitMap.regist(new TreeObject(animal, true));
			splitMap.regist(new TreeObject(animal, false));
		}
	}
	
	// フレーム毎に実行される更新メソッド (このクラスの肝)
	private void update(){
		// エンティティリストをコピー
		List<Entity> updateList = new ArrayList<Entity>(entities);
		// コピーしたリストをディスプレイにセット
		display.setEntityList(updateList);
		
		// 当たりそうなエンティティの検出
		splitMap.collisionCheck();
		
		// 全てのエンティティを更新
		for(int i = 0; i < updateList.size(); i++){
			updateList.get(i).update();
		}
		
		// 計算にかかったFPSを算出
		double timeSum = 0;
		for(long time: processTime){
			timeSum += time;
		}
		realFPS = 1000.0/(timeSum/processTime.size());
	}
	
	// スレッドのrunメソッド
	@Override
	public void run() {
		long error = 0;	// 前フレームの誤差
		long beforeTime, oldTime;	// 実行前の時間
		long newTime = System.currentTimeMillis() << 16;
		
		// 実行ループ
		while (running) {
			// スレッドの停止
			while (suspended) {
			    synchronized(this) {
			    	try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    }
			}

			// 実行前の時間を記録
			beforeTime = oldTime = newTime;
			
			// 更新の実行
			update();
			
			// 実行後の時間を記録
		  	newTime = System.currentTimeMillis() << 16;

		  	// どれだけの時間sleepするか計算する
		  	long idealSleep = (1000 << 16) / idealFPS;
		  	long sleepTime = idealSleep - (newTime - oldTime) - error;
		  	if (sleepTime < 0){
		  		sleepTime = 0;
		  	}
		  	oldTime = newTime;
		  	
		  	// sleepの実行
		  	try {
		  		Thread.sleep(sleepTime >> 16);
		  	} catch (InterruptedException e) {
		  		e.printStackTrace();
		  	}
		  	newTime = System.currentTimeMillis() << 16;
		  	
		  	// 誤差の計算
		  	error = newTime - oldTime - sleepTime;
		  	processTime.pop();
		  	processTime.addLast((newTime-beforeTime) >> 16);
		}
	}
	
	// ゲッタ
	public SplitMap getSplitMap(){ return splitMap; }
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	public double getRealFPS(){ return realFPS; }
}