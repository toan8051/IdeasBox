package vn.alovoice.ideasbox;

import java.util.Date;

/**
 * Created by VIENTHONG on 7/25/2015.
 */
public class idea {
    private int id;
    private String mNoiDung;
    private Date mNgayTao;
    private String mAnh;
    private String mDienThoai;
    private int id_theloai;
    private Boolean mDuocChon;
    private String mGhiChu;

    public idea(){}
    public idea(int id, String noidung, Date ngaytao, String dienthoai){
        this.id = id;
        this.mNoiDung = noidung;
        this.mNgayTao = ngaytao;
        this.mDienThoai = dienthoai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoiDung() {
        return mNoiDung;
    }

    public void setNoiDung(String mNoiDung) {
        this.mNoiDung = mNoiDung;
    }

    public Date getNgayTao() {
        return mNgayTao;
    }

    public void setNgayTao(Date mNgayTao) {
        this.mNgayTao = mNgayTao;
    }

    public String getAnh() {
        return mAnh;
    }

    public void setAnh(String mAnh) {
        this.mAnh = mAnh;
    }

    public String getDienThoai() {
        return mDienThoai;
    }

    public void setDienThoai(String mDienThoai) {
        this.mDienThoai = mDienThoai;
    }

    public int getId_theloai() {
        return id_theloai;
    }

    public void setId_theloai(int id_theloai) {
        this.id_theloai = id_theloai;
    }

    public Boolean getDuocChon() {
        return mDuocChon;
    }

    public void setDuocChon(Boolean mDuocChon) {
        this.mDuocChon = mDuocChon;
    }

    public String getGhiChu() {
        return mGhiChu;
    }

    public void setGhiChu(String mGhiChu) {
        this.mGhiChu = mGhiChu;
    }
}
