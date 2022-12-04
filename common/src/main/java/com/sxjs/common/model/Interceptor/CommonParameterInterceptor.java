package com.sxjs.common.model.Interceptor;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CommonParameterInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        //get参数
                HttpUrl modifiedUrl = original.url().newBuilder()
                        .addEncodedQueryParameter("token", "token")
                        .build();
//                request = originalRequest.newBuilder().url(modifiedUrl).build();
        //post参数
       /* Request.Builder requestBuilder = original.newBuilder();
//                        .header("APIKEY", Constant.API_KEY);
        //请求体定制：统一添加token参数
        if(original.body() instanceof FormBody){
            FormBody.Builder newFormBody = new FormBody.Builder();
            FormBody oidFormBody = (FormBody) original.body();
            for (int i = 0;i<oidFormBody.size();i++){
                newFormBody.addEncoded(oidFormBody.encodedName(i),oidFormBody.encodedValue(i));
            }
            newFormBody.add("token", "token");
            requestBuilder.method(original.method(),newFormBody.build());
        }*/

        Request request = original.newBuilder().url(modifiedUrl).build();
        return chain.proceed(request);
    }
}
