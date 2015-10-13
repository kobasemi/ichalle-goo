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
	private static final long serialVersionUID = -1156954138923309157L;

	// コンストラクタ
	public MainFrame(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize.width, screenSize.height);

		FstFrame firstF = new FstFrame("フレーム"); 	// 下部にフレームを作るクラスがある．
		firstF.setSize(getWidth(), getHeight());
		SecondFrame secondF = new SecondFrame(getWidth(), getHeight());  	// visibleを決めるためインスタンス化する
		// FstFrameに貼るボタンの設定
		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("btn.png"));
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
	    		// スレッドで音楽を鳴らす
	    		//ExThread1 thread1 = new ExThread1();
	    		//thread1.start();
	    		
	    		// スタートボタンを押したタイミングでワールドを動かし始める
	    		secondF.getDisplay().startWorld();
	    	}
	    });
	    btn.setBounds(getWidth()/2-50, getHeight()-getHeight()/3, 100, 50);
	    JLabel titleL = new JLabel("Waltz for AI");
	    titleL.setFont(new Font("Century", Font.ITALIC, 100));
	    titleL.setForeground(new Color(255, 239, 233));
	    titleL.setBounds(getWidth()/2-300, getHeight()/2-100, getWidth()+300, 200);
	    // FstFrameに背景画像をラベルで載せる
	    JLabel lblimg = new JLabel();
		lblimg.setIcon(new ImageIcon(getClass().getClassLoader().getResource("bgi.jpg")));
		lblimg.setBounds(0, 0, getWidth(), getHeight());
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
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setLayout(null);
	}
}



/*class ExThread1 extends Thread {
	public void run() {
		File soundFile = new File("./Forest.wav");
		AudioInputStream audioStream = null;
		try {
			audioStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		AudioFormat format = audioStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		Clip line = null;
		try {
			line = (Clip) AudioSystem.getLine(info);
			line.open(audioStream);
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
		line.start();
		line.drain();
		line.close();
	}
}*/
