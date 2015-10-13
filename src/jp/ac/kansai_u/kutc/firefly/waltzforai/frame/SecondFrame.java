package jp.ac.kansai_u.kutc.firefly.waltzforai.frame;

import java.awt.BasicStroke;
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
public class SecondFrame extends JFrame{
	private static final long serialVersionUID = -4410222598510368172L;
	
	private Display processing;
	private JLayeredPane layerPane;
	private IncreaseRate increaseRate;
	private ChartPanel chartP;
	private ChartPanel oresenP;
	private ChartPanel bouP;
	private JPanel infoP;
	private JPanel titleP;
	private JPanel baseP;
	private int plant, fleshE, plantE, omni;
	private int sec = 2, secNum = 20;

	private String[] speciesName = { "plant", "plantEater", "fleshEater",
			"omvirous" };
	private LinkedList<String> time = new LinkedList<String>();
	private LinkedList<Integer> plantList = new LinkedList<Integer>();
	private LinkedList<Integer> plantEList = new LinkedList<Integer>();
	private LinkedList<Integer> fleshEList = new LinkedList<Integer>();
	private LinkedList<Integer> omniList = new LinkedList<Integer>();

	// パネルに載せるラベル
	private JLabel titleLabel;
	private JLabel bgmLabel;
	private JLabel numLabel;
	private JLabel plantLabel;
	private JLabel plantEatLabel;
	private JLabel fleshEatLabel;
	private JLabel omniLabel;

	public SecondFrame(int width, int height) {
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		increaseRate = new IncreaseRate();
		
		// 折れ線グラフで使うlistの初期化
		for (int i = 0; i < 10; i++) {
			time.add(String.valueOf(sec * (i + 1)));
			plantList.add(0);
			plantEList.add(0);
			fleshEList.add(0);
			omniList.add(0);
		}

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
		setJMenuBar(menubar);

		layerPane = new JLayeredPane(); // 配置は重ねることができるが，色は重ねられないみたい
		layerPane.setLayout(null);
		layerPane.setBackground(new Color(0, 0, 0, 0));

		// 　ベースパネル
		baseP = new JPanel();
		baseP.setBackground(new Color(0, 40, 90));
		baseP.setBounds(0, 0, width / 3, height);

		// ラインパネル
		/*LineP1 = new JPanel();
		LineP1.setBounds(0, MainFrame.h/8*3, MainFrame.w/3,MainFrame.h/8*3+10);
		LineP1.setBackground(new Color(255, 255, 240));
		*/
		// タイトルパネル
		titleP = new JPanel();
		titleP.setBackground(new Color(0, 0, 0, 0));
		titleP.setBounds(0, 0, width / 3, height / 8);
		
		// タイトルラベル
		titleLabel = new JLabel("Waltz for AI");
		titleLabel.setFont(new Font("Century", Font.ITALIC, 64));
		titleLabel.setForeground(new Color(255, 255, 240));
		titleLabel.setBackground(new Color(1, 1, 1, 0));
		titleP.add(titleLabel);

		// 情報パネル
		infoP = new JPanel();
		infoP.setBounds(0, height / 8, width / 3, height / 8*2);
		infoP.setLayout(new BoxLayout(infoP, BoxLayout.Y_AXIS)); // 縦に配置するレイアウト
		infoP.setBackground(new Color(0, 40, 90));
		// 情報ラベル（BGM）
		/*JLabel infoLabel = new JLabel("<html>　BGM<br><br><html>");
		infoLabel.setFont(new Font("Century", Font.PLAIN, 34));
		infoLabel.setForeground(new Color(255, 255, 240));
		infoLabel.setBackground(new Color(1, 1, 1, 0));
		*/
		// 情報ラベル（BGM）
		bgmLabel = new JLabel("  Dominance: " + increaseRate.getAdvantage());
		bgmLabel.setFont(new Font("Century", Font.PLAIN, 30));
		bgmLabel.setForeground(new Color(255, 255, 240));
		bgmLabel.setBackground(new Color(1, 1, 1, 0));
		// 情報ラベル（個体数)
		numLabel = new JLabel("<html>　Population<br><html>");
		numLabel.setFont(new Font("Century", Font.PLAIN, 30));
		numLabel.setForeground(new Color(255, 255, 240));
		// 情報ラベル（個体数　草)
		plantLabel = new JLabel("     Plant: " + plant);
		plantLabel.setFont(new Font("Century", Font.PLAIN, 24));
		plantLabel.setForeground(new Color(255, 255, 240));
		// 情報ラベル（草食）
		plantEatLabel = new JLabel("     Plant-eater: " + plantE);
		plantEatLabel.setFont(new Font("Century", Font.PLAIN, 24));
		plantEatLabel.setForeground(new Color(255, 255, 240));
		// 情報ラベル（肉食）
		fleshEatLabel = new JLabel("     Flesh-eater: " + fleshE);
		fleshEatLabel.setFont(new Font("Century", Font.PLAIN, 24));
		fleshEatLabel.setForeground(new Color(255, 255, 240));
		// 情報ラベル（雑食食）
		omniLabel = new JLabel("     Omnivorous: " + omni);
		omniLabel.setFont(new Font("Century", Font.PLAIN, 24));
		omniLabel.setForeground(new Color(255, 255, 240));
		// パネルにラベルを登録していく
		infoP.add(bgmLabel);
		infoP.add(numLabel);
		infoP.add(plantLabel);
		infoP.add(plantEatLabel);
		infoP.add(fleshEatLabel);
		infoP.add(omniLabel);

		// 折れ線パネル
		oresenP = new ChartPanel(oresendraw());
		oresenP.setBackground(new Color(0, 40, 90));
		oresenP.setBounds(0, height*2 / 3,
				width / 3 - 50, height / 3 - 50);
		
		// 円グラフ描画
		chartP = new ChartPanel(piedraw());
		chartP.setBackground(new Color(0, 40, 90));
		chartP.setBounds(0, getHeight() / 3 + 50, getWidth() / 6,
				getHeight() / 3 - 100);
		
		// 棒グラフ
		bouP = new ChartPanel(boudraw());
		bouP.setBackground(new Color(0, 40, 90));
		bouP.setBounds(getWidth()/6, getHeight() / 3+50, getWidth()/6, getHeight() / 3 - 100);
		
		// Processingパネル
		processing = new Display();
		processing.setBounds(width/3, 0, width*2/3, height);
		processing.init();

		// 前面から背面にレイアウトする．
		layerPane.add(infoP);
		layerPane.add(titleP);
		layerPane.add(chartP);
		layerPane.add(oresenP);
		layerPane.add(bouP);
		layerPane.add(baseP);
		layerPane.add(processing);
		
		// フレームへ貼っつける
		getContentPane().add(layerPane);
	}

