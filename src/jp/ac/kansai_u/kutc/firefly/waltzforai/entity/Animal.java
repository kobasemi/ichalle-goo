package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import java.util.ArrayList;
import java.util.List;

import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.gene.GeneManager;
import jp.ac.kansai_u.kutc.firefly.waltzforai.gene.GeneNode;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.SplitMap;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.TreeSight;

public abstract class Animal extends Entity {	
	protected TreeSight treeSight;			// 視野の所属ツリーオブジェクト
	
	// 遺伝情報
	protected GeneNode geneHead;			// 行動を表すツリーのヘッド
	protected float sight;					// 視野の広さ (半径)
	protected double fov;					// 視野角 (ラジアン)
	protected double speed;					// 足の速さ
	protected float r, g, b;				// 色
	protected double cost;					// 行動コスト
	
	// ステータス
	protected double direction;				// 進行方向 (ラジアン)
	protected double walkPace;				// 移動ペース (0.0~2.0)
	
	// 近接エンティティに関するリスト
	protected List<Entity> nearEntities;	// 近接エンティティリスト
	protected List<Animal> inSightFriends;	// 視界内友好エンティティリスト
	protected List<Animal> inSightEnemies;	// 視界内敵エンティティリスト
	protected Entity avoidEnemy;			// 今自分が離れようとしているエンティティ
	protected Entity target;				// 今自分が近づこうとしているエンティティ
	
	// クラスビルダー
	public static abstract class Builder{
		private World world;
		private float x, y;
		private double energy;
		
		private GeneNode geneHead;
		private float sight;
		private double fov;
		private double speed;
		private float r, g, b;
		private double cost;
		
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
		
		public abstract Animal build();

		public void setGeneHead(GeneNode geneHead) { this.geneHead = geneHead; }
		public void setSight(float sight) { this.sight = sight; }
		public void setFov(double fov) { this.fov = fov; }
		public void setSpeed(double speed) { this.speed = speed; }
		public void setColor(float r, float g, float b) { this.r = r; this.g = g; this.b = b; }
		public void setCost(double cost) { this.cost = cost; }
	}
	
	public Animal(Builder b) {
		super(b.world, b.x, b.y, b.energy);
		this.geneHead = b.geneHead;
		this.sight = b.sight;
		this.fov = b.fov;
		this.speed = b.speed;
		this.r = b.r; this.g = b.g; this.b = b.b;
		this.cost = b.cost;
		
		GeneManager gm = world.getGeneManager();
		float sizeMin = gm.getSizeMin(), sizeMax = gm.getSizeMax();
		this.size = (float)(sizeMin + (this.cost/20 > 1.0 ? 1.0 : this.cost/20) * (sizeMax-sizeMin));
		this.nearEntities = new ArrayList<Entity>();
		this.inSightFriends = new ArrayList<Animal>();
		this.inSightEnemies = new ArrayList<Animal>();
		this.direction = Math.random() * Math.PI * 2;
		this.walkPace = 1.0;
	}

	// 1フレーム毎に実行される更新メソッド
	public abstract void update();
	
	// 衝突判定
	protected abstract void collisionCheck();
	
	// 1フレーム分の移動
	protected void move(){
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
		world.returnEnergy(this);
		
		reregist();	// 空間ツリーへの再登録
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
	
	// セッタ
	public void setTreeSight(TreeSight obj){ this.treeSight = obj; }
	public void setWalkPace(double walkPace) { this.walkPace = walkPace; }
	public void setDirection(double direction) { this.direction = direction; }
	public void setAvoidEnemy(Entity avoidEnemy) { this.avoidEnemy = avoidEnemy; }
	
	// ゲッタ	
	public float getSight(){ return sight; }
	public double getFov(){ return fov; }
	public double getDirection() { return direction; }
	public double getWalkPace() { return walkPace; }
	public double getCost(){ return cost; }
	public List<Entity> getNearEntities() { return nearEntities; }
	public List<Animal> getInSightFriends() { return inSightFriends; }
	public List<Animal> getInSightEnemies() { return inSightEnemies; }
}
