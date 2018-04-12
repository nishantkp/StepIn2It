package com.stepin2it.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.stepin2it.R;
import com.stepin2it.ui.activity.ProductDetailsActivity;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.utils.IConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductInfoFragment extends Fragment {

    private ProductInfo mProductInfo;

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

    @BindView(R.id.txv_dimension_weight)
    TextView txvDimensionWeight;

    public ProductInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param productInfo ProductInfo object
     * @return A new instance of fragment ProductInfoFragment.
     */
    public static ProductInfoFragment newInstance(ProductInfo productInfo) {
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE, productInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProductInfo = getArguments().getParcelable(IConstants.KEY_PRODUCT_DETAIL_PARCELABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_info, container, false);
        ButterKnife.bind(this, view);
        displayInfo();
        return view;
    }

    /**
     * Call this method to populate detail activity layout
     */
    private void displayInfo() {
        // Get the each detail from productInfo and display it appropriately
        txvNameDetailInfo.setText(mProductInfo.getProductName());
        txvDescriptionDetailInfo.setText(mProductInfo.getDescription());
        txvPriceDetailInfo.setText(String.format("$%s", mProductInfo.getPrice()));
        txvDimensionLength.setText(mProductInfo.getDimensions().getLength());
        txvDimensionWidth.setText(mProductInfo.getDimensions().getWidth());
        txvDimensionHeight.setText(mProductInfo.getDimensions().getHeight());
        txvDimensionWeight.setText(mProductInfo.getWeight());

        btnCallDetailInfo.setText(mProductInfo.getProductPhone());
        // Attach a click listener on call button to make a phone call when user clicks on it
        btnCallDetailInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + mProductInfo.getProductPhone()));
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        // Attach a click listener on web button open a url in web browser when user clicks on it
        btnWebDetailInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetailsActivity productDetailsActivity = (ProductDetailsActivity) getActivity();
                if (productDetailsActivity != null) {
                    productDetailsActivity.openWebViewTab();
                } else {
                    Uri webPage = Uri.parse(mProductInfo.getProductWebUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            }
        });

        // If we have multiple images for a single product, select the first image to display
//        if (mProductInfo.getImageList().size() > 0) {
//            Glide.with(getActivity()).load(mProductInfo.getImageList().get(0)).into(imvImageDetailInfo);
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
