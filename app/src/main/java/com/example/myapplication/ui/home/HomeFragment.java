package com.example.myapplication.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.hardware.camera2.params.TonemapCurve;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final int UPDATE_ITEM = 1;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private int[] imageResIds = {R.mipmap.timg, R.mipmap.timg2, R.mipmap.timg3, R.mipmap.timg4};
    private String[] descs = {"为梦想坚持", "我相信我是黑马", "黑马公开课", "Google/IO"};

    private static final int REQUEST_CODE_GENERAL = 105;
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
    private static final int REQUEST_CODE_ACCURATE = 108;

    private HomeViewModel homeViewModel;

    private boolean hasGotToken = false;
    private AlertDialog.Builder alertDialog;
    private ViewPager viewPager;
    private TextView tv_desc;
    private LinearLayout layout_dot;
    private int count = 10000000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATE_ITEM:
                    upDataItem();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        alertDialog = new AlertDialog.Builder(getActivity());

        //初始化控件
        viewPager = root.findViewById(R.id.vp);
        tv_desc = root.findViewById(R.id.desc);
        layout_dot = root.findViewById(R.id.layout_dot);

        //给viewPager设置适配器
        viewPager.setAdapter(new MyPagerAdapter());
        //对viewPager设置监听器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //当页面滚动时触发的时间
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //当页面被选中时触发的方法
            @Override
            public void onPageSelected(int position) {
                //对position进行处理
                position = position % imageViews.size();
                //当页面被选中的时候,改变描述文本
                tv_desc.setText(descs[position]);
                changeDots(position);
            }

            //当页面状态滚动状态发生改变时触发的事件
            @Override
            public void onPageScrollStateChanged(int state) {
                //当页面空闲状态被改变的时候
                if (state == viewPager.SCROLL_STATE_IDLE) {
                    handler.sendEmptyMessageDelayed(UPDATE_ITEM, 3000);
                } else {
                    handler.removeMessages(UPDATE_ITEM);
                }
            }
        });

//        final Button btn_general_basic_ = root.findViewById(R.id.general_basic_button);
//        btn_general_basic_.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkTokenStatus()) {
//                    return;
//                }
//                Intent intent = new Intent(getActivity(),CameraActivity.class);
//                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                        FileUtil.getSaveFile(getActivity().getApplication()).getAbsolutePath());
//                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
//                        CameraActivity.CONTENT_TYPE_GENERAL);
//                startActivityForResult(intent, REQUEST_CODE_GENERAL_BASIC);
//
//                RecognizeService.recGeneral(getActivity(), FileUtil.getSaveFile(getActivity().getApplicationContext()).getAbsolutePath(),
//                new RecognizeService.ServiceListener() {
//                    @Override
//                    public void onResult(String result) {
//                        infoPopText(result);
//                    }
//                });
//            }
//        });

        // 请选择您的初始化方式
//        initAccessToken();
        //initAccessTokenWithAkSk();

        //初始化图片
        initImage();
        //初始化文字下方的点
        initDot();

        //使两边都可以无限轮播
        viewPager.setCurrentItem(count / 2);

        //页面加载时更新
        upDataItem();
        return root;
    }


    private void upDataItem() {
        int index = viewPager.getCurrentItem();
        viewPager.setCurrentItem(++index);
        handler.sendEmptyMessageDelayed(UPDATE_ITEM, 3000);
    }

    //添加图片,准备一个ImageView集合,用来交给instantiateItem添加到页面
    private void initImage() {
        for (int i = 0; i < imageResIds.length; i++) {
            //创建出ImageView对象
            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            imageView.setImageResource(imageResIds[i]);
            imageViews.add(imageView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //当页面显示的时候,更新轮播图
        handler.sendEmptyMessageDelayed(UPDATE_ITEM, 3000);
    }

    @Override
    public void onStop() {
        super.onStop();
        //当页面不可见时,停止更新
        handler.removeCallbacksAndMessages(null);
    }

    //选中对应的原点
    private void changeDots(int position) {
        //先把所有的点恢复为白色
        for (int i = 0; i < layout_dot.getChildCount(); i++) {
            View view = layout_dot.getChildAt(i);
            view.setSelected(false);
        }
        //获取当前被选中的条目 设置为选中状态
        layout_dot.getChildAt(position).setSelected(true);

    }


    //初始化文字下方的点
    private void initDot() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
        layoutParams.setMargins(1, 1, 1, 1);
        for (int i = 0; i < imageViews.size(); i++) {
            View view = new View(getActivity());
            view.setBackgroundResource(R.drawable.seletor_dot);
            view.setLayoutParams(layoutParams);
            layout_dot.addView(view);
        }
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            //判断这个view是不是通过instantiateItem创建出来的
            return view == object;
        }

        //用来创建条目
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % imageResIds.length;
            //获取条目
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        //用来销毁条目,,且最多会创建出三个条目,多出来的条目将会被销毁
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            //销毁创建的条目
            container.removeView((View) object);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(), "requestCode:" + requestCode + " resultCode:" + resultCode, Toast.LENGTH_LONG);

        // 识别成功回调，通用文字识别（含位置信息）
        if (requestCode == REQUEST_CODE_GENERAL && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneral(getActivity(), FileUtil.getSaveFile(getActivity().getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }

        // 识别成功回调，通用文字识别（含位置信息高精度版）
        if (requestCode == REQUEST_CODE_ACCURATE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recAccurate(getActivity(), FileUtil.getSaveFile(getActivity().getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }
                    });
        }
    }

    private void infoPopText(final String result) {
        alertText("", result);
    }

    private void alertText(final String title, final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alertDialog.setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("确定", null)
                        .show();
            }
        });
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getActivity().getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    /**
     * 以license文件方式初始化
     */
    private void initAccessToken() {
        OCR.getInstance(getActivity()).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("licence方式获取token失败", error.getMessage());
            }
        }, getActivity().getApplicationContext());
    }

    /**
     * 用明文ak，sk初始化
     */
    private void initAccessTokenWithAkSk() {
        OCR.getInstance(getActivity()).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                System.out.println("token:" + token);
                Toast.makeText(getActivity(), "hasGotToken:" + hasGotToken, Toast.LENGTH_LONG);
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getActivity().getApplicationContext(), "请填入您的AK", "请填入您的SK");
    }

    /**
     * 自定义license的文件路径和文件名称，以license文件方式初始化
     */
    private void initAccessTokenLicenseFile() {
        OCR.getInstance(getActivity()).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                alertText("自定义文件路径licence方式获取token失败", error.getMessage());
            }
        }, "aip.license", getActivity().getApplicationContext());
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        // 释放内存资源
//        OCR.getInstance(getActivity()).release();
//    }


}