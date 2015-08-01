package vn.alovoice.ideasbox;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;


/**
 * A placeholder fragment containing a simple view.
 */
public class IdeaFragment extends Fragment {
    private idea mIdea;
    private EditText mTitleField;
    private Button mButtonSendIdeas;


    public IdeaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String TAG_LOG = "onCreateView";
        View v = inflater.inflate(R.layout.fragment_idea, container, false);
        mTitleField = (EditText)v.findViewById(R.id.idea_noidung);

        mButtonSendIdeas = (Button)v.findViewById(R.id.send_ideas);
        mButtonSendIdeas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idea mIdea = new idea();
                mIdea.setNoiDung(mTitleField.getText().toString());

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String mDienThoai = prefs.getString(getString(R.string.dienthoai_key), getString(R.string.dienthoai_default));
                mIdea.setDienThoai(mDienThoai);
                Log.e(TAG_LOG, mDienThoai);
                new AsyncAddIdeas().execute(mIdea);
            }
        });

        return v;
    }

    public String send_ideas(idea mIdea){
         final String TAG_LOG = "Ksoap2";



        try{
            final String URL="http://www.alovoice.vn/food.php";
            final String NAMESPACE="http://www.alovoice.vn/foodservice";
            final String METHOD_NAME="food.addIdeas";

            //final String SOAP_ACTION=NAMESPACE+METHOD_NAME;
            final String SOAP_ACTION="http://www.alovoice.vn/foodservice#addIdeas";
            SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);

            // đưa thông số cho hàm
            PropertyInfo i_ideas_noidung = new PropertyInfo();
            i_ideas_noidung.setName("noidung");
            i_ideas_noidung.setValue(mIdea.getNoiDung());
            i_ideas_noidung.setType(String.class);
            request.addProperty(i_ideas_noidung);

            PropertyInfo i_ideas_ngaytao = new PropertyInfo();
            i_ideas_ngaytao.setName("ngaytao");
            i_ideas_ngaytao.setValue(new Date().getDay());
            i_ideas_ngaytao.setType(String.class);
            request.addProperty(i_ideas_ngaytao);

            PropertyInfo i_ideas_anh = new PropertyInfo();
            i_ideas_anh.setName("anh");
            i_ideas_anh.setValue("");
            i_ideas_anh.setType(String.class);
            request.addProperty(i_ideas_anh);

            PropertyInfo i_ideas_dienthoai = new PropertyInfo();
            i_ideas_dienthoai.setName("dienthoai");
            i_ideas_dienthoai.setValue(mIdea.getDienThoai());
            i_ideas_dienthoai.setType(String.class);
            request.addProperty(i_ideas_dienthoai);

            SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE androidHttpTransport= new HttpTransportSE(URL,5000000);
            androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            androidHttpTransport.debug=true;
            //SSLConnection.allowAllSSL();
            //gọi thực hiện hàm addIdeas của webservice
            androidHttpTransport.call(SOAP_ACTION, envelope);

            String soapArray = (String)envelope.getResponse();
            String dump= androidHttpTransport.requestDump.toString();
            //Toast.makeText(getBaseContext(),  "KQ: " + size + " - " + soapObject.getPropertyAsString("GIAOPHIEU_ID"), Toast.LENGTH_LONG).show();

            Log.e(TAG_LOG, dump);
            return soapArray.toString();

        } catch(Exception e) {
            //Toast.makeText(getActivity(),"Lỗi nè : " + e.toString(), Toast.LENGTH_LONG).show();
            Log.e(TAG_LOG, e.toString());
            e.printStackTrace();
            return e.toString();
        }

	}
    private class AsyncAddIdeas extends AsyncTask<idea, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(idea... params) {
            idea mIdea=params[0];
            return send_ideas(mIdea);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(),  "KQ: " + s, Toast.LENGTH_LONG).show();
        }
    }


}
