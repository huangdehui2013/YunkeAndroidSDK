package com.shykad.yunke.sdk.okhttp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by wanghong.he on 2019/2/28.
 * description：
 * @author 38302
 */
public class AdAppidRequest implements Parcelable {
    String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appid) {
        this.appId = appid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appId);
    }

    public AdAppidRequest() {
    }

    protected AdAppidRequest(Parcel in) {
        this.appId = in.readString();
    }

    public static final Parcelable.Creator<AdAppidRequest> CREATOR = new Parcelable.Creator<AdAppidRequest>() {
        @Override
        public AdAppidRequest createFromParcel(Parcel source) {
            return new AdAppidRequest(source);
        }

        @Override
        public AdAppidRequest[] newArray(int size) {
            return new AdAppidRequest[size];
        }
    };
}
