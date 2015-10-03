package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import java.util.List;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;

public class ActionNode extends GeneNode {
	private Action action;			// どの行動を実行するか
	private double value;			// 0.0~1.0の値。この値を元に行動の内容を変化させる
	private GeneNode next; 			// 次のアクション
	
	@Override
	public void perform(Entity entity) {
		// このif文でアクションメソッドを呼び出す
		if(action.equals(Action.runFromEnemy)){
			runFromEnemy(entity);
		}else if(action.equals(Action.setWalkPace)){
			setWalkPace(entity);
		}
		
		// 次のアクションを呼び出す
		if(next != null){
			next.perform(entity);
		}
	}
	
	
	/* 以下が実行されうるアクションメソッドです。思いつきでどんどん追加しちゃってOKです */
	/* アクションメソッドを追加したら、enumとperformメソッドの方にも情報を追加してやってください */
	
	// 近くの敵から逃げる
	private void runFromEnemy(Entity entity){
		List<Entity> list = entity.getInSightEnemies();
		if(list.size() > 0){
			// 複数いる場合はvalueで決定する
			entity.setAvoidEnemy((list.get((int)(list.size()*value))));
		}
	}
	
	// 移動速度の変更
	private void setWalkPace(Entity entity){
		// valueから最小値から最大値の間で算出する
		double min = gm.getWalkPaceMin(), max = gm.getWalkPaceMax();
		entity.setWalkPace(min + value*(max - min));
	}
}

enum Action{
	runFromEnemy, setWalkPace
}