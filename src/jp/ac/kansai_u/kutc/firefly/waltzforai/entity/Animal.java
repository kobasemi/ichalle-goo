package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import java.util.ArrayList;
import java.util.List;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import jp.ac.kansai_u.kutc.firefly.waltzforai.Util;
import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.gene.GeneManager;
import jp.ac.kansai_u.kutc.firefly.waltzforai.gene.GeneNode;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.SplitMap;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.TreeSight;

public class Animal extends Entity {
	private TreeSight treeSight;			// 視野の所属ツリーオブジェクト

	// 遺伝情報
	private Animal parent1, parent2;		// 親Animal
	private GeneNode geneHead;				// 行動を表すツリーのヘッド
	private Edibility edibility;			// 食性 (捕食可能なもの)
	private float sight;					// 視野の広さ (半径)
	private double fov;						// 視野角 (ラジアン)
	private double speed;					// 足の速さ
	private int lifeSpan;					// 寿命の長さ
	private int childSpan;					// 子供が作れるようになるまでの時間
	private float r, g, b;					// 色
	private double cost;					// 行動コスト

	// ステータス
	private double direction;				// 進行方向 (ラジアン)
	private double walkPace;				// 移動ペース (0.0~2.0)
	private int age;						// 年齢
	private int childTime;					// 子供を作ってから経過した時間

	// 近接エンティティに関するリスト
	private List<Entity> nearEntities;		// 近接エンティティリスト
	private List<Entity> inSightPreys;		// 視界内捕食可能エンティティリスト
	private List<Animal> inSightFriends;	// 視界内友好エンティティリスト
	private List<Animal> inSightEnemies;	// 視界内敵エンティティリスト
	private Entity avoidEnemy;				// 今自分が離れようとしているエンティティ
	private Entity target;					// 今自分が近づこうとしているエンティティ

	// クラスビルダー
	public static class Builder{
		private World world;
		private float x, y;
		private double energy;

		private Animal parent1, parent2;
		private GeneNode geneHead;
		private Edibility edibility;
		private float sight;
		private double fov;
		private double speed;
		private float r, g, b;
		private double cost;
		private int lifeSpan;
		private int childSpan;

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
		public void setSight(float sight) { this.sight = sight; }
		public void setFov(double fov) { this.fov = fov; }
		public void setSpeed(double speed) { this.speed = speed; }
		public void setColor(float r, float g, float b) { this.r = r; this.g = g; this.b = b; }
		public void setCost(double cost) { this.cost = cost; }
		public void setLifeSpan(int lifeSpan) {	this.lifeSpan = lifeSpan; }
		public void setChildSpan(int childSpan) { this.childSpan = childSpan; }
	}

	public Animal(Builder b) {
		super(b.world, b.x, b.y, b.energy);
		this.parent1 = b.parent1;
		this.parent2 = b.parent2;
		this.geneHead = b.geneHead;
		this.edibility = b.edibility;
		this.fov = b.fov;
		this.speed = b.speed;
		this.childSpan = b.childSpan;
		this.lifeSpan = b.lifeSpan;
		this.r = b.r; this.g = b.g; this.b = b.b;
		this.cost = b.cost;

		GeneManager gm = world.getGeneManager();
		float sizeMin = gm.getSizeMin(), sizeMax = gm.getSizeMax();
		this.size = (float)(sizeMin + (this.cost/20 > 1.0 ? 1.0 : this.cost/20) * (sizeMax-sizeMin));
		this.sight = this.size + b.sight;	// 視野は身体のサイズにプラスする
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
		// 謙隆add
		display.omnivorousNum++;
		if(edibility.equals(Edibility.Plant)){
			display.ellipse(x, y, size*2, size*2);
			// 謙隆add
			display.plantEaterNum++;
		}else if(edibility.equals(Edibility.Flesh)){
			display.beginShape();
			display.vertex((float)(x+Math.cos(direction)*size), (float)(y+Math.sin(direction)*size));
			display.vertex((float)(x+Math.cos(direction+Math.PI*2/3)*size), (float)(y+Math.sin(direction+Math.PI*2/3)*size));
			display.vertex((float)(x+Math.cos(direction+Math.PI)*size), (float)(y+Math.sin(direction+Math.PI)*size));
			display.vertex((float)(x+Math.cos(direction-Math.PI*2/3)*size), (float)(y+Math.sin(direction-Math.PI*2/3)*size));
			display.endShape();
			// 謙隆add
			display.fleshEaterNum++;
		}else{
			display.rect(x-size/2, y-size/2, size, size);	//仮描画
		}
		display.text(String.format("%.0f", energy), x+20, y-20);
		display.text(String.format("%d", lifeSpan-age), x+20, y+20);
	}

	// 1フレーム毎に実行される更新メソッド
	@Override
	public void update() {
		collisionCheck();			// 衝突判定
		geneHead.perform(this);		// 行動ツリーの実行
		age++; childTime++;			// 経過時間に加算
		if(lifeSpan < age){
			world.returnAllEnergy(this);
		}
	}

	// 衝突判定
	protected void collisionCheck(){
		for(int i = 0; i < nearEntities.size(); i++){
			Entity entity = nearEntities.get(i);
			if(edibility.equals(Edibility.Flesh) && entity instanceof Plant){
				continue;	// 肉食の場合、plantをスルーする
			}
			if(entity != this && Util.inSight(this, entity)){				// 視界内にいるか？
				if(Util.isCollided(this, entity)){							// 衝突しているか？
					if(world.getGeneManager().isEdible(this, entity)){		// 捕食可能か？
						eat(entity);
					}else if(world.getGeneManager().isFriend(this, (Animal)entity)){
						world.getGeneManager().crossEntities(this, (Animal)entity);
					}
				}else{
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
		if(energy <= 0){	// 死亡時は何もしない
			return;
		}

		double vecX = Math.cos(direction)*speed*walkPace, vecY = Math.sin(direction)*speed*walkPace;
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

	// セッタ
	public void setTreeSight(TreeSight obj) { this.treeSight = obj; }
	public void setWalkPace(double walkPace) { this.walkPace = walkPace; }
	public void setDirection(double direction) { this.direction = direction; }
	public void setAvoidEnemy(Entity avoidEnemy) { this.avoidEnemy = avoidEnemy; }
	public void setTarget(Entity target) { this.target = target; }
	public void setChildTime(int childTime) { this.childTime = childTime; }

	// ゲッタ
	public GeneNode getGeneHead(){ return geneHead; }
	public Edibility getEdibility() { return edibility; }
	public float getSight(){ return sight; }
	public double getFov(){ return fov; }
	public double getDirection() { return direction; }
	public double getSpeed() { return speed; }
	public int getChildSpan() { return childSpan; }
	public int getLifeSpan() { return lifeSpan; }
	public double getWalkPace() { return walkPace; }
	public float getR(){ return r; }
	public float getG(){ return g; }
	public float getB(){ return b; }
	public double getCost(){ return cost; }
	public List<Entity> getNearEntities() { return nearEntities; }
	public List<Animal> getInSightFriends() { return inSightFriends; }
	public List<Animal> getInSightEnemies() { return inSightEnemies; }
}