	// FisrFlameからボタンでフレームを切り替えられる
	public void VisibleFlg(boolean flg) {
		if (flg) {
			setVisible(true);
			// グラフ再描画スレッド
			Thread thread = new Thread(new RepT());
			thread.start();
		} else {
			setVisible(false);
		}
	}

	// 再描画メソッド
	public void resetChart() {
		// 情報ラベル（BGM）
		bgmLabel.setText("  Dominance: " + increaseRate.getAdvantage());
		// 情報ラベル（個体数　草)
		plantLabel.setText("     Plant: " + plant);
		// 情報ラベル（草食）
		plantEatLabel.setText("     Plant-eater: " + plantE);
		// 情報ラベル（草食）
		fleshEatLabel.setText("     Flesh-eater: " + fleshE);
		// 情報ラベル（雑食食）
		omniLabel.setText("     Omnivorous: " + omni);
		
		// グラフの更新
		oresenP.setChart(oresendraw());	// 折れ線グラフ描画
		chartP.setChart(piedraw());		// 円グラフ
		bouP.setChart(boudraw());		// 棒グラフ
		
		setVisible(true); // 更新を変え終わったら呼ぶ必要があるみたい
	}
	public JFreeChart oresendraw(){
		// 折れ線グラフ
		DefaultCategoryDataset data2 = new DefaultCategoryDataset();
		// データの更新
		plantList.remove(0);plantList.add(plant);
		plantEList.remove(0);plantEList.add(plantE);
		fleshEList.remove(0);fleshEList.add(fleshE);
		omniList.remove(0);omniList.add(omni);
		time.remove(0);secNum = secNum + sec;time.add(String.valueOf(secNum));
		for (int i = 0; i < 10; i++) {
			//data2.addValue(plantList.get(i), seriesName[0], time.get(i));
			data2.addValue(plantEList.get(i), speciesName[1], time.get(i));
			data2.addValue(fleshEList.get(i), speciesName[2], time.get(i));
			data2.addValue(omniList.get(i), speciesName[3], time.get(i));
		}
		JFreeChart oresen = ChartFactory.createLineChart(
				"Population Transition", "time(sec)", "Number", data2,
				PlotOrientation.VERTICAL, true, false, false);
		TextTitle oresenTitle = oresen.getTitle();
		oresenTitle.setPaint(new Color(255, 255, 240));
		Plot oresen_plot = oresen.getPlot();
		oresen_plot.setBackgroundPaint(new Color(0, 0, 0, 0));
		oresen.setBackgroundPaint(new Color(0, 0, 0, 0));
		// 線の色
		oresen_plot.setOutlinePaint(Color.white);
		// 折れ線パネル
		CategoryPlot plot = oresen.getCategoryPlot();
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		
	    // シリーズの設定
	    for (int i = 0; i < data2.getRowCount(); i++) {
	        renderer.setSeriesStroke(i, new BasicStroke(3));
	        renderer.setSeriesShapesVisible(i, true);
	    }
		 
		// 凡例
		LegendTitle senlegend = oresen.getLegend();
		senlegend.setBackgroundPaint(new Color(0, 40, 90));
		senlegend.setItemPaint(new Color(255, 255, 240));
		// 線の色
		renderer.setSeriesPaint(0, ChartColor.GREEN);
		renderer.setSeriesPaint(1, ChartColor.RED);
		renderer.setSeriesPaint(2, ChartColor.GRAY);
		
		return oresen;
	}

