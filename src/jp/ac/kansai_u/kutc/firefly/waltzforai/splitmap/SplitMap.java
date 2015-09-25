package jp.ac.kansai_u.kutc.firefly.waltzforai.splitmap;

import java.util.ArrayList;
import java.util.List;

import jp.ac.kansai_u.kutc.firefly.waltzforai.World;
import jp.ac.kansai_u.kutc.firefly.waltzforai.entity.Entity;

// 4分木分割空間の管理クラス
public class SplitMap {
	private final World world;
	private final int splitLevel;			// 空間分割レベル (0なら無分割，1で4分割，2で16分割)
	private final int[] spaceNum;
	private final Space[] spaceTree;		// 空間ツリー
	private final int width, height;		// ワールドの大きさ
	private final float sWidth, sHeight;	// 最下レベル空間の大きさ
	
	public SplitMap(World world, int splitLevel){
		this.world = world;
		width = world.getWidth();
		height = world.getHeight();
		this.splitLevel = splitLevel;
		sWidth = (float)width / (1<<splitLevel);
		sHeight = (float)height / (1<<splitLevel);
		
		// 空間数の算出
		int[] spaceNumTmp = new int[splitLevel+2];
		spaceNumTmp[0] = 1;
		for(int i = 1; i < splitLevel+2; i++){
			spaceNumTmp[i] = spaceNumTmp[i-1] * 4;
		}
		spaceNum = new int[splitLevel+1];
		for(int i = 0; i < splitLevel+1; i++){
			spaceNum[i] = (spaceNumTmp[i+1]-1)/3;
		}
		
		// 空間ツリー配列の作成
		spaceTree = new Space[spaceNum[splitLevel]];
	}
	
	// ツリーオブジェクトの登録
	public boolean regist(TreeObject obj){
		Entity e = obj.getEntity();
		int elem = getTreeNumber(e.getX(), e.getY(), e.getX()+e.getSize(), e.getY()+e.getSize());
		if(elem > -1){
			if(spaceTree[elem] == null){
				createNewSpace(elem);
			}
			spaceTree[elem].push(obj);
		}
		
		return false;
	}
	
	// 空間とその親空間を生成
	private boolean createNewSpace(int elem){
		while(spaceTree[elem] == null){
			spaceTree[elem] = new Space();
			// 親空間にジャンプ
			elem = (elem-1)>>2;
		}
		return true;
	}
	
	// 座標から所属する空間の要素番号を割り出す
	private int getTreeNumber(float left, float top, float right, float bottom){
		// 左上と右下の空間番号を算出
		long lt = getMortonNumber(left, top);
		System.out.println(lt);
		long rb = getMortonNumber(right, bottom);
		System.out.println(rb);
		
		// 所属レベルを算出
		long def = lt ^ rb;
		int hiLevel = 0;
		for(int i = 0; i < splitLevel; i++){
			long check = (def >> (i*2)) & 0x3;
			if(check != 0){
				hiLevel = i+1;
			}
		}
		
		// ツリーの要素番号を算出
		int treeNum = (int)(rb >> (hiLevel*2));
		int addNum = spaceNum[splitLevel-(hiLevel+1)];
		treeNum += addNum;
		
		if(treeNum > spaceNum[splitLevel]){
			return -1;
		}
		
		return treeNum;
	}
	
	// 座標から空間番号を割り出す
	private long getMortonNumber(float x, float y){
		return (bitSeparate((int)(x/sWidth)) | (bitSeparate((int)(y/sHeight))<<1));
	}
	
	private long bitSeparate(long n){
		n = (n|(n<<8)) & 0x00ff00ff; 	// 00000000111111110000000011111111
		n = (n|(n<<4)) & 0x0f0f0f0f;	// 00001111000011110000111100001111
		n = (n|(n<<2)) & 0x33333333; 	// 00110011001100110011001100110011
		return (n|(n<<1)) & 0x55555555;	// 01010101010101010101010101010101
	}
}