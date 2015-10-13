package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Util;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;

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
		// このswitch文でアクションメソッドを呼び出す
		// メソッドを追加したらここも書き加えてください
		switch(action){
		case runFromEnemy: runFromEnemy(animal); break;
		case runFromEnemyDirection: runFromEnemyDirection(animal); break;
		case chasePrey: chasePrey(animal); break;
		case chasePreyDirection: chasePreyDirection(animal); break;
		case approachFriend: approachFriend(animal); break;
		case approachFriendDirection: approachFriendDirection(animal); break;
		case changeDirection: changeDirection(animal); break;
		case setWalkPace: setWalkPace(animal); break;
		case setRandomWalkPace: setRandomWalkPace(animal); break;
		case doNothing: break;
		default: break;
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
			int elem = (int)(value*list.size());
			animal.setDirection(Util.getRadian(list.get(elem).getX(), list.get(elem).getY(), animal.getX(), animal.getY()));
		}
	}
	
	// 一定の角度で近くの敵から逃げる
	private void runFromEnemyDirection(Animal animal){
		List<Animal> list = animal.getInSightEnemies();
		if(list.size() > 0){
			animal.setDirection(Util.getRadian(list.get(0).getX(), list.get(0).getY(), animal.getX(), animal.getY())+(value*2*Math.PI-Math.PI));
		}
	}
	
	// 近くの獲物へ向かう
	private void chasePrey(Animal animal){
		List<Entity> list = animal.getInSightPreys();
		if(list.size() > 0){
			int elem = (int)(value*list.size());
			animal.setDirection(Util.getRadian(animal.getX(), animal.getY(), list.get(elem).getX(), list.get(elem).getY()));
		}
	}
	
	// 近くの獲物へ向かう
	private void chasePreyDirection(Animal animal){
		List<Entity> list = animal.getInSightPreys();
		if(list.size() > 0){
			animal.setDirection(Util.getRadian(list.get(0).getX(), list.get(0).getY(), animal.getX(), animal.getY())+(value*2*Math.PI-Math.PI));
		}
	}
	
	// 近くの友好エンティティへ向かう
	private void approachFriend(Animal animal){
		List<Animal> list = animal.getInSightFriends();
		if(list.size() > 0){
			int elem = (int)(value*list.size());
			animal.setDirection(Util.getRadian(animal.getX(), animal.getY(), list.get(elem).getX(), list.get(elem).getY()));
		}
	}
	
	// 近くの獲物へ向かう
	private void approachFriendDirection(Animal animal){
		List<Animal> list = animal.getInSightFriends();
		if(list.size() > 0){
			animal.setDirection(Util.getRadian(list.get(0).getX(), list.get(0).getY(), animal.getX(), animal.getY())+(value*2*Math.PI-Math.PI));
		}
	}

	// 移動速度の変更
	private void setWalkPace(Animal animal){
		// valueから最小値から最大値の間で算出する
		double min = gm.getWalkPaceMin(), max = gm.getWalkPaceMax();
		animal.setWalkPace(min + value*(max - min));
	}
	
	// 移動速度をランダムに変更する
	private void setRandomWalkPace(Animal animal){
		// valueから最小値から最大値の間で算出する
		double min = gm.getWalkPaceMin(), max = gm.getWalkPaceMax();
		animal.setWalkPace(min + Math.random()*(max - min));
	}
	
	// エンティティの向きを変える
	private void changeDirection(Animal animal){
		animal.setDirection(animal.getDirection()+((value*2*Math.PI-Math.PI)*gm.getWorld().getGameSpeed()));
	}
	
	/*// エンティティの向きをランダムに変える
	private void setRandomDirection(Animal animal){
		animal.setDirection(animal.getDirection()+(Math.random()*2*Math.PI-Math.PI));
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
	}*/
}

enum Action{
	// ここにメソッド名を追加する
	// メソッドを追加したらここも書き加えてください
	runFromEnemy, runFromEnemyDirection, chasePrey, chasePreyDirection, approachFriend, 
	approachFriendDirection, changeDirection, setWalkPace, setRandomWalkPace, doNothing;
	
	// 以下はランダム選択用
	private static final List<Action> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();
	public static Action random(){
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}