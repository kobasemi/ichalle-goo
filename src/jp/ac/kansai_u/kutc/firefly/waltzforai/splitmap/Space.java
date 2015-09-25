package jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap;

public class Space {
	private TreeObject entityHead;	// エンティティ実体の最新のオブジェクト
	private TreeObject sightHead;	// エンティティ視界の最新のオブジェクト
	
	public boolean push(TreeObject obj){
		if(obj == null){
			return false;	// nullチェック
		}
		if(obj.getSpace().equals(this)){
			return false;	// 二重登録防止
		}
		
		TreeObject head;
		if(obj.isSubstance()){
			head = entityHead;
		}else{
			head = sightHead;
		}
		
		// 最新のオブジェクトの更新
		if(head == null){
			head = obj;
		}else{
			obj.setNext(head);
			head.setPrev(obj);
			head = obj;
		}
		
		obj.setSpace(this);
		return true;
	}
	
	// 削除されるオブジェクトのチェック
	public boolean onRemove(TreeObject obj){
		TreeObject head;
		if(obj.isSubstance()){
			head = entityHead;
		}else{
			head = sightHead;
		}
		
		if(head.equals(obj)){
			if(head != null){
				head = head.getNext();
			}
		}
		return true;
	}
	
	// ゲッタ
	public TreeObject getEntityHead(){ return entityHead; }
	public TreeObject getSightHead(){ return sightHead; }
}
