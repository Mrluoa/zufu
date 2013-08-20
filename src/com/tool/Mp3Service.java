package com.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * 
 * @description 
 * @version 1.0
 * @author Mrluo
 * @date 2013-8-1 ����4:10:29
 */
public class Mp3Service extends Service {
	private static final String TAG = "PlayMusicSevice";
	private String mppath = "zufu";
	private String pathName = "";
	private String mp3_path = "";
	private HttpDownloader httpDownloadUtil = new HttpDownloader();

	/**
	 * �����ʾ
	 */
	public static final String PLAY_MUCIC_SERVICE = "wyd.network.music.play.sevice";

	/**
	 * mp3��������·��
	 */
	private String mp3 = null;
	/**
	 * ���ֲ��Ŷ���
	 */
	private static MediaPlayer mediaPlayer;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "���ǵ�һ�������õķ���");

	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int state = (Integer) intent.getExtras().get("state");
		int type = (Integer) intent.getExtras().get("type");
		String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/";
		
		pathName = type+".mp3";
		mp3_path = SDPath+mppath+"/"+pathName;
		System.out.println("·��:"+mp3_path);
		File file = new File(mp3_path);
		
		switch (state) {
		// ��������
		case 0:
			mediaPlayer = new MediaPlayer();
			try {
				System.out.println("���ֵ�ַ<====>"+mp3);
				FileInputStream fis = new FileInputStream(file); 
				mediaPlayer.setDataSource(fis.getFD());
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (!mediaPlayer.isPlaying()) {
				mediaPlayer.start();
				System.out.println("���ſ�ʼ");
			}else{
				mediaPlayer.stop();
			}
			break;
		// ֹͣ����
		case 1:
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			break;
		//��ͣ
		case 2:
			if(mediaPlayer.isPlaying()){
				mediaPlayer.pause();
			}else{
				mediaPlayer.start();
			}
		default:
			break;
		}

		// START_STICKY �������������������÷���
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
	}
	/**
	 * 
	 * @description ��ͣ����
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-1 ����11:48:59
	 * @return void
	 * @throws
	 */
	public static void setPase(){
		if(mediaPlayer.isPlaying()){
			mediaPlayer.pause();
		}else{
			mediaPlayer.start();
		}
	}
	/**
	 * 
	 * @description ����״̬
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-1 ����11:49:12
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean getIsPlaying(){
		return mediaPlayer.isPlaying();
	}
	/**
	 * 
	 * @description ��ȡ�������ֵ��ܳ���
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-1 ����11:52:04
	 * @return
	 * @return int
	 * @throws
	 */
	public static int getDuration(){
		return mediaPlayer.getDuration();
	}
	/**
	 * 
	 * @description ��ȡ��ǰ���Ž���
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-1 ����11:52:51
	 * @return
	 * @return int
	 * @throws
	 */
	public static int getCurrentPosition(){
		return mediaPlayer.getCurrentPosition();
	}
	
}
