package vn.alovoice.ideasbox;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by VIENTHONG on 8/5/2015.
 */
public class RefreshService extends IntentService {
    static final String TAG = "RefreshService";

    public RefreshService() { //
        super(TAG);
    }
    @Override
    public void onCreate() { //
        super.onCreate();
        Log.e(TAG, "onCreated");
    }
    // Executes on a worker thread
    @Override
    protected void onHandleIntent(Intent intent) { //
        Log.e(TAG, "onStarted");
        ArrayList<idea> mIdea = new ArrayList<idea>();
        doGetIdeas(this,mIdea);

    }
    @Override
    public void onDestroy() { //
        super.onDestroy();
        Log.e(TAG, "onDestroyed");
    }

    public static void doGetIdeas(Context ct, ArrayList<idea> arrIdea) {
        try{
            final String URL="http://www.alovoice.vn/food.php";
            final String NAMESPACE="http://www.alovoice.vn/foodservice";
            final String METHOD_NAME="food.getIdeas";

            //final String SOAP_ACTION=NAMESPACE+METHOD_NAME;
            final String SOAP_ACTION="http://www.alovoice.vn/foodservice#getIdeas";
            SoapObject request=new SoapObject(NAMESPACE, METHOD_NAME);


            //int ID=Integer.parseInt(id_luotgiao);
            PropertyInfo i_ngaytao = new PropertyInfo();
            i_ngaytao.setName("ngaytao");
            i_ngaytao.setValue("");//id_nhanvientc
            i_ngaytao.setType(String.class);
            request.addProperty(i_ngaytao);

            SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            //N?u truy?n s? th?c trên m?ng b?t bu?c ph?i ??ng ký MarshalFloat
            //không có nó thì b? báo l?i
            //MarshalFloat marshal=new MarshalFloat();
            // marshal.register(envelope);

            HttpTransportSE androidHttpTransport= new HttpTransportSE(URL);
            androidHttpTransport.debug=true;
            //SSLConnection.allowAllSSL();
            androidHttpTransport.call(SOAP_ACTION, envelope);

            //String kq = androidHttpTransport.requestDump;

            //Get Array Catalog into soapArray
            //SoapObject soapArray=(SoapObject) envelope.getResponse();
            Vector<?> soapArray = (Vector<?>) envelope.getResponse();
            //SoapObject soapArray=(SoapObject) envelope.bodyIn;
            //final int size = soapArray.size();
            //arrlg.clear();
            Vector<?> soapArray_data = (Vector<?>)soapArray.get(1);

            //Log.e(TAG, soapArray_data.size() + "");
            //Toast.makeText(getBaseContext(),  "KQ: " + size, Toast.LENGTH_LONG).show();

            for(int i=0;i<soapArray_data.size();i++){
                SoapObject soapObject=(SoapObject)soapArray_data.get(i);

                String mNoiDung=soapObject.getPropertyAsString("noidung");
                Log.e(TAG, mNoiDung);
                //??y vào array
                //Luotgiao lg = new Luotgiao("ID_luotgiao", luotgiao, "nhanvientc", soluong_hd, sotien, "ngaygiao","ghichu");
                //arrlg.add(lg);
            }
        } catch(Exception e){
            //Toast.makeText(ct, "L?i nè! " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());
            e.printStackTrace();

        }
    }
}

