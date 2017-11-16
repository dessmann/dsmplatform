package com.dsm.platform.presenter.loadcontactlist;


import com.dsm.platform.util.contact.PhoneContact;

import java.util.List;

/**
 * Created by dccjll on 2017/3/22.
 * 请求通讯录权限并读取通讯录列表，需要在发起请求的活动界面接收权限请求回调
 */

public interface ILoadPhoneContactListView {
    void loadContactListSuccess(List<PhoneContact> phoneContactList);
    void loadContactListFailure(String error, int loglevel);
}
