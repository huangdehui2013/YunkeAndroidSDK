package com.shykad.yunke.sdk.engine;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.callback.AQuery2;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;
import com.qq.e.comm.util.GDTLogger;
import com.shykad.yunke.sdk.R;
import com.shykad.yunke.sdk.config.HttpConfig;
import com.shykad.yunke.sdk.manager.ShykadManager;
import com.shykad.yunke.sdk.manager.TTAdManagerHolder;
import com.shykad.yunke.sdk.okhttp.bean.GetAdResponse;
import com.shykad.yunke.sdk.ui.widget.YunkeTemplateView;
import com.shykad.yunke.sdk.utils.LogUtils;
import com.shykad.yunke.sdk.utils.SPUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Create by wanghong.he on 2019/3/20.
 * description：信息流广告
 */
public class InfoFlowEngine {

    private InfoFlowAdCallBack infoFlowAdCallBack;
    private Object response;
    private GetAdResponse.AdCotent adCotent;
    private static Activity mContext;
    private static InfoFlowEngine instance;
    private NativeExpressAD mADManager;
    private YunkeTemplateView templateView;

    private InfoFlowEngine(){

    }

    public static InfoFlowEngine getInstance(Activity context){
        mContext = context;
        if(instance == null){
            synchronized (InfoFlowEngine.class){
                if(instance == null) instance = new InfoFlowEngine();
            }
        }
        return instance;
    }

    /**
     * 初始化原生广告引擎
     * @param response
     * @param infoFlowAdCallBack
     * @return InfoFlowEngine
     */
    public InfoFlowEngine initEngine(Object response, InfoFlowAdCallBack infoFlowAdCallBack){
        this.response = response;
        this.infoFlowAdCallBack = infoFlowAdCallBack;
        templateView = new YunkeTemplateView(mContext);

        return this;
    }

    /**
     * 加载原生广告引擎
     */
    public InfoFlowEngine launchInfoFlow(ViewGroup infoFlowContainer,int adCount){
        if (response instanceof GetAdResponse){
            adCotent = ((GetAdResponse) response).getData();
            flowOptimization(infoFlowContainer,adCount);
        }
        return this;
    }

    /**
     * 流量转化:0 云客 1：腾讯 2：头条
     */
    private void flowOptimization(ViewGroup infoFlowContainer,int adCount){
        int channel = ((GetAdResponse) response).getData().getChannel();
        switch (channel){
            case HttpConfig.AD_CHANNEL_YUNKE:
                showYunkeInfoFlow(infoFlowContainer,adCount);
                break;
            case HttpConfig.AD_CHANNEL_TENCENT:
                showTencentInfoFlow(adCotent.getSlotId(), (String) SPUtil.get(mContext,SPUtil.TX_APPID,adCotent.getAppId()),adCount);
                break;
            case HttpConfig.AD_CHANNEL_BYTEDANCE:
//                showByteDanceInfoFlow(container,adCotent.getSlotId(),adCount);
                break;
            default:
                break;
        }
    }

