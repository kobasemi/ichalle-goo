package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import jp.ac.kansai_u.kutc.firefly.waltzforai.Util;
import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.SplitMap;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.TreeObject;

public class Animal extends Entity {
	protected TreeObject sightObj;	// 視野の所属ツリーオブジェクト
	float sight;					// 視野の半径
	double direction;				// 進行方向 (ラジアン)
	double fov;						// 視野角
	
	public Animal(World world, float x, float y, float size, int energy) {
		super(world, x, y, size, energy);
		
		this.sight = size * 2;	// TODO 要変更
		this.direction = Math.random() * Math.PI * 2;	// TODO 要変更
	}

	@Override
	public void update() {
		r = g = b = 0;
		for(int i = 0; i < nearEntities.size(); i++){
			if(Util.isCollided(this, nearEntities.get(i)) && nearEntities.get(i) != this){
				r = 100;
				g = 50;
			}
		}
		clearNearEntity();
		
		x += Math.cos(direction);
		y += Math.sin(direction);
		
		reregist();
	}

	@Override
	public void draw(Display display) {
		display.noStroke();
		display.fill(r, g, b);
		display.ellipse(x, y, size*2, size*2);
	}
	
	@Override
	protected void reregist() {
		obj.remove();
		sightObj.remove();
		SplitMap sm = world.getSplitMap();
		sm.regist(obj);
		sm.regist(sightObj);
	}
	
	// セッタ
	public void setSightTreeObject(TreeObject obj){ this.sightObj = obj; }

	// ゲッタ
	public float getSight(){ return sight; }
}
