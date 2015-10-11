package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import java.util.ArrayList;
import java.util.List;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import jp.ac.kansai_u.kutc.firefly.waltzforai.Util;
import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.gene.GeneNode;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.SplitMap;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.TreeSight;

public class Animal extends Entity {
	private TreeSight treeSight;			// 視野の所属ツリーオブジェクト

	// 遺伝情報
	private Animal parent1, parent2;		// 親Animal
	private GeneNode geneHead;				// 行動を表すツリーのヘッド
	private Edibility edibility;			// 食性 (捕食可能なもの)
	private double preyRank;				// 捕食ランク
	private float sight;					// 視野の広さ (半径)
	private double fov;						// 視野角 (ラジアン)
	private double speed;					// 足の速さ
	private double lifeSpan;				// 寿命の長さ
	private double childSpan;				// 子供が作れるようになるまでの時間
	private float r, g, b;					// 色
	private double cost;					// 行動コスト

	// ステータス
	private double direction;				// 進行方向 (ラジアン)
	private double walkPace;				// 移動ペース (0.0~2.0)
	private double age;						// 年齢
	private double childTime;				// 子供を作ってから経過した時間

	// 近接エンティティに関するリスト
	private List<Entity> nearEntities;		// 近接エンティティリスト
	private List<Entity> inSightPreys;		// 視界内捕食可能エンティティリスト
	private List<Animal> inSightFriends;	// 視界内友好エンティティリスト
	private List<Animal> inSightEnemies;	// 視界内敵エンティティリスト

	// クラスビルダー
	public static class Builder{
		private World world;
		private float x, y;
		private double energy;

		private Animal parent1, parent2;
		private GeneNode geneHead;
		private Edibility edibility;
		private double preyRank;
		private float size;
		private float sight;
		private double fov;
		private double speed;
		private float r, g, b;
		private double cost;
		private double lifeSpan;
		private double childSpan;

		public Builder(World world, float x, float y, double energy){
			this.world = world;
			this.x = x;
			this.y = y;
			this.energy = energy;
		}

		public Builder random(){
			world.getGeneManager().buildRandomAnimal(this);
			return this;
		}

		public Animal build(){
			return new Animal(this);
		}

		public void setParents(Animal parent1, Animal parent2) { this.parent1 = parent1; this.parent2 = parent2; }
		public void setGeneHead(GeneNode geneHead) { this.geneHead = geneHead; }
		public void setEdibility(Edibility edibility) { this.edibility = edibility; }
		public void setPreyRank(double preyRank) { this.preyRank = preyRank; }
		public void setSight(float sight) { this.sight = sight; }
		public void setFov(double fov) { this.fov = fov; }
		public void setSpeed(double speed) { this.speed = speed; }
		public void setColor(float r, float g, float b) { this.r = r; this.g = g; this.b = b; }
		public void setCost(double cost) { this.cost = cost; }
		public void setLifeSpan(double lifeSpan) {	this.lifeSpan = lifeSpan; }
		public void setChildSpan(double childSpan) { this.childSpan = childSpan; }
		public void setSize(float size) { this.size = size; }
	}

	public Animal(Builder b) {
		super(b.world, b.x, b.y, b.energy);
		this.parent1 = b.parent1;
		this.parent2 = b.parent2;
		this.geneHead = b.geneHead;
		this.edibility = b.edibility;
		this.preyRank = b.preyRank;
		this.size = b.size;
		this.sight = b.sight;
		this.fov = b.fov;
		this.speed = b.speed;
		this.childSpan = b.childSpan;
		this.lifeSpan = b.lifeSpan;
		this.r = b.r; this.g = b.g; this.b = b.b;
		this.cost = b.cost;
		this.nearEntities = new ArrayList<Entity>();
		this.inSightPreys = new ArrayList<Entity>();
		this.inSightFriends = new ArrayList<Animal>();
		this.inSightEnemies = new ArrayList<Animal>();
		this.direction = Math.random() * Math.PI * 2;
		this.walkPace = 1.0;
		this.age = this.childTime = 0;
	}

