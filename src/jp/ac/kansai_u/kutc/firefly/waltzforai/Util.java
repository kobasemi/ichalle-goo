package jp.ac.kansai_u.kutc.firefly.waltzforai;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;

// ユーティリティークラス
// このクラスのメソッドはstaticで作成すること
public class Util {
	private Util(){	}	// インスタンス生成の禁止

	// 2点間のユークリッド距離を求める
	public static double getDistance(float x1, float y1, float x2, float y2){
		float xDist = Math.abs(x2 - x1);
		float yDist = Math.abs(y2 - y1);
		return Math.sqrt(xDist*xDist + yDist*yDist);
	}
	
	// 2つのエンティティが衝突しているか
	public static boolean isCollided(Entity e1, Entity e2){
		if(getDistance(e1.getX(), e1.getY(), e2.getX(), e2.getY()) < e1.getSize() + e2.getSize()){
			return true;
		}
		return false;
	}
	
	// 他エンティティが視界内にいるか (se:主体  oe:客体)
	// seの視野は視界半径と視野角から求められる扇型
	// それとoe本体との衝突判定（衝突していればtrue）
	public static boolean inSight(Animal se, Entity oe){
		// TODO: 未実装
		return false;
	}
}