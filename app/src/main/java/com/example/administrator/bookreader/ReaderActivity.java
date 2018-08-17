package com.example.administrator.bookreader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReaderActivity extends AppCompatActivity {

    private TextView reader;
    private String searchResponse = null;
    private final String TAG = "ReaderActivity";

    int chapterCountNum;
    int pageCountNum;
    private int chapterNum = 0;

    private int pageNum = 0;

    private TextView readerTop;

    private TextView readBottom;

    private Dialog loadingDialog;

    private boolean isNextOrPre = false;

    private boolean isLoading = false;
    private ArrayList<String> lists = null;
    static float alpha = 1;

    static Handler mhandler;
    private Chapter cpt = new Chapter();

    public static ReaderActivity activity = null;

    private ReaderPop readerPopBottom;

    public static ChapterListAdapter chapterAdapter = null;

    private ReaderPopLeft popLeft;

    private ReaderPopBottomSZ popSZ;

    private View readerView;

    private ChapterLoadService.LoadChapterBinder loadChapterBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            loadChapterBinder = (ChapterLoadService.LoadChapterBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    LinearLayout muLu;

    RecyclerView chapterRecycler;

    public static List<Chapter> chapters = new LinkedList<>();

    public static final String BOOK_CHAPTER_API = "http://api.zhuishushenqi.com/mix-atoc/";

    public static final String CHAPTER_LINK_API = "http://chapter2.zhuishushenqi.com/chapter/http:%2F%2Fbook.my716.com%2FgetBooks.aspx%3F";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        readerView = (LinearLayout) findViewById(R.id.reader_view);
        reader = (TextView) findViewById(R.id.book_content);
        if(BookAdapter.getReadingBook().getIsAdd() == 1){
            chapterNum = BookAdapter.getReadingBook().getChapterNum();
            pageNum = BookAdapter.getReadingBook().getPageNum();
        }
        loadingDialog = createLoadingDialog(this);
        loadingDialog.show();
        setFullScreen(true);
        setStatusBarVisibility(false);
        new ChapterLoadTask().execute(BOOK_CHAPTER_API + BookAdapter.getReadingBook().getBookid() + "?view=chapters");
        activity = this;
        readerTop = (TextView) findViewById(R.id.reader_top);
        readBottom = (TextView) findViewById(R.id.reader_bottom);
        popLeft = new ReaderPopLeft(getApplicationContext());
        popSZ = new ReaderPopBottomSZ(getApplicationContext());
        readerPopBottom = new ReaderPop(getApplicationContext());

        new BookChapterTask().execute();
        inintEvents();

    }

    public  Dialog createLoadingDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }

    public void readTheChapter(int i,String url){
            chapterNum = i;
            pageNum = 0;
            if(lists!= null) {
                lists = null;
            }
            new BookChapterTask().execute(BOOK_CHAPTER_API + BookAdapter.getReadingBook().getBookid() + "?view=chapters");
            popLeft.dismiss();
    }






    private class BookChapterTask extends AsyncTask<String, Void, Void> {

        public BookChapterTask() {

        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(BOOK_CHAPTER_API + BookAdapter.getReadingBook().getBookid() + "?view=chapters").build();
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
//            lists = getPageContentStringInfo(reader.getPaint(),content,19,reader.getWidth());
//            reader.setText(lists.get(pageNum));
//            if(cpt != null) {
                readerTop.setText(cpt.getTitle());
                int chapterRealNum = chapterNum + 1;
                readBottom.setText("第" + chapterRealNum + "章/共" + chapterCountNum + "章");
                new ReaderBookTask().execute(cpt.getLink());
//            }
        }




        //json解析网络响应
        private void parseResponse() {
            try {
                JSONObject response = new JSONObject(searchResponse);
                JSONObject chapter = response.getJSONObject("mixToc");
                String chapterCount = chapter.getString("chaptersCount1");
                chapterCountNum = Integer.parseInt(chapterCount);
                JSONArray chaps = chapter.getJSONArray("chapters");
                JSONObject chap = chaps.getJSONObject(chapterNum);
                cpt.setTitle(chap.getString("title"));
                String link = chap.getString("link").substring(chap.getString("link").indexOf("method="));
                cpt.setLink(CHAPTER_LINK_API + link);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }




    public  void showPopupWindowLeft(View view, PopupWindow window) {


        window.showAtLocation(view, Gravity.LEFT, 0, 0);
        ReaderPopLeft.chapterRecycler.scrollToPosition(chapterNum);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(alpha>0.5f){
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(8);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message msg =mhandler.obtainMessage();
                    msg.what = 1;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha-=0.01f;
                    msg.obj =alpha ;
                    mhandler.sendMessage(msg);

                }
            }
        }).start();
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失的时候恢复成原来的透明度
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //此处while的条件alpha不能<= 否则会出现黑屏
                        while(alpha<1f){
                            try {
                                Thread.sleep(4);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("headportrait","alpha:"+alpha);
                            Message msg =mhandler.obtainMessage();
                            msg.what = 1;
                            alpha+=0.01f;
                            msg.obj =alpha ;
                            mhandler.sendMessage(msg);
                        }
                    }
                }).start();
            }

        });
    }


    public  void showPopupWindowBottomSZ(View view, PopupWindow window) {


        window.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(alpha>0.5f){
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(8);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message msg =mhandler.obtainMessage();
                    msg.what = 1;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha-=0.01f;
                    msg.obj =alpha ;
                    mhandler.sendMessage(msg);

                }
            }
        }).start();
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失的时候恢复成原来的透明度
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //此处while的条件alpha不能<= 否则会出现黑屏
                        while(alpha<1f){
                            try {
                                Thread.sleep(4);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("headportrait","alpha:"+alpha);
                            Message msg =mhandler.obtainMessage();
                            msg.what = 1;
                            alpha+=0.01f;
                            msg.obj =alpha ;
                            mhandler.sendMessage(msg);
                        }
                    }
                }).start();
            }

        });
    }






    private void readNextChapter(){
        if(chapterNum < chapterCountNum - 1){
            chapterNum = chapterNum + 1;
            pageNum = 0;
            if(lists!= null) {
                lists = null;
            }
            loadingDialog.show();
            new BookChapterTask().execute(BOOK_CHAPTER_API + BookAdapter.getReadingBook().getBookid() + "?view=chapters");
        }else {
            Toast.makeText(this,"已无下一章",Toast.LENGTH_SHORT).show();
        }
    }

    private void readPreChapter(){
        if(chapterNum == 0){
            Toast.makeText(this,"已无前一章",Toast.LENGTH_SHORT).show();
        }else {
            isNextOrPre = true;
            chapterNum = chapterNum - 1;
            if (lists != null) {
                lists = null;
            }
            loadingDialog.show();
            new BookChapterTask().execute(BOOK_CHAPTER_API + BookAdapter.getReadingBook().getBookid() + "?view=chapters");
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void showPopupWindowTop(View view,PopupWindow window) {
//
//
//        window.showAtLocation(view, Gravity.TOP, 0, 0);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                while(alpha>0.5f){
//                    try {
//                        //4是根据弹出动画时间和减少的透明度计算
//                        Thread.sleep(8);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    Message msg =mhandler.obtainMessage();
//                    msg.what = 1;
//                    //每次减少0.01，精度越高，变暗的效果越流畅
//                    alpha-=0.01f;
//                    msg.obj =alpha ;
//                    mhandler.sendMessage(msg);
//
//                }
//            }
//        }).start();
//        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                //popupwindow消失的时候恢复成原来的透明度
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //此处while的条件alpha不能<= 否则会出现黑屏
//                        while(alpha<1f){
//                            try {
//                                Thread.sleep(4);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            Log.d("headportrait","alpha:"+alpha);
//                            Message msg =mhandler.obtainMessage();
//                            msg.what = 1;
//                            alpha+=0.01f;
//                            msg.obj =alpha ;
//                            mhandler.sendMessage(msg);
//                        }
//                    }
//                }).start();
//            }
//
//        });
//    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showPopupWindow(View view,PopupWindow window) {


            window.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        new Thread(new Runnable() {
            @Override
            public void run() {

                while(alpha>0.5f){
                    try {
                        //4是根据弹出动画时间和减少的透明度计算
                        Thread.sleep(8);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message msg =mhandler.obtainMessage();
                    msg.what = 1;
                    //每次减少0.01，精度越高，变暗的效果越流畅
                    alpha-=0.01f;
                    msg.obj =alpha ;
                    mhandler.sendMessage(msg);

                }
            }
        }).start();
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失的时候恢复成原来的透明度
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //此处while的条件alpha不能<= 否则会出现黑屏
                        while(alpha<1f){
                            try {
                                Thread.sleep(4);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("headportrait","alpha:"+alpha);
                            Message msg =mhandler.obtainMessage();
                            msg.what = 1;
                            alpha+=0.01f;
                            msg.obj =alpha ;
                            mhandler.sendMessage(msg);
                        }
                    }
                }).start();
            }

        });
    }





