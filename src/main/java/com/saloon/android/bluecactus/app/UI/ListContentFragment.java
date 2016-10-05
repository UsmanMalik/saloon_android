/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.saloon.android.bluecactus.app.UI;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.saloon.android.bluecactus.R;
import com.saloon.android.bluecactus.app.Models.Division;
import com.saloon.android.bluecactus.app.Utils.CustomVolleyRequestQueue;
import com.saloon.android.bluecactus.app.Utils.GlobalVariables;

import java.util.ArrayList;

/**
 * Provides UI for the view with List.
 */
public class ListContentFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avator;
        public TextView name;
        public TextView description;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            avator = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_desc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(DetailActivity.EXTRA_POSITION, getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 4;

        private NetworkImageView mNetworkImageView;
        private ImageLoader mImageLoader;
        final String url = "http://192.168.43.108:3000";


        private final String[] mPlaces;
        private final String[] mPlaceDesc;
        private final Drawable[] mPlaceAvators;
        private ArrayList<Division> divisionList;

        public ContentAdapter(Context context) {
//            Log.i("Container List size: ",GlobalVariables.getInstance().getArrayList().get(0).getImage_url()+ "");
            divisionList = GlobalVariables.getInstance().getArrayList();

            // Instantiate the RequestQueue.
            mImageLoader = CustomVolleyRequestQueue.getInstance(context)
                    .getImageLoader();


            Resources resources = context.getResources();
            mPlaces = resources.getStringArray(R.array.places);
            mPlaceDesc = resources.getStringArray(R.array.place_desc);
            TypedArray a = resources.obtainTypedArray(R.array.place_avator);
            mPlaceAvators = new Drawable[a.length()];
            for (int i = 0; i < mPlaceAvators.length; i++) {
                mPlaceAvators[i] = a.getDrawable(i);
            }
            a.recycle();
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            mNetworkImageView = (NetworkImageView) holder.avator;

            //Image URL - This can point to any image file supported by Android
            mImageLoader.get(url+divisionList.get(position).getImage_url(), ImageLoader.getImageListener(mNetworkImageView,
                    R.mipmap.ic_launcher, android.R.drawable
                            .ic_dialog_alert));
            mNetworkImageView.setImageUrl(url, mImageLoader);

//            holder.avator.setImageDrawable(mPlaceAvators[position % mPlaceAvators.length]);
            holder.name.setText(divisionList.get(position).getTitle());
            holder.description.setText(mPlaceDesc[position % mPlaceDesc.length]);
        }

        @Override
        public int getItemCount() {
            return divisionList.size();
        }
    }

}