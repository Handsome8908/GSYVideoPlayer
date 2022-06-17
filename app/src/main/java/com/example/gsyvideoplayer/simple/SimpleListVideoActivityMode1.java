package com.example.gsyvideoplayer.simple;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.gsyvideoplayer.R;
import com.example.gsyvideoplayer.simple.adapter.SimpleListVideoModeAdapter;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.logging.Logger;

/**
 * 简单列表实现模式1
 */
public class SimpleListVideoActivityMode1 extends AppCompatActivity {

    ListView videoList;

    SimpleListVideoModeAdapter listNormalAdapter;
    private String TAG="SimpleListVideoActivityMode1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_video);

        videoList = (ListView)findViewById(R.id.video_list);

        listNormalAdapter = new SimpleListVideoModeAdapter(this);
        videoList.setAdapter(listNormalAdapter);

        videoList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            /**
             *
             * @param view
             * @param firstVisibleItem:第一个可见视频item的position索引
             * @param visibleItemCount：屏幕可见的视频item的数量4个或者5个
             * @param totalItemCount：列表中视频的总数量：模拟40个
             * GSYVideoManager.instance().getPlayPosition()：当前正在播放的视频的position索引，没有播放
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                Log.d(TAG,"firstVisibleItem=>"+firstVisibleItem+";visibleItemCount=>"+visibleItemCount+";totalItemCount=>"+totalItemCount);
                //当前正在播放的视频的position索引，大于等于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    Log.d(TAG,"GSYVideoManager.instance().getPlayPosition()=>"+GSYVideoManager.instance().getPlayPosition());
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(SimpleListVideoModeAdapter.TAG)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {
                        if(GSYVideoManager.isFullState(SimpleListVideoActivityMode1.this)) {
                            return;
                        }
                        //如果滑出去了上面和下面就是否，和今日头条一样
                        GSYVideoManager.releaseAllVideos();
                        listNormalAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}