    /**
     * 云克信息流广告
     */
    private void showYunkeInfoFlow(ViewGroup infoFlowContainer,int adCount){
        templateView.setTemplateTitle(adCotent.getTitle())
                .setTemplateDesc(adCotent.getDesc())
                .setTemplateCancel(mContext.getResources().getDrawable(R.drawable.yunke_dislike_icon))
                .lanchTemplate(infoFlowContainer, response, new YunkeTemplateView.TemplateViewCallBack() {
                    @Override
                    public void onAdClicked(YunkeTemplateView templateView) {
                        LogUtils.d("shykad","模板广告点击");
                        if (infoFlowAdCallBack!=null){
                            infoFlowAdCallBack.onAdClick(true);
                        }
                    }

                    @Override
                    public void onAdShow(YunkeTemplateView templateView) {
                        LogUtils.d("shykad","模板广告展示");
                        if (infoFlowAdCallBack!=null){
                            infoFlowAdCallBack.onAdShow();
                        }
                    }

                    @Override
                    public void onAdError(String err) {
                        LogUtils.d("shykad","feed广告异常："+err);
                        if (infoFlowAdCallBack!=null){
                            infoFlowAdCallBack.onAdError(err);
                        }
                    }

                    @Override
                    public void onAdCancel(YunkeTemplateView templateView) {
                        LogUtils.d("模板广告关闭");
                        if (infoFlowAdCallBack!=null){
                            infoFlowAdCallBack.onAdCancel(templateView);
                        }
                    }

                    @Override
                    public void onAdLoad(YunkeTemplateView templateView) {
                        LogUtils.d("模板广告数据加载完成");
                        if (infoFlowContainer.getVisibility() != View.VISIBLE) {
                            infoFlowContainer.setVisibility(View.VISIBLE);
                        }
                        if (infoFlowContainer.getChildCount() > 0) {
                            infoFlowContainer.removeAllViews();
                        }
                        List<YunkeTemplateView> adList = new ArrayList<>();
                        for (int i=0;i<=adCount;++i){
                            adList.add(templateView);
                        }
                        if (infoFlowAdCallBack!=null){
                            infoFlowAdCallBack.onAdLoad(adList,HttpConfig.AD_CHANNEL_YUNKE);
                        }
                    }
                });

        ViewGroup parent = (ViewGroup) templateView.getParent();
        if (parent!=null){
            templateView.removeView(parent);
            templateView.removeAllViews();
        }
        infoFlowContainer.addView(templateView);
    }

    /**
     * 腾讯信息流广告
     */
    private void showTencentInfoFlow(String posId,String appId,int AD_COUNT){
        ADSize adSize = new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT); // 消息流中用AUTO_HEIGHT
        mADManager = new NativeExpressAD(mContext, adSize, appId, posId, new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onADLoaded(List<NativeExpressADView> adList) {
                LogUtils.d("shykad-gdt", "onADLoaded: " + adList.size());
                if (infoFlowAdCallBack!=null){
                    infoFlowAdCallBack.onAdLoad(adList,HttpConfig.AD_CHANNEL_TENCENT);
                }
            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
                LogUtils.d("shykad-gdt", "onRenderFail: " + nativeExpressADView.toString());
                if (infoFlowAdCallBack!=null){
                    infoFlowAdCallBack.onAdError("renderFail:"+nativeExpressADView.toString());
                }
            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                LogUtils.d("shykad-gdt", "onRenderSuccess: " + nativeExpressADView.toString() + ", adInfo: " + getAdInfo(nativeExpressADView));
            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
                LogUtils.d("shykad-gdt", "onADExposure: " + nativeExpressADView.toString());
                showAdTask();
            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
                LogUtils.d("shykad-gdt", "onADClicked: " + nativeExpressADView.toString());
                clickAdTask(false);
            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                LogUtils.d("shykad-gdt", "onADClosed: " + nativeExpressADView.toString());
                if (infoFlowAdCallBack!=null){
                    infoFlowAdCallBack.onAdCancel(nativeExpressADView);
                }
            }

            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
                LogUtils.d("shykad-gdt", "onADLeftApplication: " + nativeExpressADView.toString());
            }

