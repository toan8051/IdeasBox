package vn.alovoice.ideasbox;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by VIENTHONG on 8/9/2015.
 */
public class IdeaContract {
    // DB specific constants
    public static final String DB_NAME = "ideabox.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "ideas";
    public static final String DEFAULT_SORT = Column. NGAYTAO + " DESC";
    public class Column {
        public static final String ID = BaseColumns._ID;
        public static final String DIENTHOAI = "dienthoai";
        public static final String NOIDUNG = "noidung";
        public static final String NGAYTAO = "ngaytao";
    }
    // Provider specific constants
    // content://vn.alovoice.ideasbox.IdeaProvider/idea
    public static final String AUTHORITY = "vn.alovoice.ideasbox.IdeaProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE);
    public static final int IDEA_ITEM = 1;
    public static final int IDEA_DIR = 2;
    public static final String IDEA_TYPE_ITEM =
            "vnd.android.cursor.item/vnd.vn.alovoice.ideasbox.IdeaProvider.idea";
    public static final String STATUS_TYPE_DIR =
            "vnd.android.cursor.dir/vnd.vn.alovoice.ideasbox.IdeaProvider.idea";
}
