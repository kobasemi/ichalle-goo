package jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap;

import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Animal;

public class TreeSight extends TreeObject{
	public TreeSight(Animal animal) {
		super(animal);
		((Animal)this.entity).setTreeSight(this);
	}
	
	@Override
	public float getObjectSize() {
		return ((Animal)entity).getSight();
	}
	
	@Override
	public Animal getEntity(){ return (Animal)entity; }
	@Override
	public TreeSight getNext(){ return (TreeSight)next; }
}