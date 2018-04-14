package com.stepin2it.ui.activity;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.stepin2it.R;
import com.stepin2it.data.NetworkUtils;
import com.stepin2it.ui.adapter.ProductAdapter;
import com.stepin2it.ui.base.BaseActivity;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.utils.IConstants;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
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
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeContainer;

    @BindView(R.id.tb_dashboard)
    Toolbar tbDashBoard;

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        // Bind view with butter knife library
        ButterKnife.bind(DashBoardActivity.this);

        setSupportActionBar(tbDashBoard);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.dashboard_title);

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

        // Attach listener on swipe refresh container to perform refresh operation
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshListonSwipeAction();
            }
        });

        displayProductList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            Timber.d("in onDestroy()");
        }
    }

    /**
     * Call this method to refresh the content when user performs swipe to refresh operation
     */
    private void refreshListonSwipeAction() {
        if (NetworkUtils.isNetworkAvailable(DashBoardActivity.this)) {
            Call<List<ProductInfo>> call = mApiInterface.getProductInfo();
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
                        swipeContainer.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<List<ProductInfo>> call, Throwable t) {
                }
            });
        } else {
            Toast.makeText(DashBoardActivity.this,
                    getResources().getString(R.string.enable_your_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Call this method to display data on UI
     */
    private void displayProductList() {
        List<ProductInfo> productInfoList = mDatabaseHelper.readProducts();
        if (NetworkUtils.isNetworkAvailable(DashBoardActivity.this)) {
            txvEmptyView.setVisibility(View.GONE);
            if (productInfoList.size() == 0) {
                // If we don't have any cached data, fetch data from web
                getProductInfo();
            } else {
                pgbDashBoard.setVisibility(View.GONE);
                mProductAdapter.swapData(productInfoList);
            }
        } else {
            // If we don't have internet connection load data from cache, hide progressbar
            // and network connection textView and notify user to check
            // network connection
            if (productInfoList.size() > 0) {
                txvEmptyView.setVisibility(View.GONE);
                pgbDashBoard.setVisibility(View.GONE);
                mProductAdapter.swapData(productInfoList);
                Toast.makeText(DashBoardActivity.this,
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
        Intent productDetailIntent = new Intent(DashBoardActivity.this, ProductDetailsActivity.class);
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
            if (productInfo.getImageList().size() > 0) {
                String url = productInfo.getImageList().get(0);
                Intent imageFileNameIntent = new Intent(DashBoardActivity.this, ProductImageActivity.class);
                // imageFileNameIntent.putExtra(IConstants.KEY_PRODUCT_IMAGE_INTENT, fileName);
                imageFileNameIntent.putExtra(IConstants.KEY_PRODUCT_IMAGE_URL, url);
                startActivity(imageFileNameIntent);
            }
            // BitmapDrawable bitmapDrawable = ((BitmapDrawable) ((ImageView) view).getDrawable());
            // Bitmap bitmap = bitmapDrawable.getBitmap();
            // write image info file system
            // String fileName = writeBitmapIntoFile(bitmap);

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
            case R.id.action_test:
                // Perform text to getting started with Rx
                testRx();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Getting started with Rx "Sample"
     */
    private void testRx() {
        Observable<String> observable = Observable.just(1, 2, 3, 4, 5).flatMap(new Function<Integer, Observable<String>>() {
            @Override
            public Observable<String> apply(Integer integer) throws Exception {
                String data = null;
                switch (integer) {
                    case 1:
                        data = "one";
                        break;
                    case 2:
                        data = "two";
                        break;
                    case 3:
                        data = "three";
                        break;
                    default:
                        data = "No case";
                        break;
                }
                Thread.sleep(10000);
                return Observable.just(data);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(String string) {
                Timber.d("Next : %s", string);
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("Error");
            }

            @Override
            public void onComplete() {
                Timber.d("Complete!");
            }
        });
    }
}
