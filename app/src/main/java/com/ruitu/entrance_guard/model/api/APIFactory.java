package com.ruitu.entrance_guard.model.api;

/**
 * Created by wubin on 2017/4/20.
 */

public class APIFactory {
    private static ApiService apiServer;
    protected static final Object monitor = new Object();

    public static ApiService getApiInstance() {
        synchronized (monitor) {
            if (apiServer == null) {
                apiServer = APIRetrofit.getDefault();
            }
            return apiServer;
        }
    }

    private static class SingletonHolder {
        private static APIFactory instance = new APIFactory();
    }
}
