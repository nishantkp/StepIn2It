package com.stepin2it.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stepin2it.R;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.utils.IConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailActivity extends AppCompatActivity {

    @BindView(R.id.txv_name_detail_info)
    TextView txvNameDetailInfo;

    @BindView(R.id.txv_description_detail_info)
    TextView txvDescriptionDetailInfo;

    @BindView(R.id.txv_phone_detail_info)
    TextView txvPhoneDetailInfo;

    @BindView(R.id.txv_web_detail_info)
    TextView txvWebDetailInfo;

    @BindView(R.id.txv_price_detail_info)
    TextView txvPriceDetailInfo;

    @BindView(R.id.txv_dimension_length)
    TextView txvDimensionLength;

    @BindView(R.id.txv_dimension_width)
    TextView txvDimensionWidth;

    @BindView(R.id.txv_dimension_height)
    TextView txvDimensionHeight;

    @BindView(R.id.txv_latitude_detail_info)
    TextView txvLatitudeDetailInfo;

    @BindView(R.id.txv_longitude_detail_info)
    TextView txvLongitudeDetailInfo;

    @BindView(R.id.imv_image_detail_info)
    ImageView imvImageDetailInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Bing the butter-knife with current activity
        ButterKnife.bind(ProductDetailActivity.this);

        ProductInfo productInfo;
        Intent intent = getIntent();
        if (intent.hasExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE)) {
            productInfo = intent.getParcelableExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE);
            displayInfo(productInfo);
        }
    }

    /**
     * Call this method to populate detail activity layout
     *
     * @param productInfo ProductInfo object containing all the details about product
     */
    private void displayInfo(ProductInfo productInfo) {
        // Get the each detail from productInfo and display it appropriately
        txvNameDetailInfo.setText(productInfo.getProductName());
        txvDescriptionDetailInfo.setText(productInfo.getDescription());
        // Use glide to display product image on ImageView
        Glide.with(ProductDetailActivity.this).load(productInfo.getProductImageUrl()).into(imvImageDetailInfo);
        txvPriceDetailInfo.setText(productInfo.getPrice());
        txvPhoneDetailInfo.setText(productInfo.getProductPhone());
        txvWebDetailInfo.setText(productInfo.getProductWebUrl());
        txvWebDetailInfo.setText(productInfo.getProductWebUrl());
        txvDimensionLength.setText(productInfo.getDimensions().getLength());
        txvDimensionWidth.setText(productInfo.getDimensions().getWidth());
        txvDimensionHeight.setText(productInfo.getDimensions().getHeight());
        txvLongitudeDetailInfo.setText(productInfo.getWarehouseLocation().getLongitude());
        txvLatitudeDetailInfo.setText(productInfo.getWarehouseLocation().getLatitude());
    }
}
