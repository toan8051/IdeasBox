package vn.alovoice.ideasbox;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "onStarted");
        ArrayList<idea> mIdea = new ArrayList<idea>();
        doGetIdeas(this, mIdea);
        int count = 0;

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for (idea simpleIdea : mIdea) {
            values.clear();
            values.put(IdeaContract.Column.ID, simpleIdea.getId());
            values.put(IdeaContract.Column.DIENTHOAI,
                    simpleIdea.getDienThoai());
            values.put(IdeaContract.Column.NOIDUNG,
                    simpleIdea.getNoiDung());
            values.put(IdeaContract.Column.NGAYTAO, simpleIdea.getNgayTao().getTime());
            //db.insertWithOnConflict(IdeaContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            Uri uri = getContentResolver().insert(IdeaContract.CONTENT_URI, values);
            if (uri != null) {
                count++;
                Log.e(TAG, String.format("%s : %s : %s", simpleIdea.getDienThoai(), simpleIdea.getNoiDung(),simpleIdea.getNgayTao()));
            }
            //Log.e(TAG, String.format("%s: nok%s : ", simpleIdea.getDienThoai(), simpleIdea.getNoiDung()));
        }

        //Truy van du lieu xem da luu vao SQLite thong qua provider duoc chua
        String dictTextView="";
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(IdeaContract.CONTENT_URI, null, null, null, null);


        try {
            int noidungColumn = cursor.getColumnIndex(IdeaContract.Column.NOIDUNG);
            int ngaytaoColumn = cursor.getColumnIndex(IdeaContract.Column.NGAYTAO);
            int dienthoaiColumn = cursor.getColumnIndex(IdeaContract.Column.DIENTHOAI);
            // Duy?t con tr? ?? l?y t?t c? các dòng
            //cursor.moveToFirst();
            while (cursor.moveToNext()) {
                //L?y d? li?u c?a các c?t
                int dienthoai = cursor.getInt(dienthoaiColumn);
                String noidung = cursor.getString(noidungColumn);
                String ngaytao = cursor.getString(ngaytaoColumn);
                //??a vào bi?n dictTextView ?? hi?n th? lên màn hình.
                dictTextView =  dictTextView + "\n dt: " + dienthoai + " - noidung: " + noidung + " - ngaytao: " + ngaytao;
            }

        } catch(Exception e){
            Log.e(TAG, e.getMessage()+"that la");

        } finally {
            // ?óng Cursor
            cursor.close();
        }
        Log.e(TAG, dictTextView);
        //Log.e(TAG, "onStarted-end");

        return;
    }

    @Override
    public void onDestroy() {
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

                int mid =Integer.parseInt(soapObject.getPropertyAsString("id"));
                String mNoiDung = soapObject.getPropertyAsString("noidung");
                String mNgayTao = soapObject.getPropertyAsString("ngaytao");
                String mDienThoai = soapObject.getPropertyAsString("dienthoai");

                //??y vào array
                //Luotgiao lg = new Luotgiao("ID_luotgiao", luotgiao, "nhanvientc", soluong_hd, sotien, "ngaygiao","ghichu");
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                String dateInString = "07/06/2013";
                Date date = formatter.parse(dateInString);

                idea mIdea = new idea(mid, mNoiDung, date, mDienThoai);
                arrIdea.add(mIdea);
                //Log.e(TAG, mNoiDung);
            }
        } catch(Exception e){
            //Toast.makeText(ct, "L?i nè! " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, e.getMessage());
            e.printStackTrace();

        }
    }
}

