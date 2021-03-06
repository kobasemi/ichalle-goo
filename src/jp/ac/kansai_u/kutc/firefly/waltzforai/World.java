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
		energy = 800000000;
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
			animalspend = 500000 + Math.random()*500000;
			if(energy < plantSpend + animalspend){
				animalspend = energy;
				plantSpend = 0;
			}
			energy -= plantSpend + animalspend;
			float sizeMax = geneManager.getSizeMax();
			addEntity(new Plant(this, (float)(sizeMax+Math.random()*(width-sizeMax*2)), (float)(sizeMax+Math.random()*(height-sizeMax*2)), plantSpend));
			addEntity(new Animal.Builder(this, (float)(sizeMax+Math.random()*(width-sizeMax*2)), (float)(sizeMax+Math.random()*(height-sizeMax*2)), animalspend).random().build());
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
		
		// 1000分の1の確率でplantを追加する
		if((int)(Math.random()*1000/gameSpeed) == 0){
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
	
	// エネルギーをワールドに戻す
	public void returnEnergy(double energy){
		this.energy += energy;
	}
	
	// animalの周りにplantを出現させる
	private void popPlantAroundAnimal(Animal animal, double spend){
		float spawnX = (float)(animal.getX()+(500-Math.random()*1000));
		float spawnY = (float)(animal.getY()+(500-Math.random()*1000));
		float sizeMax = geneManager.getSizeMax();
		if(spawnX < 0){
			spawnX = -spawnX + sizeMax;
		}else if(width < spawnX){
			spawnX = width*2 - spawnX - sizeMax;
		}
		if(spawnY < 0){
			spawnY = -spawnY + sizeMax;
		}else if(height < spawnY){
			spawnY = height*2 - spawnY - sizeMax;
		}
		addEntity(new Plant(this, spawnX, spawnY, spend));
	}
	
	// エンティティをワールドに追加する
	public void addEntity(Entity entity){
		if(!entities.add(entity)){
			return;
		}
		
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
		if(!entities.remove(entity)){
			return;
		}
		
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
		// 衝突判定
		splitMap.allEntityCollisionCheck();
		
		// エンティティリストをコピー
		List<Entity> updateList = new ArrayList<Entity>(entities);
		// コピーしたリストをディスプレイにセット
		display.setEntityList(updateList);
		
		// 全てのエンティティの状態を更新
		for(int i = 0; i < updateList.size(); i++){
			updateList.get(i).update();
		}
		
		// 全てのエンティティの位置を更新
		for(int i = 0; i < updateList.size(); i++){
			updateList.get(i).move();
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
	public void addGameSpeed(double d){
		gameSpeed = gameSpeed+d < 0.1 ? 0.1 : 3.0 < gameSpeed+d ? 3.0 : gameSpeed+d;
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
	public boolean isSuspended(){ return suspended; }
}