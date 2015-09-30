package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import java.util.ArrayList;
import java.util.List;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.TreeObject;

public abstract class Entity {
	protected World world;					// 所属ワールド
	protected TreeObject obj;				// 所属ツリーオブジェクト
	protected float x, y;					// 現在の座標
	protected float size;					// エンティティの大きさ(半径)
	protected float r, g, b;				// 色
	protected int energy;					// エネルギー
	protected List<Entity> nearEntities;	// 近接エンティティリスト
	
	public Entity(World world, float x, float y, float size, int energy){
		this.world = world;
		this.x = x;
		this.y = y;
		this.size = size;
		this.energy = energy;
		this.nearEntities = new ArrayList<Entity>();
	}
	
	// フレーム毎に状態を更新
	public abstract void update();
	
	// ディスプレイに描画
	public abstract void draw(Display display);
	
	// ツリーオブジェクトを再登録する
	protected abstract void reregist();
	
	public void addNearEntity(Entity entity){
		nearEntities.add(entity);
	}
	
	protected void clearNearEntity(){
		nearEntities.clear();
	}
	
	// セッタ
	public void setTreeObject(TreeObject obj){ this.obj = obj; }
	
	// ゲッタ
	public float getX(){ return x; }
	public float getY(){ return y; }
	public float getSize(){ return size; }
}
