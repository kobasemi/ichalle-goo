package jp.ac.kansai_u.kutc.firefly.waltzforai.frame;

import java.awt.Color;

import javax.swing.JPanel;

import jp.ac.kansai_u.kutc.firefly.waltzforai.Display;

public class MoniterPanel extends JPanel {
	private static final long serialVersionUID = -6249988198605321756L;
	
	public Display display;
	String str;
	MainFrame mf;

	public MoniterPanel(MainFrame m, String s){
		mf = m;
		str = s;

		this.setName("mp");
        setSize(mf.getWidth(), mf.getHeight());

        setBackground(Color.WHITE);
        display = new Display(mf);
		
		// PAppletを埋め込む
		display.init();
	}
	
	public void pc(String str){
		this.setVisible(false);
        mf.PanelChange((JPanel)this, str);
    }
}

