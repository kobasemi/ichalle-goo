package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Edibility;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Plant;

// 遺伝子に関する管理クラス
public class GeneManager {
	private World world;	// 所属ワールド
	
	private double plantEnergyMin = 1000;	// 植物のエネルギー
	private double plantEnergyMax = 3000;
	
	// 遺伝子情報、ステータスの最小値，最大値、コスト
	private float rgbMin = 0;					// 体色
	private float rgbMax = 255;
	private double FleshEaterCost = 2;			// 肉食のコスト
	private double PlantEaterCost = 5;			// 草食のコスト
	private double MixedEaterCost = 10;			// 雑食のコスト
	private double EdibilityChangeRate = 0.05;	// 食性が変わる確率
	private double fovMin = Math.PI/9.0;		// 視野角
	private double fovMax = Math.PI*2;
	private double fovCost = 5;
	private float sightMin = 10.0f;				// 視野
	private float sightMax = 150.0f;
	private float sightCost = 5;
	private double speedMin = 0.2;				// スピード
	private double speedMax = 2.0;
	private double speedCost = 10;
	private int lifeSpanMin = 50000;			// 寿命の長さ
	private int lifeSpanMax = 250000;
	private int lifeSpanCost = 10;
	private int childSpanMin = 60;				// 子供が作れるようになるまでの時間 (小さい方が高コスト)
	private int childSpanMax = 1800;
	private int childSpanCost = 5;
	private float sizeMin = 5.0f;				// 大きさ
	private float sizeMax = 10.0f;
	private double walkPaceMin = 0.0;			// 歩く速さ
	private double walkPaceMax = 2.0;
	private int MutationMin = 1;				// 突然変異が起きる箇所の数
	private int MutationMax = 3;
	private double MutationInfluence = 0.05;	// 能力の突然変異の影響の割合
	private double MutationRate = 0.5;			// 突然変異の確率
	
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
		
		// 各種能力値の設定
		double cost = 0;
		
		float clr = (float)(rgbMin+Math.random()*(rgbMax-rgbMin));
		float clg = (float)(rgbMin+Math.random()*(rgbMax-rgbMin));
		float clb = (float)(rgbMin+Math.random()*(rgbMax-rgbMin));
		
		Edibility edibility = Edibility.random();
		if(edibility.equals(Edibility.Flesh)){
			cost += FleshEaterCost;
		}else if(edibility.equals(Edibility.Plant)){
			cost += PlantEaterCost;
		}else if(edibility.equals(Edibility.Mixed)){
			cost += MixedEaterCost;
		}
		
		double fovVal = Math.random();
		double fov = fovMin + fovVal*(fovMax-fovMin);
		cost += fovVal*fovCost;
		
		float sightVal = (float)Math.random();
		float sight = sightMin + sightVal*(sightMax-sightMin);
		cost += sightVal*sightCost;
		
		double speedVal = Math.random();
		double speed = speedMin + speedVal*(speedMax-speedMin);
		cost += speedVal*speedCost;
		
		double lifeSpanVal = Math.random();
		int lifeSpan = (int)(lifeSpanMin + lifeSpanVal*(lifeSpanMax-lifeSpanMin));
		cost += lifeSpanVal*lifeSpanCost;
		
		double childSpanVal = Math.random();
		int childSpan = (int)(childSpanMin + childSpanVal*(childSpanMax-childSpanMin));
		cost += (1.0-childSpanVal)*childSpanCost;
		
