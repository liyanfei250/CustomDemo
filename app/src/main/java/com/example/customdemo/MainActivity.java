package com.example.customdemo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.customdemo.bean.BannerResult;
import com.example.customdemo.dialog.UpdateVersionDialog;
import com.example.customdemo.view.BankCardTextWatcher;
import com.example.customdemo.view.BrokenLineChartView;
import com.example.customdemo.view.ClearEditText;
import com.example.customdemo.view.ColumnView;
import com.example.customdemo.view.DragInerfaces;
import com.example.customdemo.view.HomeBanner;
import com.example.customdemo.view.LBarChartView;
import com.example.customdemo.view.LineChartView;
import com.example.customdemo.view.PieChartView;
import com.example.customdemo.view.RadarChartView;
import com.example.customdemo.view.RadarChartViewTwo;
import com.example.customdemo.view.RoundButton;
import com.example.customdemo.view.newLineChartView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.customdemo.view.BankCardTextWatcher.TYPE_BANKCARD;

/*
 * 常用控件的自定义，基本为组合控件，不是单纯的绘制
 * */
public class MainActivity extends AppCompatActivity {

    private int yellowColor = Color.argb(255, 253, 197, 53);
    private int greenColor = Color.argb(255, 27, 147, 76);
    private int redColor = Color.argb(255, 211, 57, 53);
    private int blueColor = Color.argb(255, 76, 139, 245);

    PieChartView pieChartView;
    RadarChartView radarChartView;
    LineChartView lineChartView;
    com.example.customdemo.view.LBarChartView LBarChartView;

    HomeBanner homeBanner;
    private TextView tvUpgrade;
    private ClearEditText cardStyle;
    private ClearEditText bankStyle;
    private RoundButton tvCountdowm;
    private CountDownTimer timer = new CountDownTimer(10000, 1000) {

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            if (!MainActivity.this.isFinishing()) {
                tvCountdowm.setEnabled(false);
                tvCountdowm.setText((millisUntilFinished / 1000) + "s");
            }

        }

