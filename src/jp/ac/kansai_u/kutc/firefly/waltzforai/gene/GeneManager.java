package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;

// 遺伝子に関する管理クラス
public class GeneManager {
	private World world;	// 所属ワールド
	
	// 遺伝子情報、ステータスの最小値，最大値
	private float rgbMin = 0;
	private float rgbMax = 255;
	private double fovMin = Math.PI/9.0;
	private double fovMax = Math.PI*2;
	private double fovCost = 5;
	private float sightMin = 10.0f;
	private float sightMax = 150.0f;
	private float sightCost = 5;
	private double speedMin = 0.2;
	private double speedMax = 2.0;
	private double speedCost = 10;
	private float sizeMin = 5.0f;
	private float sizeMax = 50.0f;
	private double walkPaceMin = 0.0;	// 歩く速さの最小値
	private double walkPaceMax = 2.0;	// 歩く速さの最大値
	
	public GeneManager(World world){
		this.world = world;
	}
	
	// 動物をランダムに生成する
	public void buildRandomAnimal(Animal.Builder b){
		// 行動ツリーをランダム生成
		IfNode geneHead = new IfNode(this);
		geneHead.setFalseNext(new ActionNode(this));
		GeneNode geneNode = new ActionNode(this);
		geneHead.setNext(geneNode);
		for(int i = 0; i < 6; i++){
			if(Math.random() < 0.5){
				ActionNode node = new ActionNode(this);
				geneNode.setNext(node);
				geneNode = node;
			}else{
				IfNode node = new IfNode(this);
				node.setFalseNext(new ActionNode(this));
				geneNode.setNext(node);
				geneNode = node;
			}
		}
		
		double cost = 0;
		
		float clr = (float)(rgbMin+Math.random()*(rgbMax-rgbMin));
		float clg = (float)(rgbMin+Math.random()*(rgbMax-rgbMin));
		float clb = (float)(rgbMin+Math.random()*(rgbMax-rgbMin));

		double fovVal = Math.random();
		double fov = fovMin + fovVal*(fovMax-fovMin);
		cost += fovVal*fovCost;
		
		float sightVal = (float)Math.random();
		float sight = sightMin + sightVal*(sightMax-sightMin);
		cost += sightVal*sightCost;
		
		double speedVal = Math.random();
		double speed = speedMin + speedVal*(speedMax-speedMin);
		cost += speedVal*speedCost;
		
		b.setGeneHead(geneHead);
		b.setColor(clr, clg, clb);
		b.setFov(fov);
		b.setSight(sight);
		b.setSpeed(speed);
		b.setCost(cost);
	}
	
	// 動物の交配
	public void crossEntities(Animal e1, Animal e2){
		// TODO: 二つのAnimalクラスの遺伝子情報に基づいて子供のインスタンスを生成し，Worldに登録する
	}

	public float getSizeMin() { return sizeMin; }
	public float getSizeMax() { return sizeMax; }
	public double getWalkPaceMin() { return walkPaceMin; }
	public double getWalkPaceMax() { return walkPaceMax; }
}
