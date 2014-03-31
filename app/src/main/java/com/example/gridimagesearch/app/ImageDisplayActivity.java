package com.example.gridimagesearch.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.loopj.android.image.SmartImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageDisplayActivity extends SherlockFragmentActivity {

    private ShareActionProvider mShareActionProvider;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
        final SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
//        ivImage.setImageUrl(result.getFullUrl());
        String url = result.getFullUrl();

        final ProgressBar progressbar = (ProgressBar) findViewById(R.id.image_progress);
        imageLoader = ImageLoader.getInstance();
        ImageSize size = new ImageSize(0, 0);
        DisplayImageOptions options = null;
//        progressbar.setProgress(0);
        imageLoader.loadImage(url, size, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // Do whatever you want with Bitmap
                Log.d("f", "F");
                ivImage.setImageBitmap(loadedImage);
                createImageShareIntent();
            }

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
                progressbar.setIndeterminate(false);
                progressbar.setProgress(0);
            }

        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                progressbar.setProgress(current);
                progressbar.setMax(total);
            }
        }
       );
//        imageLoader.displayImage(imageUri, imageView);
        setTitle(result.getTitle());

////        Intent i = new Intent(this, ImageDisplayActivity.class);
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("image/*");
//        Uri uri = Uri.fromFile(new File(getFilesDir(), "foo.jpg"));
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

        // Fetch Bitmap Uri locally
//        SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
    }

    private void createImageShareIntent() {
        SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
        Uri bmpUri = getLocalBitmapUri(ivImage); // see previous section
        // Create share intent as described above
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        // Attach share event to the menu item provider
        setShareIntent(shareIntent);
    }

    public Uri getLocalBitmapUri(SmartImageView imageView) {
        Bitmap bitmap = getImageBitmap(imageView);
        // Write image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image.png");
            file.mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public Bitmap getImageBitmap(SmartImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else { // workaround to convert color to bitmap
            bmp = Bitmap.createBitmap(drawable.getBounds().width(),
                    drawable.getBounds().height(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmp);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return bmp;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.image_display, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        return true;
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_item_share) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
