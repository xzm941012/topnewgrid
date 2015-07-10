package com.example.topnewgrid.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.topnewgrid.R;

import io.rong.imkit.fragment.ConversationGroupListFragment;


public class Fragment_huihua extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ry_fragment_huihualist, container, false);
        System.out.println("hahahhaa");
		FragmentTransaction ft = this.getActivity().getSupportFragmentManager()
				.beginTransaction();
		ConversationGroupListFragment conversationGroupListFragment = new io.rong.imkit.fragment.ConversationGroupListFragment();
		ft.replace(R.id.conversation_huihualist, conversationGroupListFragment);
		ft.commit();
        return view;
    }

}

