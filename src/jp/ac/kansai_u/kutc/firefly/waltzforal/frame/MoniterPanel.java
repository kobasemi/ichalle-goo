package jp.ac.kansai_u.kutc.firefly.waltzforal.frame;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import processing.core.PApplet;

public class MoniterPanel extends JPanel {
	Display a;
	String str;
	MainFrame mf;

	public MoniterPanel(MainFrame m, String s){
		mf = m;
		str = s;

		this.setName("mp");
        setSize(mf.getW(), mf.getH());

        setBackground(Color.WHITE);
		PApplet processing = new Display(mf);
		// PAppletを埋め込む
		this.add(processing);
		processing.init();

		/* メニューの追加
		JMenuBar menubar = new JMenuBar();
		JMenu menu1 = new JMenu("Menu");
		menubar.add(menu1);
		JMenuItem menuitem1 = new JMenuItem("Re-Start");
	    menuitem1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                pc(mf.PanelNames[0]);
            }
        });
		JMenuItem menuitem2 = new JMenuItem("Statistics");
	    JMenuItem menuitem3 = new JMenuItem("Close");
	    menuitem3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
               System.exit(0);
            }
        });
	    menu1.add(menuitem1);
	    menu1.add(menuitem2);
	    menu1.add(menuitem3);

	    m.setJMenuBar(menubar);
	    */
	}
	public void pc(String str){
		this.setVisible(false);
        mf.PanelChange((JPanel)this, str);
    }
}

