package com.android.chattingapp.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.chattingapp.Fragments.CallFragment;
import com.android.chattingapp.Fragments.ChatsFragment;
import com.android.chattingapp.Fragments.OppointMentFragment;
import com.android.chattingapp.Fragments.UserChatFragment;

public class UserFragmentAdapter extends FragmentPagerAdapter {
    public UserFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0: return new UserChatFragment();
            case 1: return new OppointMentFragment();
            case 2: return new CallFragment();
            default: return new UserChatFragment();
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position==0){
            title = "Chats";
        }
        if(position==1){
            title = "Status";
        }
        if(position==2){
            title = "Calls";
        }
        return title;
    }
}
