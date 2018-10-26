package com.example.administrator.bookreader.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.administrator.bookreader.R;
import com.example.administrator.bookreader.adapter.BookAdapter;

/**
 * Created by Administrator on 2018/7/2 0002.
 */

public class ReaderPop extends PopupWindow {

    private Context mContext;

    public View tview;

    float alpha = 1;

    Handler mhandler;

    RecyclerView listView;

    public LinearLayout MuLu;

    public LinearLayout BaiTianOrHeiYe;

    public LinearLayout SheZhi;

    public TextView textBaitian;

    private LinearLayout content;



    private TextView bookTitle;
    private Activity activity;



    public ReaderPop(Context context) {

        mContext = context;


        this.tview = LayoutInflater.from(mContext).inflate(R.layout.pop_window_main, null);
//        listLin = (LinearLayout) view.findViewById(R.id.pop_content_wai);

//        btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
//        btn_pick_photo = (Button) view.findViewById(R.id.btn_pick_photo);
        MuLu = (LinearLayout) tview.findViewById(R.id.pop_mulu);
        BaiTianOrHeiYe = (LinearLayout) tview.findViewById(R.id.pop_baitian_heiye);
        SheZhi = (LinearLayout) tview.findViewById(R.id.pop_shezhi);
        textBaitian = (TextView) tview.findViewById(R.id.text_baitian_heiye);
        bookTitle = (TextView) tview.findViewById(R.id.pop_book_title);
        content = (LinearLayout) tview.findViewById(R.id.pop_content_zj);



//        MuLu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        bookTitle.setText(BookAdapter.getReadingBook().getTitle());
//        listLin = (LinearLayout) view.findViewById(R.id.play_model_pop);
//        // 取消按钮
//        listLin.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                // 销毁弹出框
//                dismiss();
//            }
//        });
        // 设置按钮监听
//        btn_pick_photo.setOnClickListener(itemsOnClick);
//        btn_take_photo.setOnClickListener(itemsOnClick);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        this.view.setOnTouchListener(new View.OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//
////                int height = view.findViewById(R.id.pop_layout).getTop();
////
////                int y = (int) event.getY();
////                if (event.getAction() == MotionEvent.ACTION_UP) {
////                    if (y < height) {
////                        dismiss();
////                    }
////                }
//                dismiss();
//
//                return true;
//            }
//        });




//        this.setAnimationStyle(R.style.popup_window_anim);
        // TODO: 2016/5/17 设置背景颜色
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
        // TODO: 2016/5/17 设置可以获取焦点
//        this.setFocusable(true);
//        this.setOutsideTouchable(true);
        this.setFocusable(false);
        this.setBackgroundDrawable(null);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        this.setOutsideTouchable(true);
        // TODO：更新popupwindow的状态
        this.update();


        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.tview);
        // 设置弹出窗体的宽和高
        this.setHeight(height);
        this.setWidth(width);

    }







}
