package com.example.airlineticketing.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.airlineticketing.R;
import com.example.airlineticketing.ui.activity.QueryActivity;
import com.example.airlineticketing.util.MySqliteOpenHelper;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 首页
 */

public class UserHomeFragment extends Fragment {
    MySqliteOpenHelper helper = null;
    private Activity myActivity;//上下文
    private Banner mBanner;//轮播顶部
    private TextView start_city;
    private TextView arrive_city;
    private TextView time_view;
    private ImageView transition;
    private Switch onlyGD;
    private Button search_button;
    private Integer userType;
    private String mDate;
    private Integer userId;
    private String transitionName;
    //申明对象
    CityPickerView mPicker = new CityPickerView();
    Calendar currentDate;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        //预先加载仿iOS滚轮实现的全部数据
        mPicker.init(myActivity);
        helper = new MySqliteOpenHelper(myActivity);

        mBanner = view.findViewById(R.id.banner);
        start_city = view.findViewById(R.id.start_city);
        arrive_city = view.findViewById(R.id.arrive_city);
        transition = view.findViewById(R.id.transition);
        time_view = view.findViewById(R.id.time_view);
        onlyGD = view.findViewById(R.id.onlyGD);
        search_button = view.findViewById(R.id.search_button);

        initBanner();//初始化轮播图
        setSelectCity();//设置选择城市
        setSelectDate();//初始化时间
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        transition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionName = start_city.getText().toString();
                start_city.setText(arrive_city.getText().toString());
                arrive_city.setText(transitionName);
            }
        });
        return view;
    }
    //初始化轮播图
    private void initBanner(){
        //图片资源
        int[] imageResourceID = new int[]{R.drawable.ic_a, R.drawable.ic_b, R.drawable.ic_c, R.drawable.ic_d, R.drawable.ic_e};
        List<Integer> imgeList = new ArrayList<>();
        //轮播标题
        for (int i = 0; i < imageResourceID.length; i++) {
            imgeList.add(imageResourceID[i]);//把图片资源循环放入list里面
            //设置图片加载器，通过Glide加载图片
            mBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(myActivity).load(path).into(imageView);
                }
            });
            //设置轮播的动画效果,里面有很多种特效,可以到GitHub上查看文档。
            mBanner.setImages(imgeList);//设置图片资源
            //设置指示器位置（即图片下面的那个小圆点）
            mBanner.setDelayTime(3000);//设置轮播时间3秒切换下一图
            mBanner.start();//开始进行banner渲染
        }
    }

    //设置选择城市
    private void setSelectCity(){
        start_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityConfig cityConfig = new CityConfig.Builder()
                        .title("选择出发城市")//标题
                        .titleTextSize(18)//标题文字大小
                        .titleTextColor("#008EFF")//标题文字颜  色
                        .titleBackgroundColor("#E9E9E9")//标题栏背景色
                        .confirTextColor("#008EFF")//确认按钮文字颜色
                        .confirmText("确定")//确认按钮文字
                        .confirmTextSize(16)//确认按钮文字大小
                        .cancelTextColor("#585858")//取消按钮文字颜色
                        .cancelText("取消")//取消按钮文字
                        .cancelTextSize(16)//取消按钮文字大小
                        //显示类，只显示省份一级，显示省市两级还是显示省市区三级
                        .setCityWheelType(CityConfig.WheelType.PRO_CITY)
                        .showBackground(true)//是否显示半透明背景
                        .visibleItemsCount(7)//显示item的数量
                        .province("上海市")//默认显示的省份
                        .provinceCyclic(true)//省份滚轮是否可以循环滚动
                        .cityCyclic(true)//城市滚轮是否可以循环滚动
                        .districtCyclic(true)//区县滚轮是否循环滚动
                        .setCustomItemLayout(R.layout.item_city)//自定义item的布局
                        //自定义item布局里面的textViewid
                        .setCustomItemTextViewId(R.id.item_city_name_tv)
                        .drawShadows(false)//滚轮不显示模糊效果
                        .setLineColor("#03a9f4")//中间横线的颜色
                        .setLineHeigh(5)//中间横线的高度
                        .setShowGAT(true)//是否显示港澳台数据，默认不显示
                        .build();

                //设置自定义的属性配置
                mPicker.setConfig(cityConfig);
                mPicker.showCityPicker();
                //监听选择点击事件及返回结果
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                        //省份province
                        //城市city
                        //地区district
                        start_city.setText(city.getName().replace("市", ""));
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showLongToast(myActivity, "已取消");
                    }
                });
            }
        });
        arrive_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityConfig cityConfig = new CityConfig.Builder()
                        .title("选择到达城市")//标题
                        .titleTextSize(18)//标题文字大小
                        .titleTextColor("#008EFF")//标题文字颜  色
                        .titleBackgroundColor("#E9E9E9")//标题栏背景色
                        .confirTextColor("#008EFF")//确认按钮文字颜色
                        .confirmText("确定")//确认按钮文字
                        .confirmTextSize(16)//确认按钮文字大小
                        .cancelTextColor("#585858")//取消按钮文字颜色
                        .cancelText("取消")//取消按钮文字
                        .cancelTextSize(16)//取消按钮文字大小
                        //显示类，只显示省份一级，显示省市两级还是显示省市区三级
                        .setCityWheelType(CityConfig.WheelType.PRO_CITY)
                        .showBackground(true)//是否显示半透明背景
                        .visibleItemsCount(7)//显示item的数量
                        .province("北京市")//默认显示的省份
                        .provinceCyclic(true)//省份滚轮是否可以循环滚动
                        .cityCyclic(true)//城市滚轮是否可以循环滚动
                        .districtCyclic(true)//区县滚轮是否循环滚动
                        .setCustomItemLayout(R.layout.item_city)//自定义item的布局
                        //自定义item布局里面的textViewid
                        .setCustomItemTextViewId(R.id.item_city_name_tv)
                        .drawShadows(false)//滚轮不显示模糊效果
                        .setLineColor("#03a9f4")//中间横线的颜色
                        .setLineHeigh(5)//中间横线的高度
                        .setShowGAT(true)//是否显示港澳台数据，默认不显示
                        .build();

                //设置自定义的属性配置
                mPicker.setConfig(cityConfig);
                mPicker.showCityPicker();
                //监听选择点击事件及返回结果
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                        //省份province
                        //城市city
                        //地区district
                        arrive_city.setText(city.getName().replace("市", ""));
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showLongToast(myActivity, "已取消");
                    }
                });
            }
        });
    }

    //设置选择日期
    public void setSelectDate() {

        //设置初始日期
        currentDate = Calendar.getInstance(Locale.CHINA);

        int currentYear = currentDate.get(Calendar.YEAR) ;
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);
        int week = currentDate.get(Calendar.DAY_OF_WEEK);
        String nums[] = {"日", "一", "二", "三", "四", "五", "六"};
        String currentTime = currentMonth + "月" + currentDay + "日" + "   " + "周" + nums[week - 1];

        mDate= String.format("%s/%s/%s",currentYear,currentMonth,currentDay);;

        time_view.setText(currentTime);

        //点击后弹出日期选择器
        time_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取当前时间
                currentDate = Calendar.getInstance(Locale.CHINA);

                //弹出日期选择器
                new DatePickerDialog(myActivity, 0, new DatePickerDialog.OnDateSetListener() {
                    // 绑定监听器(How the parent is notified that the date is set.)
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 此处得到选择的时间，可以进行你想要的操作


                        //根据日期计算周几
                        int week = DateToWeek(year, monthOfYear + 1, dayOfMonth);
                        String nums[] = {"日", "一", "二", "三", "四", "五", "六"};
                        String date = (monthOfYear + 1) + "月" + dayOfMonth + "日" + "   " + "周" + nums[week - 1];
                        time_view.setText(date);
                        mDate= String.format("%s/%s/%s",year,(monthOfYear + 1),dayOfMonth);


                    }

                }
                        // 设置初始日期
                        , currentDate.get(Calendar.YEAR)
                        , currentDate.get(Calendar.MONTH)
                        , currentDate.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    //将日期转化为周几
    public int DateToWeek(int year, int month, int day) {
        String datetime = year + "-" + month + "-" + day;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK);

        return w;
    }
    //查询
    public void search(){
        //获取开始城市，到达城市，时间，是否勾选只看高铁/动车
        String start_city_name=start_city.getText().toString();
        String arrive_city_name=arrive_city.getText().toString();
        String time=time_view.getText().toString();
        boolean only=onlyGD.isChecked();

        if ("请选择".equals(start_city_name)) {
            Toast.makeText(myActivity, "请选择出发城市", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("请选择".equals(start_city_name)) {
            Toast.makeText(myActivity, "请选择达到城市", Toast.LENGTH_SHORT).show();
            return;
        }

        //跳转到查询界面并传递信息
        Intent intent=new Intent(myActivity, QueryActivity.class);

        intent.putExtra("start_city_name",start_city_name);
        intent.putExtra("arrive_city_name",arrive_city_name);
        intent.putExtra("time",time);
        intent.putExtra("date", mDate);
        intent.putExtra("only",only);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
