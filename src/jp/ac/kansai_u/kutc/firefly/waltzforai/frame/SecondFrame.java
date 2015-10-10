package jp.ac.kansai_u.kutc.firefly.waltzforai.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import processing.core.PApplet;

//　プレイ画面を表示するフレーム
public class SecondFrame{
	public Display processing;
	MainFrame mf;
	private static JLayeredPane layerPane;
	static int category1 = 10;
	private static JFreeChart chart;
	private static ChartPanel chartP;
	private static ChartPanel oresenP;
	static JFrame frame;
	private static JPanel infoP;
	private static JPanel titleP;
	private static JPanel baseP;
	public static int plant, fleshE, plantE, omni; 
	static int sec=0;
	
	private static String[] series = {"plant", "plantEater", "fleshEater", "omvirous"};
	private static String[] time = {"0", "2", "4", "6", "8", "10"};


	SecondFrame(){
		frame = new JFrame();
	    frame.setSize(MainFrame.w, MainFrame.h);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		layerPane = new JLayeredPane(); // 配置は重ねることができるが，色は重ねられないみたい
		layerPane.setLayout(null);
		layerPane.setBackground(new Color(0,0,0,0));

		// 　ベースパネル
		baseP = new JPanel();
		baseP.setBackground(new Color(0,40,90));
		baseP.setBounds(0, 0, MainFrame.w / 3, MainFrame.h);

		// タイトルパネル
		titleP = new JPanel();
		titleP.setBackground(new Color(0,0,0,0));
		titleP.setBounds(0, 0, MainFrame.w / 3, MainFrame.h / 8);

		// タイトルラベル
		JLabel titleLabel = new JLabel("Waltz for AI");
		titleLabel.setFont(new Font("Century", Font.ITALIC, 64));
		titleLabel.setForeground(new Color(255, 255, 240));
		titleLabel.setBackground(new Color(1,1,1,0));
		titleP.add(titleLabel);

		// 情報パネル
		infoP = new JPanel();
		infoP.setBounds(0, MainFrame.h/8, MainFrame.w/3, MainFrame.h/2);
		infoP.setLayout(new BoxLayout(infoP, BoxLayout.Y_AXIS)); // 縦に配置するレイアウト
		infoP.setBackground(new Color(0, 0, 0, 0));
		// 情報ラベル（BGM）
		JLabel infoLabel = new JLabel("<html>　BGM<br><br><html>");
		infoLabel.setFont(new Font("Century", Font.PLAIN, 24));
		infoLabel.setForeground(new Color(255, 255, 240));
		infoLabel.setBackground(new Color(1,1,1,0));
		// 情報ラベル（個体数)
		JLabel numL = new JLabel("<html>　Number of individuals<br><html>");
		numL.setFont(new Font("Century", Font.PLAIN, 30));
		numL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（個体数　草)
		JLabel grassL = new JLabel("　・plant:"+plant);
		grassL.setFont(new Font("Century", Font.PLAIN, 24));
		grassL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（草食）
		JLabel grassEatL = new JLabel("　・plant-eater:"+plantE);
		grassEatL.setFont(new Font("Century", Font.PLAIN, 24));
		grassEatL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（肉食）
		JLabel fleshEatL = new JLabel("　・flesh-eater:" + fleshE);
		fleshEatL.setFont(new Font("Century", Font.PLAIN, 24));
		fleshEatL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（雑食食）
		JLabel omniL = new JLabel("　・omnivorous:" + omni);
		omniL.setFont(new Font("Century", Font.PLAIN, 24));
		omniL.setForeground(new Color(255, 255, 240));
		// パネルにラベルを登録していく
		infoP.add(infoLabel);
		infoP.add(numL);
		infoP.add(grassL);
		infoP.add(grassEatL);
		infoP.add(fleshEatL);
		infoP.add(omniL);

