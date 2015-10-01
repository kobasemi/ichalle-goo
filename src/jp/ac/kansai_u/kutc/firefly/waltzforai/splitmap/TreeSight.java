package jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;

public class TreeSight extends TreeObject{
	public TreeSight(Entity entity) {
		super(entity);
		entity.setTreeSight(this);
	}
	
	@Override
	public float getObjectSize() {
		return entity.getSight();
	}
}