package jp.ac.kansai_u.kutc.firefly.waltzforai.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;


//はじめに表示されるウィンドウ　setVisibleでフレームを切り替える
public class MainFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	public static int w;
	public static int h;
	// コンストラクタ
	public MainFrame(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize.width, screenSize.height);
		w = screenSize.width;
		h = screenSize.height;

		FstFrame firstF = new FstFrame("フレーム"); // 下部にフレームを作るクラスがある．
		SecondFrame secondF = new SecondFrame();  // visibleを決めるためインスタンス化する
		// FstFrameに貼るボタンの設定
		ImageIcon icon = new ImageIcon("btn.png");
	    JButton btn = new JButton(icon);
	    JLabel l = new JLabel("Start");
	    l.setFont(new Font("Century", Font.ITALIC, 30));
	    l.setForeground(new Color(255, 239, 233));
	    btn.add(l);
	    btn.setPreferredSize(new Dimension(200,50));
	    btn.setHorizontalTextPosition(JButton.CENTER);
	    btn.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		//FrameChange(firstF, secondF);
	    		firstF.setVisible(false);
	    		secondF.VisibleFlg(true);  // SecondFrameで実装したメソッド
	    	}
	    });
	    btn.setBounds(w/2-50, h-h/3, 100, 50);
	    JLabel titleL = new JLabel("Waltz for AI");
	    titleL.setFont(new Font("Century", Font.ITALIC, 100));
	    titleL.setForeground(new Color(255, 239, 233));
	    titleL.setBounds(w/2-300, h/2-100, w+300, 200);
	    // FstFrameに背景画像をラベルで載せる
	    JLabel lblimg = new JLabel();
		lblimg.setIcon(new ImageIcon("bgi.jpg"));
		lblimg.setBounds(0,0,MainFrame.w, MainFrame.h);
		// FstFrameに上に貼りたい物から順にadd
		firstF.add(titleL);
	    firstF.add(btn);
	    firstF.add(lblimg);
	    //初期の状態
	    firstF.setVisible(true);
	    secondF.VisibleFlg(false);
	}
	// フレームの切り替え(消すフレーム，表示するフレーム)
	// SecondFrameがJFrameを継承しなくなったので使わない　visibleflgで遷移
	/* public static void FrameChange(JFrame del_frame, JFrame cre_frame){
			del_frame.setVisible(false);
			cre_frame.setVisible(true);
	   }*/
}

class FstFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	public FstFrame(String title){
	    setSize(MainFrame.w, MainFrame.h);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setLayout(null);
	}
}