package com.stepin2it.ui.activity;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stepin2it.R;
import com.stepin2it.data.NetworkUtils;
import com.stepin2it.ui.adapter.ProductAdapter;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.utils.IConstants;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DashBoardActivity extends AppCompatActivity
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
        if (NetworkUtils.isNetworkAvailable(DashBoardActivity.this)) {
            txvEmptyView.setVisibility(View.GONE);
            // Initialize loader
            getLoaderManager().initLoader(PRODUCT_LIST_LOADER_ID, null, DashBoardActivity.this);
        } else {
            txvEmptyView.setVisibility(View.VISIBLE);
        }
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
    public void onItemClick(String urlString) {
        Uri webPage = Uri.parse(urlString);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
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
