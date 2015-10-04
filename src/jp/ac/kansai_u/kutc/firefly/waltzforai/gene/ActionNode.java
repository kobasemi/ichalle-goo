package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;

public class ActionNode extends GeneNode {
	private Action action;			// どの行動を実行するか
	private double value;			// 0.0~1.0の値。この値を元に行動の内容を変化させる
	private GeneNode next; 			// 次のアクション
	
	public ActionNode(GeneManager gm) {
		super(gm);
		this.action = Action.random();
		this.value = Math.random();
	}
	
	@Override
	public void perform(Animal animal) {
		// このif文でアクションメソッドを呼び出す
		if(action.equals(Action.runFromEnemy)){
			runFromEnemy(animal);
		}else if(action.equals(Action.setWalkPace)){
			setWalkPace(animal);
		}
		
		// 次のアクションを呼び出す
		if(next != null){
			next.perform(animal);
		}
	}
	
	// セッタ
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
	
	// 移動速度の変更
	private void setWalkPace(Animal animal){
		// valueから最小値から最大値の間で算出する
		double min = gm.getWalkPaceMin(), max = gm.getWalkPaceMax();
		animal.setWalkPace(min + value*(max - min));
	}
}

enum Action{
	// ここにメソッド名を追加する
	runFromEnemy, setWalkPace;
	
	// 以下はランダム選択用
	private static final List<Action> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();
	public static Action random(){
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}