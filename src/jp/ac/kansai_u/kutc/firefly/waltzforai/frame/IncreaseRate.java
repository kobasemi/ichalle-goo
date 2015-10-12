package jp.ac.kansai_u.kutc.firefly.waltzforai.frame;

// 増加率の高い個体を調べる
public class IncreaseRate {
	public static int preplantNum = 0;
	public static int preplantENum = 0;
	public static int prefleshENum = 0;
	public static int preomniNum = 0;
	public static int incleaseplant;
	public static int incleaseplantE;
	public static int incleasefleshE;
	public static int incleaseomni;
	public static String advantage = "plant";
	public static String prebgm = "plant";

	public static void renew() {
		int plantNum = SecondFrame.plant;
		int plantENum = SecondFrame.plantE;
		int fleshENum = SecondFrame.fleshE;
		int omniNum = SecondFrame.omni;

		// 0での除算を避ける
		if(preplantNum == 0)
			incleaseplant = plantNum;
		else
			incleaseplant = plantNum / preplantNum * 100;

		if(preplantENum == 0 )
			incleaseplantE = plantENum;
		else
			incleaseplantE = plantENum / preplantENum * 100;

		if (prefleshENum == 0)
			incleasefleshE = fleshENum;
		else
			incleasefleshE = fleshENum / prefleshENum * 100;

		if(preomniNum == 0)
			incleaseomni = omniNum;
		else
			incleaseomni = omniNum / preomniNum * 100;

		preplantNum = plantNum;
		preplantENum = plantENum;
		prefleshENum = fleshENum;
		preomniNum = omniNum;

		String[] speices = {"plant", "plant-eater", "flesh-eater", "omnivorous"};
		String maxSeries = speices[0];
		int[] series = { incleaseplant, incleaseplantE, incleasefleshE,
				incleaseomni }; // 配列を作成
		int max = series[0];
		for (int i = 0; i < series.length; i++) {
			if (max < series[i]) { // 現在の最大値よりも大きい値が出たら
				max = series[i]; // 変数maxに値を入れ替える
				maxSeries = speices[i];
			}
		}
		advantage = maxSeries;
	}
}
