package es.esy.rafaelsilva.tcc.util;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Rafael on 24/10/2016.
 */
public class App extends Application  {
    public static final String TAG = App.class.getSimpleName();
    public static App mInstance;

    private RequestQueue mRequestQueue;


    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        mInstance = this;

    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequest(Object tag) {
        getRequestQueue().cancelAll(tag);
    }
}
