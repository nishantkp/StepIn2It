package com.stepin2it;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stepin2it.utils.IConstants;
import com.stepin2it.utils.NetworkUtils;

import java.util.List;

public class DashBoardActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<ProductInfo>>
        , ProductAdapter.IProductAdapterClickHandler {

    // Loader ID
    private static final int PRODUCT_LIST_LOADER_ID = 456;
    private RecyclerView mRecyclerView;
    private ProductAdapter mProductAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        mRecyclerView = findViewById(R.id.rv_product_list);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                DashBoardActivity.this
                , LinearLayoutManager.VERTICAL
                , false);
        mRecyclerView.setLayoutManager(layoutManager);
        /* Use setHasFixedSize to improve performance,
         * if you know the changes in content does not change layout size
         */
        mRecyclerView.setHasFixedSize(true);

        mProductAdapter = new ProductAdapter(DashBoardActivity.this
                , DashBoardActivity.this, null);
        mRecyclerView.setAdapter(mProductAdapter);
        // Initialize loader
        getLoaderManager().initLoader(PRODUCT_LIST_LOADER_ID, null, DashBoardActivity.this);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<ProductInfo>> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case PRODUCT_LIST_LOADER_ID:
                return new AsyncTaskLoader<List<ProductInfo>>(DashBoardActivity.this) {
                    @Override
                    protected void onStartLoading() {
                        forceLoad();
                    }

                    @Override
                    public List<ProductInfo> loadInBackground() {
                        return NetworkUtils.fetchProductInfoFromUrl(IConstants.IJsonServer.REQUEST_URL);
                    }
                };
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    /**
     * Called when loader has finished loading data
     *
     * @param loader          the loader that has finished
     * @param productInfoList data generated ny loader
     */
    @Override
    public void onLoadFinished(Loader<List<ProductInfo>> loader, List<ProductInfo> productInfoList) {
        mProductAdapter.swapData(productInfoList);
        if (mPosition == RecyclerView.NO_POSITION) {
            mPosition = 0;
        }
        mRecyclerView.smoothScrollToPosition(mPosition);
    }

    /**
     * Called when previously created loader is being reset, thus making it's data invalid
     * so application should remove any reference it has to it's loader data
     *
     * @param loader loader that being reset
     */
    @Override
    public void onLoaderReset(Loader<List<ProductInfo>> loader) {
        // Now loader's data is invalid, we have to clear adapter that is displaying data
        mProductAdapter.swapData(null);
    }

    /**
     * Implement onItemClick method of IProductAdapterClickHandler interface to get the position of
     * clicked item and start the web browser
     *
     * @param urlString url of item which is clicked on
     */
    @Override
    public void onItemClick(String urlString) {
        Uri webPage = Uri.parse(urlString);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
