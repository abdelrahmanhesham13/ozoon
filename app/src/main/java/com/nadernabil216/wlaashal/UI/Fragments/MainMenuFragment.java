package com.nadernabil216.wlaashal.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.UI.Activities.AboutActivity;
import com.nadernabil216.wlaashal.UI.Activities.AddNewAdvertActivity;
import com.nadernabil216.wlaashal.UI.Activities.ContactUsActivity;
import com.nadernabil216.wlaashal.UI.Activities.SettingsActivity;
import com.nadernabil216.wlaashal.Utils.GMethods;

/**
 * Created by NaderNabil216@gmail.com on 5/11/2018.
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {
    ImageView profile_pic;
    TextView user_name;
    CardView add_advert_card, profile_card, chat_card, settings_card, about_card, contact_us_card, privacy_card, share_card, logout_card;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_fragment, container, false);
        GMethods.ChangeViewFont(view);
        InitViews(view);
        return view;
    }

    private void InitViews(View view) {
        profile_pic = view.findViewById(R.id.profile_image);
        user_name = view.findViewById(R.id.user_name);
        add_advert_card = view.findViewById(R.id.add_advert_card);
        profile_card = view.findViewById(R.id.profile_card);
        chat_card = view.findViewById(R.id.chat_card);
        settings_card = view.findViewById(R.id.settings_card);
        about_card = view.findViewById(R.id.about_card);
        contact_us_card = view.findViewById(R.id.contact_us_card);
        privacy_card = view.findViewById(R.id.privacy_card);
        share_card = view.findViewById(R.id.share_card);
        logout_card = view.findViewById(R.id.logout_card);

        add_advert_card.setOnClickListener(this);
        profile_card.setOnClickListener(this);
        chat_card.setOnClickListener(this);
        settings_card.setOnClickListener(this);
        about_card.setOnClickListener(this);
        contact_us_card.setOnClickListener(this);
        privacy_card.setOnClickListener(this);
        share_card.setOnClickListener(this);
        logout_card.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_advert_card:
                startActivity(new Intent(getActivity(), AddNewAdvertActivity.class));
                break;
            case R.id.profile_card:

                break;
            case R.id.chat_card:

                break;
            case R.id.settings_card:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.about_card:
                startActivity(new Intent(getActivity(),AboutActivity.class));
                break;
            case R.id.contact_us_card:
                startActivity(new Intent(getActivity(),ContactUsActivity.class));
                break;
            case R.id.privacy_card:
                GMethods.OpenPopUpBrowser(getActivity(),GMethods.Privacy_Url);
                break;
            case R.id.share_card:
                GMethods.ShareAppLink(getActivity());
                break;
            case R.id.logout_card:

                break;

        }
    }
}
