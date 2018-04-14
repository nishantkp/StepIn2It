package com.stepin2it.ui.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stepin2it.R;
import com.stepin2it.ui.adapter.ProductAdapter;
import com.stepin2it.ui.base.BaseActivity;
import com.stepin2it.ui.models.ProductInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardMvpActivity
        extends BaseActivity implements DashboardMvpView {
    private ProductAdapter mProductAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    //Show progress bar while fetching data
    @BindView(R.id.pgb_dash_board)
    ProgressBar pgbDashBoard;
    @BindView(R.id.txv_empty_view)
    TextView txvEmptyView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.tb_dashboard)
    Toolbar tbDashBoard;

    @BindView(R.id.rv_product_list)
    RecyclerView rvProductList;

    DashboardMvpPresenter mDashboardMvpPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);

        mDashboardMvpPresenter = new DashboardMvpPresenter(this);
        mDashboardMvpPresenter.attachView(this);

        setSupportActionBar(tbDashBoard);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.dashboard_title);

        rvProductList = findViewById(R.id.rv_product_list);
        txvEmptyView.setVisibility(View.GONE);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                DashBoardMvpActivity.this
                , LinearLayoutManager.VERTICAL
                , false);
        rvProductList.setLayoutManager(layoutManager);
        rvProductList.setHasFixedSize(true);

        mProductAdapter = new ProductAdapter(DashBoardMvpActivity.this
                , null, null);
        rvProductList.setAdapter(mProductAdapter);
        mDashboardMvpPresenter.getProductInfo();
    }

    @Override
    public void loadProductData(List<ProductInfo> productInfoList) {
        if (productInfoList != null) {
            pgbDashBoard.setVisibility(View.GONE);
            txvEmptyView.setVisibility(View.GONE);
            mProductAdapter.swapData(productInfoList);
        } else {
            txvEmptyView.setVisibility(View.VISIBLE);
            txvEmptyView.setText("No data!");
        }
    }

    @Override
    public void displayError(String message) {
        Toast.makeText(this, "Error : " + message, Toast.LENGTH_SHORT).show();
    }
}
