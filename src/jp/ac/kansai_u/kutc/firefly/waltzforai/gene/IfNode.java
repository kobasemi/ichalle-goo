package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;

// 遺伝子コード、分岐。非終端子
public class IfNode extends GeneNode {
	private Condition condition;	// どの条件で分岐させるか
	private double value;			// 0.0~1.0の値。この値を元に分岐条件を変化させる
	private GeneNode tNext, fNext;	// 分岐条件を満たした時と満たさなかった時のアクション
	
	public IfNode(GeneManager gm) {
		super(gm);
		this.condition = Condition.random();
		this.value = Math.random();
	}
	
	// 突然変異
	@Override
	public void mutation() {
		if(Math.random() < 0.5){	// 逆位50% ランダム変異50%
			this.condition = Condition.random();
			this.value = Math.random();
		}else{
			GeneNode tmp = tNext;
			tNext = fNext;
			fNext = tmp;
		}
	}
	
	@Override
	public void perform(Animal animal) {
		// 条件分岐の結果
		boolean result = true;
		
		// このswitch文で条件分岐メソッドを呼び出す
		// メソッドを追加したらここも書き加えてください
		switch(condition){
		case preyInSight: result = preyInSight(animal); break;
		case enemyInSight: result = enemyInSight(animal); break;
		case friendInSight: result = friendInSight(animal); break;
		case randomBranch: result = randomBranch(animal); break;
		case canMakeChild: result = canMakeChild(animal); break;
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
	
	// ゲッタ
	@Override
	public GeneNode getNext(){ return tNext; }
	public GeneNode getFalseNext(){ return fNext; }
	
	// セッタ
	@Override
	public void setNext(GeneNode gn){ this.tNext = gn; }
	public void setFalseNext(GeneNode gn){ this.fNext = gn; }
	
	
	/* 以下が発生しうる条件分岐です。思いつきでどんどん追加しちゃってOKです。戻り値はboolean型で */
	/* 条件分岐メソッドを追加したら、enumとperformメソッドの方にも情報を追加してやってください */

	// 視界内に獲物がいるか
	private boolean preyInSight(Animal animal){
		if(animal.getInSightPreys().size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	// 視界内に敵がいるか
	private boolean enemyInSight(Animal animal){
		if(animal.getInSightEnemies().size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	// 視界内に味方がいるか
	private boolean friendInSight(Animal animal){
		if(animal.getInSightFriends().size() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	// 確率で分岐する
	private boolean randomBranch(Animal animal){
		if(Math.random() < value){
			return true;
		}else{
			return false;
		}
	}
	
	//子どもが作れるか
	private boolean canMakeChild(Animal animal){
		return animal.canMakeChild();
	}
}

enum Condition{
	// ここに条件分岐メソッド名を追加する
	// メソッドを追加したらここも書き加えてください
	preyInSight, enemyInSight, friendInSight, randomBranch, canMakeChild;
	
	// 以下はランダム選択用
	private static final List<Condition> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();
	public static Condition random(){
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}
