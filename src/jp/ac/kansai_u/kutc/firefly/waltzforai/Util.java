package jp.ac.kansai_u.kutc.firefly.waltzforai;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;

// ユーティリティークラス
// このクラスのメソッドはstaticで作成すること
public class Util {
	private Util(){	}	// インスタンス生成の禁止
	
	// 2点間のユークリッド距離を求める
	public static double getDistance(float x1, float y1, float x2, float y2){
		float dx = x2 - x1, dy = y2 - y1;
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	// 最適化のためにAnimalクラス内に移動
	// 2つのエンティティが衝突しているか
	/*public static boolean isCollided(Entity e1, Entity e2){
		float dx = e1.getX() - e2.getX(), dy = e1.getY() - e2.getY(), ar = e1.getSize() + e2.getSize();
		if(dx*dx + dy*dy < ar * ar){
			return true;
		}
		return false;
	}*/
	
	// 2点を結ぶベクトルの角度を求める
	public static double getRadian(float x1, float y1, float x2, float y2) {
		return Math.atan2(y2 - y1, x2 - x1);
	}
	
	// 2本のベクトルがなす角度を求める
	public static double getRadianSub(double deg1, double deg2){
		double digreeSub = deg2 - deg1;
		digreeSub -= (int)(digreeSub/(Math.PI*2))*Math.PI*2;
		return digreeSub > Math.PI ? digreeSub - Math.PI*2 : digreeSub;
	}

	// 最適化のためにAnimalクラス内に移動
	// 他エンティティが視界内にいるか (se:主体  oe:客体)
	// seの視野は視界半径と視野角から求められる扇型
	// それとoe本体との衝突判定（衝突していればtrue）
	/*public static boolean inSight(Animal se, Entity oe){
		// 主体と客体の距離が(主体の視界半径+客体の大きさの半径)より大きい場合
	    float dx = oe.getX() - se.getX();
	    float dy = oe.getY() - se.getY();
	    double distSpr = dx*dx + dy*dy;
		double ar = se.getSight() + oe.getSize();	// 主体の視界半径+客体の大きさの半径
		if(distSpr > ar*ar){
			return false;
		}
		
		// 客体の座標が主体の視野の扇形の2ベクトル間にある場合
		double v1Rad = se.getDirection() + se.getFov()/2;	// 扇形の1つめのベクトルの角度(ラジアン)
		double v1x = se.getSight() * Math.cos(v1Rad);
		double v1y = se.getSight() * Math.sin(v1Rad);
		double v2Rad = v1Rad - se.getFov();					// 扇形の2つめのベクトルの角度(ラジアン)
		double v2x = se.getSight() * Math.cos(v2Rad);
		double v2y = se.getSight() * Math.sin(v2Rad);
		double delta = v1x*v2y - v2x*v1y;
		double alpha = (dx*v2y - dy*v2x) / delta;
		double beta = (-dx*v1y + dy*v1x) / delta;
		if(alpha >= 0.0 && beta >= 0.0){	// 2ベクトルのなす角度が180度を超えると判定が逆転する
			if(se.getFov() < Math.PI){
				return true;
			}
		}else{
			if(Math.PI < se.getFov()){
				return true;
			}
		}

		//主体の視界である扇形の2つのベクトル線分のいずれかと客体が交点を持つ場合
		double a = v1x*v1x + v1y*v1y;
		double b = -(v1x*dx + v1y*dy);
		double c = dx*dx + dy*dy - oe.getSize()*oe.getSize();
		double d = b*b - a*c;	// 判別式D
		if(d >= 0.0){
			double t = (-b - Math.sqrt(d) / a);
			if(t >= 0.0 && t <= 1.0){
				return true;
			}
		}
		a = v2x*v2x + v2y*v2y;
		b = -(v2x*dx + v2y*dy);
		d = b*b - a*c;
		if(d >= 0.0){
			double t = (-b - Math.sqrt(d) / a);
			if(t >= 0.0 && t <= 1.0){
				return true;
			}
		}

		// 主体の座標が客体の内部にある場合
		if(distSpr < oe.getSize()*oe.getSize()){
			return true;
		}
		
		return false;
	}*/
}