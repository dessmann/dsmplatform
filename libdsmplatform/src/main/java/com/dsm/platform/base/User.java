package com.dsm.platform.base;

import net.tsz.afinal.annotation.sqlite.Table;

import java.io.Serializable;

/**
 * 用户的数据结构
 */
@Table(name = "tb_user")
public class User implements Serializable{
    private Long serialVersionUID = 0L;

    public Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSerialVersionUID(Long serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
    }

    private Integer id;
    /**
     * token
     */
    private String token;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 姓名
     */
    private String name;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 地址
     */
    private String useraddress;
    private String birthday;
    /**
     * 用户类型
     */
    private Integer type;
    /**
     * 头像地址
     */
    private String iconurl;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 证件类型
     */
    private String cardtype;
    /**
     * 身份证号
     */
    private String cardnum;
    private String email;
    /**
     * 上一次登陆的手机类型
     */
    private String phonetype;
    private String time;
    private String phoneseriesnumber;
    /**
     * 废弃(无用字段)
     */
    private String baiduuserid;
    /**
     * 推送cid
     */
    private String baiduchannelid;
    /**
     * 状态
     */
    private String state;
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String item5;
    /**
     * 是否物业业主的标
     */
    private String item6;
    private Integer userPoint;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonetype() {
        return phonetype;
    }

    public void setPhonetype(String phonetype) {
        this.phonetype = phonetype;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhoneseriesnumber() {
        return phoneseriesnumber;
    }

    public void setPhoneseriesnumber(String phoneseriesnumber) {
        this.phoneseriesnumber = phoneseriesnumber;
    }

    public String getBaiduuserid() {
        return baiduuserid;
    }

    public void setBaiduuserid(String baiduuserid) {
        this.baiduuserid = baiduuserid;
    }

    public String getBaiduchannelid() {
        return baiduchannelid;
    }

    public void setBaiduchannelid(String baiduchannelid) {
        this.baiduchannelid = baiduchannelid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public String getItem4() {
        return item4;
    }

    public void setItem4(String item4) {
        this.item4 = item4;
    }

    public String getItem5() {
        return item5;
    }

    public void setItem5(String item5) {
        this.item5 = item5;
    }

    public String getItem6() {
        return item6;
    }

    public void setItem6(String item6) {
        this.item6 = item6;
    }

    public Integer getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(Integer userPoint) {
        this.userPoint = userPoint;
    }

    @Override
    public String toString() {
        return "User{" +
                "serialVersionUID=" + serialVersionUID +
                ", id=" + id +
                ", token='" + token + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", useraddress='" + useraddress + '\'' +
                ", birthday='" + birthday + '\'' +
                ", type=" + type +
                ", iconurl='" + iconurl + '\'' +
                ", mobile='" + mobile + '\'' +
                ", cardtype='" + cardtype + '\'' +
                ", cardnum='" + cardnum + '\'' +
                ", email='" + email + '\'' +
                ", phonetype='" + phonetype + '\'' +
                ", time='" + time + '\'' +
                ", phoneseriesnumber='" + phoneseriesnumber + '\'' +
                ", baiduuserid='" + baiduuserid + '\'' +
                ", baiduchannelid='" + baiduchannelid + '\'' +
                ", state='" + state + '\'' +
                ", item1='" + item1 + '\'' +
                ", item2='" + item2 + '\'' +
                ", item3='" + item3 + '\'' +
                ", item4='" + item4 + '\'' +
                ", item5='" + item5 + '\'' +
                ", item6='" + item6 + '\'' +
                ", userPoint='" + userPoint + '\'' +
                '}';
    }
}