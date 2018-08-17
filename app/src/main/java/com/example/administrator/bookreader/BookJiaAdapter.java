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

public class BookJiaAdapter extends RecyclerView.Adapter<BookJiaAdapter.ViewHolder>{

    private Context mContext;

    private List<NetBook> bookList;

    public interface OnItemOnClickListener{

        void onItemLongOnClick(View view ,int pos);
    }
    public OnItemOnClickListener mOnItemOnClickListener;
    public void setOnItemClickListener(OnItemOnClickListener listener){
        this.mOnItemOnClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout bookView;
        ImageView bookCover;
        TextView bookTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            bookView = (LinearLayout) itemView.findViewById(R.id.book_jia_book_view);
            bookTitle = (TextView) itemView.findViewById(R.id.book_jia_book_title);
            bookCover = (ImageView) itemView.findViewById(R.id.book_jia_book_cover);
        }
    }







    public BookJiaAdapter(List<NetBook> chapterList1,Context context){
        mContext = context;
        bookList = chapterList1;
    }

    @NonNull
    @Override
    public BookJiaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_jia_item,parent,false);

        final BookJiaAdapter.ViewHolder holder = new BookJiaAdapter.ViewHolder(view);
        if(mOnItemOnClickListener!=null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = holder.getAdapterPosition();
                    mOnItemOnClickListener.onItemLongOnClick(holder.itemView,position);
                    return false;
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookJiaAdapter.ViewHolder holder, int position) {
        NetBook cpt = bookList.get(position);
        holder.bookTitle.setText(cpt.getTitle());
        Glide.with(mContext).load(cpt.getCover()).fitCenter().into(holder.bookCover);

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }




}




