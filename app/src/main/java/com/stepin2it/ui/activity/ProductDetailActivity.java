package com.stepin2it.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

    @BindView(R.id.btn_call_detail_info)
    Button btnCallDetailInfo;

    @BindView(R.id.btn_web_detail_info)
    Button btnWebDetailInfo;

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

    private ProductInfo mProductInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Bing the butter-knife with current activity
        ButterKnife.bind(ProductDetailActivity.this);

        Intent intent = getIntent();
        if (intent.hasExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE)) {
            mProductInfo = intent.getParcelableExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE);
            displayInfo();
        }
    }

    /**
     * Call this method to populate detail activity layout
     */
    private void displayInfo() {
        // Get the each detail from productInfo and display it appropriately
        txvNameDetailInfo.setText(mProductInfo.getProductName());
        txvDescriptionDetailInfo.setText(mProductInfo.getDescription());
        // Use glide to display product image on ImageView
        Glide.with(ProductDetailActivity.this).load(mProductInfo.getProductImageUrl()).into(imvImageDetailInfo);
        txvPriceDetailInfo.setText(mProductInfo.getPrice());
        txvDimensionLength.setText(mProductInfo.getDimensions().getLength());
        txvDimensionWidth.setText(mProductInfo.getDimensions().getWidth());
        txvDimensionHeight.setText(mProductInfo.getDimensions().getHeight());
        txvLongitudeDetailInfo.setText(mProductInfo.getWarehouseLocation().getLongitude());
        txvLatitudeDetailInfo.setText(mProductInfo.getWarehouseLocation().getLatitude());

        btnCallDetailInfo.setText(mProductInfo.getProductPhone());
        // Attach a click listener on call button to make a phone call when user clicks on it
        btnCallDetailInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mProductInfo.getProductPhone()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        // Attach a click listener on web button open a url in web browser when user clicks on it
        btnWebDetailInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webPage = Uri.parse(mProductInfo.getProductWebUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
}
