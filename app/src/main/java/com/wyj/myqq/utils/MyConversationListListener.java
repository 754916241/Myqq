package com.wyj.myqq.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.wyj.myqq.activity.ConfirmFriend;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;

/**
 * Created by wyj on 2017/3/24.
 */

public class MyConversationListListener implements RongIM.ConversationListBehaviorListener {
    @Override
    public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s) {
        return false;
    }

    @Override
    public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation) {
        return false;
    }

    @Override
    public boolean onConversationClick(Context context, View view, UIConversation uiConversation) {
        if(uiConversation.getMessageContent() instanceof ContactNotificationMessage){
            ContactNotificationMessage message = (ContactNotificationMessage) uiConversation.getMessageContent();
            if(message.getOperation().equals(ContactNotificationMessage.CONTACT_OPERATION_REQUEST)){
                Intent intent = new Intent(context, ConfirmFriend.class);
                context.startActivity(intent);
            }
            return true;
        }
        return false;
    }
}
