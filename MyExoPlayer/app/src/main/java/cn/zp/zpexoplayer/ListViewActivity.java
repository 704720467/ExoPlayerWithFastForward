package cn.zp.zpexoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.zp.zpexoplayer.adapter.LeftAdapter;
import cn.zp.zpexoplayer.adapter.RightAdapter;
import cn.zp.zpexoplayer.model.MyTag;
import cn.zp.zpexoplayer.model.TagType;
import cn.zp.zpexoplayer.view.HaveHeaderListView;

import static cn.zp.zpexoplayer.model.TagType.EnumTagType.PLAYER;
import static cn.zp.zpexoplayer.model.TagType.EnumTagType.TACTICS;
import static cn.zp.zpexoplayer.model.TagType.EnumTagType.TECHNOLOGY;

public class ListViewActivity extends AppCompatActivity {

    //左边的ListView
    private ListView lv_left;
    //左边ListView的Adapter
    private LeftAdapter leftAdapter;
    //左边的数据存储

    private ArrayList<TagType> leftStr;

    //右边的数据存储
    private List<List<MyTag>> rightStr;
    //左边数据的标志
    private List<Boolean> flagArray;
    //右边的ListView
    private HaveHeaderListView lv_right;
    //右边的ListView的Adapter
    private RightAdapter rightAdapter;
    //是否滑动标志位
    private Boolean isScroll = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        //初始化控件
        initView();
        //初始化数据
        initData();
        lv_left.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isScroll = false;
                for (int i = 0; i < leftStr.size(); i++) {
                    if (i == position) {
                        flagArray.set(i, true);
                    } else {
                        flagArray.set(i, false);
                    }
                }
                //更新
                leftAdapter.notifyDataSetChanged();
                int rightSection = 0;
                for (int i = 0; i < position; i++) {
                    //查找
                    rightSection += rightAdapter.getCountForSection(i) + 1;
                }
                //显示到rightSection所代表的标题
                lv_right.setSelection(rightSection);
            }
        });
        lv_right.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (lv_right.getLastVisiblePosition() == (lv_right.getCount() - 1)) {
                            lv_left.setSelection(ListView.FOCUS_DOWN);
                        }
                        // 判断滚动到顶部
                        if (lv_right.getFirstVisiblePosition() == 0) {
                            lv_left.setSelection(0);
                        }
                        break;
                }

            }

            int y = 0;
            int x = 0;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isScroll) {
                    for (int i = 0; i < rightStr.size(); i++) {
                        if (i == rightAdapter.getSectionForPosition(lv_right.getFirstVisiblePosition())) {
                            flagArray.set(i, true);
                            //获取当前标题的标志位
                            x = i;
                        } else {
                            flagArray.set(i, false);
                        }
                    }
                    if (x != y) {
                        leftAdapter.notifyDataSetChanged();
                        //将之前的标志位赋值给y，下次判断
                        y = x;
                    }
                } else {
                    isScroll = true;
                }
            }
        });
    }

    private void initData() {
        //左边相关数据
        flagArray.add(true);
        flagArray.add(false);
        flagArray.add(false);
        flagArray.add(false);
        flagArray.add(false);
        flagArray.add(false);
        flagArray.add(false);
        flagArray.add(false);
        leftStr.add(new TagType(PLAYER, "球员类"));
        leftStr.add(new TagType(TECHNOLOGY, "技术类"));
        leftStr.add(new TagType(TACTICS, "战术类"));
        leftAdapter.notifyDataSetChanged();
        //右边相关数据
        //面食类
        List<MyTag> food1 = new ArrayList<>();
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        food1.add(new MyTag(false, "1号 贾妮马", PLAYER));
        //盖饭
        List<MyTag> food2 = new ArrayList<>();
        food2.add(new MyTag(false, "转身跳投", TECHNOLOGY));
        food2.add(new MyTag(false, "转身跳投", TECHNOLOGY));
        food2.add(new MyTag(false, "转身跳投", TECHNOLOGY));
        food2.add(new MyTag(false, "转身跳投", TECHNOLOGY));
        //寿司
        List<MyTag> food3 = new ArrayList<>();
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        food3.add(new MyTag(false, "2+3联防", TACTICS));
        rightStr.add(food1);
        rightStr.add(food2);
        rightStr.add(food3);
        rightAdapter.notifyDataSetChanged();
    }


    private void initView() {
        lv_left = (ListView) findViewById(R.id.lv_left);
        flagArray = new ArrayList<>();
        rightStr = new ArrayList<List<MyTag>>();
        leftStr = new ArrayList<>();
        leftAdapter = new LeftAdapter(ListViewActivity.this, leftStr, flagArray);
        lv_left.setAdapter(leftAdapter);
        lv_right = (HaveHeaderListView) findViewById(R.id.lv_right);
        rightAdapter = new RightAdapter(ListViewActivity.this, leftStr, rightStr);
        lv_right.setAdapter(rightAdapter);
    }
}
