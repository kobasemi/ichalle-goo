package jp.ac.kansai_u.kutc.firefly.waltzforai.frame;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class MainFrame extends JFrame{
	private static final long serialVersionUID = 8560089064861399672L;
	
	public static String[] PanelNames = {"fp","mp","statistic","setting"};
	private MoniterPanel mp;
	private FirstPanel fp;

	public MainFrame(){
		// 使用しているモニタのサイズに合わせてパネルサイズを求めます
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setSize(screenSize.width, screenSize.height);
	    
		// 画面の解像度からサイズを求める
		/*GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		Rectangle rect = env.getMaximumWindowBounds();
		setBounds(rect);*/

		// メニューの実装
		JMenuBar menubar = new JMenuBar();
		JMenu menu1 = new JMenu("Menu");
		menubar.add(menu1);
		JMenuItem menuitem1 = new JMenuItem("Re-Start");
		menuitem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mp.setVisible(false);
				fp.setVisible(true);
			}
		});
		JMenuItem menuitem2 = new JMenuItem("Statistics");
		JMenuItem menuitem3 = new JMenuItem("Close");
		menuitem3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu1.add(menuitem1);
		menu1.add(menuitem2);
		menu1.add(menuitem3);
		this.setJMenuBar(menubar);
		
		mp = new MoniterPanel(this, PanelNames[1]);
	    fp = new FirstPanel(this);
	    
		// 初期画面で写すパネルをセットする．．
		this.add(fp);
		fp.setVisible(true);
		this.add(mp);
		mp.setVisible(false);
	}

	// 自身を消して次を呼ぶ
	// 各パネルから呼び出される　（切り替えもとのパネル，切り替えるパネル名）
	public void PanelChange(JPanel jp, String str) {
		// 自身のパネルは見えないようにする
		String name = jp.getName();
		if (name == PanelNames[0])
			fp = (FirstPanel)jp; fp.setVisible(false);
		if (name == PanelNames[1])
			mp = (MoniterPanel)jp; mp.setVisible(false);
		// 開くパネルを見えるようにする
		if (str == PanelNames[0])
			fp.setVisible(true);
		if (str == PanelNames[1]){
			mp.setVisible(true);
			mp.add(mp.display);
		}
	}
}
