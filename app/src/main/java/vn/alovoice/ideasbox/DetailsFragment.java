package vn.alovoice.ideasbox;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by VIENTHONG on 8/11/2015.
 */
public class DetailsFragment extends Fragment { //
    private TextView textNoidung, textNgaytao, textDienthoai;

    public DetailsFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_item, null, false); //
        textNgaytao = (TextView) view.findViewById(R.id.list_item_text_ngaytao);
        textNoidung = (TextView) view.findViewById(R.id.list_item_text_noidung);
        textDienthoai = (TextView) view.findViewById(R.id.list_item_text_dienthoai);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        long id = getActivity().getIntent().getLongExtra(IdeaContract.Column.ID, -1); //
        //Log.e("DetailsFragment", "onListItemClick - noidung " + id);
        updateView(id);
    }

    public void updateView(long id) { //
        //Log.e("DetailsFragment", "noidung");
        if (id == -1) {
            textNoidung.setText("");
            textNgaytao.setText("");
            textDienthoai.setText("");
            return;
        }

        Uri uri = ContentUris.withAppendedId(IdeaContract.CONTENT_URI, id);
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        if (!cursor.moveToFirst())
            return;

        Long ngaytao = cursor.getLong(cursor
                .getColumnIndex(IdeaContract.Column.NGAYTAO));
        String noidung = cursor.getString(cursor
                .getColumnIndex(IdeaContract.Column.NOIDUNG));
        String dienthoai = cursor.getString(cursor
                .getColumnIndex(IdeaContract.Column.DIENTHOAI));
        textDienthoai.setText(dienthoai);
        textNoidung.setText(noidung);
        textNgaytao.setText(DateUtils.getRelativeTimeSpanString(ngaytao));
        //Log.e("DetailsFragment", noidung);
        cursor.close();
    }

}