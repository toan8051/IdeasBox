package vn.alovoice.ideasbox;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A placeholder fragment containing a simple view.
 */
public class CrimeFragment extends Fragment {
    private idea mIdea;
    private EditText mTitleField;
    public CrimeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_idea, container, false);
        mTitleField = (EditText)v.findViewById(R.id.idea_noidung);
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(
                    CharSequence c, int start, int before, int count) {
                //mIdea.setNoiDung(c.toString());
            }
            public void beforeTextChanged(
                    CharSequence c, int start, int count, int after) {
        // This space intentionally left blank
            }
            public void afterTextChanged(Editable c) {
        // This one too
            }
        });

        return v;
    }
}
