package com.stepin2it.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.stepin2it.R;
import com.stepin2it.ui.fragments.MasterListFragment;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.utils.IConstants;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DashBoardActivity extends BaseActivity
        implements MasterListFragment.OnItemClickListener {

    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        // Bind view with butter knife library
        ButterKnife.bind(DashBoardActivity.this);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(R.string.dashboard_title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            Timber.d("in onDestroy()");
        }
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

    @Override
    public void onFragmentItemClick(ProductInfo productInfo) {
        Intent productDetailIntent = new Intent(DashBoardActivity.this, ProductDetailsActivity.class);
        productDetailIntent.putExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE, productInfo);
        startActivity(productDetailIntent);
    }

    @Override
    public void onFragmentImageClick(ProductInfo productInfo) {
        if (productInfo.getImageList().size() > 0) {
            String url = productInfo.getImageList().get(0);
            Intent imageFileNameIntent = new Intent(DashBoardActivity.this, ProductImageActivity.class);
            imageFileNameIntent.putExtra(IConstants.KEY_PRODUCT_IMAGE_URL, url);
            startActivity(imageFileNameIntent);
        }
    }
}
