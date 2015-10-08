package jp.ac.kansai_u.kutc.firefly.waltzforai.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Edibility{
	// 草食、肉食、雑食
	Plant, Flesh, Mixed;
	
	// 以下はランダム選択用
	private static final List<Edibility> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();
	public static Edibility random(){
		return VALUES.get(RANDOM.nextInt(SIZE));
	}
}