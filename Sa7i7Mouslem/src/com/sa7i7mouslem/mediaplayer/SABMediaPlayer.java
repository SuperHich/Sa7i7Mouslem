package com.sa7i7mouslem.mediaplayer;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;

import com.sa7i7mouslem.R;

/**
 * AlMoufasserAlSaghir
 * @author HICHEM LAROUSSI - RAMI TRABELSI
 * Copyright (c) 2014 Zad Group. All rights reserved.
 */

public class SABMediaPlayer {

	public static MediaPlayer m ;
	private Context context;
	private MediaPlayer player;
	private IMediaPlayerNotifier notifier;
	public enum MediaType{STREAM, LOCAL};
	private MediaType currentMediaType = MediaType.LOCAL;
	
	public SABMediaPlayer(Context context) {
		this.context = context;
	}
	
	public SABMediaPlayer(Context context, IMediaPlayerNotifier notifier) {
		this.context = context;
		this.notifier = notifier;
		
	}
	
	public boolean isPlaying(){
		
		if (player != null) 
			return player.isPlaying();
		
		return false ;
	}
	
	public void pause(){
		if (player != null){
			player.pause();
		}
	}
	
	public void resume(){
		if (player != null){
			player.start();
		}
	}
	
	public void stop(){
		if (player != null){
			player.stop();
			player.release();
			player = null ;
		}
		
		if(mProgressThread != null)
			mProgressThread.interrupt();
		
	}
	
	public void playWithCompletion(String audio){
		try{
			stop();

			AssetFileDescriptor afd = context.getAssets().openFd(audio);

			player = new MediaPlayer();
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

			player.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {

					notifier.onCompletion();

				}
			});

			player.prepare();
			player.start();

		}catch(IOException ex) {
			ex.printStackTrace();
		}
		catch(IllegalStateException ex) {
			ex.printStackTrace();
		}
	}
	
	public void playFromSdcardWithCompletion(String audio){
		try{
			stop();

			player = new MediaPlayer();
			player.setDataSource(audio);

			player.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {

					notifier.onCompletion();
					stop();

				}
			});

			player.prepare();
			
			notifier.onConfigProgress(player.getDuration());
			player.start();
			
			mProgressThread = new ProgressThread();
			mProgressThread.start();

		}catch(IOException ex) {
			ex.printStackTrace();
		}
		catch(IllegalStateException ex) {
			ex.printStackTrace();
		}
	}
	
	
	public void play(String audio) {
		try{
			stop();

			AssetFileDescriptor afd = context.getAssets().openFd(audio);

			player = new MediaPlayer();
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			player.prepare();
			player.start();
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		catch(IllegalStateException ex) {
			ex.printStackTrace();
		}
	}
	
	public void playFromSdcard(String audio) {
		try{
			stop();

			player = new MediaPlayer();
			player.setDataSource(audio);
			player.prepare();
			player.start();

		}catch(IOException ex) {
			ex.printStackTrace();
		}
		catch(IllegalStateException ex) {
			ex.printStackTrace();
		}
	}
	
	AsyncTask<String, Void, Boolean> playerAsync;
	ProgressDialog pd;
	
	public void playFromUrlWithCompletion(String streamAudio){
		
		playerAsync = new AsyncTask<String, Void, Boolean>() {

			protected void onPreExecute() {

				stop();
				
				pd = new ProgressDialog(context);
				pd.setCancelable(false);
				pd.setMessage(context.getString(R.string.preparing_audio));
				pd.show();

			};

			@Override
			protected Boolean doInBackground(String... params) {

				String stream = params[0];
				/**
				 * If there is more than 1 link to stream, we should initialize the MediaPlayer
				 */
				if(stream != null)
				{
					if(stream.length() != 0){
						return initMediaPlayer(stream);
					}
				}
				
				return false;

			}

			@Override
			protected void onPostExecute(Boolean result) {
				pd.dismiss();
				
				if(result){
					/**
					 * Starting MediaPlayer...
					 */
					notifier.onConfigProgress(player.getDuration());
					player.start();
					
					mProgressThread = new ProgressThread();
					mProgressThread.start();
					
				}
				else{
					/**
					 * Killing MediaPlayer...
					 */
					if(!isCancelled())
					{
						stop();		
					}
					
					notifier.onErrorPlayer();

				}

				cancel(true);
			}

		}.execute(streamAudio);
	}
	
	private boolean initMediaPlayer(String streamAudio){
		try{
			stop();

			player = MediaPlayer.create(context, Uri.parse(streamAudio));

			player.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {

					notifier.onCompletion();
					stop();

				}
			});
			player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
				
				@Override
				public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
					// TODO Auto-generated method stub
				}
			});
			player.setOnInfoListener(new OnInfoListener() {
				
				@Override
				public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			player.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			player.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
					stop();
					return false;
				}
			});

		} catch(IllegalStateException ex) {
			ex.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	ProgressThread mProgressThread;
	class ProgressThread extends Thread{
		@Override
		public void run() {
			while(true && !isInterrupted()){
				try {
					notifier.onProgressPlayer(player.getCurrentPosition());
					Thread.sleep(1000);
				}catch(Exception e){}
			}
		}
	}
}
