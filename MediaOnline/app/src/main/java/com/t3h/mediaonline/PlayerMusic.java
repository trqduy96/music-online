package com.t3h.mediaonline;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class PlayerMusic{

    private String path;
    private MediaPlayer mediaPlayer;

    public PlayerMusic() {
    }

    public boolean inits(Context context, String path) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(path);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            mediaPlayer = null;
            return false;
        }
    }

    public boolean prepare(MediaPlayer.OnCompletionListener listener) {
        if (mediaPlayer == null) {
            return false;
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(listener);
        return true;
    }

    public boolean play() {
        if (mediaPlayer == null) {
            return false;
        }
        mediaPlayer.start();
        return true;
    }

    public boolean pause() {
        if (mediaPlayer == null) {
            return false;
        }
        mediaPlayer.pause();
        return true;
    }

    public boolean stop() {
        if (mediaPlayer == null) {
            return false;
        }
        mediaPlayer.stop();
        return true;
    }

    public void release() {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public boolean seek(int currentSeek) {
        if (mediaPlayer == null) {
            return false;
        }
        mediaPlayer.seekTo(currentSeek);
        return true;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }


//    @Override
//    public void onPrepared(MediaPlayer mp) {
//
//        mp.start();
//    }
}
