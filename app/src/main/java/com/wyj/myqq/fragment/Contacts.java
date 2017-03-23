package com.wyj.myqq.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.wyj.myqq.R;
import com.wyj.myqq.adapter.ContactsAdapter;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.utils.Constant;

import java.util.List;

import io.rong.imkit.RongIM;

/**

 */
public class Contacts extends ListFragment implements View.OnClickListener{

    private List<Friends> list = null;
    private EditText edtSearch;
    private RelativeLayout rlAddFriend,rlGroupChat;
    private ContactsAdapter adapter;


    private Friends friends;
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View contactsLayout = inflater.inflate(R.layout.fragment_contacts, container, false);
        rlAddFriend = (RelativeLayout) contactsLayout.findViewById(R.id.rl_new_friend);
        rlGroupChat = (RelativeLayout) contactsLayout.findViewById(R.id.rl_group_chat);
        edtSearch = (EditText) contactsLayout.findViewById(R.id.edt_search);
        if (getArguments() != null) {
            if (getArguments().getSerializable(Constant.KEY_FRIENDS) != null) {
                list = (List<Friends>) getArguments().getSerializable(Constant.KEY_FRIENDS);
            }

            adapter = new ContactsAdapter(getActivity(), list);
            setListAdapter(adapter);
        }
        //listLongClick();
        rlAddFriend.setOnClickListener(this);

        return contactsLayout;
    }

    @Override
    public void onClick(View v) {
        if(onContactsListener!=null){
            onContactsListener.contactsClick(v);
        }
    }

    private void listLongClick(){
        lv = getListView();
        lv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
     //   getContext();
        String targetQQ = adapter.getItem(position).getFriendQQ();
        if(RongIM.getInstance()!=null){
            RongIM.getInstance().startPrivateChat(getActivity(),targetQQ,
                    "与"+adapter.getItem(position).getFriendNick()+"聊天中");
            Log.e("nick","nickname="+adapter.getItem(position).getFriendNick()+"targetQQ="+targetQQ);
        }
    }

    private OnContactsListener onContactsListener;

    public void setOnContactsListener(OnContactsListener onContactsListener) {
        this.onContactsListener = onContactsListener;
    }


    public interface OnContactsListener {
        void contactsClick(View view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onContactsListener = (OnContactsListener) context;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_CODE_ADDFRIENDS) {
            if (resultCode == Constant.RESULT_CODE_ADDFRIENDS) {
                Bundle bundle = data.getExtras();
                friends = (Friends) bundle.getSerializable(Constant.KEY_FRIENDS_ITEM);
                list.add(friends);
                setListAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }


}
