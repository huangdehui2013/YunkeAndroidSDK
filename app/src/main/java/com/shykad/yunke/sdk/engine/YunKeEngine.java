package com.shykad.yunke.sdk.engine;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.shykad.yunke.sdk.config.HttpConfig;
import com.shykad.yunke.sdk.config.UrlConfig;
import com.shykad.yunke.sdk.okhttp.RequestUtils;
import com.shykad.yunke.sdk.okhttp.bean.AdAppidRequest;
import com.shykad.yunke.sdk.okhttp.bean.AdAppidResponse;
import com.shykad.yunke.sdk.okhttp.bean.FeedbackAdRequest;
import com.shykad.yunke.sdk.okhttp.bean.FeedbackAdResponse;
import com.shykad.yunke.sdk.okhttp.bean.GetAdRequest;
import com.shykad.yunke.sdk.okhttp.bean.GetAdResponse;
import com.shykad.yunke.sdk.utils.LogUtils;
import com.shykad.yunke.sdk.utils.NetworkUtils;
import com.shykad.yunke.sdk.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Create by wanghong.he on 2019/3/5.
 * description：
 */
public class YunKeEngine {

    private Context mContext;

    private volatile static YunKeEngine instance;

    private YunKeEngine(Context context){
        mContext = context.getApplicationContext();
    }

    public static YunKeEngine getInstance(Context context){

        if(instance == null){
            synchronized (YunKeEngine.class){
                if(instance == null) {
                    instance = new YunKeEngine(context);
                }
            }
        }
        return instance;
    }

    /**
     * 初始化参数请求
     * 此appId为云客平台申请
     * @param appId 1086104688845262848
     */
    public void yunkeInit(String appId,YunKeInitCallBack initCallBack){
        YunKeInitCallBack initCallBack1 = initCallBack;
        if (initCallBack1 ==null){
            LogUtils.d("please init CallBack");
            return;
        }
        if (!NetworkUtils.checkWifiAndGPRS(mContext)){
            initCallBack1.initFailed("网络异常，请检查网络链接状况");
            return;
        }
        if (TextUtils.isEmpty(appId)){
            initCallBack1.initFailed("AppId 不能为空");
            return;
        }
        AdAppidRequest params = new AdAppidRequest();
        params.setAppId(appId);
        RequestUtils.getInstance().doPostJsonRequest(HttpConfig.baseUrl()+ UrlConfig.GET_APPID,params, new RequestUtils.RequestCallBack() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {

                String result = response.body().string();
                LogUtils.d("-----"+result);
                return new Gson().fromJson(result, AdAppidResponse.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d("-----"+e);
                if (initCallBack!=null){
                    initCallBack.initFailed(e.getMessage());
                }
            }

            /**
             * {"txAppId":"1105344611","ttAppId":"5000546","code":0,"message":null}
             */
            @Override
            public void onResponse(Object response, int id) {

                if (response instanceof AdAppidResponse){
                    if (((AdAppidResponse) response).getCode() == HttpConfig.CODE_SUCCESS){
                        if (initCallBack!=null){
                            initCallBack.initSuccess(response);
                        }
                    }else {
                        if (initCallBack!=null){
                            initCallBack.initFailed(((AdAppidResponse) response).getMessage());
                        }
                    }
                }
            }
        });
    }

