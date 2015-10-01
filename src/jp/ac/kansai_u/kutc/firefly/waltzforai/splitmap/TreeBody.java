package jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;

public class TreeBody extends TreeObject{
	public TreeBody(Entity entity) {
		super(entity);
		entity.setTreeBody(this);
	}
	
	@Override
	public float getObjectSize() {
		return entity.getSize();
	}
}