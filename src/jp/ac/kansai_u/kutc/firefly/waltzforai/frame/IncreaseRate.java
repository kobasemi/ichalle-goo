package jp.ac.kansai_u.kutc.firefly.waltzforai.frame;

// 増加率の高い個体を調べる
public class IncreaseRate {
	private int preplantNum = 0;
	private int preplantENum = 0;
	private int prefleshENum = 0;
	private int preomniNum = 0;
	private int incleaseplant;
	private int incleaseplantE;
	private int incleasefleshE;
	private int incleaseomni;
	private String advantage = "plant-eater";
	private String prebgm = "plant-eater";
	
	private int plantNum = 0;
	private int plantENum = 0;
	private int fleshENum = 0;
	private int omniNum = 0;

	public void renew() {
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

		String[] speices = { "plant-eater", "flesh-eater", "omnivorous"};
		String maxSeries = speices[0];
		int[] series = { incleaseplantE, incleasefleshE,incleaseomni }; // 配列を作成
		int max = series[0];
		for (int i = 0; i < series.length; i++) {
			if (max < series[i]) { // 現在の最大値よりも大きい値が出たら
				max = series[i]; // 変数maxに値を入れ替える
				maxSeries = speices[i];
			}
		}
		advantage = maxSeries;
	}
	
	// ゲッタ
	public String getAdvantage(){ return advantage; }
	public String getPrebgm(){ return prebgm; }
	
	// セッタ
	public void setPlantNum(int num){ this.plantNum = num; }
	public void setPlantENum(int num){ this.plantENum = num; }
	public void setFleshENum(int num){ this.fleshENum = num; }
	public void setOmniNum(int num){ this.omniNum = num; }
}
