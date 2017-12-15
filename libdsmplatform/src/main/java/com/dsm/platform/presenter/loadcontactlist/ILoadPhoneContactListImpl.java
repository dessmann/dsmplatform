package com.dsm.platform.presenter.loadcontactlist;

import android.app.Activity;
import android.content.Context;

import com.dsm.platform.R;
import com.dsm.platform.listener.OnPermissionResult;
import com.dsm.platform.util.PermisstionUtil;
import com.dsm.platform.util.contact.ContactUtil;
import com.dsm.platform.util.contact.PhoneContact;

import java.util.Collections;
import java.util.List;

/**
 * Created by dccjll on 2017/3/22.
 * 请求通讯录权限并读取通讯录列表，需要在发起请求的活动界面接收权限请求回调
 */

public class ILoadPhoneContactListImpl implements ILoadPhoneContactList {

    private final Context context;
    private final ILoadPhoneContactListView iLoadPhoneContactListView;

    public ILoadPhoneContactListImpl(Context context, ILoadPhoneContactListView iLoadPhoneContactListView) {
        this.context = context;
        this.iLoadPhoneContactListView = iLoadPhoneContactListView;
    }

    @Override
    public void requestLoadPhoneContactList() {
        requestReadContactListPermission();
    }

    private void requestReadContactListPermission() {
        PermisstionUtil.requestPermissions(
                context,
                PermisstionUtil.CONTACTS,
                PermisstionUtil.CONTACTS_CODE,
                context.getString(R.string.explain_read_contact_permission),
                new OnPermissionResult() {
                    @Override
                    public void granted(int requestCode) {
                        requestContactList();
                    }

                    @Override
                    public void denied(int requestCode) {
                        iLoadPhoneContactListView.loadContactListFailure(-60021);
                    }
                }
        );
    }

    private void requestContactList() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        final List<PhoneContact> phoneContactList;
                        try {
                            phoneContactList = ContactUtil.getAllPhoneContacts(context);
                            Collections.sort(phoneContactList, new PhoneContact());
                        } catch (Exception e) {
                            e.printStackTrace();
                            iLoadPhoneContactListView.loadContactListFailure(-60034);
                            return;
                        }
                        ((Activity)context).runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        iLoadPhoneContactListView.loadContactListSuccess(phoneContactList);
                                    }
                                }
                        );
                    }
                }
        ).start();
    }
}
