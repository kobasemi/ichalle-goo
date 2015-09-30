package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import jp.ac.kansai_u.kutc.firefly.waltzforai.World;

public class Plant extends Entity {
	public Plant(World world, float x, float y, float size, int energy) {
		super(world, x, y, size, energy);
	}

	@Override
	public void update() {
		// TODO 植物のアップデートすることあるかな？
	}

	@Override
	public void draw(Display display) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	protected void reregist() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
