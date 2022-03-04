package com.hngy.zp.neteasecloud;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter {
    List<Music> musicList;
    Context context;


    public MyAdapter(List<Music> musicList, Context context) {
        this.musicList = musicList;
        this.context = context;
    }

    public MyAdapter(List<Music> musicList) {
        this.musicList = musicList;
    }

    class ViewHoled extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewAuthor;
        View view;

        public ViewHoled(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.tv_author);
            textViewName = itemView.findViewById(R.id.tv_name);
            view = itemView;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item, parent, false);
        ViewHoled viewHoled = new ViewHoled(view);
        return viewHoled;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHoled holed1 = (ViewHoled) holder;
        Music music = musicList.get(position);
        String name = music.name;
        String author = music.getAuthor();
        holed1.textViewName.setText(name);
        holed1.textViewAuthor.setText(author);
        holed1.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MusicListItem.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("position", position);
//                原因：使用Activity的startActivity()没有限制，但是context的startActivity()就需要重新添加一个线程
//                解决方案：添加
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                标识开启一个新的线程
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size() == 0 ? 0 : musicList.size();
    }
}
