package com.stepin2it.data.remote;

import com.stepin2it.data.models.RqLogin;
import com.stepin2it.data.models.RsToken;
import com.stepin2it.ui.models.ProductInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {

    /**
     * Call back method for getting product list from
     * http://my-json-server.typicode.com/nishantkp/apitest/productData
     *
     * @return list of product
     */
    @GET("/nishantkp/apitest/productData")
    Call<List<ProductInfo>> getProductInfo();

    /**
     * Callback method for sending username and password to
     * https://reqres.in and getting token in response
     *
     * @param url     url
     * @param rqLogin login object containing username and password
     * @return token object
     */
    @POST
    Call<RsToken> login(@Url String url, @Body RqLogin rqLogin);
}
