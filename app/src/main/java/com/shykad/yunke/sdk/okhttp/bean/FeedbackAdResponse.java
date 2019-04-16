package com.shykad.yunke.sdk.okhttp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by wanghong.he on 2019/2/28.
 * description：
 */
public class FeedbackAdResponse implements Parcelable {

    int code;
    String message;

    protected FeedbackAdResponse(Parcel in) {
        code = in.readInt();
        message = in.readString();
    }

    public static final Creator<FeedbackAdResponse> CREATOR = new Creator<FeedbackAdResponse>() {
        @Override
        public FeedbackAdResponse createFromParcel(Parcel in) {
            return new FeedbackAdResponse(in);
        }

        @Override
        public FeedbackAdResponse[] newArray(int size) {
            return new FeedbackAdResponse[size];
        }
    };

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(message);
    }

    @Override
    public String toString() {
        return "FeedbackAdResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
