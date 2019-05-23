package com.shykad.yunke.sdk.okhttp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by wanghong.he on 2019/2/28.
 * description：
 */
public class GetAdRequest implements Parcelable {

    /**
     * ios \android
     */
    private String os;
    /**
     * feed\ banner \splash \interstitial \wake-up \wake-up-strict
     */
    private String type;
    /**
     * 广告位id
     */
    private String slotId;
    /**
     * IMEI 或 IDFA
     */
    private String deviceNo;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.os);
        dest.writeString(this.type);
        dest.writeString(this.slotId);
        dest.writeString(this.deviceNo);
    }

    public GetAdRequest() {
    }

    protected GetAdRequest(Parcel in) {
        this.os = in.readString();
        this.type = in.readString();
        this.slotId = in.readString();
        this.deviceNo = in.readString();
    }

    public static final Parcelable.Creator<GetAdRequest> CREATOR = new Parcelable.Creator<GetAdRequest>() {
        @Override
        public GetAdRequest createFromParcel(Parcel source) {
            return new GetAdRequest(source);
        }

        @Override
        public GetAdRequest[] newArray(int size) {
            return new GetAdRequest[size];
        }
    };
}
