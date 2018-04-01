package com.stepin2it.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.stepin2it.R;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.utils.IConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailActivity extends AppCompatActivity {

    @BindView(R.id.txv_product_detail)
    TextView txvProductDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ButterKnife.bind(ProductDetailActivity.this);

        ProductInfo productInfo;
        Intent intent = getIntent();
        if (intent.hasExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE)) {
            productInfo = intent.getParcelableExtra(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE);
            String name = productInfo.getProductName();
            String description = productInfo.getDescription();
            txvProductDetail.append(name);
            txvProductDetail.append("\n" + description);
        }
    }
}
