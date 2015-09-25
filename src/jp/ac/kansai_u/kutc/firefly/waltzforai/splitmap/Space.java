package jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap;

public class Space {
	private TreeObject latestObj;	// 最新のオブジェクト
	
	public boolean push(TreeObject obj){
		if(obj == null){
			return false;	// nullチェック
		}
		if(obj.getSpace().equals(this)){
			return false;	// 二重登録防止
		}
		
		// 最新のオブジェクトの更新
		if(latestObj == null){
			latestObj = obj;
		}else{
			obj.setNextObject(latestObj);
			latestObj.setPrevObject(obj);
			latestObj = obj;
		}
		
		obj.setSpace(this);
		return true;
	}
	
	// 削除されるオブジェクトのチェック
	public boolean onRemove(TreeObject obj){
		if(latestObj.equals(obj)){
			if(latestObj != null){
				latestObj = latestObj.getNectObject();
			}
		}
		return true;
	}
}
