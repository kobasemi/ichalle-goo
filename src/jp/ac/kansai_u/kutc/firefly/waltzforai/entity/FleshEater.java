package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import jp.ac.kansai_u.kutc.firefly.waltzforai.Util;
import jp.ac.kansai_u.kutc.firefly.waltzforai.World;

public class FleshEater extends Animal {
	public FleshEater(Builder builder) {
		super(builder);
	}

	public static class FleshEaterBuilder extends Builder{
		public FleshEaterBuilder(World world, float x, float y, double energy) {
			super(world, x, y, energy);
		}

		@Override
		public Animal build() {
			return new FleshEater(this);
		}
	}
	
	// 描画メソッド
	@Override
	public void draw(Display display) {
		display.noStroke();
		display.fill(r, g, b, 200);
		display.ellipse(x, y, size*2, size*2);
	}
	
	@Override
	public void update() {
		collisionCheck();			// 衝突判定
		geneHead.perform(this);		// 行動ツリーの実行
		move();						// 移動
	}
	
	@Override
	protected void collisionCheck(){
		for(int i = 0; i < nearEntities.size(); i++){
			if(Util.isCollided(this, nearEntities.get(i)) && nearEntities.get(i) != this){
				
			}
		}
		
		clearNearEntity(); // 近接エンティティリストをクリア
	}
}
