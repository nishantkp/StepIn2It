package com.stepin2it.ui.fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stepin2it.R;
import com.stepin2it.data.NetworkUtils;
import com.stepin2it.data.local.DatabaseHelper;
import com.stepin2it.data.remote.ApiClient;
import com.stepin2it.data.remote.ApiInterface;
import com.stepin2it.ui.adapter.ProductAdapter;
import com.stepin2it.ui.models.ProductInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterListFragment extends Fragment
        implements ProductAdapter.IProductAdapterClickHandler {

    @BindView(R.id.rv_product_list)
    RecyclerView rvProductList;
    @BindView(R.id.pgb_dash_board)
    ProgressBar pgbDashBoard;
    @BindView(R.id.txv_empty_view)
    TextView txvEmptyView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    ProductAdapter mProductAdapter;
    ApiInterface apiInterface;
    DatabaseHelper mDatabaseHelper;
    OnItemClickListener mItemClickCallBack;

    public interface OnItemClickListener {
        void onFragmentItemClick(ProductInfo productInfo);

        void onFragmentImageClick(ProductInfo productInfo);
    }

    // Empty constructor
    public MasterListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        mDatabaseHelper = DatabaseHelper.getInstance(getActivity());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mItemClickCallBack = (OnItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_list, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                view.getContext()
                , LinearLayoutManager.VERTICAL
                , false);
        rvProductList.setLayoutManager(layoutManager);

        mProductAdapter = new ProductAdapter(getActivity(), this, null);
        rvProductList.setAdapter(mProductAdapter);

        // Attach listener on swipe refresh container to perform refresh operation
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListOnSwipeAction();
            }
        });
        displayProductList();
        return view;
    }

    /**
     * Call this method to refresh the content when user performs swipe to refresh operation
     */
    private void refreshListOnSwipeAction() {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            Call<List<ProductInfo>> call = apiInterface.getProductInfo();
            call.enqueue(new Callback<List<ProductInfo>>() {
                @Override
                public void onResponse(Call<List<ProductInfo>> call, Response<List<ProductInfo>> response) {
                    if (response.body() != null) {
                        // If we get something in response, delete the data from cache(i.e stored in
                        // database table) and insert new batch of data
                        pgbDashBoard.setVisibility(View.GONE);
                        mDatabaseHelper.deleteAllData();
                        mProductAdapter.clearData();
                        mDatabaseHelper.insertProducts(response.body());
                        mProductAdapter.swapData(response.body());
                        // Call setRefreshing(false) to signal refresh has finished
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<List<ProductInfo>> call, Throwable t) {
                }
            });
        } else {
            Toast.makeText(getActivity(),
                    getResources().getString(R.string.enable_your_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Call this method to display data on UI
     */
    private void displayProductList() {
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            txvEmptyView.setVisibility(View.GONE);
            getProductInfo();
        } else {
            List<ProductInfo> productInfoList = mDatabaseHelper.readProducts();
            // If we don't have internet connection load data from cache, hide progressbar
            // and network connection textView and notify user to check
            // network connection
            if (productInfoList.size() > 0) {
                txvEmptyView.setVisibility(View.GONE);
                pgbDashBoard.setVisibility(View.GONE);
                mProductAdapter.swapData(productInfoList);
                Toast.makeText(getActivity(),
                        getResources().getString(R.string.enable_your_internet_connection),
                        Toast.LENGTH_SHORT).show();
            } else {
                txvEmptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Call this method to make an API call to get the new batch of data and to display it on UI
     */
    private void getProductInfo() {
        pgbDashBoard.setVisibility(View.VISIBLE);
        Call<List<ProductInfo>> call = apiInterface.getProductInfo();
        call.enqueue(new Callback<List<ProductInfo>>() {
            @Override
            public void onResponse(Call<List<ProductInfo>> call, Response<List<ProductInfo>> response) {
                pgbDashBoard.setVisibility(View.GONE);
                mDatabaseHelper.insertProducts(response.body());
                mProductAdapter.swapData(response.body());
            }

            @Override
            public void onFailure(Call<List<ProductInfo>> call, Throwable t) {
                pgbDashBoard.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(String urlString, ProductInfo productInfo) {
        mItemClickCallBack.onFragmentItemClick(productInfo);
    }

    @Override
    public void onImageClick(int index, View view, ProductInfo productInfo) {
        mItemClickCallBack.onFragmentImageClick(productInfo);
    }
}