	// 1フレーム毎に実行される描画メソッド
	@Override
	public void draw(Display display) {
		display.noStroke();
		display.fill(230, 230, 0, 20);
		display.arc(x, y, sight*2, sight*2, (float)(direction-fov/2), (float)(direction+fov/2));
		display.fill(r, g, b, 200);
		if(edibility.equals(Edibility.Plant)){
			// 草食動物
			display.ellipse(x, y, size*2, size*2);
		}else if(edibility.equals(Edibility.Flesh)){
			// 肉食動物
			display.beginShape();
			display.vertex((float)(x+Math.cos(direction)*size), (float)(y+Math.sin(direction)*size));
			display.vertex((float)(x+Math.cos(direction+Math.PI*2/3)*size), (float)(y+Math.sin(direction+Math.PI*2/3)*size));
			display.vertex((float)(x+Math.cos(direction+Math.PI)*size), (float)(y+Math.sin(direction+Math.PI)*size));
			display.vertex((float)(x+Math.cos(direction-Math.PI*2/3)*size), (float)(y+Math.sin(direction-Math.PI*2/3)*size));
			display.endShape();
		}else{
			// 雑食動物
			display.beginShape();
			display.vertex((float)(x+Math.cos(direction)*size), (float)(y+Math.sin(direction)*size));
			display.vertex((float)(x+Math.cos(direction+Math.PI*1/3)*size), (float)(y+Math.sin(direction+Math.PI*1/3)*size));
			display.vertex((float)(x+Math.cos(direction+Math.PI*5/6)*size), (float)(y+Math.sin(direction+Math.PI*5/6)*size));
			display.vertex((float)(x+Math.cos(direction-Math.PI*5/6)*size), (float)(y+Math.sin(direction-Math.PI*5/6)*size));
			display.vertex((float)(x+Math.cos(direction-Math.PI*1/3)*size), (float)(y+Math.sin(direction-Math.PI*1/3)*size));
			display.endShape();
		}
		
		display.text(String.format("%.0f", energy), x+20, y-20);
		display.text(String.format("%.0f", lifeSpan-age), x+20, y+20);
	}

	// 1フレーム毎に実行される更新メソッド
	@Override
	public void update() {
		if(!isAlive()){
			return;
		}
		collisionCheck();					// 衝突判定
		geneHead.perform(this);				// 行動ツリーの実行	
		age += world.getGameSpeed();
		childTime += world.getGameSpeed();	// 経過時間に加算
		if(lifeSpan < age){
			world.returnAllEnergy(this);
		}
	}

	// 衝突判定
	protected void collisionCheck(){
		for(int i = 0; i < nearEntities.size(); i++){
			Entity entity = nearEntities.get(i);
			if(entity.equals(this) || !entity.isAlive() || (edibility.equals(Edibility.Flesh) && entity instanceof Plant)){
				continue;
			}
			if(Util.isCollided(this, entity)){									// 衝突しているか？
				if(world.getGeneManager().isFriend(this, entity)){
					world.getGeneManager().crossEntities(this, (Animal)entity);
				}else if(world.getGeneManager().isEdible(this, entity)){		// 捕食可能か？
					eat(entity);
				}
			}else{
				if(Util.inSight(this, entity)){			// 視界内にいるか？
					assortInSightEntity(entity);
				}
			}
		}

		clearNearEntity(); // 近接エンティティリストをクリア
	}

	// 視界内のエンティティを仕分ける
	private void assortInSightEntity(Entity entity){
		if(entity instanceof Plant){
			inSightPreys.add(entity);
		}else{
			if(world.getGeneManager().isFriend(this, (Animal)entity)){	// 友好関係にあるか？
				inSightFriends.add((Animal)entity);
			}else{	// 味方ではない
				if(world.getGeneManager().isEdible(this, entity)){	// 捕食可能か？
					inSightPreys.add(entity);
				}else if(world.getGeneManager().isEdible((Animal)entity, this)){	// 捕食されうるか？
					inSightEnemies.add((Animal)entity);
				}
			}
		}
	}

