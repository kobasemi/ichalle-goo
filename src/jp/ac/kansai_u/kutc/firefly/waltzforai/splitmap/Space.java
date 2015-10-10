package jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap;

public class Space {
	private TreeBody bodyHead;	// エンティティ実体の最新のオブジェクト
	private TreeSight sightHead;	// エンティティ視界の最新のオブジェクト
	
	public boolean push(TreeObject obj){
		if(obj == null){
			return false;	// nullチェック
		}
		if(this.equals(obj.getSpace())){
			return false;	// 二重登録防止
		}
		
		// 最新のオブジェクトの更新
		if(obj instanceof TreeBody){
			if(bodyHead == null){
				bodyHead = (TreeBody) obj;
			}else{
				obj.setNext(bodyHead);
				bodyHead.setPrev(obj);
				bodyHead = (TreeBody) obj;
			}
		}else{
			if(sightHead == null){
				sightHead = (TreeSight) obj;
			}else{
				obj.setNext(sightHead);
				sightHead.setPrev(obj);
				sightHead = (TreeSight) obj;
			}
		}
		
		obj.setSpace(this);
		return true;
	}
	
	// 削除されるオブジェクトのチェック
	public boolean onRemove(TreeObject obj){
		if(obj instanceof TreeBody){
			if(bodyHead != null && bodyHead.equals(obj)){
				bodyHead = bodyHead.getNext();
			}
		}else{
			if(sightHead != null && sightHead.equals(obj)){
				sightHead = sightHead.getNext();
			}
		}
		
		return true;
	}
	
	// ゲッタ
	public TreeBody getEntityHead(){ return bodyHead; }
	public TreeSight getSightHead(){ return sightHead; }
}
