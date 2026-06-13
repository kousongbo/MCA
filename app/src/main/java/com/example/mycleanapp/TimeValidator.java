package com.example.mycleanapp;

import android.content.Context;

import java.util.Calendar;
import java.util.TimeZone;

public class TimeValidator {

    private Context context;
    private Callback callback;

    public interface Callback {
        void onSuccess();       // 验证通过
        void onExpired();       // 版本已过期
        void onError(int code); // 1=无需联网（保留接口）
    }

    public TimeValidator(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void start() {
        // 直接使用本地时间，不需要网络
        long localTime = System.currentTimeMillis();
        long deadline = getDeadline();

        if (localTime > deadline) {
            callback.onExpired();
        } else {
            callback.onSuccess();
        }
    }

    private long getDeadline() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2026, Calendar.JUNE, 15, 23, 59, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }
}