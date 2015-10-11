package jp.ac.kansai_u.kutc.firefly.waltzforai.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

//　プレイ画面を表示するフレーム // 一番下にスレッド
public class SecondFrame {
	public Display processing;
	MainFrame mf;
	private static JLayeredPane layerPane;
	static int category1 = 10;
	private static ChartPanel chartP;
	private static ChartPanel oresenP;
	static JFrame frame;
	private static JPanel infoP;
	private static JPanel titleP;
	private static JPanel baseP;
	public static int plant, fleshE, plantE, omni;
	private static int sec = 2, secNum = 20;
	public static JFreeChart oresen;
	public static ChartPanel bouP;

	private static String[] seriesName = { "plant", "plantEater", "fleshEater",
			"omvirous" };
	private static LinkedList<String> time = new LinkedList<String>();
	private static LinkedList<Integer> plantList = new LinkedList<Integer>();
	private static LinkedList<Integer> plantEList = new LinkedList<Integer>();
	private static LinkedList<Integer> fleshEList = new LinkedList<Integer>();
	private static LinkedList<Integer> omniList = new LinkedList<Integer>();

	SecondFrame() {
		// 折れ線グラフで使うlistの初期化
		for (int i = 0; i < 10; i++) {
			time.add(String.valueOf(sec * (i + 1)));
			plantList.add(0);
			plantEList.add(0);
			fleshEList.add(0);
			omniList.add(0);
		}
		frame = new JFrame();
		frame.setSize(MainFrame.w, MainFrame.h);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// メニューバー
		JMenuBar menubar = new JMenuBar();
		JMenu menu1 = new JMenu("Menu");
		menu1.setOpaque(false);
		menubar.setBackground(Color.BLACK);
		menubar.add(menu1);
		JMenuItem menuitem1 = new JMenuItem("Close");
		menuitem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu1.add(menuitem1);
		frame.setJMenuBar(menubar);

		layerPane = new JLayeredPane(); // 配置は重ねることができるが，色は重ねられないみたい
		layerPane.setLayout(null);
		layerPane.setBackground(new Color(0, 0, 0, 0));

		// 　ベースパネル
		baseP = new JPanel();
		baseP.setBackground(new Color(0, 40, 90));
		baseP.setBounds(0, 0, MainFrame.w / 3, MainFrame.h);

		// ラインパネル
		/*LineP1 = new JPanel();
		LineP1.setBounds(0, MainFrame.h/8*3, MainFrame.w/3,MainFrame.h/8*3+10);
		LineP1.setBackground(new Color(255, 255, 240));
		*/
		// タイトルパネル
		titleP = new JPanel();
		titleP.setBackground(new Color(0, 0, 0, 0));
		titleP.setBounds(0, 0, MainFrame.w / 3, MainFrame.h / 8);
		// タイトルラベル
		JLabel titleLabel = new JLabel("Waltz for AI");
		titleLabel.setFont(new Font("Century", Font.ITALIC, 64));
		titleLabel.setForeground(new Color(255, 255, 240));
		titleLabel.setBackground(new Color(1, 1, 1, 0));
		titleP.add(titleLabel);

		// 情報パネル
		infoP = new JPanel();
		infoP.setBounds(0, MainFrame.h / 8, MainFrame.w / 3, MainFrame.h/8*2);
		infoP.setLayout(new BoxLayout(infoP, BoxLayout.Y_AXIS)); // 縦に配置するレイアウト
		infoP.setBackground(new Color(0, 0, 0, 0));
		// 情報ラベル（BGM）
		/*JLabel infoLabel = new JLabel("<html>　BGM<br><br><html>");
		infoLabel.setFont(new Font("Century", Font.PLAIN, 34));
		infoLabel.setForeground(new Color(255, 255, 240));
		infoLabel.setBackground(new Color(1, 1, 1, 0));
		*/
		// 情報ラベル（BGM）
		JLabel bgmLabel = new JLabel("　BGM ： natural");
		bgmLabel.setFont(new Font("Century", Font.PLAIN, 30));
		bgmLabel.setForeground(new Color(255, 255, 240));
		bgmLabel.setBackground(new Color(1, 1, 1, 0));
		// 情報ラベル（個体数)
		JLabel numL = new JLabel("<html>　Number of individuals<br><html>");
		numL.setFont(new Font("Century", Font.PLAIN, 30));
		numL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（個体数　草)
		JLabel grassL = new JLabel("　・plant : " + plant);
		grassL.setFont(new Font("Century", Font.PLAIN, 24));
		grassL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（草食）
		JLabel grassEatL = new JLabel("　・plant-eater : " + plantE);
		grassEatL.setFont(new Font("Century", Font.PLAIN, 24));
		grassEatL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（肉食）
		JLabel fleshEatL = new JLabel("　・flesh-eater : " + fleshE);
		fleshEatL.setFont(new Font("Century", Font.PLAIN, 24));
		fleshEatL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（雑食食）
		JLabel omniL = new JLabel("　・omnivorous : " + omni);
		omniL.setFont(new Font("Century", Font.PLAIN, 24));
		omniL.setForeground(new Color(255, 255, 240));
		// パネルにラベルを登録していく
		infoP.add(bgmLabel);
		//infoP.add(infoLabel);
		infoP.add(numL);
		infoP.add(grassL);
		infoP.add(grassEatL);
		infoP.add(fleshEatL);
		infoP.add(omniL);

