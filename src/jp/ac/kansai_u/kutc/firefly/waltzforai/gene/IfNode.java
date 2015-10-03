package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;

public class IfNode extends GeneNode {
	private Condition condition;	// どの条件で分岐させるか
	private double value;			// 0.0~1.0の値。この値を元に分岐条件を変化させる
	private GeneNode tNext, fNext;	// 分岐条件を満たした時と満たさなかった時のアクション
	
	@Override
	public void perform(Entity entity) {
		// 条件分岐の結果
		boolean result = true;
		
		// このif文で条件分岐メソッドを呼び出す
		if(condition.equals(Condition.enemyInRange)){
			result = enemyInRange(entity);
		}else if(condition.equals(Condition.friendInRange)){
			result = friendInRange(entity);
		}
		
		// 結果に基づいて次のアクションを呼び出す
		if(result){
			if(tNext != null){
				tNext.perform(entity);
			}
		}else{
			if(fNext != null){
				fNext.perform(entity);
			}
		}
	}
	
	/* 以下が発生しうる条件分岐です。思いつきでどんどん追加しちゃってOKです。戻り値はboolean型で */
	/* 条件分岐メソッドを追加したら、enumとperformメソッドの方にも情報を追加してやってください */

	// 視界内に敵がいるか
	private boolean enemyInRange(Entity entity){
		if(entity.getInSightEnemies().size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	// 視界内に味方がいるか
	private boolean friendInRange(Entity entity){
		if(entity.getInSightFriends().size() > 0){
			return true;
		}else{
			return false;
		}
	}
}

enum Condition{
	enemyInRange, friendInRange
}