	public JFreeChart piedraw() {
		// 円グラフ再設定
		DefaultPieDataset data = new DefaultPieDataset();
		//data.setValue("plant", plant);
		data.setValue("plantEater", plantE);
		data.setValue("fleshEater", fleshE);
		data.setValue("omnivorous", omni);
		JFreeChart chart = ChartFactory.createRingChart(
				"Population Ratio", data, true, true, false);
		TextTitle pieTitle = chart.getTitle();
		pieTitle.setPaint(new Color(255, 255, 240));
		RingPlot chart_plot = (RingPlot) chart.getPlot();
		chart_plot.setBackgroundPaint(new Color(0, 0, 0, 0));
		// 円グラフのセクションの色を設定する
		RingPlot piePlot = (RingPlot) chart.getPlot();
		//piePlot.setSectionPaint("plant", Color.GREEN);
		piePlot.setSectionPaint("plantEater", Color.GREEN);
		piePlot.setSectionPaint("fleshEater", Color.RED);
		piePlot.setSectionPaint("omnivorous", Color.GRAY);
		// 凡例
		LegendTitle chlegend = chart.getLegend();
		chlegend.setBackgroundPaint(new Color(0, 40, 90));
		chlegend.setItemPaint(new Color(255, 255, 240));
		chart.setBackgroundPaint(new Color(0, 0, 0, 0));
		
		return chart;
	}

	// 棒グラフの描画
	public JFreeChart boudraw(){
		// 棒グラフ
		DefaultCategoryDataset boudata = new DefaultCategoryDataset();
		//boudata.addValue(plant, "Plant", "");
		boudata.addValue(plantE, "PlantEater", "");
		boudata.addValue(fleshE, "FleshEater", "");
		boudata.addValue(omni, "omnivorous", "");
		JFreeChart bouchart = ChartFactory.createBarChart("Population Chart", "species", "num",
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
		bourenderer.setSeriesPaint(1, ChartColor.RED);
		bourenderer.setSeriesPaint(2, ChartColor.GRAY);
		
		return bouchart;
	}
	
	// ゲッタ
	public Display getDisplay(){ return processing; }
	
	// 2秒毎に再描画メソッドを呼び出す
	class RepT implements Runnable {
		public void run() {
			// 初期の音楽を鳴らす
			String premusic = "plant-eater";
			int i=1;
			PlayBGM p = new PlayBGM();
			p.start(premusic);
			while (true) {
				plant = processing.getWorld().getPlantNum();
				plantE = processing.getWorld().getPlantEaterNum();
				fleshE = processing.getWorld().getFleshEaterNum();
				omni = processing.getWorld().getOmnivorousNum();
				try {
					// 20秒間の増加率を調べ，最も増加の多い音楽に切り替える
					if(i%10 == 0){
						//increaseRate.setPlantNum(plant);
						increaseRate.setPlantENum(plantE);
						increaseRate.setFleshENum(fleshE);
						increaseRate.setOmniNum(omni);
						increaseRate.renew(); // 増加率を更新
						if(!increaseRate.getAdvantage().equals(premusic)){
							p.stop();
							p.start(increaseRate.getAdvantage());
							premusic = increaseRate.getAdvantage();
						}
					}
					i++;
					Thread.sleep(2000);
					resetChart();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
