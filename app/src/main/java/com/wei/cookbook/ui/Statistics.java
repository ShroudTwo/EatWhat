package com.wei.cookbook.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.wei.cookbook.App;
import com.wei.cookbook.R;
import com.wei.cookbook.config.Config;
import com.wei.cookbook.model.BaseBean;
import com.wei.cookbook.model.CommentBean;
import com.wei.cookbook.net.FoodPresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.OnClick;


public class Statistics extends BaseActivity<FoodPresenter> {
    ImageView back;
    private PieChart mChart;

    LinearLayout linearLayout;

    static String tempName = "肉";
    @Override
    protected int getLayoutResource()
    {
        return R.layout.activity_statics;
    }

    @Override
    protected void setStatusBarColor()
    {

    }

    @Override
    protected FoodPresenter createPresenter()
    {
        return new FoodPresenter(this,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics);

        mChart = (PieChart) findViewById(R.id.art);
       showChart();

        TextView imageView_recommand = (TextView)findViewById(R.id.recommand);
        imageView_recommand.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i= new Intent(Statistics.this,RecommandActivity.class);
                        i.putExtra("name",tempName);
                        startActivity(i);
                    }
                }
        );

    }

    private void showChart() {
        mChart.setUsePercentValues(true);//设置value是否用显示百分数,默认为false
        //mChart.getDescription().setEnabled(false);    //设置pieChart图表的描述
        //mChart.setBackgroundColor(Color.WHITE);      //设置pieChart图表背景色
        mChart.setRotationEnabled(true);//可以手动旋转
        mChart.setDragDecelerationFrictionCoef(0.95f);//设置pieChart图表转动阻力摩擦系数[0,1]
        mChart.setHighlightPerTapEnabled(true);       //设置piecahrt图表点击Item高亮是否可用
        mChart.setCenterTextSize(15f);
       // mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);// 设置pieChart图表展示动画效果
        mChart.animateXY(1000, 1000);
        Legend l = mChart.getLegend();
        l.setEnabled(true);                    //是否启用图列（true：下面属性才有意义
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        //l.setForm(Legend.LegendForm.DEFAULT); //设置图例的形状
        l.setFormSize(10);                      //设置图例的大小
        l.setFormToTextSpace(10f);              //设置每个图例实体中标签和形状之间的间距
        l.setDrawInside(false);
        l.setWordWrapEnabled(true);              //设置图列换行(注意使用影响性能,仅适用legend位于图表下面)
        l.setXEntrySpace(10f);                  //设置图例实体之间延X轴的间距（setOrientation = HORIZONTAL有效）
        l.setYEntrySpace(8f);                  //设置图例实体之间延Y轴的间距（setOrientation = VERTICAL 有效）
        l.setYOffset(0f);                      //设置比例块Y轴偏移量
        l.setTextSize(14f);                      //设置图例标签文本的大小
        l.setTextColor(Color.parseColor("#333333"));//设置图例标签文本的颜色

        ArrayList<PieEntry> pieEntryList = new ArrayList();
        pieEntryList.add(new PieEntry(30f,"家常菜"));
        pieEntryList.add(new PieEntry(10f,"快手菜"));
        pieEntryList.add(new PieEntry(20f,"创意菜"));
        pieEntryList.add(new PieEntry(50f,"炒菜"));
        pieEntryList.add(new PieEntry(10f,"汤"));
        pieEntryList.add(new PieEntry(34f,"鲁菜"));
        ArrayList<Integer> colors = new ArrayList();//颜色列表
        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));
        colors.add(Color.rgb(60, 179, 113));
        colors.add(Color.rgb(218, 112, 147));

        // 设置饼图颜色

        //饼状图数据集 PieDataSet
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "图例");
        pieDataSet.setColors(colors);

        pieDataSet.setSliceSpace(3f);           //设置饼状Item之间的间隙
        pieDataSet.setSelectionShift(10f);      //设置饼状Item被选中时变化的距离
        pieDataSet.setColors(colors);           //为DataSet中的数据匹配上颜色集(饼图Item颜色)
        //最终数据 PieData
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);            //设置是否显示数据实体(百分比，true:以下属性才有意义)
        pieData.setValueTextColor(Color.BLUE);  //设置所有DataSet内数据实体（百分比）的文本颜色
        pieData.setValueTextSize(12f);//设置所有DataSet内数据实体（百分比）的文本字体大小
        pieData.setValueFormatter(new PercentFormatter());//设置所有DataSet内数据实体（百分比）的文本字体格式
        mChart.setData(pieData);
        mChart.highlightValues(null);
        mChart.invalidate();
    }


}
