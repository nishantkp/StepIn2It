package com.stepin2it.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.request.RequestOptions;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.stepin2it.R;
import com.stepin2it.ui.fragments.ProductInfoFragment;
import com.stepin2it.ui.fragments.ProductMapFragment;
import com.stepin2it.ui.fragments.WebFragment;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.utils.IConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailsActivity extends AppCompatActivity {

    @BindView(R.id.vp_product_detail)
    ViewPager vpProductDetail;

    @BindView(R.id.tbl_product_detail)
    TabLayout tblProductDetail;

    @BindView(R.id.plv_collapse_toolbar)
    CollapsingToolbarLayout plvCollapseToolbar;

//    @BindView(R.id.plv_header_image)
//    ImageView plvHeaderImage;

    @BindView(R.id.plv_toolbar)
    Toolbar plvToolbar;

    @BindView(R.id.slider)
    SliderLayout sliderLayout;

    ProductInfo mProductInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ButterKnife.bind(ProductDetailsActivity.this);

        // Setup tool bar, give title and make going back to home button visible in toolbar
        setSupportActionBar(plvToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.product_detail));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE)) {
            mProductInfo = intent.getParcelableExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE);
        }

        loadImageSlider();
        // Attach a listener on glide, so when the image is ready, convert it into bitmap
        // Use palette API to generate vibrant colors from Bitmap and set that colors to toolbar and
        // status bar
//        Glide.with(this).load(mProductInfo.getFirstImageUrl()).listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model
//                    , Target<Drawable> target, boolean isFirstResource) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target
//                    , DataSource dataSource, boolean isFirstResource) {
//                try {
//                    Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
//                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                        @SuppressWarnings("ResourceType")
//                        @Override
//                        public void onGenerated(Palette palette) {
//                            int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
//                            int vibrantDarkColor = palette.getDarkVibrantColor(R.color.colorPrimaryDark);
//                            plvCollapseToolbar.setContentScrimColor(vibrantColor);
//                            plvCollapseToolbar.setStatusBarScrimColor(vibrantDarkColor);
//                        }
//                    });
//
//                } catch (Exception e) {
//                    // if Bitmap fetch fails, fallback to primary colors
//                    Timber.e(e.fillInStackTrace(), "onCreate: failed to create bitmap from background");
//                    plvCollapseToolbar.setContentScrimColor(
//                            ContextCompat.getColor(ProductDetailsActivity.this, R.color.colorPrimary)
//                    );
//                    plvCollapseToolbar.setStatusBarScrimColor(
//                            ContextCompat.getColor(ProductDetailsActivity.this, R.color.colorPrimaryDark)
//                    );
//                }
//                return false;
//            }
//        }).into(plvHeaderImage);
//
        FragmentManager fragmentManager = getSupportFragmentManager();
        vpProductDetail.setAdapter(new ProductDetailPagerAdapter(fragmentManager));
        tblProductDetail.setupWithViewPager(vpProductDetail);
    }

    private void loadImageSlider() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();

        for (int i = 0; i < mProductInfo.getImageList().size(); i++) {
            TextSliderView sliderView = new TextSliderView(this);
            // if you want show image only / without description text use DefaultSliderView instead

            // initialize SliderLayout
            sliderView
                    .image(mProductInfo.getImageList().get(i))
                    .description(String.valueOf(i))
                    .setRequestOption(requestOptions)
                    .setBackgroundColor(Color.WHITE)
                    .setProgressBarVisible(true);

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString("extra", mProductInfo.getImageList().get(i));
            sliderLayout.addSlider(sliderView);
        }
        // set Slider Transition Animation
        // mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);

        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);
    }

    // Jump to web-tab when user clicks on web button from info-fragment
    public void openWebViewTab() {
        vpProductDetail.setCurrentItem(3);
    }

    private class ProductDetailPagerAdapter extends FragmentStatePagerAdapter {

        public ProductDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ProductInfoFragment.newInstance(mProductInfo);
                case 1:
                    return ProductInfoFragment.newInstance(mProductInfo);
                case 2:
                    return ProductMapFragment.newInstance(mProductInfo.getWarehouseLocation());
                case 3:
                    return WebFragment.newInstance(mProductInfo.getProductWebUrl());
            }
            return null;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.info_label);
                case 1:
                    return getString(R.string.image_label);
                case 2:
                    return getString(R.string.location_label);
                case 3:
                    return "Web";
            }
            return "";
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
