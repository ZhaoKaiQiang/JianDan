package com.socks.jiandan.ui.fragment;

import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.socks.jiandan.R;
import com.socks.jiandan.adapter.PictureAdapter;
import com.socks.jiandan.base.BaseFragment;
import com.socks.jiandan.callback.LoadFinishCallBack;
import com.socks.jiandan.callback.LoadMoreListener;
import com.socks.jiandan.callback.LoadResultCallBack;
import com.socks.jiandan.model.NetWorkEvent;
import com.socks.jiandan.model.Picture;
import com.socks.jiandan.utils.JDMediaScannerConnectionClient;
import com.socks.jiandan.utils.NetWorkUtil;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.view.AutoLoadRecyclerView;
import com.socks.jiandan.view.imageloader.ImageLoadProxy;
import com.victor.loading.rotate.RotateLoading;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class PictureFragment extends BaseFragment implements LoadResultCallBack, LoadFinishCallBack {

    @InjectView(R.id.recycler_view)
    AutoLoadRecyclerView mRecyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.loading)
    RotateLoading loading;

    private PictureAdapter mAdapter;
    //用于记录是否是首次进入
    private boolean isFirstChange;
    //记录最后一次提示显示时间，防止多次提示
    private long lastShowTime;
    private MediaScannerConnection connection;
    protected Picture.PictureType mType;

    public PictureFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        isFirstChange = true;
        mType = Picture.PictureType.BoringPicture;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auto_load, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void loadMore() {
                mAdapter.loadNextPage();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.loadFirst();
            }
        });

        mRecyclerView.setOnPauseListenerParams(false, true);
        mAdapter = new PictureAdapter(getActivity(), this, mRecyclerView, mType);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setmSaveFileCallBack(this);
        mAdapter.loadFirst();
        loading.start();
    }

    public void onEventMainThread(NetWorkEvent event) {

        if (event.getType() == NetWorkEvent.AVAILABLE) {
            if (NetWorkUtil.isWifiConnected(getActivity())) {
                mAdapter.setIsWifi(true);
                if (!isFirstChange && (System.currentTimeMillis() - lastShowTime) > 3000) {
                    ShowToast.Short("已切换为WIFI模式，自动加载GIF图片");
                    lastShowTime = System.currentTimeMillis();
                }
            } else {
                mAdapter.setIsWifi(false);
                if (!isFirstChange && (System.currentTimeMillis() - lastShowTime) > 3000) {
                    ShowToast.Short("已切换为省流量模式，只加载GIF缩略图");
                    lastShowTime = System.currentTimeMillis();
                }
            }
            isFirstChange = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //清除内存缓存，避免由于内存缓存造成的图片显示不完整
        ImageLoadProxy.getImageLoader().clearMemoryCache();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            mSwipeRefreshLayout.setRefreshing(true);
            mAdapter.loadFirst();
            return true;
        }
        return false;
    }


    @Override
    public void onSuccess(int result, Object object) {
        loading.stop();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onError(int code, String msg) {
        loading.stop();
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void setType(Picture.PictureType mType) {
        this.mType = mType;
    }

    @Override
    public void loadFinish(Object obj) {
        Bundle bundle = (Bundle) obj;
        boolean isSmallPic = bundle.getBoolean(DATA_IS_SIAMLL_PIC);
        String filePath = bundle.getString(DATA_FILE_PATH);
        File newFile = new File(filePath);
        JDMediaScannerConnectionClient connectionClient = new JDMediaScannerConnectionClient(isSmallPic,
                newFile);
        connection = new MediaScannerConnection(getActivity(), connectionClient);
        connectionClient.setMediaScannerConnection(connection);
        connection.connect();
    }
}