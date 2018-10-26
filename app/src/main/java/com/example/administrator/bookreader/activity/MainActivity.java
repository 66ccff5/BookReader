package com.example.administrator.bookreader.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.bookreader.bean.NetBook;
import com.example.administrator.bookreader.R;
import com.example.administrator.bookreader.adapter.BookAdapter;
import com.example.administrator.bookreader.adapter.BookJiaAdapter;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static BookJiaAdapter bookJIaAdapter;
    public static List<NetBook>  books;
    private RecyclerView bookJiaRecycler;
    private int position;


    private SwipeRefreshLayout recyclerFresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_books);
        setSupportActionBar(toolbar);
        books = DataSupport.findAll(NetBook.class);
        bookJiaRecycler = (RecyclerView) findViewById(R.id.book_jia_recycler);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        bookJiaRecycler.setLayoutManager(layoutManager);
        bookJIaAdapter = new BookJiaAdapter(books,this);
        bookJiaRecycler.setAdapter(bookJIaAdapter);

        recyclerFresh = (SwipeRefreshLayout) findViewById(R.id.main_recycler_reflesh);
        recyclerFresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerFresh.setRefreshing(false);
            }
        });

        bookJIaAdapter.setOnItemClickListener(new BookJiaAdapter.OnItemOnClickListener() {

            @Override
            public void onItemLongOnClick(View view, int pos) {
                    showPopMenu(view, pos);
            }
        });

    }

    public void showPopMenu(View view,final int pos){
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.book_jia_item,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dowloadItem:
                        Toast.makeText(getApplicationContext(),"下载",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.removeItem:
                        DataSupport.delete(NetBook.class,books.get(pos).getId());
                        books.remove(pos);
                        bookJIaAdapter.notifyDataSetChanged();
                        break;

                }
                return false;
            }
        });
        popupMenu.show();
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
//        MenuItem searchItem = menu.findItem(R.id.menu_search);
//        //通过MenuItem得到SearchView
//        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return true;
    }

    public void onMainItemClick(View view) {
        int childAdapterPosition = bookJiaRecycler.getChildAdapterPosition(view);
        BookAdapter.setNowBook(books.get(childAdapterPosition));
        Intent intent = new Intent(this,ReaderActivity.class);
        startActivity(intent);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_search:
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }



}
