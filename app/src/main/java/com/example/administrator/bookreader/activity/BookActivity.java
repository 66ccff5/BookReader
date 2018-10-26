package com.example.administrator.bookreader.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.bookreader.bean.NetBook;
import com.example.administrator.bookreader.R;
import com.example.administrator.bookreader.adapter.BookAdapter;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.litepal.crud.DataSupport;

import java.util.List;


public class BookActivity extends AppCompatActivity {

    private ImageView bookCover;
    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView lastChapter;
    private TextView bookType;
    private ExpandableTextView shortIn;
    private TextView dowload;
    private TextView reader;
    private  TextView getToBox;
    boolean isAdd = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_book_every);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });
        initViews();
        initDates();
        initEvents();


    }

    private void initViews(){
        bookCover = (ImageView) findViewById(R.id.book_cover);
        bookAuthor = (TextView) findViewById(R.id.book_author);
        bookTitle = (TextView) findViewById(R.id.book_title);
        lastChapter = (TextView) findViewById(R.id.last_chapter);
        bookType = (TextView) findViewById(R.id.book_type);
        shortIn = (ExpandableTextView) findViewById(R.id.expand_text_view);
        reader = (TextView) findViewById(R.id.book_reader);
        getToBox = (TextView) findViewById(R.id.get_to_box);
    }

    private void initDates(){
        Glide.with(this).load(BookAdapter.getReadingBook().getCover()).fitCenter().into(bookCover);
        bookAuthor.setText("作者：" + BookAdapter.getReadingBook().getAuthor());
        bookTitle.setText(BookAdapter.getReadingBook().getTitle());
        lastChapter.setText("最新：" + BookAdapter.getReadingBook().getLastChapter());
        bookType.setText("类型：" + BookAdapter.getReadingBook().getType());
        shortIn.setText(BookAdapter.getReadingBook().getShortIntr());

    }

    private void initEvents(){
        reader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<NetBook> books = DataSupport.findAll(NetBook.class);
                NetBook netBook = null;
                for (int i=0;i<books.size();i++){
                    if(BookAdapter.getReadingBook().getBookid().equals(books.get(i).getBookid())){
                        isAdd = true;
                        netBook = books.get(i);
                    }
                }
                if(isAdd) {
                    BookAdapter.setNowBook(netBook);
                }
//                }else {
//                    NetBook book = new NetBook();
//                    book.setTitle(BookAdapter.getReadingBook().getTitle());
//                    book.setBookid(BookAdapter.getReadingBook().getBookid());
//                    book.setAuthor(BookAdapter.getReadingBook().getAuthor());
//                    book.setCover(BookAdapter.getReadingBook().getCover());
//                    book.setShortIntr(BookAdapter.getReadingBook().getShortIntr());
//                    book.setLastChapter(BookAdapter.getReadingBook().getLastChapter());
//                    book.setType(BookAdapter.getReadingBook().getType());
//                    book.setChapterNum(0);
//                    book.setPageNum(0);
//                    book.setIsAdd(1);
//                    book.save();
//                    BookAdapter.setNowBook(book);
//                    MainActivity.books.add(book);
//                    MainActivity.bookJIaAdapter.notifyDataSetChanged();
//                    Toast.makeText(this,"加入成功",Toast.LENGTH_SHORT).show();
//                }

                Intent intent = new Intent(BookActivity.this,ReaderActivity.class);
                startActivity(intent);
            }
        });

        getToBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addToBookJIa();
            }
        });

    }

    private void addToBookJIa(){
        List<NetBook> books = DataSupport.findAll(NetBook.class);
        NetBook netBook = null;
        for (int i=0;i<books.size();i++){
            if(BookAdapter.getReadingBook().getBookid().equals(books.get(i).getBookid())){
                isAdd = true;
                netBook = books.get(i);
            }
        }
        if(isAdd){
            Toast.makeText(this,"已加入过书架",Toast.LENGTH_SHORT).show();
        }else {
            NetBook book = new NetBook();
            book.setTitle(BookAdapter.getReadingBook().getTitle());
            book.setBookid(BookAdapter.getReadingBook().getBookid());
            book.setAuthor(BookAdapter.getReadingBook().getAuthor());
            book.setCover(BookAdapter.getReadingBook().getCover());
            book.setShortIntr(BookAdapter.getReadingBook().getShortIntr());
            book.setLastChapter(BookAdapter.getReadingBook().getLastChapter());
            book.setType(BookAdapter.getReadingBook().getType());
            book.setChapterNum(0);
            book.setPageNum(0);
            book.setIsAdd(1);
            BookAdapter.setNowBook(book);
            book.save();
            MainActivity.books.add(book);
            MainActivity.bookJIaAdapter.notifyDataSetChanged();
            Toast.makeText(this,"加入成功",Toast.LENGTH_SHORT).show();
        }






    }





}
