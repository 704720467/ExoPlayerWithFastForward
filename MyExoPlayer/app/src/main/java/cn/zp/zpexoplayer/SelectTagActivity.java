package cn.zp.zpexoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.zp.zpexoplayer.adapter.RightAdapter;
import cn.zp.zpexoplayer.adapter.TagTypeAdapter;
import cn.zp.zpexoplayer.model.MyTag;
import cn.zp.zpexoplayer.model.TagType;
import cn.zp.zpexoplayer.util.DeviceUtil;
import cn.zp.zpexoplayer.view.HaveHeaderListView;

import static cn.zp.zpexoplayer.model.TagType.EnumTagType.PLAYER;
import static cn.zp.zpexoplayer.model.TagType.EnumTagType.TACTICS;
import static cn.zp.zpexoplayer.model.TagType.EnumTagType.TECHNOLOGY;

public class SelectTagActivity extends FillScreenBaseActivity implements View.OnClickListener {
    private String TAG = "SelectTagActivity";
    private RecyclerView tagTypeRv;
    private TagTypeAdapter tagTypeAdapter;

    private HaveHeaderListView tagTypeContRv;
    //右边的ListView的Adapter
    private RightAdapter rightAdapter;
    private ArrayList<TagType> leftStr;
    //右边的数据存储
    private List<List<MyTag>> rightStr;
    private List<List<MyTag>> selectTags;
    private List<String> selectTagsNmuber;//被选中的位置例如0-1：第一类位置是1的标签
    private int currentType = 0;
    //是否滑动标志位
    private Boolean isScroll = false;
    private TextView selectTagCount;
    private int selectTagCountNumber = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag);

        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = DeviceUtil.getScreenHeightSize(this) * 2 / 3;
        lp.windowAnimations = R.style.buttomdialogAnim;
        window.setAttributes(lp);


        initView();
        initData();
        tagTypeAdapter.setOnItemClickListener(new TagTypeAdapter.MyItemClickListener() {
            @Override
            public void onTransitionItemClick(View view, int postion) {
                isScroll = false;
                int rightSection = 0;
                for (int i = 0; i < postion; i++) {
                    //查找
                    rightSection += rightAdapter.getCountForSection(i) + 1;
                }
                //显示到rightSection所代表的标题
                tagTypeContRv.setSelection(rightSection);
            }
        });

        rightAdapter.setOnItemClickListener(new RightAdapter.MyItemClickListener() {
            @Override
            public void onTransitionItemClick(View view, int section, int postion, boolean isPlus) {
                leftStr.get(section).setSelectCount(leftStr.get(section).getSelectCount() + (isPlus ? (1) : (-1)));
                tagTypeAdapter.changSelectState(section);
                selectTagCountNumber += (isPlus ? (1) : (-1));
                selectTagCount.setText(selectTagCountNumber + "");
            }
        });

        tagTypeContRv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (tagTypeContRv.getLastVisiblePosition() == (tagTypeContRv.getCount() - 1)) {
                            tagTypeRv.scrollToPosition(tagTypeContRv.getCount() - 1);
                        }
                        // 判断滚动到顶部
                        if (tagTypeContRv.getFirstVisiblePosition() == 0) {
                            tagTypeRv.scrollToPosition(0);
                        }
                        break;
                }
            }

            int y = 0;
            int x = 0;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isScroll) {
                    findFristItem(firstVisibleItem);
                } else {
                    isScroll = true;
                }
            }
        });
    }

    private void initView() {
        selectTags = new ArrayList<List<MyTag>>();
        selectTags.add(new ArrayList<MyTag>());
        selectTags.add(new ArrayList<MyTag>());
        selectTags.add(new ArrayList<MyTag>());
        rightStr = new ArrayList<List<MyTag>>();
        leftStr = new ArrayList<>();
        findViewById(R.id.tv_cancle).setOnClickListener(this);
        findViewById(R.id.bt_commit).setOnClickListener(this);
        selectTagCount = (TextView) findViewById(R.id.select_tag_count);
        tagTypeRv = (RecyclerView) findViewById(R.id.tag_type_list_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        tagTypeRv.setLayoutManager(linearLayoutManager);
        tagTypeAdapter = new TagTypeAdapter(this, leftStr);
        tagTypeRv.setAdapter(tagTypeAdapter);
        tagTypeContRv = (HaveHeaderListView) findViewById(R.id.content_list_view);
        rightAdapter = new RightAdapter(SelectTagActivity.this, leftStr, rightStr);
        tagTypeContRv.setAdapter(rightAdapter);
    }

    private void initData() {
        //获取被选中的标签集合
        selectTagsNmuber = (List<String>) getIntent().getSerializableExtra("selectTagsNmuber");

        leftStr.add(new TagType(PLAYER, "球员类"));
        leftStr.add(new TagType(TECHNOLOGY, "技术类"));
        leftStr.add(new TagType(TACTICS, "战术类"));
        tagTypeAdapter.notifyDataSetChanged();
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

        collateDate();

        rightAdapter.notifyDataSetChanged();
    }

    /**
     * 整理数据
     */
    private void collateDate() {
        if (selectTagsNmuber == null)
            return;

        //        for (int i = 0; i < selectTagsNmuber.size(); i++) {
        //        String s = selectTagsNmuber.get(i);
        for (String s : selectTagsNmuber) {
            int typeNumber = Integer.parseInt(s.substring(0, s.indexOf("-")));
            int tagNumber = Integer.parseInt(s.substring(s.indexOf("-") + 1, s.length()));
            rightStr.get(typeNumber).get(tagNumber).setSelected(true);
            leftStr.get(typeNumber).setSelectCount(leftStr.get(typeNumber).getSelectCount() + 1);
            tagTypeAdapter.changSelectState(typeNumber);
            selectTags.get(typeNumber).add(rightStr.get(typeNumber).get(tagNumber));
            selectTagCountNumber += 1;
            selectTagCount.setText(selectTagCountNumber + "");
        }
    }

    private void findFristItem(int firstVisibleItem) {

        int count = 0;
        for (int i = 0; i < rightStr.size(); i++) {
            count += rightStr.get(i).size();
            if (firstVisibleItem < count) {
                if (i == currentType)
                    return;
                currentType = i;
                tagTypeAdapter.changSelectState(i);
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                finish();
                break;
            case R.id.bt_commit:
                setCallBackData();
                finish();
                break;
        }
    }

    /**
     * 回调数据
     */
    private void setCallBackData() {
        try {
            if (selectTagsNmuber == null)
                selectTagsNmuber = new ArrayList<>();
            selectTagsNmuber.clear();
            selectTags.get(0).clear();
            selectTags.get(1).clear();
            selectTags.get(2).clear();
            for (int i = 0; i < rightStr.size(); i++) {
                List<MyTag> myTags = rightStr.get(i);
                for (int j = 0; j < myTags.size(); j++) {
                    if (myTags.get(j).isSelected()) {
                        selectTags.get(i).add(myTags.get(j));
                        selectTagsNmuber.add(i + "-" + j);
                    }
                }
            }

            Intent intent = new Intent();
            intent.putExtra("selectTags", (Serializable) selectTags);
            intent.putExtra("selectTagsNmuber", (Serializable) selectTagsNmuber);
            setResult(TagEditActivity.RESULT_ADDTAG_OK, intent);
        } catch (Exception e) {
            Log.e("", "fanhui " + e.getMessage());
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.flipper_bottom_out);
    }
}
