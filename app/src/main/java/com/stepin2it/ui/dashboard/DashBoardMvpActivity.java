package com.stepin2it.ui.dashboard;

import android.content.Intent;
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
import com.stepin2it.ui.activity.ProductDetailsActivity;
import com.stepin2it.ui.activity.ProductImageActivity;
import com.stepin2it.ui.adapter.ProductAdapter;
import com.stepin2it.ui.base.BaseActivity;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.utils.IConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashBoardMvpActivity
        extends BaseActivity implements DashboardMvpView, ProductAdapter.IProductAdapterClickHandler {
    private ProductAdapter mProductAdapter;
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

        txvEmptyView.setVisibility(View.GONE);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                DashBoardMvpActivity.this
                , LinearLayoutManager.VERTICAL
                , false);
        rvProductList.setLayoutManager(layoutManager);
        rvProductList.setHasFixedSize(true);

        mProductAdapter = new ProductAdapter(DashBoardMvpActivity.this
                , DashBoardMvpActivity.this, null);
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

    /**
     * Implement onItemClick method of IProductAdapterClickHandler interface to view the
     * details of product
     *
     * @param urlString   url of item which is clicked on
     * @param productInfo ProductInfo object containing details ab out product
     */
    @Override
    public void onItemClick(String urlString, ProductInfo productInfo) {
        Intent productDetailIntent = new Intent(DashBoardMvpActivity.this, ProductDetailsActivity.class);
        productDetailIntent.putExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE, productInfo);
        startActivity(productDetailIntent);
    }

    @Override
    public void onImageClick(int index, View view, ProductInfo productInfo) {
        if (productInfo.getImageList().size() > 0) {
            String url = productInfo.getFirstImageUrl();
            Intent imageFileNameIntent = new Intent(DashBoardMvpActivity.this, ProductImageActivity.class);
            imageFileNameIntent.putExtra(IConstants.KEY_PRODUCT_IMAGE_URL, url);
            startActivity(imageFileNameIntent);
        }
    }
}
