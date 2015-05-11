package com.sibedge.sibedgetestapp.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.sibedge.sibedgetestapp.MainActivity;
import com.sibedge.sibedgetestapp.R;

import java.util.HashMap;

public class ItemEdit extends Activity implements OnClickListener{

    private Button save, cancel;
    private String mode;
    private EditText name;
    private String id;
    private CheckBox checkbox;
    private NotificationManager manager;
    private int lastId = 0;
    private HashMap<Integer, Notification> notifications;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notifications = new HashMap<Integer, Notification>();

        if (this.getIntent().getExtras() != null){
            Bundle bundle = this.getIntent().getExtras();
            mode = bundle.getString("mode");
        }

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        name = (EditText) findViewById(R.id.name);
        checkbox = (CheckBox) findViewById(R.id.checkbox);

        if(mode.trim().equalsIgnoreCase("add")){
            cancel.setVisibility(View.INVISIBLE);
            cancel.setClickable(false);
        }
        else{
            Bundle bundle = this.getIntent().getExtras();
            id = bundle.getString("rowId");
            loadItem();
        }
    }

    public void onClick(View v) {

        if(name.getText().toString().trim().equalsIgnoreCase("")){
            finish();
        }

        switch (v.getId()) {
            case R.id.save:
                saveItem();
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        if(name.getText().toString().trim().equalsIgnoreCase("")){
            finish();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save_item);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveItem();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

    private void saveItem(){
        ContentValues values = new ContentValues();
        values.put(ItemsDB.COLUMN_CHECKBOX, Boolean.toString(checkbox.isChecked()));
        values.put(ItemsDB.COLUMN_TITLE, name.getText().toString());

        if(mode.trim().equalsIgnoreCase("add")){
            getContentResolver().insert(TestContentProvider.CONTENT_URI, values);
            createInfoNotification("Add Item!");
        }
        else {
            Uri uri = Uri.parse(TestContentProvider.CONTENT_URI + "/" + id);
            getContentResolver().update(uri, values, null, null);
            createInfoNotification("Saved Changes!");
        }
    }

    private void loadItem(){

        String[] projection = {
                ItemsDB.COLUMN_ID,
                ItemsDB.COLUMN_TITLE,
                ItemsDB.COLUMN_CHECKBOX};
        Uri uri = Uri.parse(TestContentProvider.CONTENT_URI + "/" + id);
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(ItemsDB.COLUMN_TITLE));
            String myCheck = cursor.getString(cursor.getColumnIndexOrThrow(ItemsDB.COLUMN_CHECKBOX));
            name.setText(myName);
            checkbox.setChecked(Boolean.valueOf(myCheck));
        }
    }

    public int createInfoNotification(String message){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.android_logo)
                .setAutoCancel(true)
                .setTicker(message)
                .setContentText(message)
                .setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(System.currentTimeMillis())
                .setContentTitle("SibEDGE")
                .setDefaults(Notification.DEFAULT_ALL);

        Notification notification = nb.getNotification();
        manager.notify(lastId, notification);
        notifications.put(lastId, notification);
        return lastId++;
    }
}