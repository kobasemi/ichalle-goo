package jp.ac.kansai_u.kutc.firefly.waltzforai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Plant;
import jp.ac.kansai_u.kutc.firefly.waltzforai.gene.GeneManager;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.SplitMap;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.TreeBody;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.TreeSight;

// 計算クラス
public class World extends Thread{
	private Display display;
	
	private final List<Entity> entities;	// ワールド内に存在する全てのエンティティのリスト
	private double energy;					// ワールドのエネルギー総量
	private final int width, height;		// ワールドの大きさ
	private SplitMap splitMap;				// 4分木分割空間
	private GeneManager geneManager;		// 遺伝子管理クラス
	
	private double gameSpeed;				// ゲームスピード
	private int limitFPS;					// FPSの制限
	private LinkedList<Long> processTime;	// 過去数フレームの実行時間
	private double realFPS;					// 実際のFPS
	private boolean suspended;				// 一時停止中か？
	private boolean running;				// 実行中か？
	
	private int fleshEaterNum = 0;			// 各エンティティの数
	private int plantEaterNum = 0;
	private int omnivorousNum = 0;
	private int plantNum = 0;
	
	public World(Display display, int width, int height){
		this.display = display;
		
		this.width = width;
		this.height = height;
		splitMap = new SplitMap(this, 5);
		geneManager = new GeneManager(this);
		
		entities = new ArrayList<Entity>();
		energy = 100000000;
		randomCreateEntity(energy);
		display.setEntityList(new ArrayList<Entity>(entities));
		
		gameSpeed = 1.0;
		realFPS = limitFPS = 60;
		processTime = new LinkedList<Long>();
		for(int i = 0; i < 30; i++){	// 過去30フレームを記録する
			processTime.add(1000L/limitFPS);
		}
		suspended = false;
		running = true;
	}
	
	// エネルギー分だけ
	private void randomCreateEntity(double energy){
		if(this.energy < energy){
			return;
		}
		this.energy -= energy;
		double energyMin = geneManager.getPlantEnergyMin(), energyMax = geneManager.getPlantEnergyMax();
		double plantSpend, animalspend;
		while(energy > 0.01){
			plantSpend = energyMin + Math.random()*(energyMax-energyMin);
			animalspend = 100000 + Math.random()*100000;
			if(energy < plantSpend + animalspend){
				animalspend = energy;
				plantSpend = 0;
			}
			energy -= plantSpend + animalspend;
			addEntity(new Plant(this, (float)(Math.random()*width), (float)(Math.random()*height), plantSpend));
			addEntity(new Animal.Builder(this, (float)(Math.random()*width), (float)(Math.random()*height), animalspend).random().build());
		}
	}
	
	// animalの全てのエネルギーをワールドに戻す
	public void returnAllEnergy(Animal animal){
		double energy = animal.getEnergy();
		animal.reduceEnergy(energy);

		// エンティティのenergyを全て使って周りにplantを出現させる
		double energyMin = geneManager.getPlantEnergyMin(), energyMax = geneManager.getPlantEnergyMax();
		while(energy > 0){
			double spend = energyMin + Math.random()*(energyMax-energyMin);
			if(energy > spend){
				energy -= spend;
			}else{
				spend = energy;
				energy = 0;
			}
			popPlantAroundAnimal(animal, spend);
		}
	}
	
	// animalのcostエネルギーをワールドに戻す
	public void returnCostEnergy(Animal animal){
		double moveCost = animal.getCost() * animal.getWalkPace() * gameSpeed;
		if(animal.getEnergy() <= moveCost){
			moveCost = animal.getEnergy();
		}
		animal.reduceEnergy(moveCost);
		this.energy += moveCost;
		
		// 300分の1の確率でplantを追加する
		if((int)(Math.random()*300/gameSpeed) == 0){
			double energyMin = geneManager.getPlantEnergyMin(), energyMax = geneManager.getPlantEnergyMax();
			double spend = energyMin + Math.random()*(energyMax-energyMin);
			if(this.energy > spend){
				this.energy -= spend;
			}else{
				spend = this.energy;
				this.energy = 0;
			}
			popPlantAroundAnimal(animal, spend);
		}
	}
	
