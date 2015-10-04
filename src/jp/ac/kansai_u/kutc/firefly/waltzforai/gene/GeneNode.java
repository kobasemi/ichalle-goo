package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;

public abstract class GeneNode {
	protected GeneManager gm;	// 遺伝子の管理クラス
	
	public GeneNode(GeneManager gm){
		this.gm = gm;
	}
	
	public abstract void perform(Animal animal);
	public abstract void setNext(GeneNode gn);
}
