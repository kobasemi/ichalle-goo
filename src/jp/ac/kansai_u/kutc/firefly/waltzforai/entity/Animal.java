package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import java.util.ArrayList;
import java.util.HashSet;
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
	private List<HashSet<Animal>> parents;	// 親Animal
	private GeneNode geneHead;				// 行動を表すツリーのヘッド
	private Edibility edibility;			// 食性 (捕食可能なもの)
	private double preyRank;				// 捕食ランク
	private double energyLimit;				// エネルギーの上限
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
	private boolean isCollide;				// 衝突している

	// 近接エンティティに関するリスト
	private List<Entity> inSightPreys;		// 視界内捕食可能エンティティリスト
	private List<Animal> inSightFriends;	// 視界内友好エンティティリスト
	private List<Animal> inSightEnemies;	// 視界内敵エンティティリスト

	// クラスビルダー
	public static class Builder{
		private World world;
		private float x, y;
		private double energy;

		private List<HashSet<Animal>> parents;
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
		private double energyLimit;

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

		public void setParents(List<HashSet<Animal>> parents) { this.parents = parents; }
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
		public void setEnergyLimit(double energyLimit) { this.energyLimit = energyLimit; }
	}

	public Animal(Builder b) {
		super(b.world, b.x, b.y, b.energy);
		this.parents = b.parents;
		this.geneHead = b.geneHead;
		this.edibility = b.edibility;
		this.preyRank = b.preyRank;
		this.energyLimit = b.energyLimit;
		this.size = b.size;
		this.sight = b.sight;
		this.fov = b.fov;
		this.speed = b.speed;
		this.childSpan = b.childSpan;
		this.lifeSpan = b.lifeSpan;
		this.r = b.r; this.g = b.g; this.b = b.b;
		this.cost = b.cost;
		this.inSightPreys = new ArrayList<Entity>();
		this.inSightFriends = new ArrayList<Animal>();
		this.inSightEnemies = new ArrayList<Animal>();
		this.direction = Math.random() * Math.PI * 2;
		this.walkPace = 1.0;
		this.age = this.childTime = 0;
		this.isCollide = false;
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
		if(isCollide){
			isCollide = false;
		}else{
			geneHead.perform(this);				// 行動ツリーの実行	
		}
	}
	
	// 衝突
	private void collide(Entity entity) {
		if(world.getGeneManager().isFriend(this, entity)){
			world.getGeneManager().crossEntities(this, (Animal)entity);
			direction = Util.getRadian(entity.getX(), entity.getY(), x, y);
			isCollide = true;
		}else if(world.getGeneManager().isEdible(this, entity)){		// 捕食可能か？
			eat(entity);
		}else{
			direction = Util.getRadian(entity.getX(), entity.getY(), x, y);
			isCollide = true;
		}
	}
	
	// 視界内
	private void inSight(Entity entity) {
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
	
	// エンティティとの位置関係を調べる
	public void collisionCheck(Entity e){
		if((edibility.equals(Edibility.Flesh) && e instanceof Plant) || e.equals(this) || !e.isAlive()){
			return;
		}
		
		// 主体と客体の距離が(主体の視界半径+客体の大きさの半径)より大きい場合
	    float dx = e.getX() - x;
	    float dy = e.getY() - y;
	    double distSpr = dx*dx + dy*dy;
		double sar = sight + e.getSize();	// 主体の視界半径+客体の大きさの半径
		double dar = size + e.getSize();	// 主体の大きさ半径+客体の大きさの半径
		
		if(distSpr > sar*sar){
			return;
		}
		
		if(distSpr < dar*dar){
			// 衝突
			collide(e);
			return;
		}
		
		// 客体の座標が主体の視野の扇形の2ベクトル間にある場合
		double v1Rad = direction + fov/2;	// 扇形の1つめのベクトルの角度(ラジアン)
		double v1x = sight * Math.cos(v1Rad);
		double v1y = sight * Math.sin(v1Rad);
		double v2Rad = v1Rad - fov;					// 扇形の2つめのベクトルの角度(ラジアン)
		double v2x = sight * Math.cos(v2Rad);
		double v2y = sight * Math.sin(v2Rad);
		double delta = v1x*v2y - v2x*v1y;
		double alpha = (dx*v2y - dy*v2x) / delta;
		double beta = (-dx*v1y + dy*v1x) / delta;
		if(alpha >= 0.0 && beta >= 0.0){	// 2ベクトルのなす角度が180度を超えると判定が逆転する
			if(fov < Math.PI){
				// 視界内
				inSight(e);
				return;
			}
		}else{
			if(Math.PI < fov){
				inSight(e);
				return;
			}
		}

		//主体の視界である扇形の2つのベクトル線分のいずれかと客体が交点を持つ場合
		double a = v1x*v1x + v1y*v1y;
		double b = -(v1x*dx + v1y*dy);
		double c = dx*dx + dy*dy - e.getSize()*e.getSize();
		double d = b*b - a*c;	// 判別式D
		if(d >= 0.0){
			double t = (-b - Math.sqrt(d) / a);
			if(t >= 0.0 && t <= 1.0){
				inSight(e);
				return;
			}
		}
		a = v2x*v2x + v2y*v2y;
		b = -(v2x*dx + v2y*dy);
		d = b*b - a*c;
		if(d >= 0.0){
			double t = (-b - Math.sqrt(d) / a);
			if(t >= 0.0 && t <= 1.0){
				inSight(e);
				return;
			}
		}

		// 衝突してる場合はここまでプログラムが進まない
		/*// 主体の座標が客体の内部にある場合
		if(distSpr < e.getSize()*e.getSize()){
			inSight(e);
			return;
		}*/
	}

	// 1フレーム分の移動
	@Override
	public void move(){
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
		
		// 経過時間を加算
		age += world.getGameSpeed();
		childTime += world.getGameSpeed();
		if(lifeSpan < age){
			world.returnAllEnergy(this);
		}

		// 移動に使ったenergyをworldに返す
		world.returnCostEnergy(this);
		
		reregist();	// 空間ツリーへの再登録
		clearInSightEntity(); // エンティティリストのクリア
	}

	// 捕食
	protected void eat(Entity entity){
		double energy = entity.getEnergy();
		if(energyLimit < this.energy + energy){
			energy = energyLimit - this.energy;
		}
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
		if(!isAlive){
			return;
		}
		treeBody.remove();
		treeSight.remove();
		SplitMap sm = world.getSplitMap();
		sm.regist(treeBody);
		sm.regist(treeSight);
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
	public List<HashSet<Animal>> getParents(){ return parents; }
	public GeneNode getGeneHead(){ return geneHead; }
	public Edibility getEdibility() { return edibility; }
	public double getEnergyLimit() { return energyLimit; }
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
	public List<Entity> getInSightPreys() { return inSightPreys; }
	public List<Animal> getInSightFriends() { return inSightFriends; }
	public List<Animal> getInSightEnemies() { return inSightEnemies; }
}