        @Override
        public void onFinish() {
            if (!MainActivity.this.isFinishing()) {
                tvCountdowm.setEnabled(true);
                tvCountdowm.setText("获取验证码");
            }

        }
    };
    private ColumnView columnView;
    private RadarChartViewTwo radarViewTwo;
    private BrokenLineChartView lineChart;
    private newLineChartView newlineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeBanner = findViewById(R.id.home);
        tvUpgrade = findViewById(R.id.tv_upgrade);
        cardStyle = findViewById(R.id.card_style);
        bankStyle = findViewById(R.id.bank_style);
        tvCountdowm = findViewById(R.id.tv_countdowm);

        pieChartView = (PieChartView) findViewById(R.id.pieView);
        radarChartView = (RadarChartView) findViewById(R.id.radarView);
        lineChartView = (LineChartView) findViewById(R.id.lineView);
        LBarChartView = (LBarChartView) findViewById(R.id.frameNewBase);
        columnView = (ColumnView) findViewById(R.id.columnView);
        radarViewTwo = (RadarChartViewTwo) findViewById(R.id.radarViewTwo);
        lineChart = (BrokenLineChartView) findViewById(R.id.brokenLineChartView);
        newlineChart = (newLineChartView) findViewById(R.id.newLineChartView);

        initPieDatas();
        initRadarDatas();
        initLineDatas();
        initNewBarDatas();
        initColumDatas();
        initraterDatas();
        initlineDatass();
        initNewlineDatass();


        /*banar图*/
        List<BannerResult> list = new ArrayList<>();
        BannerResult bean = new BannerResult();
        bean.setUrl("https://pic1.zhimg.com/80/v2-99fa8968795ebd84cb1618acaae8219e_hd.jpg");
        bean.setLink("www.baidu.com");
        bean.setLinkType(1);

        BannerResult bean2 = new BannerResult();
        bean2.setUrl("https://pic1.zhimg.com/80/v2-99fa8968795ebd84cb1618acaae8219e_hd.jpg");
        bean2.setLink("www.baidu.com");
        bean2.setLinkType(1);

        list.add(bean);
        list.add(bean2);

        setBannerData(list);

        /*版本升级*/
        tvUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateVersionDialog dialog = new UpdateVersionDialog(MainActivity.this);
                dialog.setNewVersion("2");
                dialog.setUpdateInfo("练习");
                dialog.isForce(false);
                dialog.setUpdateUrl("");
                dialog.show();
            }
        });

        /*输入框输入手机号和银行卡卡号的模式*/
        BankCardTextWatcher.bind(cardStyle);
        BankCardTextWatcher.bind(bankStyle, TYPE_BANKCARD);

        /*倒计时*/
        tvCountdowm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.start();
            }
        });

    }

    private void initlineDatass() {
        float[] values = new float[]{2, 6, 8, 1, 10};

        lineChart.setValues(values);

        String[] weeks = new String[]{"周一", "周二", "周三", "周四", "周五"};

        lineChart.setWeeks(weeks);
    }

    private void initNewlineDatass() {
        float[] values = new float[]{2, 6, 8, 1, 10, 100, 20, 30, 10};

        newlineChart.setValues(values);

        String[] weeks = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日", "周一","周二"};

        newlineChart.setWeeks(weeks);
    }

    private void initraterDatas() {
        LinkedHashMap valueHash = new LinkedHashMap<>();
        valueHash.put("盈利能力", (float) 10);
        valueHash.put("抗风险能力", (float) 20);
        valueHash.put("稳定性", (float) 30);
        valueHash.put("准确性", (float) 20);
        valueHash.put("可复制性", (float) 50);
        radarViewTwo.setData(valueHash);
        radarViewTwo.setRatingData((int) 99);
    }

    private void initColumDatas() {
        final List<Long> datas = new ArrayList<>();
        final List<String> description = new ArrayList<>();
        datas.add((long) 30);
        datas.add((long) 40);
        datas.add((long) 30);
        datas.add((long) 100);
        datas.add((long) 110);
        datas.add((long) 30);
        datas.add((long) 100);
        description.add("one");
        description.add("two");
        description.add("three");
        description.add("three");
        description.add("three");
        description.add("four");
        description.add("five");

        columnView.updateThisData(datas, description);
    }

    public void setBannerData(List<BannerResult> result) {
        homeBanner.setBannerData(result);
        homeBanner.setOnBannerQuoClick(new HomeBanner.OnBannerQueClick() {

            @Override
            public void onClickView(int position) {
                //点击跳转

            }
        });
    }

    private void initNewBarDatas() {
        final List<Double> datas = new ArrayList<>();
        final List<String> description = new ArrayList<>();

        datas.add(100d);
        datas.add(20d);
        datas.add(40d);
        datas.add(50d);
        datas.add(60d);
        datas.add(200d);

        datas.add(10d);
        datas.add(30d);
        datas.add(40d);
        datas.add(15d);
        datas.add(38d);
        datas.add(60d);
        datas.add(10d);

        description.add("one");
        description.add("two");
        description.add("three");
        description.add("four");
        description.add("five");
        description.add("six");
        description.add("seven");
        description.add("eight");
        description.add("eight1");
        description.add("eight2");
        description.add("eight3");
        description.add("eight4");
        description.add("eight5");

        LBarChartView.setDatas(datas, description, true);
        LBarChartView.setDragInerfaces(new DragInerfaces() {
            @Override
            public void onEnd() {
                final List<Double> datas = new ArrayList<>();
                final List<String> description = new ArrayList<>();
                datas.add(40d);
                datas.add(15d);
                datas.add(38d);
                datas.add(60d);
                datas.add(10d);

                description.add("one");
                description.add("two");
                description.add("three");
                description.add("four");
                description.add("five");
                LBarChartView.addEndMoreData(datas, description);
            }

            @Override
            public void onStart() {

            }
        });
    }

    private void initLineDatas() {
        List<Double> datas = new ArrayList<>();
        datas.add(100d);
        datas.add(20d);
        datas.add(40d);
        datas.add(50d);
        datas.add(50d);
        datas.add(60d);
        datas.add(60d);
        datas.add(80d);
        datas.add(80d);

        List<String> description = new ArrayList<>();
        description.add("one");
        description.add("two");
        description.add("three");
        description.add("four");
        description.add("five");
        description.add("six");
        description.add("six");
        description.add("six");
        description.add("six");

        lineChartView.setDatas(datas, description);
    }

    private void initRadarDatas() {
        List<Float> datas = new ArrayList<>();
        List<String> description = new ArrayList<>();

        datas.add(0.5f);
        datas.add(0.3f);
        datas.add(0.3f);
        datas.add(0.8f);
        datas.add(1f);
        datas.add(0.4f);

        description.add("one");
        description.add("two");
        description.add("three");
        description.add("four");
        description.add("five");
        description.add("six");

        //点击动画开启
        radarChartView.setCanClickAnimation(true);
        radarChartView.setDatas(datas);
        radarChartView.setPolygonNumbers(6);
        radarChartView.setDescriptions(description);
    }

    private void initPieDatas() {

        List<Float> mRatios = new ArrayList<>();

        List<String> mDescription = new ArrayList<>();

        List<Integer> mArcColors = new ArrayList<>();

        mRatios.add(0.2f);
        mRatios.add(0.3f);
        mRatios.add(0.4f);
        mRatios.add(0.1f);

        mArcColors.add(blueColor);
        mArcColors.add(redColor);
        mArcColors.add(yellowColor);
        mArcColors.add(greenColor);

        mDescription.add("描述一");
        mDescription.add("描述二");
        mDescription.add("描述三");
        mDescription.add("描述四");

        //点击动画开启
        pieChartView.setCanClickAnimation(true);
        pieChartView.setDatas(mRatios, mArcColors, mDescription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
