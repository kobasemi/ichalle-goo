package jp.ac.kansai_u.kutc.firefly.waltzforai;

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
	
	// エンティティ同士の衝突判定
	public static boolean isCollided(Entity e1, Entity e2){
		// TODO: 未実装
		return false;
	}
	
	// 他エンティティが視界内にいるか (se:主体  oe:客体)
	public static boolean inSight(Entity se, Entity oe){
		// TODO: 未実装
		return false;
	}
}