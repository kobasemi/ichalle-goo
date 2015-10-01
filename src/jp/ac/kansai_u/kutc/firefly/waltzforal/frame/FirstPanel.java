package jp.ac.kansai_u.kutc.firefly.waltzforal.frame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class FirstPanel extends JPanel{
	private static final long serialVersionUID = -5189756701008520048L;
	
	private MainFrame mf;
	
	// s = パネル名
	public FirstPanel(MainFrame m){
		mf = m;
		this.setName("fp");
		//this.setLayout(null);
        this.setSize(mf.getWidth(), mf.getHeight());
        this.setBackground(Color.WHITE);

		//this.setSize(400,200);
		JButton btn = new JButton("Start");
		btn.setBounds(20,100,150,40);
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				pc(MainFrame.PanelNames[1]);
			}
		});
		this.add(btn);
	}
	
	// ボタンが押されない場合：str = 自身の名前
	// ボタンが押された場合：str = 切り替えるパネルの名前
	// PanelChange　に自身を渡すのは自身の名前を送信するため
	public void pc(String str){
		mf.PanelChange((JPanel)this, str);
	}
}
