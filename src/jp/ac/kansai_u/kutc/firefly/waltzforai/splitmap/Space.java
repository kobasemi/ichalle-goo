package jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap;

public class Space {
	private TreeObject entityHead;	// エンティティ実体の最新のオブジェクト
	private TreeObject sightHead;	// エンティティ視界の最新のオブジェクト
	
	public boolean push(TreeObject obj){
		if(obj == null){
			return false;	// nullチェック
		}
		if(this.equals(obj.getSpace())){
			return false;	// 二重登録防止
		}
		
		// 最新のオブジェクトの更新
		if(obj.isSubstance()){
			if(entityHead == null){
				entityHead = obj;
			}else{
				obj.setNext(entityHead);
				entityHead.setPrev(obj);
				entityHead = obj;
			}
		}else{
			if(sightHead == null){
				sightHead = obj;
			}else{
				obj.setNext(sightHead);
				sightHead.setPrev(obj);
				sightHead = obj;
			}
		}
		
		obj.setSpace(this);
		return true;
	}
	
	// 削除されるオブジェクトのチェック
	public boolean onRemove(TreeObject obj){
		if(obj.isSubstance()){
			if(entityHead != null && entityHead.equals(obj)){
				entityHead = entityHead.getNext();
			}
		}else{
			if(sightHead != null && sightHead.equals(obj)){
				sightHead = sightHead.getNext();
			}
		}
		
		return true;
	}
	
	// ゲッタ
	public TreeObject getEntityHead(){ return entityHead; }
	public TreeObject getSightHead(){ return sightHead; }
}
