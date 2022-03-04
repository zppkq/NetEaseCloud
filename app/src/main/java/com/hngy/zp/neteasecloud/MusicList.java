package com.hngy.zp.neteasecloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MusicList extends AppCompatActivity {
    List<Music> list = null;
    RecyclerView recyclerView;
    String name[] = {"DEAR JOHN ", "执迷不悟", "我很好（吉他版）", "天外来物", "踏山河", "他只是经过", "是想你的声音啊", "三号线（吉他版)", "努力工作吧", "茫", "经济舱 (Live)"
            , "会不会（吉他版）", "花.间.酒", "耗尽", "海底", "还是分开", "过", "沉醉的青丝 (想你 念你)", "把你揉碎捏成苹果", "阿拉斯加海湾"
    };
    String author[] = {"比莉", "鱿籽酱", "刘大壮", "薛之谦", "是七叔呢", "h3R3_Felix Bennett", "傲七爷", "刘大壮", "赛文&GOD", "李润祺", "Kafe.Hu"
            , "刘大壮", "澄海伯伯_超级橘子Orange_River", "薛之谦_郭聪明", "一支榴莲", "张叶蕾", "王嘉尔_林俊杰", "你的大表哥曲甲", "薛之谦", "蓝心羽"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_music_list);
        init();
        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new Music(name[i], author[i]));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(list,getApplicationContext()));
    }

    private void init() {
        recyclerView = findViewById(R.id.rectangles);
    }
}