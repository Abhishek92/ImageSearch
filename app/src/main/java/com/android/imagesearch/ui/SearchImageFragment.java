package com.android.imagesearch.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.imagesearch.R;
import com.android.imagesearch.network.ImageSearchApiClient;
import com.android.imagesearch.network.model.ImageData;
import com.android.imagesearch.utils.ConnectionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import retrofit.mime.TypedByteArray;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchImageFragment extends Fragment {


    private RecyclerView mImageList;
    private GridLayoutManager mGridLayoutManager;
    private ImageListAdapter mAdapter;

    private final int GRID_SPAN_COUNT = 2;
    private EditText searchEt;
    private TextView mEmptyText;
    private ProgressBar mProgressBar;

    private List<ImageData> mImageDataList = new ArrayList<>();

    public SearchImageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_image, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageList = (RecyclerView) view.findViewById(R.id.frag_search_image_list);
        searchEt = (EditText) view.findViewById(R.id.frag_search_image_et);
        mEmptyText = (TextView) view.findViewById(R.id.emptyText);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mGridLayoutManager = new GridLayoutManager(getActivity(), GRID_SPAN_COUNT);
        mImageList.setLayoutManager(mGridLayoutManager);
        if (ConnectionUtils.isNetworkConnected(getActivity())) {
            searchImages();
        } else
            Toast.makeText(getActivity(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
    }

    private void searchImages() {
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 1) {
                    changeVisibility(View.GONE, View.VISIBLE);
                    getImageList(charSequence.toString(), "225");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getImageList(String searchText, String thumbSize) {

        mImageDataList.clear();
        ImageSearchApiClient.getImageSearchApi().getImageList(searchText, thumbSize, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                if (response != null) {
                    String jsonResponse = new String(((TypedByteArray) response.getBody()).getBytes());
                    parseJsonResponse(jsonResponse);
                    setImageListAdapter();
                    changeVisibility(View.GONE, View.GONE);
                } else {
                    changeVisibility(View.VISIBLE, View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                changeVisibility(View.VISIBLE, View.GONE);
            }
        });
    }

    private void setImageListAdapter() {
        mAdapter = new ImageListAdapter(getActivity(), mImageDataList);
        mImageList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void changeVisibility(int progressVisibility, int textVisbility) {
        mEmptyText.setVisibility(progressVisibility);
        mProgressBar.setVisibility(textVisbility);
    }

    private void parseJsonResponse(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            if (obj.has("query")) {
                JSONObject query = obj.getJSONObject("query");
                JSONObject pagesObj = query.getJSONObject("pages");
                Iterator<String> iter = pagesObj.keys();

                while (iter.hasNext()) {
                    String key = iter.next();
                    ImageData imageData = new ImageData();
                    JSONObject value = (JSONObject) pagesObj.get(key);
                    imageData.setTitle(value.getString("title"));
                    if (value.has("thumbnail")) {
                        JSONObject thumbnail = value.getJSONObject("thumbnail");
                        imageData.setUrl(thumbnail.getString("source"));
                    }
                    mImageDataList.add(imageData);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
