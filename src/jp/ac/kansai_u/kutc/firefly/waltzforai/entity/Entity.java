package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import java.util.List;

public class Entity {
	protected float x, y; 				// 現在の座標
	protected float size;					// エンティティの大きさ(半径)
	protected List<Entity> nearEntities;	// 近接エンティティリスト
	
	public void addNearEntity(Entity entity){
		nearEntities.add(entity);
	}
	
	protected void clearNearEntity(){
		nearEntities.clear();
	}
	
	// ゲッタ
	public float getX(){ return x; }
	public float getY(){ return y; }
	public float getSize(){ return size; }
}
