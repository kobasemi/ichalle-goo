package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import java.util.ArrayList;
import java.util.List;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.gene.GeneNode;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.TreeBody;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.TreeSight;

// 動物・植物などのフィールド上の全てのオブジェクトの元となるクラス
public abstract class Entity {
	// ワールド情報
	protected World world;					// 所属ワールド
	protected TreeBody treeBody;			// 所属ツリーオブジェクト
	protected TreeSight treeSight;			// 視野の所属ツリーオブジェクト
	
	// 遺伝情報
	protected GeneNode geneHead;			// 行動を表すツリーのヘッド
	protected float size;					// エンティティの大きさ (半径)
	protected float sight;					// 視野の広さ (半径)
	protected double fov;					// 視野角 (ラジアン)
	protected double speed;					// 足の速さ
	protected float r, g, b;				// 色
	protected double cost;					// 行動コスト
	
	// ステータス
	protected float x, y;					// 座標
	protected int energy;					// エネルギー
	protected double direction;				// 進行方向 (ラジアン)
	protected double walkPace;				// 移動ペース (0.0~2.0)
	
	// 近接エンティティに関するリスト
	protected List<Entity> nearEntities;	// 近接エンティティリスト
	protected List<Entity> inSightFriends;	// 視界内友好エンティティリスト
	protected List<Entity> inSightEnemies;	// 視界内敵エンティティリスト
	protected Entity avoidEnemy;			// 今自分が離れようとしているエンティティ
	protected Entity target;				// 今自分が近づこうとしているエンティティ
	
	public Entity(World world, float x, float y, float size, int energy){
		this.world = world;
		this.x = x;
		this.y = y;
		this.size = size;
		this.sight = size;
		this.energy = energy;
		this.nearEntities = new ArrayList<Entity>();
	}
	
	// フレーム毎に状態を更新
	public abstract void update();
	
	// ディスプレイに描画
	public abstract void draw(Display display);
	
	// ツリーオブジェクトを再登録する
	protected abstract void reregist();
	
	// 近接エンティティリストへの追加
	public void addNearEntity(Entity entity){
		nearEntities.add(entity);
	}
	
	// 近接エンティティリストのクリア
	protected void clearNearEntity(){
		nearEntities.clear();
	}
	
	// セッタ
	public void setTreeBody(TreeBody obj){ this.treeBody = obj; }
	public void setTreeSight(TreeSight obj){ this.treeSight = obj; }
	public void setWalkPace(double walkPace) { this.walkPace = walkPace; }
	public void setDirection(double direction) { this.direction = direction; }
	public void setAvoidEnemy(Entity avoidEnemy) { this.avoidEnemy = avoidEnemy; }
	
	// ゲッタ
	public float getX(){ return x; }
	public float getY(){ return y; }
	public float getSize(){ return size; }
	public float getSight(){ return sight; }
	public double getDirection() { return direction; }
	public double getWalkPace() { return walkPace; }
	public List<Entity> getNearEntities() { return nearEntities; }
	public List<Entity> getInSightFriends() { return inSightFriends; }
	public List<Entity> getInSightEnemies() { return inSightEnemies; }
}
