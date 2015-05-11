package com.sibedge.sibedgetestapp.scaling;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.sibedge.sibedgetestapp.MainActivity;
import com.sibedge.sibedgetestapp.R;

public class ImageActivity extends ActionBarActivity {

    private TouchImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.loadConfiguration(getApplicationContext());

        mImageView = new TouchImageView(this);

        String path = this.getIntent().getStringExtra("path");
        Bitmap selectedphoto = BitmapFactory.decodeFile(path);

        mImageView.setImageBitmap(selectedphoto);
        mImageView.setMaxZoom(4f);
        setContentView(mImageView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_zoom_in) {
            mImageView.zoomIn();
            return true;
        }

        if (id == R.id.action_zoom_out) {
            mImageView.zoomOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