	// animalの周りにplantを出現させる
	private void popPlantAroundAnimal(Animal animal, double spend){
		float spawnX = (float)(animal.getX()+(200-Math.random()*400));
		float spawnY = (float)(animal.getY()+(200-Math.random()*400));
		if(spawnX < 0){
			spawnX = -spawnX;
		}else if(width < spawnX){
			spawnX = width*2 - spawnX;
		}
		if(spawnY < 0){
			spawnY = -spawnY;
		}else if(height < spawnY){
			spawnY = height*2 - spawnY;
		}
		addEntity(new Plant(this, spawnX, spawnY, spend));
	}
	
	// エンティティをワールドに追加する
	public void addEntity(Entity entity){
		entities.add(entity);
		if(entity instanceof Animal){
			splitMap.regist(new TreeBody(entity));
			splitMap.regist(new TreeSight((Animal) entity));
		}else{
			splitMap.regist(new TreeBody(entity));
		}
		
		// 種類に応じてカウント
		if(entity instanceof Plant){
			plantNum++;
		}else{
			switch(((Animal)entity).getEdibility()){
			case Plant: plantEaterNum++; break;
			case Flesh: fleshEaterNum++; break;
			case Mixed: omnivorousNum++; break;
			}
		}
	}
	
	// エンティティをワールドから削除
	public void removeEntity(Entity entity){
		entities.remove(entity);
		
		// 種類に応じてカウント
		if(entity instanceof Plant){
			plantNum--;
		}else{
			switch(((Animal)entity).getEdibility()){
			case Plant: plantEaterNum--; break;
			case Flesh: fleshEaterNum--; break;
			case Mixed: omnivorousNum--; break;
			}
		}
	}
	
	// フレーム毎に実行される更新メソッド (このクラスの肝)
	private void update(){
		// エンティティリストをコピー
		List<Entity> updateList = new ArrayList<Entity>(entities);
		// コピーしたリストをディスプレイにセット
		display.setEntityList(updateList);
		
		long allTime = System.nanoTime();
		// 当たりそうなエンティティの検出
		splitMap.allCheckNearEntity();
		long smTime = System.nanoTime() - allTime;
		
		// 全てのエンティティの状態を更新
		for(int i = 0; i < updateList.size(); i++){
			updateList.get(i).update();
		}
		
		// 全てのエンティティの位置を更新
		for(int i = 0; i < updateList.size(); i++){
			updateList.get(i).move();
		}
		
		allTime = System.nanoTime() - allTime;
		
		System.out.format("fps %.5f\n", 1000000000.0 / allTime);
		System.out.format("sm %.5f\n", smTime / (double)allTime);
		System.out.format("gh %.5f\n", Animal.timeB / (double)allTime);
		System.out.format("mv %.5f\n\n", Animal.timeC / (double)allTime);
		
		Animal.timeB = Animal.timeC = 0;
		
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
		long error = 0;				// 前フレームの誤差
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
		  	long idealSleep = (1000 << 16) / limitFPS;
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
	
	// ゲームスピードへの加算
	public void addGameSpeed(int add){
		gameSpeed = gameSpeed+add < 0 ? 0 : gameSpeed+add;
	}
	
	// FPS制限への加算
	public void addLimitFPS(int add){
		limitFPS = limitFPS+add < 1 ? 1 : limitFPS+add;
	}
	
	// スレッドの一時停止 or 再開
	public synchronized void pause() {
		if(suspended){
			suspended = false;
			notify();
		}else{
			suspended = true;
		}
	}

	// スレッドの終了
	public void stopWorld(){
		running = false;
	}
	
	// ゲッタ
	public SplitMap getSplitMap(){ return splitMap; }
	public GeneManager getGeneManager() { return geneManager; }
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	public double getGameSpeed(){ return gameSpeed; }
	public double getRealFPS(){ return realFPS; }
	public int getPlantNum(){ return plantNum; }
	public int getPlantEaterNum(){ return plantEaterNum; }
	public int getFleshEaterNum(){ return fleshEaterNum; }
	public int getOmnivorousNum(){ return omnivorousNum; }
}