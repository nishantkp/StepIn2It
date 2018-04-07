package com.stepin2it.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stepin2it.R;
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

    @BindView(R.id.temp_details)
    TextView tempDetails;

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
        tempDetails.setText(mProductInfo.getProductName());
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