		// 円グラフ描画パネル
		DefaultPieDataset data = new DefaultPieDataset();
		data.setValue("Category 1", 10);
		data.setValue("Category 2", 20);
		data.setValue("Category 3", 30);
		chart = ChartFactory.createPieChart("Simple Pie Chart",
				data, true, true, false);
		 chartP = new ChartPanel(chart);
		chartP.setBackground(new Color(0, 0, 0, 10));
		Plot chart_plot = chart.getPlot();
		chart_plot.setBackgroundPaint(new Color(0, 0, 0, 10));
		chart_plot.setBackgroundAlpha(0.3f);
		chart.setBackgroundPaint(new Color(0, 0, 0, 10));
		chartP.setBounds(0, MainFrame.h / 3, MainFrame.w / 3,
				MainFrame.h / 3);

		// 折れ線グラフ
		DefaultCategoryDataset data2 = new DefaultCategoryDataset();
		data2.addValue(300, "Category 1", "2005年");
		data2.addValue(500, "Category 1", "2006年");
		data2.addValue(120, "Category 1", "2007年");
		JFreeChart oresen = ChartFactory.createLineChart("個体の増減", "年度", "秒",
				data2, PlotOrientation.VERTICAL, true, false, false);
		// 折れ線パネル
		oresenP = new ChartPanel(oresen);
		oresenP.setBackground(new Color(0, 0, 0, 10));
		Plot oresen_plot = oresen.getPlot();
		oresen_plot.setBackgroundPaint(new Color(0, 0, 0, 10));
		oresen_plot.setBackgroundAlpha(0.3f);
		oresen.setBackgroundPaint(new Color(0, 0, 0, 10));
		oresenP.setBounds(0, MainFrame.h - MainFrame.h / 3, MainFrame.w / 3,
				MainFrame.h / 3);

		// Processingパネルを埋め込む
		processing = new Display(mf);
		processing.init();

		// 前面から背面にレイアウトする．
		layerPane.add(infoP);
		layerPane.add(titleP);
		layerPane.add(chartP);
		layerPane.add(oresenP);
		layerPane.add(baseP);
	    layerPane.add(processing);
		//layerPane.add(underP);

