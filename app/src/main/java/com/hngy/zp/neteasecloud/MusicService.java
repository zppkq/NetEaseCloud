package com.hngy.zp.neteasecloud;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.strictmode.Violation;

import java.io.IOException;
import java.util.Random;

public class MusicService extends Service {
    MediaPlayer mediaPlayer;
    boolean hasPrepare;
    int wz = 0;
    int musicResources[] = {R.raw.a, R.raw.b, R.raw.c, R.raw.d, R.raw.e, R.raw.f, R.raw.g, R.raw.h, R.raw.i, R.raw.j, R.raw.k, R.raw.l, R.raw.m, R.raw.o, R.raw.p, R.raw.q, R.raw.r, R.raw.s, R.raw.t, R.raw.u};

    @Override
    public void onCreate() {
        super.onCreate();

    }


    class MyBinder extends Binder {

        //播放音乐
        public void plays() {
            play();
        }

        public void wz(int position) {
            wz = position;
        }

        //暂停音乐
        public void pauses() {
            pause();
        }

        //停止音乐
        public void stops() {
            stop();
        }

        //上一首音乐
        public void tops() {
            top();
        }

        public void randoms() {
            random();
        }

        //下一首音乐音乐
        public void buttons() {
            button();
        }

        //音乐总长度
        public int lengths() {
            return length();
        }

        //音乐进度
        public int positions() {
            return position();
        }

        //准备播放
        public void prepares() {
            prepare();
        }
    }

    private void prepare() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mediaPlayer = MediaPlayer.create(this, musicResources[wz]);
        }
        hasPrepare = true;
    }

    private void pause() {
        determine();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            play();
        }
    }

    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            hasPrepare = false;
        }
    }

    private void top() {
        stop();
        wz = (wz + 1) % 20;
    }

    private void button() {
        stop();
        wz = (wz + 19) % 20;
    }

    private void random() {
        stop();
        wz = new Random().nextInt(20);
    }

    private int position() {
        determine();
        return mediaPlayer.getCurrentPosition();
    }

    private int length() {
        determine();
        return mediaPlayer.getDuration();
    }

    private void determine() {
        if (mediaPlayer == null || !hasPrepare) {
            prepare();
        }
    }

    private void play() {
        determine();
        mediaPlayer.start();
    }

    public MusicService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }
}