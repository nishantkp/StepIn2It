package com.stepin2it.ui.activity;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stepin2it.R;
import com.stepin2it.data.NetworkUtils;
import com.stepin2it.data.local.IDatabase;
import com.stepin2it.ui.adapter.ProductAdapter;
import com.stepin2it.ui.models.Dimensions;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.ui.models.WarehouseLocation;
import com.stepin2it.utils.IConstants;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class DashBoardActivity extends BaseActivity
        implements LoaderManager.LoaderCallbacks<List<ProductInfo>>,
        ProductAdapter.IProductAdapterClickHandler {

    // Loader ID
    private static final int PRODUCT_LIST_LOADER_ID = 456;
    private RecyclerView mRecyclerView;
    private ProductAdapter mProductAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    //Show progress bar while fetching data
    @BindView(R.id.pgb_dash_board)
    ProgressBar pgbDashBoard;
    @BindView(R.id.txv_empty_view)
    TextView txvEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        // Bind view with butter knife library
        ButterKnife.bind(DashBoardActivity.this);

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
        displayProductList();
    }

    /**
     * Call this method to display data on UI
     */
    private void displayProductList() {
        if (NetworkUtils.isNetworkAvailable(DashBoardActivity.this)) {
            txvEmptyView.setVisibility(View.GONE);
            if (!isProductListCacheAvailable()) {
                // If we don't have any cached data, fetch data from web
                getProductInfo();
            } else {
                pgbDashBoard.setVisibility(View.GONE);
                mProductAdapter.swapData(loadDataFromCache());
            }
        } else {
            // If we don't have internet connection load data from cache, hide progressbar
            // and network connection textView and notify user to check
            // network connection
            if (isProductListCacheAvailable()) {
                txvEmptyView.setVisibility(View.GONE);
                pgbDashBoard.setVisibility(View.GONE);
                mProductAdapter.swapData(loadDataFromCache());
                Toast.makeText(DashBoardActivity.this,
                        getResources().getString(R.string.enable_your_internet_connection),
                        Toast.LENGTH_SHORT).show();
            } else {
                txvEmptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Use this method to check product list is present in database or not
     *
     * @return true if the cache available and false if not
     */
    private boolean isProductListCacheAvailable() {
        Cursor productCache = mDatabaseHelper.readProducts();
        return productCache.getCount() > 0;
    }

    /**
     * This method will read the data from cursor and make a list to update the adapter
     *
     * @return List<ProductInfo> list
     */
    private List<ProductInfo> loadDataFromCache() {
        Cursor productCache = mDatabaseHelper.readProducts();
        List<ProductInfo> productInfoList = new ArrayList<>();
        productCache.moveToFirst();
        do {
            // Get each element from Cursor and create an array list
            String productName =
                    productCache.getString(productCache.getColumnIndex(IDatabase.IProductTable.PRODUCT_NAME));
            String productDescription =
                    productCache.getString(productCache.getColumnIndex(IDatabase.IProductTable.PRODUCT_DESCRIPTION));
            String productImage =
                    productCache.getString(productCache.getColumnIndex(IDatabase.IProductTable.PRODUCT_IMAGE));
            String productPhone =
                    productCache.getString(productCache.getColumnIndex(IDatabase.IProductTable.PRODUCT_PHONE));
            String productWebUrl =
                    productCache.getString(productCache.getColumnIndex(IDatabase.IProductTable.PRODUCT_WEB));
            String productPrice =
                    productCache.getString(productCache.getColumnIndex(IDatabase.IProductTable.PRODUCT_PRICE));
            String productLength =
                    productCache.getString(productCache.getColumnIndex(IDatabase.IProductTable.PRODUCT_LENGTH));
            String productWidth =
                    productCache.getString(productCache.getColumnIndex(IDatabase.IProductTable.PRODUCT_WIDTH));
            String productHeight =
                    productCache.getString(productCache.getColumnIndex(IDatabase.IProductTable.PRODUCT_HEIGHT));
            String productLatitude =
                    productCache.getString(productCache.getColumnIndex(IDatabase.IProductTable.WAREHOUSE_LATITUDE));
            String productLongitude =
                    productCache.getString(productCache.getColumnIndex(IDatabase.IProductTable.WAREHOUSE_LONGITUDE));
            // Add new ProductInfo object into List
            productInfoList.add(new ProductInfo(
                    productName,
                    productDescription,
                    productImage,
                    productPhone,
                    productWebUrl,
                    productPrice,
                    null,
                    new Dimensions(productLength, productWidth, productHeight),
                    new WarehouseLocation(productLatitude, productLongitude)));
        } while (productCache.moveToNext());
        return productInfoList;
    }

    /**
     * Call this method to make an API call to get the new batch of data and to display it on UI
     */
    private void getProductInfo() {
        pgbDashBoard.setVisibility(View.VISIBLE);
        Call<List<ProductInfo>> call = mApiInterface.getProductInfo();
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

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<ProductInfo>> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case PRODUCT_LIST_LOADER_ID:
                return new AsyncTaskLoader<List<ProductInfo>>(DashBoardActivity.this) {
                    @Override
                    protected void onStartLoading() {
                        Timber.d("onStartLoading()");
                        pgbDashBoard.setVisibility(View.VISIBLE);
                        forceLoad();
                    }

                    @Override
                    public List<ProductInfo> loadInBackground() {
                        Timber.d("loadInBackground()");
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
        Timber.d("onLoadFinished()");
        pgbDashBoard.setVisibility(View.GONE);
        List<ProductInfo> list = new ArrayList<>();
        list.addAll(productInfoList);
        list.addAll(productInfoList);
        list.addAll(productInfoList);
        mProductAdapter.swapData(list);
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
    public void onItemClick(String urlString, ProductInfo productInfo) {
        Intent productDetailIntent = new Intent(DashBoardActivity.this, ProductDetailActivity.class);
        productDetailIntent.putExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE, productInfo);
        startActivity(productDetailIntent);
    }

    /**
     * Implement onItemClick method of IProductAdapterClickHandler interface to show image
     * when user clicks on image
     * If the view is an instance of ImageView then get the BitmapDrawable from view and write
     * that Bitmap into file system, and send the name of generated file to {@link ProductImageActivity}
     * to retrieve image form that file
     *
     * @param index index of item which is clicked on
     * @param view  list item view which is clicked on (i.e ImageView in this context)
     */
    @Override
    public void onImageClick(int index, View view, ProductInfo productInfo) {
        if (view instanceof ImageView) {
            String url = productInfo.getProductImageUrl();
            // BitmapDrawable bitmapDrawable = ((BitmapDrawable) ((ImageView) view).getDrawable());
            // Bitmap bitmap = bitmapDrawable.getBitmap();
            // write image info file system
            // String fileName = writeBitmapIntoFile(bitmap);
            Intent imageFileNameIntent = new Intent(DashBoardActivity.this, ProductImageActivity.class);
            // imageFileNameIntent.putExtra(IConstants.KEY_PRODUCT_IMAGE_INTENT, fileName);
            imageFileNameIntent.putExtra(IConstants.KEY_PRODUCT_IMAGE_URL, url);
            startActivity(imageFileNameIntent);
        }
    }

    /**
     * Use this method to store bitmap into file system after compressing the image.
     * That way we can effectively store image in memory and we can retrieve it in another activity.
     * Here MODE_PRIVATE is used to make image hidden from other applications on device
     *
     * @param bitmap image that we want to store in memory
     * @return file name
     */
    public String writeBitmapIntoFile(Bitmap bitmap) {
        // No .png or .jpg extension needed for file name
        String fileName = IConstants.PRODUCT_IMAGE_FILE_NAME;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(bytes.toByteArray());
            // Always close the file after finishing writing operation
            fileOutputStream.close();
        } catch (Exception e) {
            Timber.e(e, "Exception occurred while writing into file system");
            fileName = null;
        }
        return fileName;
    }

    // Create a menu whenever the activity is created
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();
        switch (menuItemId) {
            case R.id.action_settings:
                /* When user clicks on Settings option, launch {@link SettingsActivity}
                 */
                Intent settingsIntent = new Intent(DashBoardActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
