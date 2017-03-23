package com.example.festival_ui.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.festival_ui.R;
import com.example.festival_ui.bean.SendedMsg;
import com.example.festival_ui.db.SmsProvider;
import com.example.festival_ui.view.FlowLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by wyj on 2016/7/4.
 */
public class SmsHistoryFragment extends ListFragment {

    private static final int LOADER_ID = 1;
    private LayoutInflater inflater;
    private CursorAdapter cursorAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inflater = LayoutInflater.from(getActivity());
        initLoader();
        setupListAdapter();
    }

    private void setupListAdapter() {
        cursorAdapter = new CursorAdapter(getActivity(),null,false) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View view = inflater.inflate(R.layout.item_sender_msg,parent,false);
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView tvMsg = (TextView) view.findViewById(R.id.tv_msg);
                FlowLayout fl = (FlowLayout) view.findViewById(R.id.fl_contacts);
                TextView tvFes = (TextView) view.findViewById(R.id.tv_fes);
                TextView tvDate = (TextView) view.findViewById(R.id.tv_date);
                tvMsg.setText(cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_MSG)));
                tvFes.setText(cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_FES_NAME)));
                long date = cursor.getLong(cursor.getColumnIndex(SendedMsg.COLUMN_DATE));
                tvDate.setText(parseDate(date));
                String names = cursor.getString(cursor.getColumnIndex(SendedMsg.COLUMN_NAMES));

                if(TextUtils.isEmpty(names)){
                    return;
                }
                fl.removeAllViews();
                for(String name : names.split(":")){
                    addTag(name,fl);
                }
            }
        };
        setListAdapter(cursorAdapter);
    }
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String parseDate(long date) {
        return df.format(date);
    }

    private void addTag(String name, FlowLayout fl) {
        TextView tv = (TextView) inflater.inflate(R.layout.tag,fl,false);
        tv.setText(name);
        fl.addView(tv);
    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader loader = new CursorLoader(getActivity(), SmsProvider.URI_SMS_ALL,null,null,null,null);

                return loader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if(loader.getId() == LOADER_ID){
                    cursorAdapter.swapCursor(data);
                }

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                cursorAdapter.swapCursor(null);
            }
        });
    }
}
