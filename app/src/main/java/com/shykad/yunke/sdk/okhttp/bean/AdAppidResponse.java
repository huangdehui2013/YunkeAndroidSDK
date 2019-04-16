package com.shykad.yunke.sdk.okhttp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Create by wanghong.he on 2019/2/28.
 * description：
 */
public class AdAppidResponse implements Parcelable {

     String txAppId; // 腾迅 AppId
     String ttAppId; // 今日头条 AppId
     int code; // 0 表示接口成功 非 0 表示错误
     String message; // 错误消息

     protected AdAppidResponse(Parcel in) {
          txAppId = in.readString();
          ttAppId = in.readString();
          code = in.readInt();
          message = in.readString();
     }

     public static final Creator<AdAppidResponse> CREATOR = new Creator<AdAppidResponse>() {
          @Override
          public AdAppidResponse createFromParcel(Parcel in) {
               return new AdAppidResponse(in);
          }

          @Override
          public AdAppidResponse[] newArray(int size) {
               return new AdAppidResponse[size];
          }
     };

     public String getTxAppId() {
          return txAppId;
     }

     public void setTxAppId(String txAppId) {
          this.txAppId = txAppId;
     }

     public String getTtAppId() {
          return ttAppId;
     }

     public void setTtAppId(String ttAppId) {
          this.ttAppId = ttAppId;
     }

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
          dest.writeString(txAppId);
          dest.writeString(ttAppId);
          dest.writeInt(code);
          dest.writeString(message);
     }

     @Override
     public String toString() {
          return "AdAppidResponse{" +
                  "txAppId='" + txAppId + '\'' +
                  ", ttAppId='" + ttAppId + '\'' +
                  ", code=" + code +
                  ", message='" + message + '\'' +
                  '}';
     }
}
