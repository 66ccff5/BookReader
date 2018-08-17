package com.example.administrator.bookreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    public static final String BOOK_SEARCH_API = "http://api.zhuishushenqi.com/book/fuzzy-search?query=";
    private SearchView bookSearch;
    private RecyclerView searchRecycler;
    private BookAdapter adapter;
    private final String TAG = "SerchActivity";
    private String searchResponse = null;
    private List<NetBook> netBookList = new ArrayList<>();

    int rawHeight;
    int rawWidth;
    float newHeight;
    float newWidth;
    float heightScale;
    float widthScale;
    Matrix matrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchRecycler = (RecyclerView) findViewById(R.id.search_recycler_view);
        bookSearch = (SearchView) findViewById(R.id.book_search_view);
        bookSearch.setIconified(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        searchRecycler.setLayoutManager(layoutManager);
        adapter = new BookAdapter(netBookList,this);
        searchRecycler.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.mine_own_item_devide));
        searchRecycler.addItemDecoration(dividerItemDecoration);
        initEvents();

    }


    private void initEvents(){

        bookSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String searchUrl = getRealUrl(s);
                new SearchBookTask(searchRecycler).execute(searchUrl);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        bookSearch.setOnCloseListener(new SearchView.OnCloseListener() {

            public boolean onClose() {
                searchRecycler.setAdapter(null);
                // netMusicList.clear();adapter.notifyDataSetChanged();
                return false;
            }
        });


    }

    private class SearchBookTask extends AsyncTask<String, Void, Void> {
        private RecyclerView bookList;

        public SearchBookTask(RecyclerView BookList) {
            this.bookList = BookList;

        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(String... params) {
            String url = params[0];
            HttpURLConnection conn = null;
            BufferedReader br = null;
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Response response1 = client.newCall(request).execute();
//                conn = (HttpURLConnection)new URL(url).openConnection();
//                conn.setRequestMethod("GET");
//                conn.setConnectTimeout(8000);
//                conn.setReadTimeout(8000);
//                //使用缓存提高处理效率
//                br = new BufferedReader(new InputStreamReader((InputStream) conn.getInputStream()));
//                String line = null;
//                StringBuilder sb = new StringBuilder();
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                }

                //网络响应赋值给成员变量searchResponse
                searchResponse = response1.body().string();
                parseResponse();
                Log.d(TAG, "searchResponse = " + searchResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //adapter数据更新后通知列表更新
            adapter.notifyDataSetChanged();
            bookList.setAdapter(adapter);
        }

        //json解析网络响应
        private void parseResponse() {
            try {
                JSONObject response = new JSONObject(searchResponse);

                JSONArray books = response.getJSONArray("books");
                if (netBookList.size() > 0) netBookList.clear();
                for (int i = 0; i < books.length(); i++) {
                    JSONObject book = books.getJSONObject(i);
                    String id = book.getString("_id");
                    String title = book.getString("title");
                    String lastChapter = book.getString("lastChapter");
                    String type = book.getString("cat");
                    String author = book.getString("author");
                    String shortIn = book.getString("shortIntro");
                    String coverUrl = book.getString("cover").replace("/agent/","");
                    String coverRealUrl = toURLDecoder(coverUrl);
                    coverUrl = toURLDecoder(coverRealUrl);


                    NetBook book1 = new NetBook();
                    book1.setTitle(title);
                    book1.setAuthor(author);
                    book1.setBookid(id);
                    book1.setLastChapter(lastChapter);
                    book1.setType(type);
                    book1.setShortIntr(shortIn);
                    book1.setCover(coverUrl);
                    netBookList.add(book1);
                }
                Log.d(TAG, "搜到" + netBookList.size() + "本书");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void onItemClick(View view) {
        int childAdapterPosition = searchRecycler.getChildAdapterPosition(view);
        BookAdapter.setNowBook(netBookList.get(childAdapterPosition));
        Intent intent = new Intent(this,BookActivity.class);
        startActivity(intent);

    }


    public  String toURLDecoder(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String url = new String(paramString.getBytes(), "UTF-8");
            url = URLDecoder.decode(url, "UTF-8");
            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getRealUrl(String query) {
        String key = null;
        try {
            key = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return BOOK_SEARCH_API + key;
    }



}
