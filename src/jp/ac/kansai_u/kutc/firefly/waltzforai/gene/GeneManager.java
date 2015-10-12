package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import java.util.ArrayList;
import java.util.HashSet;
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
	
	private List<Animal> plantEaterOrigin;	// 始祖動物（初期エンティティの親）
	private List<Animal> fleshEaterOrigin;
	private List<Animal> mixedEaterOrigin;
	private int originNum = 4;				// 始祖動物の種類数
	private int decentDepth = 3;			// 親エンティティを何代前まで遡って比較するか
	
	private double plantEaterPreyRank = 1.25;	// 食性による捕食可能ランクの補正
	private double mixedEaterPreyRank = 1.0;
	private double fleshEaterPreyRank = 0.75;
	
	private double plantEnergyMin = 30000;	// 植物のエネルギー
	private double plantEnergyMax = 90000;
	
	// 遺伝子情報、ステータスの最小値，最大値、コスト
	private float rgbMin = 0;					// 体色
	private float rgbMax = 255;
	private double plantEaterCost = 10;			// 草食のコスト
	private double fleshEaterCost = 20;			// 肉食のコスト
	private double mixedEaterCost = 30;			// 雑食のコスト
	private double EdibilityChangeRate = 0.05;	// 食性が変わる確率
	private double preyRankCost = 100;			// 捕食可能ランクのコスト
	private double fovMin = Math.PI/9.0;		// 視野角
	private double fovMax = Math.PI*2;
	private double fovCost = 5;
	private float sightMin = 10.0f;				// 視野
	private float sightMax = 150.0f;
	private float sightCost = 5;
	private double speedMin = 0.2;				// スピード
	private double speedMax = 2.0;
	private double speedCost = 10;
	private double lifeSpanMin = 50000;			// 寿命の長さ
	private double lifeSpanMax = 250000;
	private double lifeSpanCost = 10;
	private double childSpanMin = 180;			// 子供が作れるようになるまでの時間 (小さい方が高コスト)
	private double childSpanMax = 360;
	private double childSpanCost = 5;
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
		
		plantEaterOrigin = new ArrayList<Animal>(originNum);
		fleshEaterOrigin = new ArrayList<Animal>(originNum);
		mixedEaterOrigin = new ArrayList<Animal>(originNum);
		for(int i = 0; i < originNum; i++){
			plantEaterOrigin.add(new Animal.Builder(this.world, 0, 0, 0).build());
			fleshEaterOrigin.add(new Animal.Builder(this.world, 0, 0, 0).build());
			mixedEaterOrigin.add(new Animal.Builder(this.world, 0, 0, 0).build());
		}
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
		
		// 親エンティティを始祖動物からランダムに選択
		int parentElem1 = (int)(Math.random()*originNum), parentElem2 = (int)(Math.random()*originNum);
		if(originNum > 1) while(parentElem1 == parentElem2) parentElem2 = (int)(Math.random()*originNum);
		Animal parent1 = null, parent2 = null;
		
		Edibility edibility = Edibility.random();
		if(edibility.equals(Edibility.Flesh)){
			cost += fleshEaterCost;
			parent1 = fleshEaterOrigin.get(parentElem1);
			parent2 = fleshEaterOrigin.get(parentElem2);
		}else if(edibility.equals(Edibility.Plant)){
			cost += plantEaterCost;
			parent1 = plantEaterOrigin.get(parentElem1);
			parent2 = plantEaterOrigin.get(parentElem2);
		}else if(edibility.equals(Edibility.Mixed)){
			cost += mixedEaterCost;
			parent1 = mixedEaterOrigin.get(parentElem1);
			parent2 = mixedEaterOrigin.get(parentElem2);
		}
		
		double preyRank = Math.random();
		cost += preyRank*preyRankCost;
		
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
		double lifeSpan = lifeSpanMin + lifeSpanVal*(lifeSpanMax-lifeSpanMin);
		cost += lifeSpanVal*lifeSpanCost;
		
		double childSpanVal = Math.random();
		double childSpan = childSpanMin + childSpanVal*(childSpanMax-childSpanMin);
		cost += (1.0-childSpanVal)*childSpanCost;
		
		float size = (float)(sizeMin + (cost/100 > 1.0 ? 1.0 : cost/100) * (sizeMax-sizeMin));
		sight = size + sight;	// 視野は身体のサイズにプラスする
		
		b.setParents(parent1, parent2);
		b.setGeneHead(geneHead);
		b.setEdibility(edibility);
		b.setColor(clr, clg, clb);
		b.setPreyRank(preyRank);
		b.setSize(size);
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
			cost += fleshEaterCost;
		}else if(edibility.equals(Edibility.Plant)){
			cost += plantEaterCost;
		}else if(edibility.equals(Edibility.Mixed)){
			cost += mixedEaterCost;
		}
		
		double preyRank = (e1.getPreyRank() + e2.getPreyRank()) / 2.0;
		preyRank = mutateState(preyRank, 1.0, 0.0);
		double preyRankVal = getStateRatio(preyRank, 1.0, 0.0);
		cost += preyRankVal*preyRankCost;
		
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
		
		double lifeSpan = (e1.getLifeSpan() + e2.getLifeSpan()) / 2.0;
		lifeSpan = mutateState(lifeSpan, lifeSpanMax, lifeSpanMin);
		double lifeSpanVal = getStateRatio(lifeSpan, lifeSpanMax, lifeSpanMin);
		cost += lifeSpanVal*lifeSpanCost;
		
		double childSpan = (e1.getChildSpan() + e1.getChildSpan() ) / 2.0;
		childSpan = mutateState(childSpan, childSpanMax, childSpanMin);
		double childSpanVal = getStateRatio(childSpan, childSpanMax, childSpanMin);
		cost += (1.0-childSpanVal)*childSpanCost;
		
		float size = (float)(sizeMin + (cost/100 > 1.0 ? 1.0 : cost/100) * (sizeMax-sizeMin));
		sight = size + sight;	// 視野は身体のサイズにプラスする
		
		// ビルダーの設定
		b.setParents(e1, e2);
		b.setGeneHead(geneTree);
		b.setEdibility(edibility);
		b.setColor(clr, clg, clb);
		b.setPreyRank(preyRankVal);
		b.setSize(size);
		b.setFov(fov);
		b.setSight(sight);
		b.setSpeed(speed);
		b.setLifeSpan(lifeSpan);
		b.setChildSpan(childSpan);
		b.setCost(cost);
		
		world.addEntity(b.build());
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
	public boolean isFriend(Animal animal, Entity entity){
		if(entity instanceof Plant){
			return false;
		}
		// ハッシュセットで親を比較する
		HashSet<Animal> descent1 = new HashSet<Animal>(), descent2 = new HashSet<Animal>();
		descent1.add(animal); descent2.add((Animal)entity);
		checkParents(animal, descent1, decentDepth);
		checkParents((Animal)entity, descent2, decentDepth);
		for(Animal parent: descent1){
			// 同じ親があれば友好
			if(descent2.contains(parent)){
				return true;
			}
		}
		return false;
	}
	
	// 親エンティティを調べる
	private void checkParents(Animal animal, HashSet<Animal> descent, int depth){
		if(depth <= 0){
			return;
		}
		Animal parent1 = animal.getParent1(), parent2 = animal.getParent2();
		if(parent1 != null && descent.add(parent1)){
			checkParents(parent1, descent, depth-1);
		}
		if(parent2 != null && descent.add(parent2)){
			checkParents(parent2, descent, depth-1);
		}
	}
	
	// 捕食可能なエンティティか？
	public boolean isEdible(Animal se, Entity oe){
		Edibility edibility = se.getEdibility();
		if(edibility.equals(Edibility.Plant) || edibility.equals(Edibility.Mixed)){
			if(oe instanceof Plant){
				return true;
			}
		}
		if(edibility.equals(Edibility.Flesh) || edibility.equals(Edibility.Mixed)){
			if(oe instanceof Animal && se.getPreyRank() > ((Animal)oe).getPreyRank()){
				return true;
			}
		}
		return false;
	}

	// ゲッタ
	public double getPlantEaterPreyRank() { return plantEaterPreyRank; }
	public double getFleshEaterPreyRank() { return fleshEaterPreyRank; }
	public double getMixedEaterPreyRank() { return mixedEaterPreyRank; }
	public double getPlantEnergyMin() { return plantEnergyMin; }
	public double getPlantEnergyMax() { return plantEnergyMax; }
	public float getSizeMin() { return sizeMin; }
	public float getSizeMax() { return sizeMax; }
	public double getWalkPaceMin() { return walkPaceMin; }
	public double getWalkPaceMax() { return walkPaceMax; }
}