		// フレームへ貼っつける
	    frame.getContentPane().add(layerPane);

	}

	// FisrFlameからボタンでフレームを切り替えられる
	public void VisibleFlg(boolean flg) {
		if (flg) {
		frame.setVisible(true);
		// グラフ再描画スレッド
	    RepT thread = new RepT();
	    Thread th = new Thread(thread);
	    th.start();
		}else{
			frame.setVisible(false);
		}
	}

	// 再描画メソッド
	public static void resetChart(){
    	layerPane.remove(chartP); //一度パネルから外す.
    	layerPane.remove(oresenP);
    	layerPane.remove(infoP);

		// 情報パネル
		infoP = new JPanel();
		infoP.setBounds(0, MainFrame.h / 8, MainFrame.w / 3, MainFrame.h / 2);
		infoP.setLayout(new BoxLayout(infoP, BoxLayout.Y_AXIS)); // 縦に配置するレイアウト
		infoP.setBackground(new Color(0, 40,90));
		// 情報ラベル（BGM）
		JLabel infoLabel = new JLabel("<html>　BGM<br><br><html>");
		infoLabel.setFont(new Font("Century", Font.PLAIN, 24));
		infoLabel.setForeground(new Color(255, 255, 240));
		infoLabel.setBackground(new Color(1, 1, 1, 0));
		// 情報ラベル（個体数)
		JLabel numL = new JLabel("<html>　Number of individuals<br><html>");
		numL.setFont(new Font("Century", Font.PLAIN, 30));
		numL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（個体数　草)
		JLabel grassL = new JLabel("　・plant:" + plant);
		grassL.setFont(new Font("Century", Font.PLAIN, 24));
		grassL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（草食）
		JLabel grassEatL = new JLabel("　・plant-eater:" + plantE);
		grassEatL.setFont(new Font("Century", Font.PLAIN, 24));
		grassEatL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（草食）
		JLabel fleshEatL = new JLabel("　・flesh-eater:" + fleshE);
		fleshEatL.setFont(new Font("Century", Font.PLAIN, 24));
		fleshEatL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（雑食食）
		JLabel omniL = new JLabel("　・omnivorous:" + omni);
		omniL.setFont(new Font("Century", Font.PLAIN, 24));
		omniL.setForeground(new Color(255, 255, 240));
		// パネルにラベルを登録していく
		infoP.add(infoLabel);
		infoP.add(numL);
		infoP.add(grassL);
		infoP.add(grassEatL);
		infoP.add(fleshEatL);
		infoP.add(omniL);

		// 円グラフ再設定
		DefaultPieDataset data = new DefaultPieDataset();
		data.setValue("plant", plant);
		data.setValue("plantEater", plantE);
		data.setValue("fleshEater", fleshE);
		data.setValue("omnivorous", omni);
		chart = ChartFactory.createPieChart("Simple Pie Chart", data, true,
				true, false);
		chartP = new ChartPanel(chart);
		chartP.setBackground(new Color(0, 40, 90));
		Plot chart_plot = chart.getPlot();
		chart_plot.setBackgroundPaint(new Color(0, 0, 0, 0));
		chart_plot.setBackgroundAlpha(0.3f);
		chart.setBackgroundPaint(new Color(0, 0, 0, 0));
		chartP.setBounds(0, MainFrame.h / 3, MainFrame.w / 3,
				MainFrame.h / 3);
		// 折れ線グラフ
		DefaultCategoryDataset data2 = new DefaultCategoryDataset();
		data2.addValue(plant, series[0], time[0]);
		data2.addValue(plantE, series[1], time[0]);
		data2.addValue(fleshE, series[2], time[0]);
		data2.addValue(omni, series[3], time[0]);
		
		data2.addValue(plant, series[0], time[1]);
		data2.addValue(plantE, series[1], time[1]);
		data2.addValue(fleshE, series[2], time[1]);
		data2.addValue(omni, series[3], time[1]);
		
		data2.addValue(plant, series[0], time[2]);
		data2.addValue(plantE, series[1], time[2]);
		data2.addValue(fleshE, series[2], time[2]);
		data2.addValue(omni, series[3], time[2]);
		
		data2.addValue(plant, series[0], time[3]);
		data2.addValue(plantE, series[1], time[3]);
		data2.addValue(fleshE, series[2], time[3]);
		data2.addValue(omni, series[3], time[3]);
		
		JFreeChart oresen = ChartFactory.createLineChart("個体の増減", "時間", "個体数",
				data2, PlotOrientation.VERTICAL, true, false, false);
		// 折れ線パネル
		oresenP = new ChartPanel(oresen);
		oresenP.setBackground(new Color(0, 40, 90));
		Plot oresen_plot = oresen.getPlot();
		oresen_plot.setBackgroundPaint(new Color(0, 0, 0, 10));
		oresen_plot.setBackgroundAlpha(0.3f);
		oresen.setBackgroundPaint(new Color(0, 0, 0, 10));
		oresenP.setBounds(0, MainFrame.h - MainFrame.h / 3, MainFrame.w / 3,
				MainFrame.h / 3);

		infoP.repaint();
		chartP.repaint(); // chartをリロードする
		oresenP.repaint(); // oresenをリロードする

		// 円グラフの描画が追いついていない可能性があるため，sleepを入れる?比率がおかしいからかな?
		/*
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		*/
		// パネルに戻す
		layerPane.add(infoP);
		layerPane.add(chartP);
		layerPane.add(oresenP);

		frame.setVisible(true); // 更新を変え終わったら呼ぶ必要があるみたい
	}

}
// 2秒毎に再描画メソッドを呼び出している
class RepT  implements Runnable {
	public void run() {
		while (true) {
			try {
				Thread.sleep(2000);
				if (SecondFrame.category1 > 90) {
					SecondFrame.category1 = 10;
				}
				SecondFrame.category1 += 10;
				SecondFrame.resetChart();
			} catch (InterruptedException e) {
			}
		}
	}
}