		// 折れ線グラフ
		DefaultCategoryDataset data2 = new DefaultCategoryDataset();
		for (int i = 0; i < 10; i++) {
			data2.addValue(plantList.get(i), seriesName[0], time.get(i));
			data2.addValue(plantEList.get(i), seriesName[1], time.get(i));
			data2.addValue(fleshEList.get(i), seriesName[2], time.get(i));
			data2.addValue(omniList.get(i), seriesName[3], time.get(i));
		}
		oresen = ChartFactory.createLineChart(
				"Transition of Individuals", "time(sec)", "Number", data2,
				PlotOrientation.VERTICAL, true, false, false);
		TextTitle oresenTitle = oresen.getTitle();
		oresenTitle.setPaint(new Color(255, 255, 240));
		Plot oresen_plot = oresen.getPlot();
		oresen_plot.setBackgroundPaint(new Color(0, 0, 0, 0));
		oresen_plot.setBackgroundAlpha(0.3f);
		oresen.setBackgroundPaint(new Color(0, 0, 0, 0));
		// 線の色
		oresen_plot.setOutlinePaint(Color.white);
		// 折れ線パネル
		oresenP = new ChartPanel(oresen);
		oresenP.setBackground(new Color(0, 0, 0, 10));
		oresenP.setBounds(50, (MainFrame.h - MainFrame.h / 3),
				MainFrame.w / 3 - 100, MainFrame.h / 3 - 50);

		oresendraw();
		// Processingパネル
		processing = new Display(mf);
		processing.init();

		// 円グラフ描画
		piedraw();
		// 棒グラフ
		boudraw();