// 显示隐藏状态栏，全屏不变，只在有全屏时有效

    private void setStatusBarVisibility(boolean enable) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (enable) {
            lp.flags |= WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
        } else {
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        getWindow().setAttributes(lp);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
    /**
     * 设置是否全屏
     * @param enable
     */
    private void setFullScreen(boolean enable) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (enable) {
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else {
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        getWindow().setAttributes(lp);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(BookAdapter.getReadingBook().getIsAdd() == 1){
            BookAdapter.getReadingBook().setChapterNum(chapterNum);
            BookAdapter.getReadingBook().setPageNum(pageNum);
            BookAdapter.getReadingBook().update(BookAdapter.getReadingBook().getId());
            MainActivity.books = DataSupport.findAll(NetBook.class);
            MainActivity.bookJIaAdapter.notifyDataSetChanged();
        }
    }

    private void inintEvents(){
        readerPopBottom.MuLu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readerPopBottom.dismiss();
                showPopupWindowLeft(readerView,popLeft);

            }
        });

        readerPopBottom.SheZhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readerPopBottom.dismiss();
                showPopupWindowBottomSZ(readerView,popSZ);
            }
        });

        readerPopBottom.BaiTianOrHeiYe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( readerPopBottom.textBaitian.getText().equals("白天")){
                    readerView.setBackgroundColor(Color.parseColor("#313131"));
                    reader.setTextColor(Color.parseColor("#949694"));
                    readerPopBottom.textBaitian.setText("黑夜");
                }else if ( readerPopBottom.textBaitian.getText().equals("黑夜")){
                    readerView.setBackgroundColor(Color.parseColor("#FFFBE6"));
                    reader.setTextColor(Color.parseColor("#42413A"));
                    readerPopBottom.textBaitian.setText("白天");
                }

            }
        });

        reader.setOnTouchListener(new View.OnTouchListener() {
            float x = 0;
            float border1;
            float border2;
            float border3;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                border1 =  view.getWidth() / 3 * 2;
                border2 = view.getWidth() / 3;
                border3 = view.getWidth();
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x = motionEvent.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        if(x <= border2 && x >= 0){
                            getPrePage();
                        }else if(x > border2 && x < border1){
                            showPopupWindow(readerView,readerPopBottom);
                        }else if(x >= border1 && x <= border3) {
                            getNextPage();
                        }
                        break;
                    default:
                        break;
                }

                return true;
            }
        });

        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        backgroundAlpha((float)msg.obj);
                        break;
                }
            }
        };


    }

