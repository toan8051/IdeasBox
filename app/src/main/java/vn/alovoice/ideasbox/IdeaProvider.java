package vn.alovoice.ideasbox;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by VIENTHONG on 8/9/2015.
 */
public class IdeaProvider extends ContentProvider {
    private static final String TAG = IdeaProvider.class.getSimpleName();
    private DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        Log.e(TAG, "onCreated");
        return true;
    }

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(IdeaContract.AUTHORITY, IdeaContract.TABLE, IdeaContract.IDEA_DIR);
        sURIMatcher.addURI(IdeaContract.AUTHORITY, IdeaContract.TABLE + "/#", IdeaContract.IDEA_ITEM);
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case IdeaContract.IDEA_DIR:
                Log.d(TAG, "gotType: " + IdeaContract.STATUS_TYPE_DIR);
                return IdeaContract.STATUS_TYPE_DIR;
            case IdeaContract.IDEA_ITEM:
                Log.d(TAG, "gotType: " + IdeaContract.IDEA_TYPE_ITEM);
                return IdeaContract.IDEA_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("Illegal uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri ret = null;
        // kiem tra chinh xac chua//
        if (sURIMatcher.match(uri) != IdeaContract.IDEA_DIR) {
            Log.e(TAG, "Illegal uri: " + uri);
            throw new IllegalArgumentException("Illegal uri: " + uri);

        }
        SQLiteDatabase db = dbHelper.getWritableDatabase(); //
        long rowId = db.insertWithOnConflict(IdeaContract.TABLE, null,
                values, SQLiteDatabase.CONFLICT_IGNORE); //
        // Them moi thanh cong khong?
        if (rowId != -1) { //
            long id = values.getAsLong(IdeaContract.Column.ID);
            ret = ContentUris.withAppendedId(uri, id); //
            Log.e(TAG, "inserted uri: " + ret);
        // Cap nhat trang thai du lieu da thay doi tren URI nay.
            getContext().getContentResolver().notifyChange(uri, null); //
        }
        return ret;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String where;
        switch (sURIMatcher.match(uri)) {
            case IdeaContract.IDEA_DIR:
            // so we count updated rows
                where = selection;
                break;
            case IdeaContract.IDEA_ITEM:
                long id = ContentUris.parseId(uri);
                where = IdeaContract.Column.ID
                        + "="
                        + id
                        + (TextUtils.isEmpty(selection) ? "" : " and ( " + selection + " )");
                break;
            default:
                throw new IllegalArgumentException("Illegal uri: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int ret = db.update(IdeaContract.TABLE, values, where, selectionArgs);
        if(ret>0) {
            // Cap nhat trang thai du lieu da thay doi tren URI nay.
             getContext().getContentResolver().notifyChange(uri, null);
        }
        Log.d(TAG, "updated records: " + ret);
        return ret;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String where;
        switch (sURIMatcher.match(uri)) {

            case IdeaContract.IDEA_DIR:
                // so we count deleted rows
                where = (selection == null) ? "1" : selection;
                break;
            case IdeaContract.IDEA_ITEM:
                long id = ContentUris.parseId(uri);
                where = IdeaContract.Column.ID
                        + "="
                        + id
                        + (TextUtils.isEmpty(selection) ? "" : " and ( "
                        + selection + " )");
                break;
            default:
                throw new IllegalArgumentException("Illegal uri: " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int ret = db.delete(IdeaContract.TABLE, where, selectionArgs);
        if(ret>0) {
            // Cap nhat trang thai du lieu da thay doi tren URI nay.
            getContext().getContentResolver().notifyChange(uri, null);
        }
        Log.d(TAG, "deleted records: " + ret);
        return ret;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables( IdeaContract.TABLE );
        switch (sURIMatcher.match(uri)) {
            case IdeaContract.IDEA_DIR:
                break;
            case IdeaContract.IDEA_ITEM:
                qb.appendWhere(IdeaContract.Column.ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Illegal uri: " + uri);
        }
        String orderBy = (TextUtils.isEmpty(sortOrder))
                ? IdeaContract.DEFAULT_SORT
                : sortOrder;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, orderBy); //
        // Cap nhat trang thai du lieu da thay doi tren URI nay.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.e(TAG, "queried records: " + uri + cursor.getCount());
        return cursor;
    }

}