package com.wyj.myqq.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wyj.myqq.R;
import com.wyj.myqq.activity.FriendDetail;
import com.wyj.myqq.adapter.ContactsAdapter;
import com.wyj.myqq.bean.Friends;
import com.wyj.myqq.utils.Constant;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**

 */
public class Contacts extends ListFragment implements View.OnClickListener{

    private ArrayList<Friends> listFriends = null;
    private TextView tvSearch;
    private RelativeLayout rlAddFriend,rlGroupChat;
    private ContactsAdapter adapter;
    private Friends friends;
    private String qqnumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contactsLayout = inflater.inflate(R.layout.fragment_contacts, container, false);
        rlAddFriend = (RelativeLayout) contactsLayout.findViewById(R.id.rl_new_friend);
        rlGroupChat = (RelativeLayout) contactsLayout.findViewById(R.id.rl_group_chat);
        tvSearch = (TextView) contactsLayout.findViewById(R.id.tv_search);
        if (getArguments() != null) {
            qqnumber = getArguments().getString(Constant.KEY_QQNUMBER);
            if (getArguments().getSerializable(Constant.KEY_FRIENDS) != null) {
                listFriends = (ArrayList<Friends>) getArguments().getSerializable(Constant.KEY_FRIENDS);
            }
            /*ArrayList<Friends> list = new ArrayList<>();
            for(Friends friend : listFriends){
                list.checkbox_normal(new Friends(friend.getFriendQQ(),friend.getFriendNick(),friend.getFriendImg()))
            }*/
            adapter = new ContactsAdapter(getActivity(),R.layout.contactslist_item,listFriends);

            setListAdapter(adapter);
        }
        rlAddFriend.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        rlGroupChat.setOnClickListener(this);
        return contactsLayout;
    }
    @Override
    public void onDestroyView() {
        Log.e("fragment state","onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Log.e("fragment state","onDetach");
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        if(onContactsListener!=null){
            onContactsListener.contactsClick(v);
        }
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        friends = listFriends.get(position);
        Intent intent = new Intent(getActivity(), FriendDetail.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_QQNUMBER,qqnumber);
        bundle.putSerializable(Constant.KEY_FRIENDS,friends);
        intent.putExtras(bundle);
        startActivityForResult(intent,Constant.REQUEST_CODE_DELETE_FRIEND);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constant.REQUEST_CODE_DELETE_FRIEND){
            if(resultCode == Constant.RESULT_CODE_DELETE_FRIEND){
                listFriends.remove(friends);
                adapter.notifyDataSetChanged();
                //RongIM.getInstance().deleteMessages();
                RongIM.getInstance().removeConversation(
                        Conversation.ConversationType.PRIVATE,friends.getFriendQQ());
            }
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

}
