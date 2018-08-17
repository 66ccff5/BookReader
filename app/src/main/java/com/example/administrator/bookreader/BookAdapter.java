package com.example.administrator.bookreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2018/6/28 0028.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private Context viewContext;

    private static NetBook nowBook = null;

    private static int chapterNum;

    private static int pageNum;

    public static int getChapterNum(){
        return chapterNum;
    }

    public static void setChapterNum(int i){
        chapterNum = i;
    }

    public static int getPageNum(){
        return pageNum;
    }

    public static void setPageNum(int i){
        pageNum = i;
    }


    private List<NetBook> bookList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView bookCover;
        TextView title;
        TextView author;
        TextView shortin;

        public ViewHolder(View itemView) {
            super(itemView);
            bookCover = (ImageView) itemView.findViewById(R.id.search_cover);
            title = (TextView) itemView.findViewById(R.id.search_title);
            author = (TextView) itemView.findViewById(R.id.search_author);
            shortin = (TextView) itemView.findViewById(R.id.search_short);
        }
    }

    public static void setNowBook(NetBook book){
        nowBook = book;
    }

    public static NetBook getReadingBook(){
        return nowBook;
    }





    public BookAdapter(List<NetBook> bookList1, Context context){
        viewContext = context;
        bookList = bookList1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NetBook book = bookList.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.shortin.setText(book.getShortIntr());
        Glide.with(viewContext).load(book.getCover()).fitCenter().into(holder.bookCover);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }




}
