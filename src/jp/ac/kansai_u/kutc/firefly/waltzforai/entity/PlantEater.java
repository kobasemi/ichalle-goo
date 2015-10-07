package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import jp.ac.kansai_u.kutc.firefly.waltzforai.World;

public class PlantEater extends Animal {
	public PlantEater(Builder builder) {
		super(builder);
	}
	
	public static class PlantEaterBuilder extends Builder{
		public PlantEaterBuilder(World world, float x, float y, double energy) {
			super(world, x, y, energy);
		}

		@Override
		public Animal build() {
			return new PlantEater(this);
		}
	}
	
	// 描画メソッド
	@Override
	public void draw(Display display) {
		display.noStroke();
		display.fill(230, 230, 0, 20);
		display.arc(x, y, sight*2, sight*2, (float)(direction-fov/2), (float)(direction+fov/2));
		display.fill(r, g, b, 200);
		display.ellipse(x, y, size*2, size*2);
		display.text(String.format("%.0f", energy), x+20, y-20);
		display.text(String.format("%d", lifeSpan-age), x+20, y+20);
	}
}
