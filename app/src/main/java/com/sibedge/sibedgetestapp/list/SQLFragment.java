package com.sibedge.sibedgetestapp.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sibedge.sibedgetestapp.R;

public class SQLFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private CustomAdapter dataAdapter;
    private OnFragmentInteractionListener mListener;

    public SQLFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, null);

        String[] columns = new String[] {ItemsDB.COLUMN_TITLE};

        final int[] to = new int[] {R.id.name};

        dataAdapter = new CustomAdapter(
                getActivity(),
                R.layout.item_list,
                null,
                columns,
                to
        );

        final ListView listView = (ListView) rootView.findViewById(R.id.list_data);
        listView.setEmptyView(rootView.findViewById(R.id.empty_view));
        listView.setAdapter(dataAdapter);
        listView.setLongClickable(true);

        getLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String itemTitle =
                        cursor.getString(cursor.getColumnIndexOrThrow(ItemsDB.COLUMN_TITLE));
                Toast.makeText(getActivity(),
                        itemTitle, Toast.LENGTH_SHORT).show();

                String rowId =
                        cursor.getString(cursor.getColumnIndexOrThrow(ItemsDB.COLUMN_ID));

                Intent intentEdit = new Intent(getActivity(), ItemEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode", "update");
                bundle.putString("rowId", rowId);
                intentEdit.putExtras(bundle);
                startActivity(intentEdit);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, final View v, final int pos, final long id) {

                final String[] stringArray = {getActivity().getResources().getString(R.string.edit_list),
                        getActivity().getResources().getString(R.string.delete) };
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.make_choice);
                builder.setItems(stringArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0:
                                editItem(listView, pos);
                                break;
                            default:
                                Uri uri = Uri.parse(TestContentProvider.CONTENT_URI + "/" + id);
                                getActivity().getContentResolver().delete(uri, null, null);
                                refreshListView();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            addItem();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

     @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ItemsDB.COLUMN_ID,
                ItemsDB.COLUMN_TITLE,
                ItemsDB.COLUMN_CHECKBOX};
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                TestContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataAdapter.swapCursor(null);
    }
    private void refreshListView(){
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshListView();
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

    private void addItem(){
        Intent countryEdit = new Intent(getActivity(), ItemEdit.class);
        Bundle bundle = new Bundle();
        bundle.putString("mode", "add");
        countryEdit.putExtras(bundle);
        startActivity(countryEdit);
    }

    private void editItem(ListView listView, int position ){
        Cursor cursor = (Cursor) listView.getItemAtPosition(position);
        String rowId =
                cursor.getString(cursor.getColumnIndexOrThrow(ItemsDB.COLUMN_ID));
        Intent intentEdit = new Intent(getActivity(), ItemEdit.class);
        Bundle bundle = new Bundle();
        bundle.putString("mode", "update");
        bundle.putString("rowId", rowId);
        intentEdit.putExtras(bundle);
        startActivity(intentEdit);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
    }
}
