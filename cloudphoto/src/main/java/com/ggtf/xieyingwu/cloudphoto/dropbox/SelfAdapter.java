package com.ggtf.xieyingwu.cloudphoto.dropbox;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dropbox.core.v2.files.FileMetadata;
import com.ggtf.xieyingwu.cloudphoto.R;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xieyingwu on 2017/5/19.
 */

public class SelfAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<SelfModel> items;
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    String accessToken;

    public void setItems(List<SelfModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_layout, null);
        return new SelfHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SelfHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class SelfHolder extends RecyclerView.ViewHolder {

        private ImageView icon;

        public SelfHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_icon);
        }

        public void bind(int pos) {
            SelfModel selfModel = items.get(pos);
            FileMetadata fileMetadata = selfModel.fileMetadata;

        }
    }
}
