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
 * @date 2013-8-1 下午4:10:29
 */
public class Mp3Service extends Service {
	private static final String TAG = "PlayMusicSevice";
	private String mppath = "zufu";
	private String pathName = "";
	private String mp3_path = "";
	private HttpDownloader httpDownloadUtil = new HttpDownloader();

	/**
	 * 服务标示
	 */
	public static final String PLAY_MUCIC_SERVICE = "wyd.network.music.play.sevice";

	/**
	 * mp3音乐下载路径
	 */
	private String mp3 = null;
	/**
	 * 音乐播放对象
	 */
	private static MediaPlayer mediaPlayer;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "我是第一个被调用的方法");

	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int state = (Integer) intent.getExtras().get("state");
		int type = (Integer) intent.getExtras().get("type");
		String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/";
		
		pathName = type+".mp3";
		mp3_path = SDPath+mppath+"/"+pathName;
		System.out.println("路径:"+mp3_path);
		File file = new File(mp3_path);
		
		switch (state) {
		// 播放音乐
		case 0:
			mediaPlayer = new MediaPlayer();
			try {
				System.out.println("音乐地址<====>"+mp3);
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
				System.out.println("播放开始");
			}else{
				mediaPlayer.stop();
			}
			break;
		// 停止音乐
		case 1:
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			break;
		//暂停
		case 2:
			if(mediaPlayer.isPlaying()){
				mediaPlayer.pause();
			}else{
				mediaPlayer.start();
			}
		default:
			break;
		}

		// START_STICKY 如果服务结束，将重启该服务
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
	 * @description 暂停播放
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-1 下午11:48:59
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
	 * @description 播放状态
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-1 下午11:49:12
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean getIsPlaying(){
		return mediaPlayer.isPlaying();
	}
	/**
	 * 
	 * @description 获取播放音乐的总长度
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-1 下午11:52:04
	 * @return
	 * @return int
	 * @throws
	 */
	public static int getDuration(){
		return mediaPlayer.getDuration();
	}
	/**
	 * 
	 * @description 获取当前播放进度
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-1 下午11:52:51
	 * @return
	 * @return int
	 * @throws
	 */
	public static int getCurrentPosition(){
		return mediaPlayer.getCurrentPosition();
	}
	
}
