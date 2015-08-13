package vn.alovoice.ideasbox;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by VIENTHONG on 8/11/2015.
 */
public class listIdeaFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
        private static final String TAG = listIdeaFragment.class.getSimpleName();
        private static final String[] FROM = {
                IdeaContract.Column.NGAYTAO,
                IdeaContract.Column.NOIDUNG,
                IdeaContract.Column.DIENTHOAI,
                };
        private static final int[] TO = {
                R.id.list_item_text_ngaytao,
                R.id.list_item_text_noidung,
                R.id.list_item_text_dienthoai,
                };
        private static final int LOADER_ID = 42; //
        private SimpleCursorAdapter mAdapter;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {

            super.onActivityCreated(savedInstanceState);
            mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item,
                    null, FROM, TO, 0);
            mAdapter.setViewBinder(VIEW_BINDER);
            setListAdapter(mAdapter);
            getLoaderManager().initLoader(LOADER_ID, null, this); //
        }

        private static final SimpleCursorAdapter.ViewBinder VIEW_BINDER = new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor,
                                        int columnIndex) {
                long timestamp;
    // Custom binding
                switch (view.getId()) {
                    case R.id.list_item_text_ngaytao:
                        timestamp = cursor.getLong(columnIndex);
                        CharSequence relTime = DateUtils.getRelativeTimeSpanString(timestamp);
                        ((TextView) view).setText(relTime);
                        return true;
//                    case R.id.list_item_freshness:
//                        timestamp = cursor.getLong(columnIndex);
//                        ((FreshnessView) view).setTimestamp(timestamp);
//                        return true;
                    default:
                        return false;
                }
            }
        };

//        /** Handles custom binding of data to view. */
//        class listIdeaViewBinder implements SimpleCursorAdapter.ViewBinder { //
//            @Override
//            public boolean setViewValue(View view, Cursor cursor,
//                                        int columnIndex) { //
//                if (view.getId() != R.id.list_item_text_ngaytao) //
//                    return false;
//                // Convert timestamp to relative time
//                long timestamp = cursor.getLong(columnIndex); //
//                CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timestamp); //
//                ((TextView) view).setText(relativeTime); //
//                return true; //
//            }
//        }

        public void onListItemClick(ListView l, View v, int position, long id) {

            // Get the details fragment
            DetailsFragment fragment = (DetailsFragment) getFragmentManager()
                    .findFragmentById(R.id.fragment_details); //
            // Is details fragment visible?
            if (fragment != null && fragment.isVisible()) { //
                fragment.updateView(id); //
            } else {
                //Log.e("DetailsFragment", "onListItemClick - noidung " + id);
                startActivity(new Intent(getActivity(), DetailsActivity.class)
                        .putExtra(IdeaContract.Column.ID, id)); //
            }
            Log.e(TAG, "ID la: " + id);
        }

        // --- Loader Callbacks ---
        // Executed on a non-UI thread
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) { //
            if (id != LOADER_ID)
                return null;
            Log.e(TAG, "onCreateLoader");
            return new CursorLoader(getActivity(), IdeaContract.CONTENT_URI,
                    null, null, null, IdeaContract.DEFAULT_SORT); //
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            // Get the details fragment
            DetailsFragment fragment = (DetailsFragment) getFragmentManager()
                    .findFragmentById(R.id.fragment_details);
            // Is details fragment visible?
            if (fragment != null && fragment.isVisible() && cursor.getCount()
                    == 0) {
                fragment.updateView(-1);
                Toast.makeText(getActivity(), "No data", Toast.LENGTH_LONG).show();
            }
            Log.e(TAG, "onLoadFinished with cursor: " + cursor.getCount());
            mAdapter.swapCursor(cursor); //
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) { //
            mAdapter.swapCursor(null);
        }

}
