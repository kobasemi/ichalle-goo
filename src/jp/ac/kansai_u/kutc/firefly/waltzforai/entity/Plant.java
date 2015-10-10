package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.gene.GeneManager;

public class Plant extends Entity {
	public Plant(World world, float x, float y, double energy) {
		super(world, x, y, energy);
		
		// energyに比例してsizeを大きくする
		GeneManager gm = this.world.getGeneManager();
		double energyMin = gm.getPlantEnergyMin(), energyMax = gm.getPlantEnergyMax(), sizeMin = gm.getSizeMin(), sizeMax = gm.getSizeMax(); 
		this.size = (float)(sizeMin + ((this.energy-energyMin) / (energyMax-energyMin)) * (sizeMax-sizeMin));
	}

	@Override
	public void update() {
		// TODO 植物のアップデートすることあるかな？
		// TODO アニメーションさせるならフレーム数の記録が必要かも
	}

	@Override
	public void move() {
		// TODO 移動もしないかな
	}
	
	@Override
	public void draw(Display display) {
		display.noStroke();
		display.fill(0, 200, 30, 200);
		display.ellipse(x, y, size*2, size*2);
		// 謙隆add
		display.plantNum++;
	}
	
	// 食べられた。ツリーとワールドから削除
	@Override
	public void die(){
		treeBody.remove();
		world.removeEntity(this);
	}
}
