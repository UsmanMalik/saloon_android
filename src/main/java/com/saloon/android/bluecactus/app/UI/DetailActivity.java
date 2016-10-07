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

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.saloon.android.bluecactus.R;
import com.saloon.android.bluecactus.app.Models.Division;
import com.saloon.android.bluecactus.app.Utils.CustomVolleyRequestQueue;
import com.saloon.android.bluecactus.app.Utils.GlobalVariables;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";

    private Division division;
    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader;
    final String url = "http://192.168.43.108:3000";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Set title of Detail page
        // collapsingToolbar.setTitle(getString(R.string.item_title));

        int postion = getIntent().getIntExtra(EXTRA_POSITION, 0);
        division = GlobalVariables.getInstance().getArrayList().get(postion);
        Resources resources = getResources();
        String[] places = resources.getStringArray(R.array.places);
        collapsingToolbar.setTitle(division.getTitle());



        String[] placeDetails = resources.getStringArray(R.array.place_details);
        TextView placeDetail = (TextView) findViewById(R.id.place_detail);
        placeDetail.setText(division.getDescription());

        String[] placeLocations = resources.getStringArray(R.array.place_locations);
        TextView placeLocation =  (TextView) findViewById(R.id.place_location);
        placeLocation.setText(division.getTitle());


        // Instantiate the RequestQueue.
        mImageLoader = CustomVolleyRequestQueue.getInstance(this)
                .getImageLoader();

        mNetworkImageView = (NetworkImageView)findViewById(R.id.image);

        //Image URL - This can point to any image file supported by Android
        mImageLoader.get(url+division.getImage_url(), ImageLoader.getImageListener(mNetworkImageView,
                R.mipmap.ic_launcher, android.R.drawable
                        .ic_dialog_alert));
        mNetworkImageView.setImageUrl(url, mImageLoader);


//        TypedArray placePictures = resources.obtainTypedArray(R.array.places_picture);
//        ImageView placePicutre = (ImageView) findViewById(R.id.image);
//        placePicutre.setImageDrawable(placePictures.getDrawable(postion % placePictures.length()));
//
//        placePictures.recycle();
    }
}
