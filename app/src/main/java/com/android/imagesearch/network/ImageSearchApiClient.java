package com.android.imagesearch.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;

/**
 * Created by hp pc on 26-12-2015.
 */
public final class ImageSearchApiClient {

    private final static String API_URL = "https://en.wikipedia.org/w";
    private static ImageSearchApiInterface imageSearchApiInterface;

    private ImageSearchApiClient() {
        //Empty Constructor
    }

    private static RestAdapter getRestAdapter() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        return restAdapter;
    }

    public static ImageSearchApiInterface getImageSearchApi() {
        if (imageSearchApiInterface == null)
            imageSearchApiInterface = getRestAdapter().create(ImageSearchApiInterface.class);

        return imageSearchApiInterface;
    }
}
