package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;

public class IfNode extends GeneNode {
	private Condition condition;	// どの条件で分岐させるか
	private double value;			// 0.0~1.0の値。この値を元に分岐条件を変化させる
	private GeneNode tNext, fNext;	// 分岐条件を満たした時と満たさなかった時のアクション
	
	public IfNode(GeneManager gm) {
		super(gm);
		this.condition = Condition.random();
		this.value = Math.random();
	}
	
	@Override
	public void perform(Animal animal) {
		// 条件分岐の結果
		boolean result = true;
		
		// このif文で条件分岐メソッドを呼び出す
		if(condition.equals(Condition.enemyInRange)){
			result = enemyInRange(animal);
		}else if(condition.equals(Condition.friendInRange)){
			result = friendInRange(animal);
		}
		
		// 結果に基づいて次のアクションを呼び出す
		if(result){
			if(tNext != null){
				tNext.perform(animal);
			}
		}else{
			if(fNext != null){
				fNext.perform(animal);
			}
		}
	}
	
	// セッタ
	@Override
	public void setNext(GeneNode gn){ this.tNext = gn; }
	public void setFalseNext(GeneNode gn){ this.fNext = gn; }
	
	
	/* 以下が発生しうる条件分岐です。思いつきでどんどん追加しちゃってOKです。戻り値はboolean型で */
	/* 条件分岐メソッドを追加したら、enumとperformメソッドの方にも情報を追加してやってください */

	// 視界内に敵がいるか
	private boolean enemyInRange(Animal animal){
		if(animal.getInSightEnemies().size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	// 視界内に味方がいるか
	private boolean friendInRange(Animal animal){
		if(animal.getInSightFriends().size() > 0){
			return true;
		}else{
			return false;
		}
	}
}

enum Condition{
	// ここに条件分岐メソッド名を追加してください
	enemyInRange, friendInRange;
	
	// 以下はランダム選択用
	private static final List<Condition> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();
	public static Condition random(){
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}
