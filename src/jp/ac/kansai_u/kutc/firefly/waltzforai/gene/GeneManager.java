package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;

// 遺伝子に関する管理クラス
public class GeneManager {
	private World world;	// 所属ワールド
	
	// 各種遺伝子情報の最小値，最大値
	private double walkPaceMin = 0.0;	// 歩く速さの最小値
	private double walkPaceMax = 2.0;	// 歩く速さの最大値
	
	public GeneManager(){
		
	}
	
	// 動物の交配
	public void crossEntities(Animal e1, Animal e2){
		// TODO: 二つのAnimalクラスの遺伝子情報に基づいて子供のインスタンスを生成し，Worldに登録する
	}

	public double getWalkPaceMin() { return walkPaceMin; }
	public double getWalkPaceMax() { return walkPaceMax; }
}
