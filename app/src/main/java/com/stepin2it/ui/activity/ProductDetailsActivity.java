package com.stepin2it.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.stepin2it.R;
import com.stepin2it.ui.fragments.ProductInfoFragment;
import com.stepin2it.ui.fragments.ProductMapFragment;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.utils.IConstants;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailsActivity extends AppCompatActivity {

    @BindView(R.id.vp_product_detail)
    ViewPager vpProductDetail;

    @BindView(R.id.tbl_product_detail)
    TabLayout tblProductDetail;

    ProductInfo mProductInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ButterKnife.bind(ProductDetailsActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE)) {
            mProductInfo = intent.getParcelableExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        vpProductDetail.setAdapter(new ProductDetailPagerAdapter(fragmentManager));
        tblProductDetail.setupWithViewPager(vpProductDetail);
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
            }
            return "";
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
