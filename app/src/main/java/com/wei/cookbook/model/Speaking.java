package com.wei.cookbook.model;

import android.provider.FontsContract;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class Speaking {

    public String getSpeaking() {
        return speaking;
    }

    public void setSpeaking(String speaking) {
        this.speaking = speaking;
    }

    private String speaking;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
    @Id(autoincrement = true)
    private Long id;

    private Date mDate;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    private String Address;

    public List<String> getDrawble() {
        return drawble;
    }

    public void setDrawble(List<String> drawble) {
        this.drawble = drawble;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getMDate() {
        return this.mDate;
    }

    public void setMDate(Date mDate) {
        this.mDate = mDate;
    }

    @Convert(columnType = String.class,converter = StringConverter.class)
    private List<String>  drawble;

    @Generated(hash = 1173245715)
    public Speaking(String speaking, Long id, Date mDate, String Address,
            List<String> drawble) {
        this.speaking = speaking;
        this.id = id;
        this.mDate = mDate;
        this.Address = Address;
        this.drawble = drawble;
    }

    @Generated(hash = 1161344253)
    public Speaking() {
    }

}
