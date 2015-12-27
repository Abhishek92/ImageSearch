package com.android.imagesearch.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.imagesearch.R;
import com.android.imagesearch.network.model.ImageData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp pc on 26-12-2015.
 */
public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {

    private Context mContext;
    private List<ImageData> mImageDataList;

    public ImageListAdapter(Context context, List<ImageData> mImageList)
    {
        mContext = context;
        mImageDataList = new ArrayList<>(mImageList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.image_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageData data = mImageDataList.get(position);
        Picasso.with(mContext).load(data.getUrl())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.searchedImage);
    }

    @Override
    public int getItemCount() {
        return mImageDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView searchedImage;

        public ViewHolder(View itemView) {
            super(itemView);
            this.searchedImage = (ImageView) itemView.findViewById(R.id.searched_image);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            /*if(listener != null)
                listener.onItemClick(view, getAdapterPosition());*/
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int position);
    }

    public void resetAdapter()
    {
        mImageDataList.clear();
        notifyDataSetChanged();
    }
}
