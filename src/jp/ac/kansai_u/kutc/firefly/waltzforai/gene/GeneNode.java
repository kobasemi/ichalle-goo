package jp.ac.kansai_u.kutc.firefly.waltzforai.gene;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;

public abstract class GeneNode {
	protected GeneManager gm;	// 遺伝子の管理クラス
	
	public abstract void perform(Entity entity);
}
