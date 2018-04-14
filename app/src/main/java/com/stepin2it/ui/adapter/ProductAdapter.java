package com.stepin2it.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stepin2it.R;
import com.stepin2it.ui.models.ProductInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter to display product list
 * Created by Nishant on 3/27/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context mContext;
    private List<ProductInfo> mProductInfoList;
    private IProductAdapterClickHandler mClickHandler;

    public ProductAdapter(Context context, IProductAdapterClickHandler clickHandler
            , List<ProductInfo> productInfoList) {
        mContext = context;
        mProductInfoList = productInfoList;
        mClickHandler = clickHandler;
    }

    // creates new view
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.product_list_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        // Get the Product info at particular location
        final ProductInfo productInfo = mProductInfoList.get(position);

        // Retrieve the information from object
        String name = productInfo.getProductName();
        String description = productInfo.getDescription();

        // Set the text to appropriate field
        holder.txvProductName.setText(name);
        holder.txvProductDescription.setText(description);
        /* Set the onClickListener on ImageView, so that when user clicks on
        ImageView, open up a new window to show that particular image
        */
        holder.imvProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickHandler != null) {
                    mClickHandler.onImageClick(position, view, mProductInfoList.get(position));
                }
            }
        });

        /* Download image from web and display it in appropriate ImageView
        with the help of Glide library
        */
        Glide.with(mContext).load(productInfo.getFirstImageUrl()).into(holder.imvProductImage);
    }

    // return the size of data set
    @Override
    public int getItemCount() {
        return mProductInfoList == null ? 0 : mProductInfoList.size();
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

    /**
     * This method is used to clear all existing data from adapter whenever new batch of data is
     * about to load.
     */
    public void clearData() {
        mProductInfoList.clear();
        notifyDataSetChanged();
    }

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    public interface IProductAdapterClickHandler {
        //The interface that receives onClick messages.
        void onItemClick(String urlString, ProductInfo productInfo);

        void onImageClick(int index, View view, ProductInfo productInfo);
    }

    // View holder class
    class ProductViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        // Use ButterKnife library to bind views
        @BindView(R.id.txv_product_name)
        TextView txvProductName;
        @BindView(R.id.txv_product_description)
        TextView txvProductDescription;
        @BindView(R.id.imv_product_image)
        ImageView imvProductImage;

        ProductViewHolder(View itemView) {
            super(itemView);
            // Bind the butter knife library with layout
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param view The View that was clicked
         */
        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            // Get the web url from the item that was clicked
            String webUrl = mProductInfoList.get(itemPosition).getProductWebUrl();
            mClickHandler.onItemClick(webUrl, mProductInfoList.get(itemPosition));
        }
    }
}