		// 前面から背面にレイアウトする．
		layerPane.add(infoP);
		layerPane.add(titleP);
		layerPane.add(chartP);
		layerPane.add(oresenP);
		layerPane.add(bouP);
		layerPane.add(baseP);
		layerPane.add(processing);
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
		} else {
			frame.setVisible(false);
		}
	}

	// 再描画メソッド
	public static void resetChart() {
		layerPane.remove(chartP); // 一度パネルから外す.
		layerPane.remove(oresenP);
		layerPane.remove(infoP);
		layerPane.remove(bouP);

		// 情報パネル
		infoP = new JPanel();
		infoP.setBounds(0, MainFrame.h / 8, MainFrame.w / 3, MainFrame.h/8*2);
		infoP.setLayout(new BoxLayout(infoP, BoxLayout.Y_AXIS)); // 縦に配置するレイアウト
		infoP.setBackground(new Color(0, 40, 90));
		// 情報ラベル（BGM）
		/*JLabel infoLabel = new JLabel("<html>　BGM<br><br><html>");
		infoLabel.setFont(new Font("Century", Font.PLAIN, 30));
		infoLabel.setForeground(new Color(255, 255, 240));
		infoLabel.setBackground(new Color(1, 1, 1, 0));
		*/
		// 情報ラベル（BGM）
		JLabel bgmLabel = new JLabel("　BGM ： "+IncreaseRate.advantage);
		bgmLabel.setFont(new Font("Century", Font.PLAIN, 30));
		bgmLabel.setForeground(new Color(255, 255, 240));
		bgmLabel.setBackground(new Color(1, 1, 1, 0));
		// 情報ラベル（個体数)
		JLabel numL = new JLabel("<html>　Number of individuals<br><html>");
		numL.setFont(new Font("Century", Font.PLAIN, 30));
		numL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（個体数　草)
		JLabel grassL = new JLabel("　・plant : " + plant);
		grassL.setFont(new Font("Century", Font.PLAIN, 24));
		grassL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（草食）
		JLabel grassEatL = new JLabel("　・plant-eater : " + plantE);
		grassEatL.setFont(new Font("Century", Font.PLAIN, 24));
		grassEatL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（草食）
		JLabel fleshEatL = new JLabel("　・flesh-eater : " + fleshE);
		fleshEatL.setFont(new Font("Century", Font.PLAIN, 24));
		fleshEatL.setForeground(new Color(255, 255, 240));
		// 情報ラベル（雑食食）
		JLabel omniL = new JLabel("　・omnivorous : " + omni);
		omniL.setFont(new Font("Century", Font.PLAIN, 24));
		omniL.setForeground(new Color(255, 255, 240));
		// パネルにラベルを登録していく
		//infoP.add(infoLabel);
		infoP.add(bgmLabel);
		infoP.add(numL);
		infoP.add(grassL);
		infoP.add(grassEatL);
		infoP.add(fleshEatL);
		infoP.add(omniL);

		// 折れ線グラフ
		oresendraw();
		// 円グラフ
		piedraw();
		// 棒グラフ
		boudraw();

		// パネルに戻す
		layerPane.add(infoP);
		layerPane.add(chartP);
		layerPane.add(oresenP);
		layerPane.add(bouP);

		frame.setVisible(true); // 更新を変え終わったら呼ぶ必要があるみたい
	}
	public static void oresendraw(){
		// 折れ線グラフ
		DefaultCategoryDataset data2 = new DefaultCategoryDataset();
		// データの更新
		plantList.remove(0);plantList.add(plant);
		plantEList.remove(0);plantEList.add(plantE);
		fleshEList.remove(0);fleshEList.add(fleshE);
		omniList.remove(0);omniList.add(omni);
		time.remove(0);secNum = secNum + sec;time.add(String.valueOf(secNum));
		for (int i = 0; i < 10; i++) {
			data2.addValue(plantList.get(i), seriesName[0], time.get(i));
			data2.addValue(plantEList.get(i), seriesName[1], time.get(i));
			data2.addValue(fleshEList.get(i), seriesName[2], time.get(i));
			data2.addValue(omniList.get(i), seriesName[3], time.get(i));
		}
		JFreeChart oresen = ChartFactory.createLineChart(
				"Transition of Individuals", "time(sec)", "Number", data2,
				PlotOrientation.VERTICAL, true, false, false);
		TextTitle oresenTitle = oresen.getTitle();
		oresenTitle.setPaint(new Color(255, 255, 240));
		Plot oresen_plot = oresen.getPlot();
		oresen_plot.setBackgroundPaint(new Color(0, 0, 0, 0));
		oresen.setBackgroundPaint(new Color(0, 0, 0, 0));
		// 線の色
		oresen_plot.setOutlinePaint(Color.white);
		// 折れ線パネル
		oresenP = new ChartPanel(oresen);
		oresenP.setBackground(new Color(0, 40, 90));
		oresenP.setBounds(0, (MainFrame.h - MainFrame.h / 3),MainFrame.w / 3 - 50,
				MainFrame.h / 3 - 50);
		CategoryPlot plot = oresen.getCategoryPlot();
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		// 凡例
		LegendTitle senlegend = oresen.getLegend();
		senlegend.setBackgroundPaint(new Color(0, 40, 90));
		senlegend.setItemPaint(new Color(255, 255, 240));
		// 線の色
		renderer.setSeriesPaint(0, ChartColor.GREEN);
		renderer.setSeriesPaint(1, ChartColor.pink);
		renderer.setSeriesPaint(2, ChartColor.RED);
		renderer.setSeriesPaint(3, ChartColor.GRAY);
	}

	public static void piedraw() {
		// 円グラフ再設定
		DefaultPieDataset data = new DefaultPieDataset();
		data.setValue("plant", plant);
		data.setValue("plantEater", plantE);
		data.setValue("fleshEater", fleshE);
		data.setValue("omnivorous", omni);
		JFreeChart chart = ChartFactory.createRingChart(
				"Percentage of Individuals", data, true, true, false);
		TextTitle pieTitle = chart.getTitle();
		pieTitle.setPaint(new Color(255, 255, 240));
		RingPlot chart_plot = (RingPlot) chart.getPlot();
		chart_plot.setBackgroundPaint(new Color(0, 0, 0, 0));
		// 円グラフのセクションの色を設定する
		RingPlot piePlot = (RingPlot) chart.getPlot();
		piePlot.setSectionPaint("plant", Color.GREEN);
		piePlot.setSectionPaint("plantEater", Color.PINK);
		piePlot.setSectionPaint("fleshEater", Color.RED);
		piePlot.setSectionPaint("omnivorous", Color.GRAY);
		// 凡例
		LegendTitle chlegend = chart.getLegend();
		chlegend.setBackgroundPaint(new Color(0, 40, 90));
		chlegend.setItemPaint(new Color(255, 255, 240));
		chartP = new ChartPanel(chart);
		chartP.setBackground(new Color(0, 40, 90));
		chart.setBackgroundPaint(new Color(0, 0, 0, 0));
		chartP.setBounds(0, MainFrame.h / 3 + 50, MainFrame.w/6,
				MainFrame.h / 3 - 100);
	}

	// 棒グラフの描画
	public static void boudraw(){
		// 棒グラフ
		DefaultCategoryDataset boudata = new DefaultCategoryDataset();
		boudata.addValue(plant, "Plant", "");
		boudata.addValue(plantE, "PlantEater", "");
		boudata.addValue(fleshE, "FleshEater", "");
		boudata.addValue(omni, "omnivorous", "");
		JFreeChart bouchart = ChartFactory.createBarChart("amount of Individual", "species", "num",
				boudata, PlotOrientation.HORIZONTAL, true, false, false);
		TextTitle bouTitle = bouchart.getTitle();
		bouTitle.setPaint(new Color(255, 255, 240));
		Plot bou_plot = bouchart.getPlot();
		bou_plot.setBackgroundPaint(new Color(0, 0, 0, 0));
		bouchart.setBackgroundPaint(new Color(0, 0, 0, 0));
		// 棒プロット
		CategoryPlot bplot = bouchart.getCategoryPlot();
		BarRenderer bourenderer = (BarRenderer) bplot.getRenderer();
		// 凡例
		LegendTitle bousenlegend = bouchart.getLegend();
		bousenlegend.setBackgroundPaint(new Color(0, 40, 90));
		bousenlegend.setItemPaint(new Color(255, 255, 240));
		// 線の色
		bourenderer.setSeriesPaint(0, ChartColor.GREEN);
		bourenderer.setSeriesPaint(1, ChartColor.pink);
		bourenderer.setSeriesPaint(2, ChartColor.RED);
		bourenderer.setSeriesPaint(3, ChartColor.GRAY);
		// 棒パネル
		bouP = new ChartPanel(bouchart);
		bouP.setBackground(new Color(0, 40, 90));
		bouP.setBounds(MainFrame.w/6 , MainFrame.h / 3+50 ,MainFrame.w/6, MainFrame.h / 3 - 100);
	}
}

// 2秒毎に再描画メソッドを呼び出す
class RepT implements Runnable {
	public void run() {
		// 初期の音楽を鳴らす
		String premusic = "natural";
		int i=1, j=1;
		PlayBGM p = new PlayBGM();
		p.start("natural");
		while (true) {
			try {
				// 10秒間の増加率を調べ，最も増加の多い音楽に切り替える
				if(i%5 == 0){
					IncreaseRate.renew(); //増加率を更新
					if(IncreaseRate.advantage.equals(premusic)){
						j++;
					}else{
						p.stop();
						p.start(IncreaseRate.advantage);
						premusic = IncreaseRate.advantage;
					}
				}
				// BGMが最後までなったら．
				if(j%11 == 0){
					p.stop();
					p.start(IncreaseRate.advantage);
					j=1;
				}
				i++;
				Thread.sleep(2000);
				SecondFrame.resetChart();
			} catch (InterruptedException e) {
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
}
