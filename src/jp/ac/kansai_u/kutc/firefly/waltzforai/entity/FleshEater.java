package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
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
}
