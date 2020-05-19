package login;

import java.awt.Choice;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import waiting.Waiting;

public class Login implements KeyListener{
	private JLabel titleLabel, idLabel, pwLabel, musicLabel, startLabel, leftLabel;
	private JTextField inputID;
	private JPasswordField inputPW;
	private JButton loginBtn, signUpBtn, lostIDBtn, lostPWBtn, updateBtn; 
	private JFrame frame;
	private JButton play;
	private JProgressBar bar;
	private Thread barT;
	private BGM bgm;
	private int barMove;
	private JDBCModel model;
	private JTextField inputChat;
	private JTextArea chatLog;
	private JButton exitBtn;
	private LoginChat loginChat;
	private String tempNickName;


	public Login() {
		
		model = new JDBCModel();
		
		frame = new JFrame("Catch Mind");
		
		//---------------------titlePanel----------------------------
		JPanel titlePanel = new JPanel();
		titlePanel.setBounds(350, 200, 300, 50);
		titlePanel.setLayout(new GridLayout(1,1));
		titleLabel = new JLabel("Catch Mind");
		titleLabel.setFont(new Font("굴림", Font.BOLD, 50));
		titlePanel.add(titleLabel);
		
		
		//-------------------------frame design----------------------------
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(titlePanel);
		lostIDBtn = new JButton("이메일 찾기");
		lostIDBtn.setBounds(313, 556, 180, 50);
		frame.getContentPane().add(lostIDBtn);
		loginBtn = new JButton("로그인");
		loginBtn.setFocusable(false);
		loginBtn.setBounds(618, 346, 96, 90);
		frame.getContentPane().add(loginBtn);
		signUpBtn = new JButton("회원가입");
		signUpBtn.setBounds(313, 496, 180, 50);
		frame.getContentPane().add(signUpBtn);
		lostPWBtn = new JButton("비밀번호 찾기");
		lostPWBtn.setBounds(505, 556, 180, 50);
		frame.getContentPane().add(lostPWBtn);
		idLabel = new JLabel("이메일 주소");
		idLabel.setBounds(287, 346, 135, 40);
		frame.getContentPane().add(idLabel);
		idLabel.setFont(new Font("굴림", Font.BOLD, 15));
		pwLabel = new JLabel("\t\t\t비밀번호");
		pwLabel.setBounds(287, 396, 120, 40);
		frame.getContentPane().add(pwLabel);
		pwLabel.setFont(new Font("굴림", Font.BOLD, 15));
		inputPW = new JPasswordField();
		inputPW.setBounds(406, 396, 200, 40);
		frame.getContentPane().add(inputPW);
		inputPW.setFont(new Font("굴림", Font.BOLD, 15));
		inputID = new JTextField();
		inputID.setBounds(406, 346, 200, 40);
		frame.getContentPane().add(inputID);
		inputID.setFont(new Font("굴림", Font.BOLD, 15));
		updateBtn = new JButton("회원정보수정");
		updateBtn.setBounds(505, 496, 180, 50);
		frame.getContentPane().add(updateBtn);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		frame.setSize(1024,768);
		frame.setLocation(300,50);

		
		//----------------------bgm-----------------------------
		String[] bgmList = {"Be Higher", "Once In A Life Time", "There's something about Super Tank", "Radio"};
		Choice choice = new Choice();
		for(int i = 0; i < bgmList.length; i++ ) {
			choice.add(bgmList[i]);
		}
		choice.setBounds(29, 26, 315, 21);
		frame.getContentPane().add(choice);
		
		play = new JButton("Play");
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!(bgm==null)) {
					bgm.getPlayer().close();
				}
				if(!(barT==null)) {
					barT.interrupt();
				}
				barMove = 1;
				bgm = new BGM(choice.getSelectedIndex());
				musicLabel.setText(choice.getSelectedItem());
				bar = new JProgressBar(0, bgm.getEndOfSong());
				
