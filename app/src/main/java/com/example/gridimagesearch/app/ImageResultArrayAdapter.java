package com.example.gridimagesearch.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.util.List;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {
    private LayoutInflater inflater;

    public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View v = view;
        SmartImageView picture;
        TextView name;

        ImageResult imageInfo = this.getItem(position);
        SmartImageView ivImage;
        if (view == null) {
            v = inflater.inflate(R.layout.gridview_item, parent, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
            v.setTag(R.id.text, v.findViewById(R.id.text));
        }

        picture = (SmartImageView) v.getTag(R.id.picture);
        name = (TextView) v.getTag(R.id.text);

        picture.setImageResource(android.R.color.transparent);

        picture.setImageUrl(imageInfo.getThumbUrl());
        name.setText(imageInfo.getTitle());

        return v;
    }
}
