package game;

import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import login.JDBCModel;

public class Game{
	
	private JFrame frame;
	private Canvas canvas;
	private JButton blackBtn, redBtn, greenBtn, blueBtn, yellowBtn, eraserBtn, clearBtn, exitBtn, start;
	private JSlider slider;
	private PaintCanvas paintCanvas;
	private JLabel timerLabel, nickname1, grade1, score1, nickname2, grade2, score2, nickname3, grade3, score3, nickname4, grade4, score4;
	private JProgressBar bar;
	private Thread timer;
	private double i, j;
	private JDBCModel model;
	private JProgressBar bar1, bar2, bar3, bar4;
	private JTextField chatInput;
	private JTextArea chatLog;
	private GameChat gameChat;
	private String nickName;
	
	public Game(String nickName, JDBCModel model) {
		this.nickName = nickName;
		this.model = model;
		
		//---------------------------frame------------------------------
		frame = new JFrame("게임방");
		frame.setBounds(500,100,1024,768);
		frame.getContentPane().setLayout(null);

		
		start = new JButton("게임시작");
		start.setBounds(212, 32, 110, 36);
		frame.getContentPane().add(start);
		
		bar = new JProgressBar();
		bar.setBounds(212, 647, 200, 14);
		frame.getContentPane().add(bar);
		
		timerLabel = new JLabel("");
		timerLabel.setBounds(222, 596, 133, 36);
		frame.getContentPane().add(timerLabel);
		
		blackBtn = new JButton("검");
		blackBtn.setBounds(212, 534, 61, 50);
		frame.getContentPane().add(blackBtn);
		
		redBtn = new JButton("빨");
		redBtn.setBounds(285, 534, 61, 50);
		frame.getContentPane().add(redBtn);
		
		greenBtn = new JButton("초");
		greenBtn.setBounds(358, 534, 61, 50);
		frame.getContentPane().add(greenBtn);
		
		blueBtn = new JButton("파");
		blueBtn.setBounds(431, 534, 61, 50);
		frame.getContentPane().add(blueBtn);
		
		yellowBtn = new JButton("노");
		yellowBtn.setBounds(503, 534, 61, 50);
		frame.getContentPane().add(yellowBtn);
		
		eraserBtn = new JButton("지우개");
		eraserBtn.setBounds(619, 534, 80, 50);
		frame.getContentPane().add(eraserBtn);
		
		clearBtn = new JButton("초기화");
		clearBtn.setBounds(711, 534, 94, 50);
		frame.getContentPane().add(clearBtn);
		
		exitBtn = new JButton("나가기");
		exitBtn.setBounds(907, 45, 61, 50);
		frame.getContentPane().add(exitBtn);
		
		slider = new JSlider();
		slider.setBounds(555, 502, 220, 36);
		frame.getContentPane().add(slider);
		
		paintCanvas = new PaintCanvas(Game.this);
		canvas = paintCanvas.getCanvas(); 
		canvas.setBounds(181, 120, 618, 424);
		frame.getContentPane().add(canvas);

		nickname1 = new JLabel("nickname1");
		nickname1.setBounds(34, 107, 94, 26);
		frame.getContentPane().add(nickname1);
		
		grade1 = new JLabel("grade1");
		grade1.setBounds(34, 143, 94, 26);
		frame.getContentPane().add(grade1);
		
		score1 = new JLabel("score1");
		score1.setBounds(34, 179, 94, 26);
		frame.getContentPane().add(score1);
		
		nickname2 = new JLabel("nickname2");
		nickname2.setBounds(858, 105, 94, 26);
		frame.getContentPane().add(nickname2);
		
		grade2 = new JLabel("grade2");
		grade2.setBounds(858, 141, 94, 26);
		frame.getContentPane().add(grade2);
		
		score2 = new JLabel("score2");
		score2.setBounds(858, 177, 94, 26);
		frame.getContentPane().add(score2);
		
		nickname3 = new JLabel("nickname3");
		nickname3.setBounds(34, 305, 94, 26);
		frame.getContentPane().add(nickname3);
		
		grade3 = new JLabel("grade3");
		grade3.setBounds(34, 341, 94, 26);
		frame.getContentPane().add(grade3);
		
		score3 = new JLabel("score3");
		score3.setBounds(34, 377, 94, 26);
		frame.getContentPane().add(score3);
		
		nickname4 = new JLabel("nickname4");
		nickname4.setBounds(858, 305, 94, 26);
		frame.getContentPane().add(nickname4);
		
		grade4 = new JLabel("grade4");
		grade4.setBounds(858, 341, 94, 26);
		frame.getContentPane().add(grade4);
		
		score4 = new JLabel("score4");
		score4.setBounds(858, 377, 94, 26);
		frame.getContentPane().add(score4);
		
		bar1 = new JProgressBar();
		bar1.setBounds(34, 215, 94, 14);
		frame.getContentPane().add(bar1);
		
		bar2 = new JProgressBar();
		bar2.setBounds(858, 213, 94, 14);
		frame.getContentPane().add(bar2);
		
		bar3 = new JProgressBar();
		bar3.setBounds(34, 413, 94, 14);
		frame.getContentPane().add(bar3);
		
		bar4 = new JProgressBar();
		bar4.setBounds(858, 430, 94, 14);
		frame.getContentPane().add(bar4);
		
		chatInput = new JTextField();
		chatInput.setBounds(485, 693, 322, 26);
		frame.getContentPane().add(chatInput);
		chatInput.setColumns(10);
		
		chatLog = new JTextArea();
		chatLog.setBounds(485, 594, 322, 92);
		frame.getContentPane().add(chatLog);
		//---------------------------End of frame------------------------------
		
		
		//---------------------------button actions----------------------------
		
		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameChat.backToWaiting();
			}
		});
		
		
		
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timer = new Thread(new Runnable() {
					@Override
					public void run() {
						i = 150;
						j = 0.1;
						while (true) {
							if(i < 0.1) {
								timerLabel.setText("");
								start.setEnabled(true);
								break;
							}
							i = i-j;
							timerLabel.setText(String.format("%.1f", i));
							bar.setValue((int)(i*2/3));
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
				start.setEnabled(false);
				timer.setDaemon(true);
				timer.start();
			}
		});
		
		frame.setVisible(true);
		
		//닉네임과 이 방에서 맞춘 점수 갯수 나타내는 스레드
		Thread nickname_and_score_Setter = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					if(!frame.isVisible()) break;
					model.displayForRoom(1593, nickname1, nickname2, nickname3, nickname4, score1, score2, score3, score4);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		nickname_and_score_Setter.setDaemon(true);
		nickname_and_score_Setter.start();

		//개인별 경험치를 프로그래스바에 나타내는 스레드
		Thread barSetter = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					if(!frame.isVisible()) break;
					//model.displayEXP(nickname1, nickname2, nickname3, nickname4);
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		barSetter.setDaemon(true);
		barSetter.start();
		
		gameChat = new GameChat(Game.this);
	}
	//GamePlayRoom Constructor
	
	
	//---------------------getter & setter-----------------------
	public JFrame getFrame() {return frame;}
	public Canvas getCanvas() {return canvas;}
	public JButton getBlackBtn() {return blackBtn;}
	public JButton getRedBtn() {return redBtn;}
	public JButton getGreenBtn() {return greenBtn;}
	public JButton getBlueBtn() {return blueBtn;}
	public JButton getYellowBtn() {return yellowBtn;}
	public JButton getEraserBtn() {return eraserBtn;}
	public JButton getClearBtn() {return clearBtn;}
	public JButton getExitBtn() {return exitBtn;}
	public JSlider getSlider() {return slider;}
	public PaintCanvas getPaintCanvas() {return paintCanvas;}
	public JTextArea getChatLog() {return chatLog;}
	public JTextField getChatInput() {return chatInput;}
	public String getNickName() {return nickName;}
	public JDBCModel getModel() {return model;}
	

}