	// 1フレーム分の移動
	@Override
	public void move(){
		if(!isAlive()){
			return;
		}
		
		double vecX = Math.cos(direction)*speed*walkPace*world.getGameSpeed();
		double vecY = Math.sin(direction)*speed*walkPace*world.getGameSpeed();
		float newX = (float)(x + vecX), newY = (float)(y + vecY);

		// 壁との衝突判定
		if(newX - size < 0){ // 左壁
			direction = Math.atan2(vecY, -vecX);
			newX = size*2 - newX;
		}else if(world.getWidth() < newX + size){ // 右壁
			direction = Math.atan2(vecY, -vecX);
			newX = world.getWidth()*2 - size*2 - newX;
		}

		if(newY - size < 0){ // 上壁
			direction = Math.atan2(-vecY, vecX);
			newY = size*2 - newY;
		}else if(world.getHeight() < newY + size){ // 下壁
			direction = Math.atan2(-vecY, vecX);
			newY = world.getHeight()*2 - size*2 - newY;
		}

		x = newX;
		y = newY;

		// 移動に使ったenergyをworldに返す
		world.returnCostEnergy(this);

		reregist();	// 空間ツリーへの再登録
		clearInSightEntity(); // エンティティリストのクリア
	}

	// 捕食
	protected void eat(Entity entity){
		double energy = entity.getEnergy();
		entity.reduceEnergy(energy);
		this.energy += energy;
	}

	// 死亡。ツリーとワールドから削除
	@Override
	public void die(){
		isAlive = false;
		treeBody.remove();
		treeSight.remove();
		world.removeEntity(this);
	}

	// 空間ツリーへの再登録
	protected void reregist() {
		treeBody.remove();
		treeSight.remove();
		SplitMap sm = world.getSplitMap();
		sm.regist(treeBody);
		sm.regist(treeSight);
	}

	// 近接エンティティリストへの追加
	public void addNearEntity(Entity entity){
		nearEntities.add(entity);
	}

	// 近接エンティティリストのクリア
	protected void clearNearEntity(){
		nearEntities.clear();
	}

	// 視界内エンティティリストのクリア
	protected void clearInSightEntity(){
		inSightEnemies.clear();
		inSightFriends.clear();
		inSightPreys.clear();
	}

	public boolean canMakeChild(){
		if(childSpan <= childTime){
			return true;
		}
		return false;
	}
	
	public double getPreyRank() {
		switch(edibility){
		case Plant: return preyRank*world.getGeneManager().getPlantEaterPreyRank();
		case Flesh:	return preyRank*world.getGeneManager().getFleshEaterPreyRank();
		case Mixed: return preyRank*world.getGeneManager().getMixedEaterPreyRank();
		default: return -1.0;
		}
	}
	
	// セッタ
	public void setTreeSight(TreeSight obj) { this.treeSight = obj; }
	public void setWalkPace(double walkPace) { this.walkPace = walkPace; }
	public void setDirection(double direction) { this.direction = direction; }
	public void setChildTime(double childTime) { this.childTime = childTime; }
	
	// ゲッタ	
	public Animal getParent1() { return parent1; }
	public Animal getParent2() { return parent2; }
	public GeneNode getGeneHead(){ return geneHead; }
	public Edibility getEdibility() { return edibility; }
	public float getSight(){ return sight; }
	public double getFov(){ return fov; }
	public double getDirection() { return direction; }
	public double getSpeed() { return speed; }
	public double getChildSpan() { return childSpan; }
	public double getLifeSpan() { return lifeSpan; }
	public double getWalkPace() { return walkPace; }
	public float getR(){ return r; }
	public float getG(){ return g; }
	public float getB(){ return b; }
	public double getCost(){ return cost; }
	public List<Entity> getNearEntities() { return nearEntities; }
	public List<Entity> getInSightPreys() { return inSightPreys; }
	public List<Animal> getInSightFriends() { return inSightFriends; }
	public List<Animal> getInSightEnemies() { return inSightEnemies; }
}