//    public boolean onTouchEvent(MotionEvent event) {
//        // TODO Auto-generated method stub
//        if ( readerPopBottom.isShowing()) {
//            readerPopBottom.dismiss();
//        }
//        if ( readerPopTop.isShowing()) {
//            readerPopTop.dismiss();
//        }
//
//        return super.onTouchEvent(event);
//    }



    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0~1.0
        getWindow().setAttributes(lp); //act 是上下文context

    }

    private void getNextPage(){
        if(pageNum == lists.size() - 1){
            readNextChapter();
        }else {
            pageNum = pageNum + 1;
            reader.setText(lists.get(pageNum));
        }
    }

    private void getPrePage(){
        if(pageNum == 0){
            readPreChapter();
        }else {
            pageNum = pageNum - 1;
            reader.setText(lists.get(pageNum));
        }
    }







    private class ReaderBookTask extends AsyncTask<String, Void, Void> {
        String content = null;

        public ReaderBookTask() {


        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(String... params) {
            String url = params[0];
            HttpURLConnection conn = null;
            BufferedReader br = null;
            try {
                conn = (HttpURLConnection)new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                //使用缓存提高处理效率
                br = new BufferedReader(new InputStreamReader((InputStream) conn.getInputStream()));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                //网络响应赋值给成员变量searchResponse
                searchResponse = sb.toString();
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
            lists = getPageContentStringInfo(reader.getPaint(),content,19,reader.getWidth());
            pageCountNum = lists.size();
            if(isNextOrPre) {
                pageNum = pageCountNum - 1;
            }
            reader.setText(lists.get(pageNum));
            loadingDialog.dismiss();
            isNextOrPre = false;
        }

        //json解析网络响应
        private void parseResponse() {
            try {
                JSONObject response = new JSONObject(searchResponse);
                JSONObject chapter = response.getJSONObject("chapter");
                content = chapter.getString("body");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    // 本方法 分行又分页
    public static ArrayList<String> getPageContentStringInfo(
            Paint m_paint, String content, int pageLines, float pageWidth) {
        boolean isEndOfH = true;
        char ch = 0;
        int w = 0;
        int istart = 0;
        int lines;
        int lineNum = 0;
        ArrayList<String> contentList = new ArrayList<String>();
        String cl = null;
        // 内容长度
        for (int i = 0; i < content.length(); i++) {

//                cl = new ArrayList<String>();
            if(cl == null && isEndOfH == true){
                cl = "        ";
                w = 108;
                isEndOfH = false;
            }
            ch = content.charAt(i);
            float[] widths = new float[1];
            String srt = String.valueOf(ch);
            m_paint.getTextWidths(srt, widths);
            if (ch == '\n') {
                // 如果遇到断行符
                lineNum++;
                if (cl == null){
                    cl =  content.substring(istart, i) + "\n" + "        ";
                }else {
                    if(lineNum == pageLines){
                        cl = cl + content.substring(istart, i) + "\n";
                    }else {
                        cl = cl + content.substring(istart, i) + "\n" + "        ";
//                    cl.add(content.substring(istart, i));
                    }
                }
                istart = i + 1;
                w = 108;
            } else {
                // 遇到字符
                w += (int) (Math.ceil(widths[0]));
                // 当长度小于宽度时
                if (w > pageWidth) {
                    lineNum++;
                    if (cl == null){
                        cl = content.substring(istart, i);
                    }else {
                        cl = cl + content.substring(istart, i);;
//                    cl.add(content.substring(istart, i));
                    }
                    istart = i;
                    i--;
                    w = 0;
                } else {
                    if (i == (content.length() - 1)) {
                        lineNum++;
                        if (cl == null){
                            cl =   content.substring(istart, i);
                        }else {
                            cl =   cl + content.substring(istart, content.length());
//                    cl.add(content.substring(istart, i));
                        }
//                        cl.add(content.substring(istart, content.length()));
                    }
                }
            }
            if (lineNum == pageLines || i == (content.length() - 1)) {
                contentList.add(cl);
//                System.out.println("每行 -- 》" + cl.toString());
                if(cl.endsWith("\n")){
                    isEndOfH = true;
                }
                cl = null;
                // 当最后一个字符时的行数
                lines = lineNum;
                lineNum = 0;
            }
        }
        return contentList;
    }
}
