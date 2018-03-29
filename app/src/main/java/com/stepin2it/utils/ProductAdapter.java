package com.stepin2it.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stepin2it.ProductInfo;
import com.stepin2it.R;

import java.util.List;

/**
 * Adapter to display product list
 * Created by Nishant on 3/27/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context mContext;
    private List<ProductInfo> mProductInfoList;

    public ProductAdapter(Context context, List<ProductInfo> productInfoList) {
        mContext = context;
        mProductInfoList = productInfoList;
    }

    // creates new view
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        // Get the Product info at particular location
        ProductInfo productInfo = mProductInfoList.get(position);

        // Retrieve the information from object
        String name = productInfo.getProductName();
        String description = productInfo.getProductDescription();
        String imageUrl = productInfo.getProductImageUrl();

        // Set the text to appropriate field
        holder.txtProductName.setText(name);
        holder.txtProductDescription.setText(description);

        // Download image from web and display it in appropriate ImageView
        // with the help of Picasso library
        Picasso.get().load(imageUrl).into(holder.imgProductImage);
    }

    // return the size of data set
    @Override
    public int getItemCount() {
        if (mProductInfoList == null) {
            return 0;
        }
        return mProductInfoList.size();
    }

    /**
     * This method is used to update the product list after received from web
     * and when new list is available, notify adapter about data set has benn changed
     *
     * @param productInfoList List of products
     */
    public void swapData(List<ProductInfo> productInfoList) {
        mProductInfoList = productInfoList;
        notifyDataSetChanged();
    }

    // View holder class
    class ProductViewHolder extends RecyclerView.ViewHolder {

        // Use ButterKnife library to bind views
        //  @BindView(R.id.txt_product_name)
        TextView txtProductName;
        // @BindView(R.id.txt_product_description)
        TextView txtProductDescription;
        ImageView imgProductImage;

        ProductViewHolder(View itemView) {
            super(itemView);
            // Bind the butter knife library with layout
            //ButterKnife.bind(mContext, itemView);
            txtProductName = itemView.findViewById(R.id.txt_product_name);
            txtProductDescription = itemView.findViewById(R.id.txt_product_description);
            imgProductImage = itemView.findViewById(R.id.img_product_image);
        }
    }
}
