package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;

// 遺伝子コード、行動。終端子
public class ActionNode extends GeneNode {
	private Action action;			// どの行動を実行するか
	private double value;			// 0.0~1.0の値。この値を元に行動の内容を変化させる
	private GeneNode next; 			// 次のアクション
	
	public ActionNode(GeneManager gm) {
		super(gm);
		mutation();
	}
	
	// 突然変異
	@Override
	public void mutation(){
		this.action = Action.random();
		this.value = Math.random();
	}
	
	@Override
	public void perform(Animal animal) {
		// このif文でアクションメソッドを呼び出す
		// メソッドを追加したらここも書き加えてください
		if(action.equals(Action.runFromEnemy)){
			runFromEnemy(animal);
		}else if(action.equals(Action.approachFriend)){
			approachFriend(animal);
		}else if(action.equals(Action.setWalkPace)){
			setWalkPace(animal);
		}else if(action.equals(Action.setRandomDirection)){
			setRandomDirection(animal);
		}else if(action.equals(Action.turnRight)){
			turnRight(animal);
		}else if(action.equals(Action.turnLeft)){
			turnLeft(animal);
		}else if(action.equals(Action.turnBack)){
			turnBack(animal);
		}else if(action.equals(Action.doNothing)){
			/* do nothing */
		}
		
		// 次のアクションを呼び出す
		if(next != null){
			next.perform(animal);
		}
	}
	
	// ゲッタ
	@Override
	public GeneNode getNext(){ return next; }
	
	// セッタ
	@Override
	public void setNext(GeneNode gn){ this.next = gn; }
	
	
	/* 以下が実行されうるアクションメソッドです。思いつきでどんどん追加しちゃってOKです */
	/* アクションメソッドを追加したら、enumとperformメソッドの方にも情報を追加してやってください */
	
	// 近くの敵から逃げる
	private void runFromEnemy(Animal animal){
		List<Animal> list = animal.getInSightEnemies();
		if(list.size() > 0){
			// 複数いる場合はvalueで決定する
			animal.setAvoidEnemy((list.get((int)(list.size()*value))));
		}
	}
	
	//視界内友好エンティティに近づく
	private void approachFriend(Animal animal){
		List<Animal> list = animal.getInSightFriends();
		if(!list.isEmpty()){
			// 複数いる場合はvalueで決定する
			animal.setTarget((list.get((int)(list.size()*value))));
		}
	}

	// 移動速度の変更
	private void setWalkPace(Animal animal){
		// valueから最小値から最大値の間で算出する
		double min = gm.getWalkPaceMin(), max = gm.getWalkPaceMax();
		animal.setWalkPace(min + value*(max - min));
	}
	
	//進行方向をランダム変更
	private void setRandomDirection(Animal animal){
		animal.setDirection(value * Math.PI * 2);
	}
	
	//進行方向を右に変更
	private void turnRight(Animal animal){
		animal.setDirection(animal.getDirection() - 1/2*Math.PI);
	}

	//進行方向を左に変更
	private void turnLeft(Animal animal){
		animal.setDirection(animal.getDirection() + 1/2*Math.PI);
	}

	//進行方向を180度変更
	private void turnBack(Animal animal){
		animal.setDirection(animal.getDirection() + Math.PI);
	}
	
}

enum Action{
	// ここにメソッド名を追加する
	// メソッドを追加したらここも書き加えてください
	runFromEnemy, approachFriend, setWalkPace, setRandomDirection, turnRight, turnLeft, turnBack, doNothing;
	
	// 以下はランダム選択用
	private static final List<Action> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();
	public static Action random(){
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}