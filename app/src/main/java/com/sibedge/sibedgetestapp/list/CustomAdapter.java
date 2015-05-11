package com.sibedge.sibedgetestapp.list;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.sibedge.sibedgetestapp.R;

public class CustomAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private Context appContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;

    public CustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context,layout,c,from,to);
        this.layout = layout;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView (Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        TextView titleS=(TextView) view.findViewById(R.id.list_title);
        titleS.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));

        CheckBox checkbox = (CheckBox) view.findViewById(R.id.list_checkbox);
        checkbox.setChecked(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)))));

        final ImageView image = (ImageView) view.findViewById(R.id.list_icon);
        final Bitmap android = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.android_logo);
        final Bitmap apple = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.apple_logo);

        if (Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))))){
            image.setImageBitmap(android);
        } else {
            image.setImageBitmap(apple);
        }
    }

}
