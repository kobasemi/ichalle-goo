package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import jp.ac.kansai_u.kutc.firefly.waltzforai.Util;
import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap.SplitMap;

public abstract class Animal extends Entity {	
	public Animal(World world, float x, float y, float size, int energy) {
		super(world, x, y, size, energy);
		
		this.sight = size * 2;		// TODO 要変更
		this.direction = Math.random() * Math.PI * 2;	// TODO 要変更
		this.speed = 1;			// TODO 要変更
	}

	@Override
	public void update() {
		collisionCheck();	// 衝突判定
		move();				// 移動
	}
	
	// 衝突判定
	private void collisionCheck(){
		boolean hit = false;
		for(int i = 0; i < nearEntities.size(); i++){
			if(Util.isCollided(this, nearEntities.get(i)) && nearEntities.get(i) != this){
				hit = true;
			}
		}
		if(hit){
			r = 255;
			g = 60;
		}else{
			r = g = b = 0;
		}
		
		clearNearEntity(); // 近接エンティティリストをクリア
	}
	
	// 1フレーム分の移動
	private void move(){
		double vecX = Math.cos(direction)*speed, vecY = Math.sin(direction)*speed;
		float newX = (float)(x + vecX), newY = (float)(y + vecY);
		
		// 壁との衝突判定
		if(newX - size < 0){ // 左壁
			direction = Math.atan2(vecY, -vecX);
			newX = size*2 - newX;
		}else if(world.getWidth() < newX + size){ // 右壁
			direction = Math.atan2(vecY, -vecX);
			newX = world.getWidth()*2 - size*2 - newX;
		}
		
		if(newY - size < 0){ // 上壁
			direction = Math.atan2(-vecY, vecX);
			newY = size*2 - newY;
		}else if(world.getHeight() < newY + size){ // 下壁
			direction = Math.atan2(-vecY, vecX);
			newY = world.getHeight()*2 - size*2 - newY;
		}
		
		x = newX;
		y = newY;
		reregist();	// 空間ツリーへの再登録
	}

	@Override
	public void draw(Display display) {
		display.noStroke();
		display.fill(r, g, b, 200);
		display.ellipse(x, y, size*2, size*2);
	}
	
	@Override
	protected void reregist() {
		treeBody.remove();
		treeSight.remove();
		SplitMap sm = world.getSplitMap();
		sm.regist(treeBody);
		sm.regist(treeSight);
	}
}
