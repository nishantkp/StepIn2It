package com.stepin2it.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.stepin2it.R;
import com.stepin2it.utils.IConstants;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ProductImageActivity extends AppCompatActivity {

    @BindView(R.id.imv_product_image_large)
    ImageView imvProductImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_image);

        // Use butter-knife to bind views
        ButterKnife.bind(ProductImageActivity.this);

        /* Get the intent and check if it has key or not,
        and then get the image form file system and set the ImageView
        */
        Intent intent = getIntent();
        if (intent.hasExtra(IConstants.KEY_PRODUCT_IMAGE_INTENT)) {
            String fileName = intent.getStringExtra(IConstants.KEY_PRODUCT_IMAGE_INTENT);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(this.openFileInput(fileName));
                imvProductImageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Timber.e(e, "File not found : %s", fileName);
            }
        }
    }
}
