package login;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.JProgressBar;

import javazoom.jl.player.Player;

public class BGM implements Runnable{

	private String bgmPath;
	private int position;
	private Player player;
	private int playOrder;
	private Thread t;
	private int endOfSong;
	
	public Thread getT () {		return t;	}
	public void setT(Thread t) {		this.t = t;	}
	public String getBgmPath() {	return bgmPath;	}
	public void setBgmPath(String bgmPath) {	this.bgmPath = bgmPath;	}
	public int getPosition() {	return position;	}
	public void setPosition(int position) {	this.position = position;	}
	public Player getPlayer() {	return player;	}
	public void setPlayer(Player player) {	this.player = player;	}
	public int getPlayOrder() {	return playOrder;	}
	public void setPlayOrder(int playOrder) {	this.playOrder = playOrder;	}
	public int getEndOfSong() {	return endOfSong;	}

	public BGM(int song) {
		switch(song) {
			case 0: bgmPath = "Be Higher.mp3";endOfSong = 318;break;
			case 1: bgmPath = "Once In A Life Time.mp3"; endOfSong = 141;break;
			case 2: bgmPath = "There's Something About Super tank.mp3"; endOfSong = 198;break;
			case 3: bgmPath = "Radio.mp3"; endOfSong = 203;break;
		}
		
		t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}
	
	@Override
	public void run() {
		File bgm = new File(bgmPath);
		try { FileInputStream fis = new FileInputStream(bgm); 
			player = new Player(fis);
			player.play(position);
			player.play(); 
		} catch (Exception e) { 
			System.out.println(e); 
		}	
	}

}