    /**
     * 获取广告
     * @param adType  feed banner splash interstitial wake-up wake-up-strict
     * @param slotId 广告位id(为云客广告位id)
     * @param deviceNo IMEI
     * @param adCallBack 广告回调
     */
    public void yunkeGetAd(String adType,String slotId,String deviceNo,YunKeAdCallBack adCallBack){

        YunKeAdCallBack adCallBack1 = adCallBack;

        if (adCallBack1 ==null){
            LogUtils.d("please init adCallBack");
            return;
        }
        if (!NetworkUtils.checkWifiAndGPRS(mContext)){
            adCallBack1.getAdFailed("网络异常，请检查网络链接状况");
            return;
        }
        String[] adTypeData = {"feed","banner","splash","interstitial","wake-up","wake-up-strict"};

        if (TextUtils.isEmpty(adType) || !contains(adTypeData,adType)){
            adCallBack1.getAdFailed("adType 不合法");
            return;
        }

        if (TextUtils.isEmpty(slotId)){
            adCallBack1.getAdFailed("slotId 不合法");
            return;
        }

        if (TextUtils.isEmpty(deviceNo)){
            adCallBack1.getAdFailed("deviceNo 不合法");
            return;
        }

        GetAdRequest params = new GetAdRequest();
        params.setOs(HttpConfig.OS);
        params.setType(adType);
        params.setSlotId(slotId);
        params.setDeviceNo(deviceNo);

        RequestUtils.getInstance().doPostJsonRequest(HttpConfig.baseUrl()+ UrlConfig.GET_AD,params, new RequestUtils.RequestCallBack() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                String result = response.body().string();
                LogUtils.d("-----"+result);
                return new Gson().fromJson(result, GetAdResponse.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d("-----"+e);
                if (adCallBack!=null){
                    adCallBack.getAdFailed(e.getMessage());
                }
            }

            @Override
            public void onResponse(Object response, int id) {

                /**
                 * 回调
                 */
                if (response instanceof GetAdResponse){
                    if (((GetAdResponse) response).getCode() == HttpConfig.CODE_SUCCESS){
                        if (adCallBack!=null){
                            adCallBack.getAdSuccess(response);
                        }
                    }else {
                        if (adCallBack!=null){
                            adCallBack.getAdFailed(((GetAdResponse) response).getMessage());
                        }
                    }
                }
            }
        });
    }


    public void yunkeFeedAd(String slotId,int actionType,YunKeFeedCallBack feedCallBack){
        YunKeFeedCallBack feedCallBack1 = feedCallBack;
        if (feedCallBack1 ==null){
            LogUtils.d("please init feedCallBack");
            return;
        }
        if (!NetworkUtils.checkWifiAndGPRS(mContext)){
            feedCallBack1.feedAdFailed("网络异常，请检查网络链接状况");
            return;
        }
        if (TextUtils.isEmpty(slotId)){
            feedCallBack1.feedAdFailed("slotId 不合法");
            return;
        }
        if (!(actionType==0 || actionType ==1)) {
            feedCallBack1.feedAdFailed("type 不合法");
            return;
        }

        FeedbackAdRequest params = new FeedbackAdRequest();
        params.setId(slotId);
        params.setType(actionType);

        RequestUtils.getInstance().doPostJsonRequest(HttpConfig.baseUrl()+ UrlConfig.FEEDBACK_AD,params, new RequestUtils.RequestCallBack() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                String result = response.body().string();
                LogUtils.d("-----"+result);
                return new Gson().fromJson(result, FeedbackAdResponse.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d("-----"+e);
                if (feedCallBack!=null){
                    feedCallBack.feedAdFailed(e.getMessage());
                }
            }

            @Override
            public void onResponse(Object response, int id) {
                if (((FeedbackAdResponse) response).getCode() == HttpConfig.CODE_SUCCESS && StringUtils.isEmpty(((FeedbackAdResponse) response).getMessage())){
                    if (feedCallBack!=null){
                        feedCallBack.feedAdSuccess("success");
                    }
                }else {
                    if (feedCallBack!=null){
                        feedCallBack.feedAdFailed(((FeedbackAdResponse) response).getMessage());
                    }
                }
            }
        });
    }

    /**
     *  判断某个字符串是否存在于数组中
     *  @param stringArray 原数组
     *  @param source 查找的字符串
     *  @return 是否找到
     */
    private static boolean contains(String[] stringArray, String source) {
        List<String> tempList = Arrays.asList(stringArray);
        return tempList.contains(source);
    }

    public interface YunKeInitCallBack{
        void initSuccess(Object response);
        void initFailed(String err);
    }

    public interface YunKeAdCallBack{
        void getAdSuccess(Object response);
        void getAdFailed(String err);
    }

    public interface YunKeFeedCallBack{
        void feedAdSuccess(String response);
        void feedAdFailed(String err);
    }
}
