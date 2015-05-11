package com.sibedge.sibedgetestapp.service;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.sibedge.sibedgetestapp.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;

    static final String URL = "http://www.sibedge.com/sites/default/files/media/testxmlfeed.xml";

    static final String KEY_QUOTE = "quote";
    static final String KEY_ID = "id";
    static final String KEY_DATE = "date";
    static final String KEY_TEXT = "text";

    private BaseAdapter adapter;
    private XMLParser parser;
    private ArrayList<HashMap<String, String>> menuItems;
    public ServiceFragment() {
    }

    private void executeAsyncTask(){
            ConnectivityManager connMgr = (ConnectivityManager)
                    getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadXMLTask().execute();
            } else {
                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        menuItems = new ArrayList<HashMap<String, String>>();

        parser = new XMLParser();

        adapter = new SimpleAdapter(getActivity(), menuItems,
                R.layout.item_service,
                new String[] { KEY_ID, KEY_DATE, KEY_TEXT },
                new int[] {R.id.id_text, R.id.date_text, R.id.message_text }
        );

        setListAdapter(adapter);

        executeAsyncTask();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    private class DownloadXMLTask extends AsyncTask<String, Void, Void> {

        ProgressDialog progDailog;

        protected Void doInBackground(String... urls) {
            String xml = parser.getXmlFromUrl(URL);
            Document doc = parser.getDomElement(xml);
            NodeList nl = doc.getElementsByTagName(KEY_QUOTE);
            for (int i = 0; i < nl.getLength(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                Element e = (Element) nl.item(i);
                map.put(KEY_ID, parser.getValue(e, KEY_ID));
                map.put(KEY_DATE, parser.getValue(e, KEY_DATE));
                map.put(KEY_TEXT, parser.getCharacterDataFromElement(e, KEY_TEXT));
                menuItems.add(map);
                progDailog.dismiss();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(getActivity());
            progDailog.setMessage("Loading...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        protected void onPostExecute() {
            adapter.notifyDataSetChanged();
        }

    }

}
