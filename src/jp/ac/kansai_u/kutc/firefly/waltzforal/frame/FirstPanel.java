package jp.ac.kansai_u.kutc.firefly.waltzforal.frame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FirstPanel extends JPanel{
	MainFrame mf;
	String str;
	// s = パネル名
	public FirstPanel(MainFrame m, String s){
		mf = m;
		str = s;
		this.setName("fp");
		//this.setLayout(null);
        this.setSize(mf.getW(), mf.getH());
        this.setBackground(Color.WHITE);

		//this.setSize(400,200);
		JButton btn = new JButton("Start");
		btn.setBounds(20,100,150,40);
		btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				pc(mf.PanelNames[1]);
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
