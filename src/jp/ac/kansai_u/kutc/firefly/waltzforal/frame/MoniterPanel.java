package jp.ac.kansai_u.kutc.firefly.waltzforal.frame;

import java.awt.Color;

import javax.swing.JPanel;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;
import processing.core.PApplet;

public class MoniterPanel extends JPanel {
	private static final long serialVersionUID = -6249988198605321756L;
	
	Display a;
	String str;
	MainFrame mf;

	public MoniterPanel(MainFrame m, String s){
		mf = m;
		str = s;

		this.setName("mp");
        setSize(mf.getWidth(), mf.getHeight());

        setBackground(Color.WHITE);
		PApplet processing = new Display(mf);
		// PAppletを埋め込む
		this.add(processing);
		processing.init();
	}
	
	public void pc(String str){
		this.setVisible(false);
        mf.PanelChange((JPanel)this, str);
    }
}

