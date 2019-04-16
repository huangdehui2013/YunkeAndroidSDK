package com.shykad.yunke.sdk.okhttp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by wanghong.he on 2019/2/28.
 * description：
 */
public class FeedbackAdRequest implements Parcelable {

    String id;//广告位id
    int type;//0 展示 1点击

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.type);
    }

    public FeedbackAdRequest() {
    }

    protected FeedbackAdRequest(Parcel in) {
        this.id = in.readString();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<FeedbackAdRequest> CREATOR = new Parcelable.Creator<FeedbackAdRequest>() {
        @Override
        public FeedbackAdRequest createFromParcel(Parcel source) {
            return new FeedbackAdRequest(source);
        }

        @Override
        public FeedbackAdRequest[] newArray(int size) {
            return new FeedbackAdRequest[size];
        }
    };
}
