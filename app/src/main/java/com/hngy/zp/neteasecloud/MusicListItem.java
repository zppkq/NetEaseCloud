package com.hngy.zp.neteasecloud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Formatter;
import java.util.Locale;

import static java.lang.Thread.interrupted;

public class MusicListItem extends AppCompatActivity {
    int position = 0;
    String TAG = "MusicListItem";
    ImageButton btnPlay, btnButton, btnTop, btnTheOrder, btnShare;
    MusicService.MyBinder binder;
    SeekBar seekBar;
    Thread thread;
    TextView start, end;
    boolean isPlay = true, order = false;
    int length = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (MusicService.MyBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_music_list_item);
        init();
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        bindService(new Intent(MusicListItem.this, MusicService.class), connection, BIND_AUTO_CREATE);
        Log.d(TAG, String.valueOf(position));
        new CountDownTimer(500, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                binder.wz(position);
                binder.plays();
                length = binder.lengths();
                String len = stringForTime(length);
                end.setText(len);
                seekBar.setMax(length);
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!interrupted()) {
                            int positions = binder.positions();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    seekBar.setProgress(positions);
                                    start.setText(stringForTime(positions));
                                    if (length == positions) {
                                        btnButton.performClick();
                                    }
                                }
                            });
                        }
                    }
                });
                thread.start();
            }
        }.start();
    }

    private void init() {
        btnPlay = findViewById(R.id.btn_play);
        start = findViewById(R.id.start_length);
        end = findViewById(R.id.end_length);
        seekBar = findViewById(R.id.seekBar);
        btnButton = findViewById(R.id.btn_button);
        btnTop = findViewById(R.id.btn_top);
        btnTheOrder = findViewById(R.id.btn_the_order);
        btnShare = findViewById(R.id.btn_share);
    }

    @Override
    protected void onDestroy() {
        // 如果线程没有退出,则退出
        if (thread != null) {
            if (!thread.isInterrupted())
                thread.interrupt();
        }
        unbindService(connection);
        binder.stops();
        super.onDestroy();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play:
                if (isPlay) {
                    isPlay = false;
                    btnPlay.setBackground(getResources().getDrawable(R.drawable.play));
                } else {
                    isPlay = true;
                    btnPlay.setBackground(getResources().getDrawable(R.drawable.pause1));
                }
                binder.pauses();
                break;
            case R.id.btn_top:
                thread.interrupt();
                binder.tops();
                change();
                break;
            case R.id.btn_button:
                thread.interrupt();
                if (order) {
                    binder.randoms();
                } else {
                    binder.buttons();
                }
                change();
                break;
            case R.id.btn_the_order:
                if (order) {
                    Toast.makeText(this, "顺序播放", Toast.LENGTH_SHORT).show();
                    btnTheOrder.setBackground(getResources().getDrawable(R.drawable.cycle));
                    order = false;
                } else {
                    Toast.makeText(this, "随机播放", Toast.LENGTH_SHORT).show();
                    btnTheOrder.setBackground(getResources().getDrawable(R.drawable.random));
                    order = true;
                }
                break;
            case R.id.btn_share:
                Toast.makeText(this, "QQ分享", Toast.LENGTH_SHORT).show();
                shareMusic();
            default:
                break;

        }
    }

    //分享音乐，这里只是可以分享到qq
    public void shareMusic() {

//        String path = "android.resource://"+getPackageName()+"/"+R.raw.e;
//        Uri uri = Uri.parse(path);
//        Intent share = new Intent(Intent.ACTION_SEND);
//        //创建组件，
//        ComponentName component = new ComponentName("com.tencent.mobileqq","com.tencent.mobileqq.activity.JumpActivity");
//        share.setComponent(component);		//设置组件
//        share.putExtra(Intent.EXTRA_STREAM, uri);
//        share.putExtra(Intent.EXTRA_SUBJECT,"QQ音乐");
//        share.setType("*/*");
//        startActivity(Intent.createChooser(share, "分享到"));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "QQ音乐");
        intent.putExtra(Intent.EXTRA_TEXT, "平凡之路");
        intent = Intent.createChooser(intent, "分享");
        startActivity(intent);

    }

    public void change() {
        isPlay = false;
        btnPlay.performClick();
        length = binder.lengths();
        seekBar.setMax(length);
        String len = stringForTime(length);
        end.setText(len);
        run();
    }

    public void run() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!interrupted()) {
                    int positions = binder.positions();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(positions);
                            start.setText(stringForTime(positions));
                            if (length == positions) {
                                btnButton.performClick();
                            }
                        }
                    });
                }
            }
        });
        thread.start();
    }

    //时间转化
    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            //超过60分钟的，显示出小时
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            //不足60分钟的，只显示分和秒
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

}