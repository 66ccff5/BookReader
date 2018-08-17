package com.example.administrator.bookreader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2018/7/3 0003.
 */

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ViewHolder>{
//    private Context viewContext;

    private List<Chapter> cptList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout chapterView;
        TextView ChapterTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            chapterView = (LinearLayout) itemView.findViewById(R.id.chapter_list_item_view);
            ChapterTitle = (TextView) itemView.findViewById(R.id.chapter_list_item);
        }
    }







    public ChapterListAdapter(List<Chapter> chapterList1){
//        viewContext = context;
        cptList = chapterList1;
    }

    @NonNull
    @Override
    public ChapterListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_item,parent,false);
        final ViewHolder holder = new ChapterListAdapter.ViewHolder(view);
        holder.chapterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Chapter chapter = ReaderActivity.chapters.get(position);
                if(ReaderActivity.activity != null){
                    ReaderActivity.activity.readTheChapter(position,chapter.getLink());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterListAdapter.ViewHolder holder, int position) {
        Chapter cpt = cptList.get(position);
        holder.ChapterTitle.setText(cpt.getTitle());
    }

    @Override
    public int getItemCount() {
        return cptList.size();
    }




}
