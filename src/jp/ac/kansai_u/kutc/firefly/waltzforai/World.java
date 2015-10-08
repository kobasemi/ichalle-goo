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
	
	private int idealFPS;					// 目標のFPS
	private LinkedList<Long> processTime;	// 過去数フレームの実行時間
	private double realFPS;					// 実際のFPS
	private boolean suspended;				// 一時停止中か？
	private boolean running;				// 実行中か？
	
	public World(Display display, int width, int height){
		this.display = display;
		
		this.width = width;
		this.height = height;
		splitMap = new SplitMap(this, 5);
		geneManager = new GeneManager(this);
		
		entities = new ArrayList<Entity>();
		energy = 100000;
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
	
	private void randomCreateEntity(double energy){
		double energyMin = geneManager.getPlantEnergyMin(), energyMax = geneManager.getPlantEnergyMax();
		double plantSpend = energyMin + Math.random()*(energyMax-energyMin);
		double animalspend = 10000 + Math.random()*90000;
		while(energy >= plantSpend + animalspend){
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
		double moveCost = animal.getCost() * animal.getWalkPace();
		if(animal.getEnergy() <= moveCost){
			moveCost = animal.getEnergy();
		}
		animal.reduceEnergy(moveCost);
		this.energy += moveCost;
		
		// 100分の1の確率でplantを追加する
		if((int)(Math.random()*100) == 0){
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
		}else if(width < spawnY){
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
	}
	
	// エンティティをワールドから削除
	public void removeEntity(Entity entity){
		entities.remove(entity);
	}
	
	// フレーム毎に実行される更新メソッド (このクラスの肝)
	private void update(){
		// エンティティリストをコピー
		List<Entity> updateList = new ArrayList<Entity>(entities);
		// コピーしたリストをディスプレイにセット
		display.setEntityList(updateList);
		
		// 当たりそうなエンティティの検出
		splitMap.allCheckNearEntity();
		
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
	public GeneManager getGeneManager() { return geneManager; }
	public int getWidth(){ return width; }
	public int getHeight(){ return height; }
	public double getRealFPS(){ return realFPS; }
}