		b.setParents(null, null);
		b.setGeneHead(geneHead);
		b.setEdibility(edibility);
		b.setColor(clr, clg, clb);
		b.setFov(fov);
		b.setSight(sight);
		b.setSpeed(speed);
		b.setLifeSpan(lifeSpan);
		b.setChildSpan(childSpan);
		b.setCost(cost);
	}
	
	// Animalの交配
	public void crossEntities(Animal e1, Animal e2){
		if(!e1.canMakeChild() || !e2.canMakeChild()){
			return;
		}
		e1.setChildTime(0);
		e2.setChildTime(0);
		
		// 子供のエネルギーを求める
		double energy = e1.getEnergy() < e2.getEnergy() ? e1.getEnergy() : e2.getEnergy();
		e1.reduceEnergy(energy/2);
		e2.reduceEnergy(energy/2);
		
		// ビルダーの生成
		Animal.Builder b = new Animal.Builder(world, (e1.getX()+e2.getX())/2f, (e1.getY()+e2.getY())/2f, energy);
		
		// ツリーの交叉
		GeneNode geneTree = crossTree(e1.getGeneHead(), e2.getGeneHead());
		
		// ツリーの突然変異
		mutateTree(geneTree);
		
		// 能力値の算出
		double cost = 0;
		
		float clr = (float)((e1.getR() + e2.getR()) / 2.0);
		float clg = (float)((e1.getG() + e2.getG()) / 2.0);
		float clb = (float)((e1.getB() + e2.getB()) / 2.0);
		clr = (float)mutateState(clr, rgbMax, rgbMin);
		clg = (float)mutateState(clg, rgbMax, rgbMin);
		clb = (float)mutateState(clb, rgbMax, rgbMin);
		
		Edibility edibility;
		double edibilityVal = Math.random();
		if(edibilityVal < EdibilityChangeRate){
			edibility = Edibility.random();
		}else if(edibilityVal < EdibilityChangeRate + (1.0-EdibilityChangeRate)/2){
			edibility = e1.getEdibility();
		}else{
			edibility = e2.getEdibility();
		}
		if(edibility.equals(Edibility.Flesh)){
			cost += FleshEaterCost;
		}else if(edibility.equals(Edibility.Plant)){
			cost += PlantEaterCost;
		}else if(edibility.equals(Edibility.Mixed)){
			cost += MixedEaterCost;
		}
		
		
		double fov = (e1.getFov() + e2.getFov()) / 2.0;
		fov = mutateState(fov, fovMax, fovMin);
		double fovVal = getStateRatio(fov, fovMax, fovMin);
		cost += fovVal*fovCost;
		
		float sight = (float)((e1.getSight() + e2.getSight()) / 2.0);
		sight = (float)mutateState(sight, sightMax, sightMin);
		double sightVal = getStateRatio(sight, sightMax, sightMin);
		cost += sightVal*sightCost;
		
		double speed = (e1.getSpeed() + e2.getSpeed()) / 2.0;
		speed = mutateState(speed, speedMax, speedMin);
		double speedVal = getStateRatio(speed, speedMax, speedMin);
		cost += speedVal*speedCost;
		
		int lifeSpan = (e1.getLifeSpan() + e2.getLifeSpan()) / 2;
		lifeSpan = (int)mutateState(lifeSpan, lifeSpanMax, lifeSpanMin);
		double lifeSpanVal = getStateRatio(lifeSpan, lifeSpanMax, lifeSpanMin);
		cost += lifeSpanVal*lifeSpanMin;
		
		int childSpan = (e1.getChildSpan() + e1.getChildSpan() ) / 2;
		childSpan = (int)mutateState(childSpan, childSpanMax, childSpanMin);
		double childSpanVal = getStateRatio(childSpan, childSpanMax, childSpanMin);
		cost += (1.0-childSpanVal)*childSpanCost;
		
		// ビルダーの設定
		b.setParents(e1, e2);
		b.setGeneHead(geneTree);
		b.setEdibility(edibility);
		b.setColor(clr, clg, clb);
		b.setFov(fov);
		b.setSight(sight);
		b.setSpeed(speed);
		b.setLifeSpan(lifeSpan);
		b.setChildSpan(childSpan);
		b.setCost(cost);
		
		// 子供の生成、ワールドへの追加
		Animal child = b.build();
		world.addEntity(child);
	}
	
	// 能力値の割合を求める
	private double getStateRatio(double state, double max, double min){
		return (state - min) / (max - min);
	}
	
	// 能力値の突然変異
	private double mutateState(double state, double max, double min){
		Random rand = new Random();
		state = state + rand.nextGaussian()*(max-min)*MutationInfluence;
		if(state < min) state = min;
		else if(max < state) state = max;
		return state;
	}
	
	// 行動ツリーの突然変異
	private void mutateTree(GeneNode tree){
		// 一定確率で突然変異なしの終了
		if(MutationRate < Math.random()){
			return;
		}
		
		// 何箇所突然変異させるか
		int mutateTimes = MutationMin + (int)(Math.random()*(MutationMax-MutationMin));
		
		List<GeneNode> list = new ArrayList<GeneNode>();
		makeNodeList(list, tree);
		
		for(int i = 0; i < mutateTimes; i++){
			int elem = (int)(Math.random()*list.size());
			list.get(elem).mutation();;
		}
	}
	
	// 行動ツリーの交叉
	private GeneNode crossTree(GeneNode t1, GeneNode t2){
		GeneNode geneTree = null;
		
		// ツリーのコピー
		GeneNode parentTree1 = copyGeneTree(t1);
		GeneNode parentTree2 = copyGeneTree(t2);
		
		// ノードリストの作成
		List<GeneNode> baseList = new ArrayList<GeneNode>();
		List<GeneNode> subList = new ArrayList<GeneNode>();
		if(Math.random() < 0.5){	// ベースとなるツリーの決定
			geneTree = parentTree1;
			makeNodeList(baseList, parentTree1);
			makeNodeList(subList, parentTree2);
		}else{
			geneTree = parentTree2;
			makeNodeList(baseList, parentTree2);
			makeNodeList(subList, parentTree1);
		}
		
		// 交叉するノードと部分木を決定
		int crossElem = (int)(Math.random()*baseList.size()) + 1;
		int subTreeElem = (int)(Math.random()*subList.size());
		
		// ツリーを合成する
		GeneNode subTree = subList.get(subTreeElem);
		if(crossElem == baseList.size()){
			geneTree = subTree;
		}else{
			GeneNode crossNode = baseList.get(crossElem);
			if(crossNode instanceof IfNode && Math.random() < 0.5){	// 真・偽、どちらに部分木を付け足すか
				((IfNode)crossNode).setFalseNext(subTree);
			}else{
				crossNode.setNext(subTree);
			}
		}
		
		return geneTree;
	}
	
	// ツリーをコピーする
	private GeneNode copyGeneTree(GeneNode head){
		if(head == null){
			return null;
		}
		GeneNode clone = (GeneNode)head.clone();
		clone.setNext(copyGeneTree(clone.getNext()));
		if(clone instanceof IfNode){
			((IfNode)clone).setFalseNext(copyGeneTree(((IfNode)clone).getFalseNext()));
		}
		
		return clone;
	}
	
	// ツリーのノードをリストにまとめる
	private void makeNodeList(List<GeneNode> list, GeneNode gn){
		if(gn == null){
			return;
		}
		list.add(gn);
		makeNodeList(list, gn.getNext());
		if(gn instanceof IfNode){
			makeNodeList(list, ((IfNode)gn).getFalseNext());
		}
	}
	
	// 友好エンティティか？
	public boolean isFriend(Animal se, Animal oe){
		if(se.getClass().equals(oe.getClass())){
			return true;
		}
		return false;
	}
	
	// 捕食可能なタイプのエンティティか？
	public boolean isEdible(Animal se, Entity oe){
		Edibility edibility = se.getEdibility();
		if(edibility.equals(Edibility.Plant) || edibility.equals(Edibility.Mixed)){
			if(oe instanceof Plant){
				return true;
			}
		}
		if(edibility.equals(Edibility.Flesh) || edibility.equals(Edibility.Mixed)){
			if(oe instanceof Animal){
				return true;
			}
		}
		return false;
	}

	// ゲッタ
	public double getPlantEnergyMin() { return plantEnergyMin; }
	public double getPlantEnergyMax() { return plantEnergyMax; }
	public float getSizeMin() { return sizeMin; }
	public float getSizeMax() { return sizeMax; }
	public double getWalkPaceMin() { return walkPaceMin; }
	public double getWalkPaceMax() { return walkPaceMax; }
}