				frame.getContentPane().add(bar);
				bar.setBounds(29, 10, 315, 14);
				
				
				startLabel.setText("0");
				leftLabel.setText(bgm.getEndOfSong()+"");
				barT = new Thread(new Runnable() {
					@Override
					public void run() {
						int i = 0;
						while(true) {
							bar.setValue(i);
							i ++;
							startLabel.setText(i+"");
							leftLabel.setText(bgm.getEndOfSong()-i+"");
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								break;
							}
						}
					}
				});
				barT.setDaemon(true);
				barT.start();
			}
		});
		play.setBounds(192, 53, 69, 40);
		frame.getContentPane().add(play);
		
		JButton stop = new JButton("Stop");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				barT.interrupt();
				startLabel.setText("");
				leftLabel.setText("");
				bgm.getPlayer().close();
				bar.setValue(0);
				bar.setVisible(false);
			}
		});
		stop.setBounds(273, 53, 71, 40);
		frame.getContentPane().add(stop);
		
		inputChat = new JTextField();
		inputChat.setBounds(12, 673, 248, 21);
		frame.getContentPane().add(inputChat);
		stop.addKeyListener(this);
		chatLog = new JTextArea();
		chatLog.setEditable(false);
		chatLog.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(chatLog);
		scrollPane.setBounds(12, 416, 248, 252);
		frame.getContentPane().add(scrollPane);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		exitBtn = new JButton("게임 종료");
		exitBtn.setBounds(865, 21, 129, 40);
		frame.getContentPane().add(exitBtn);
		
		musicLabel = new JLabel("Now Playing");
		musicLabel.setFont(new Font("굴림", Font.BOLD, 12));
		musicLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		musicLabel.setBounds(-1, 60, 191, 32);
		frame.getContentPane().add(musicLabel);
		
		startLabel = new JLabel("sec");
		startLabel.setBounds(10, 8, 31, 21);
		frame.getContentPane().add(startLabel);
		
		leftLabel = new JLabel("leftsec");
		leftLabel.setBounds(350, 10, 31, 21);
		frame.getContentPane().add(leftLabel);
		
		
		//------------------------Login Chat-------------------------
		tempNickName = JOptionPane.showInputDialog(frame, "채팅용 닉네임을 입력해주세요");
		if(tempNickName == null || tempNickName.length() == 0) {
			tempNickName = "guest";
		}
		loginChat = new LoginChat(tempNickName, Login.this);
		
		//------------------------button Actions---------------------
		
		//X 눌렀을 때
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		
		//게임종료 눌렀을 때
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		
		//비번치고 엔터 눌렀을 떄
		inputPW.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginVerifier(inputID.getText(), new String(inputPW.getPassword()));
			}
		});
		
		//로그인 클릭했을 때
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginVerifier(inputID.getText(), new String(inputPW.getPassword()));
			}
		});
		
		//회원가입 눌렀을 때
		signUpBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnDisabler();
				new SignUp(Login.this);
			}
		});
		
		//회원정보수정 눌렀을 때
		updateBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDisabler();
				new UpdateLogin(Login.this);
			}
		});
		
		//이메일 찾기 눌렀을 때
		lostIDBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnDisabler();
				new LostEmail(Login.this);
			}
		});
		
		//비번 찾기 눌렀을 때
		lostPWBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnDisabler();
				new LostPW(Login.this);
			}
		});
		
		//-----------------------test id & Key Listener----------------------------

		inputID.addKeyListener(this);
		inputPW.addKeyListener(this);
		loginBtn.addKeyListener(this);
		signUpBtn.addKeyListener(this);
		updateBtn.addKeyListener(this);
		lostIDBtn.addKeyListener(this);
		lostPWBtn.addKeyListener(this);
		play.addKeyListener(this);
		stop.addKeyListener(this);
		choice.addKeyListener(this);
		inputChat.addKeyListener(this);
	}
	//practiceLogin Constructor
	
	//--------------------------getter & setter------------------------------
	
	public JTextField getInputID() {	return inputID;	}
	public void setInputID(JTextField inputID) {	this.inputID = inputID;	}
	public JPasswordField getInputPW() {	return inputPW;	}
	public void setInputPW(JPasswordField inputPW) {	this.inputPW = inputPW;	}
	public JButton getLoginBtn() {	return loginBtn;	}
	public void setLoginBtn(JButton loginBtn) {	this.loginBtn = loginBtn;	}
	public JButton getSignUpBtn() {	return signUpBtn;	}
	public void setSignUpBtn(JButton signUpBtn) {	this.signUpBtn = signUpBtn;	}
	public JButton getLostIDBtn() {	return lostIDBtn;	}
	public void setLostIDBtn(JButton lostIDBtn) {	this.lostIDBtn = lostIDBtn;	}
	public JButton getLostPWBtn() {	return lostPWBtn;	}
	public void setLostPWBtn(JButton lostPWBtn) {	this.lostPWBtn = lostPWBtn;	}
	public JButton getUpdateBtn() {	return updateBtn;	}
	public void setUpdateBtn(JButton updateBtn) {	this.updateBtn = updateBtn;	}
	public JFrame getFrame() {	return frame;	}
	public void setFrame(JFrame frame) {	this.frame = frame;	}
	public JDBCModel getModel() {	return model;	}
	public void setModel(JDBCModel model) {	this.model = model;	}
	public JTextArea getChatLog() {	return chatLog;	}
	public void setChatLog(JTextArea chatLog) {	this.chatLog = chatLog;	}
	public JTextField getInputChat() {	return inputChat;	}
	public void setInputChat(JTextField inputChat) {	this.inputChat = inputChat;	}
	public String getTempNickName() {return this.tempNickName;}
	//--------------------------End of getter & setter------------------------------
	
	
	@Override
	public void keyPressed(KeyEvent e) {	}
	@Override
	public void keyReleased(KeyEvent e) {	}
	@Override
	public void keyTyped(KeyEvent e) {	if(e.getKeyChar()==27) {exit();}	}
	
	//X눌렀을 때와 ESC눌렀을 때 작동할 종료 확인창
	public void exit() {
		int sel = JOptionPane.showConfirmDialog(frame, "레알루 종료?");
		if(sel == JOptionPane.YES_OPTION) {
			loginChat.exit();
			System.exit(0);
		}
	}
	
	//로그인 버튼과 비번입력창에서 작동할 메소드
	public void loginVerifier(String email, String pw) {
		
		
		//--------------------공백 없음 검증------------------------------
		//이메일 공백
		if(email == null || email.length() == 0) {
			JOptionPane.showMessageDialog(frame, "이메일 주소를 입력하세요");
			return;
		}
		//비밀번호 공백
		if(pw == null || pw.length() == 0) {
			JOptionPane.showMessageDialog(frame, "비밀번호를 입력하세요");
			return;
		}
		
		//--------------------로그인 검증------------------------------
		//1. 없는 계정
		if (!model.hasEmail(email)) {
			JOptionPane.showMessageDialog(frame, "존재하지 않는 이메일주소 입니다");
			return;
		}
		//2. 있는 계정 + 틀린 비번
		if (!model.accountVerifier(email, pw)) {
			JOptionPane.showMessageDialog(frame, "비밀번호가 다릅니다");
			return;
		}
		//3. 있는 계정 + 맞는 비번 + 이미접속중
		if (!model.isNotOnline(email)) {
			JOptionPane.showMessageDialog(frame, "이미 접속중인 계정입니다");
			return;
		}
		//4. 임시 비밀번호로 로그인
		if (model.isTempPW(email)) {
			new TempPW(email, Login.this);
			return;
		}
		
		//5. 로그인 성공
		JOptionPane.showMessageDialog(frame, "로그인 성공");
		new Waiting(frame, inputID.getText(), model);
		inputID.setText("");
		inputPW.setText("");
		loginChat.exit();
		frame.dispose();
		
		//model.setOnline(email);
	}
	
	
	//로그인 이외 버튼 눌렀을 때 다른 버튼 비활성화 메소드
	public void btnDisabler() {
		inputID.setEnabled(false);
		inputPW.setEnabled(false);
		loginBtn.setEnabled(false);
		signUpBtn.setEnabled(false);
		lostIDBtn.setEnabled(false);
		lostPWBtn.setEnabled(false);
		updateBtn.setEnabled(false);
	}
	//로그인 이외 버튼 누른 후 로그인 창으로 돌아왔을 때 버튼 활성화 메소드
	public void backToLogin(JFrame frame, Login login) {
		frame.setVisible(false);
		login.getInputID().setEnabled(true);
		login.getInputPW().setEnabled(true);
		login.getLoginBtn().setEnabled(true);
		login.getSignUpBtn().setEnabled(true);
		login.getUpdateBtn().setEnabled(true);
		login.getLostPWBtn().setEnabled(true);
		login.getLostIDBtn().setEnabled(true);
	}
	
	
	
	public static void main(String[] args) {
		new Login();
	}
	
	
}
//practiceLogin class






