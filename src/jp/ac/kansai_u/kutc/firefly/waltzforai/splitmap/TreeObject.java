package jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;

public abstract class TreeObject {
	protected Entity entity;		// 対象のエンティティ
	protected Space space;			// 登録空間
	protected TreeObject prev;		// 前のオブジェクト
	protected TreeObject next;		// 次のオブジェクト
	
	public TreeObject(Entity entity){
		this.entity = entity;
	}
	
	// 空間から離脱する
	public boolean remove(){
		if(space == null){
			return false;	// 空間に登録されていない
		}
		if(!space.onRemove(this)){	// 空間に離脱を通知
			return false;			// 離脱失敗
		}
		
		// 前後のオブジェクトを結びつける
		if(prev != null){
			prev.setNext(next);
		}
		if(next != null){
			next.setPrev(prev);
		}
		space = null;
		prev = null;
		next = null;
		
		return true;
	}
	
	// オブジェクトの大きさを返す
	public abstract float getObjectSize();
	
	// セッタ
	public void setSpace(Space space){ this.space = space; }
	public void setPrev(TreeObject obj){ prev = obj; }
	public void setNext(TreeObject obj){ next = obj; }
	
	// ゲッタ
	public Space getSpace(){ return space; }
	public Entity getEntity(){ return entity; }
	public TreeObject getNext(){ return next; }
}