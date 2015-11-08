package com.socks.jiandan.ui.fragment;

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
import com.socks.jiandan.adapter.JokeAdapter;
import com.socks.jiandan.base.BaseFragment;
import com.socks.jiandan.callback.LoadMoreListener;
import com.socks.jiandan.callback.LoadResultCallBack;
import com.socks.jiandan.utils.ShowToast;
import com.socks.jiandan.view.AutoLoadRecyclerView;
import com.victor.loading.rotate.RotateLoading;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class JokeFragment extends BaseFragment implements LoadResultCallBack {

    @InjectView(R.id.recycler_view)
    AutoLoadRecyclerView mRecyclerView;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.loading)
    RotateLoading loading;

    private JokeAdapter mAdapter;

    public JokeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        mAdapter = new JokeAdapter(getActivity(), mRecyclerView, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.loadFirst();
        loading.start();
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
        ShowToast.Short(LOAD_FAILED);
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
