package com.stepin2it.data;

import android.content.Context;

import com.stepin2it.data.local.DatabaseHelper;
import com.stepin2it.data.models.RqLogin;
import com.stepin2it.data.models.RsToken;
import com.stepin2it.data.remote.ApiClient;
import com.stepin2it.data.remote.ApiInterface;
import com.stepin2it.ui.models.ProductInfo;
import com.stepin2it.utils.IConstants;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class DataManager {
    private static DataManager sDataManager;
    private static ApiInterface sApiInterface;
    private static DatabaseHelper sDatabaseHelper;
    private static Context mContext;

    public static DataManager getInstance(Context context) {
        if (sDataManager == null) {
            sDataManager = new DataManager();
            sApiInterface = ApiClient.getClient().create(ApiInterface.class);
            sDatabaseHelper = DatabaseHelper.getInstance(context);
        }
        mContext = context;
        return sDataManager;
    }

    // If the network is available make an API call to get the data otherwise
    // return the data from database
    public Observable<List<ProductInfo>> getProductInfo() {
        List<ProductInfo> productInfoList = sDatabaseHelper.readProducts();
        if (NetworkUtils.isNetworkAvailable(mContext)) {
            return sApiInterface.getProductInfoRx().flatMap(new Function<List<ProductInfo>, Observable<List<ProductInfo>>>() {
                @Override
                public Observable<List<ProductInfo>> apply(List<ProductInfo> productInfoList) throws Exception {
                    sDatabaseHelper.insertProducts(productInfoList);
                    return Observable.just(productInfoList);
                }
            });
        } else {
            return productInfoList.size() > 0 ? Observable.just(productInfoList) : null;
        }
    }

    public Observable<RsToken> getToken(RqLogin rqLogin) {
        return sApiInterface.loginRx(IConstants.IReqres.REQUEST_URL_STRING, rqLogin);
    }
}
