package com.dsm.platform.util.contact;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by yanfa on 2016/7/18.
 * 手机联系人
 */
public class PhoneContact implements Serializable, Comparator<PhoneContact> {
    private Long serialVersionUID = 0L;

    public Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSerialVersionUID(Long serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
    }
    private String name;
    private String mobile;
    private String namePinYin;
    private boolean select;//列表刷新辅助字段，无实际意义

    public PhoneContact() {
    }

    public PhoneContact(String name, String mobile, String namePinYin) {
        this.name = name;
        this.mobile = mobile;
        this.namePinYin = namePinYin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNamePinYin() {
        return namePinYin;
    }

    public void setNamePinYin(String namePinYin) {
        this.namePinYin = namePinYin;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public int compare(PhoneContact lhs, PhoneContact rhs) {
        return lhs.getNamePinYin().compareTo(rhs.getNamePinYin());
    }

    @Override
    public String toString() {
        return "PhoneContact{" +
                "serialVersionUID=" + serialVersionUID +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", namePinYin='" + namePinYin + '\'' +
                ", select=" + select +
                '}';
    }
}
