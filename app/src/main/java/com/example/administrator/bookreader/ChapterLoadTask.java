package com.example.administrator.bookreader;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/3 0003.
 */

public class ChapterLoadTask extends AsyncTask<String, Void, Void>{

    String searchResponse;
    public static final String CHAPTER_LINK_API = "http://chapter2.zhuishushenqi.com/chapter/http:%2F%2Fbook.my716.com%2FgetBooks.aspx%3F";

        public ChapterLoadTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(String... params) {
            String url = params[0];
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Response response1 = client.newCall(request).execute();

                //网络响应赋值给成员变量searchResponse
                searchResponse = response1.body().string();
                parseResponse();
//                Log.d(TAG, "searchResponse = " + searchResponse);
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
            if(ReaderActivity.chapterAdapter == null){
                ReaderActivity.chapterAdapter = new ChapterListAdapter(ReaderActivity.chapters);
            }else {
                ReaderActivity.chapterAdapter.notifyDataSetChanged();
            }
        }




        //json解析网络响应
        private void parseResponse() {
            if(ReaderActivity.chapters == null){
                ReaderActivity.chapters = new LinkedList<>();
            }
            try {
                JSONObject response = new JSONObject(searchResponse);
                JSONObject chapter = response.getJSONObject("mixToc");
                JSONArray chaps = chapter.getJSONArray("chapters");
                for (int i=0;i<chaps.length();i++) {
                    JSONObject chap = chaps.getJSONObject(i);
                    Chapter cpt = new Chapter();
                    cpt.setTitle(chap.getString("title"));
                    String link = chap.getString("link").substring(chap.getString("link").indexOf("method="));
                    cpt.setLink(CHAPTER_LINK_API + link);
                    ReaderActivity.chapters.add(cpt);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