            @Override
            public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
                LogUtils.d("shykad-gdt", "onADOpenOverlay: " + nativeExpressADView.toString());
            }

            @Override
            public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
                LogUtils.d("shykad-gdt", "onADCloseOverlay");
            }

            @Override
            public void onNoAD(AdError adError) {
                String err = String.format("onNoAD, error code: %d, error msg: %s", adError.getErrorCode(),adError.getErrorMsg());
                LogUtils.d("shykad-gdt",err);
                if (infoFlowAdCallBack!=null){
                    infoFlowAdCallBack.onAdError(err);
                }
            }
        });
        mADManager.loadAD(AD_COUNT);
    }

    /**
     * 获取广告数据
     *
     * @param nativeExpressADView
     * @return
     */
    public String getAdInfo(NativeExpressADView nativeExpressADView) {
        AdData adData = nativeExpressADView.getBoundData();
        if (adData != null) {
            StringBuilder infoBuilder = new StringBuilder();
            infoBuilder.append("title:").append(adData.getTitle()).append(",")
                    .append("desc:").append(adData.getDesc()).append(",")
                    .append("patternType:").append(adData.getAdPatternType());
            if (adData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                infoBuilder.append(", video info: ")
                        .append(getVideoInfo(adData.getProperty(AdData.VideoPlayer.class)));
            }
            return infoBuilder.toString();
        }
        return null;
    }

    /**
     * 获取播放器实例
     *
     * 仅当视频回调{@link NativeExpressMediaListener#onVideoInit(NativeExpressADView)}调用后才会有返回值
     *
     * @param videoPlayer
     * @return
     */
    public String getVideoInfo(AdData.VideoPlayer videoPlayer) {
        if (videoPlayer != null) {
            StringBuilder videoBuilder = new StringBuilder();
            videoBuilder.append("state:").append(videoPlayer.getVideoState()).append(",")
                    .append("duration:").append(videoPlayer.getDuration()).append(",")
                    .append("position:").append(videoPlayer.getCurrentPosition());
            return videoBuilder.toString();
        }
        return null;
    }

    /**
     * 销毁资源
     * @param mAdViewList
     */
    public void destoryAd(List<NativeExpressADView> mAdViewList){
        // 使用完了每一个NativeExpressADView之后都要释放掉资源。
        if (mAdViewList != null) {
            for (NativeExpressADView view : mAdViewList) {
                view.destroy();
            }
        }
    }

    /**
     * 头条信息流广告
     */
    private void showByteDanceInfoFlow(){

    }



    /**
     * 展示广告(腾讯 头条--feedback)
     */
    private void showAdTask(){
        ShykadManager.INIT_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                (new Handler(Looper.getMainLooper())).post(new Runnable() {

                    @Override
                    public void run() {// TODO: 2019/3/7 埋点

                        YunKeEngine.getInstance().yunkeFeedAd(adCotent.getId(), HttpConfig.AD_SHOW_YUNKE, new YunKeEngine.YunKeFeedCallBack() {
                            @Override
                            public void feedAdSuccess(String response) {

                                if (infoFlowAdCallBack!=null){
                                    infoFlowAdCallBack.onAdShow();
                                }

                            }

                            @Override
                            public void feedAdFailed(String err) {
                                if (infoFlowAdCallBack!=null){
                                    infoFlowAdCallBack.onAdError(err);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 点击广告(腾讯 头条--feedback)
     */
    private void clickAdTask(boolean isJump) {
        ShykadManager.INIT_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                (new Handler(Looper.getMainLooper())).post(new Runnable() {

                    @Override
                    public void run() {// TODO: 2019/3/7 埋点

                        YunKeEngine.getInstance().yunkeFeedAd(adCotent.getId(), HttpConfig.AD_CLICK_YUNKE, new YunKeEngine.YunKeFeedCallBack() {
                            @Override
                            public void feedAdSuccess(String response) {
                                if (infoFlowAdCallBack!=null){
                                    infoFlowAdCallBack.onAdClick(isJump);
                                }

                            }

                            @Override
                            public void feedAdFailed(String err) {
                                if (infoFlowAdCallBack!=null){
                                    infoFlowAdCallBack.onAdError(err);
                                }
                            }
                        });
                    }
                });
            }
        });

    }

    public interface InfoFlowAdCallBack{
        void onAdLoad(List<?> adList,int channel);
        void onAdShow();
        void onAdClick(boolean isJump);
        void onAdError(String err);
        void onAdCancel(Object view);
    }

}
