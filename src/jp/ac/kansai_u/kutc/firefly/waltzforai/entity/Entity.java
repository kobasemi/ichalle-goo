package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.TreeBody;

// 動物・植物などのフィールド上の全てのオブジェクトの元となるクラス
public abstract class Entity {
	// ワールド情報
	protected World world;					// 所属ワールド
	protected TreeBody treeBody;			// 所属ツリーオブジェクト
	
	// ステータス
	protected float x, y;					// 座標
	protected double energy;				// エネルギー
	protected float size;					// 大きさ (半径)
	
	public Entity(World world, float x, float y, double energy){
		this.world = world;
		this.x = x;
		this.y = y;
		this.energy = energy;
	}
	
	// フレーム毎に状態を更新
	public abstract void update();
	
	// 移動の実行
	public abstract void move();
	
	// ディスプレイに描画
	public abstract void draw(Display display);
	
	// 死亡
	public abstract void die();
	
	// エネルギーの消費
	public void reduceEnergy(double energy){ 
		this.energy -= energy;
		if(this.energy <= 0){
			die();
		}
	}
	
	// セッタ
	public void setTreeBody(TreeBody obj){ this.treeBody = obj; }
	
	// ゲッタ
	public float getX(){ return x; }
	public float getY(){ return y; }
	public float getSize(){ return size; }
	public double getEnergy(){ return energy; }
}
