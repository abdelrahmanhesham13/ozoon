package com.smatech.wlaashal.UI.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smatech.wlaashal.ProfileActivity;
import com.smatech.wlaashal.R;
import com.smatech.wlaashal.UI.Activities.AddNewAdvertActivity;
import com.smatech.wlaashal.UI.Activities.ContactUsActivity;
import com.smatech.wlaashal.UI.Activities.HomeActivity;
import com.smatech.wlaashal.UI.Activities.LoginActivity;
import com.smatech.wlaashal.UI.Activities.SettingsActivity;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.Helper;
import com.smatech.wlaashal.Utils.StorageUtil;
import com.squareup.picasso.Picasso;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by NaderNabil216@gmail.com on 5/11/2018.
 */
public class MainMenuFragment extends Fragment implements View.OnClickListener {
    ImageView profile_pic;
    TextView user_name,mAddProductBtn;
    StorageUtil util;
    CardView add_advert_card, profile_card, chat_card, settings_card, about_card, contact_us_card, privacy_card, share_card, logout_card;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_menu_fragment, container, false);
        GMethods.ChangeViewFont(view);
        util = StorageUtil.getInstance().doStuff(getContext());
        InitViews(view);
        setData();
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
        mAddProductBtn = view.findViewById(R.id.add_product_btn);

        add_advert_card.setOnClickListener(this);
        profile_card.setOnClickListener(this);
        chat_card.setOnClickListener(this);
        settings_card.setOnClickListener(this);
        about_card.setOnClickListener(this);
        contact_us_card.setOnClickListener(this);
        privacy_card.setOnClickListener(this);
        share_card.setOnClickListener(this);
        logout_card.setOnClickListener(this);

        if (!util.IsLogged()){
            user_name.setVisibility(View.GONE);
            profile_card.setVisibility(View.GONE);
            chat_card.setVisibility(View.GONE);
            settings_card.setVisibility(View.GONE);
            logout_card.setVisibility(View.GONE);
            mAddProductBtn.setText("تسجيل الدخول");
        }


        add_advert_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddProductBtn.getText().equals("تسجيل الدخول")){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), AddNewAdvertActivity.class));
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        setData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_card:
                if (util.IsLogged()) {
                    startActivity(new Intent(getActivity(), ProfileActivity.class).putExtra("user_id",util.GetCurrentUser().getId()));
                } else {
                    //Toast.makeText(getActivity(), "من فضلك قم بتسجيل الدخول", Toast.LENGTH_LONG).show();
                    if (getContext() != null) {
                        Helper.showSnackBarMessage("من فضلك قم بتسجيل الدخول", (AppCompatActivity) getContext());
                    }
                }
                break;
            case R.id.chat_card:
                if (!util.IsLogged()) {

                    //Toast.makeText(getActivity(),"انت غير مسجل",Toast.LENGTH_LONG).show();
                    if (getContext() != null) {
                        Helper.showSnackBarMessage("انت غير مسجل", (AppCompatActivity) getContext());
                    }
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, new ChatFragment()).commit();
                    ((HomeActivity)getActivity()).home = false;
                }
                break;
            case R.id.settings_card:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.about_card:
                GMethods.OpenPopUpBrowser(getActivity(),GMethods.About_Url);
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
                if (util.IsLogged()) {
                    util.deleteCurrentUser();
                    util.isTaxi(false);
                    util.isDelivery(false);
                    util.removePassword();
                    PreferenceManager.getDefaultSharedPreferences(getContext()).edit().remove("notificationsCount").remove("appNotifications").apply();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    preferences.edit().remove("appNotifications").apply();
                    if (((HomeActivity)getContext()).badge != null){
                        ((HomeActivity)getContext()).badge.hide(false);
                    }
                    ShortcutBadger.removeCount(getContext());
                    user_name.setText("اسم المستخدم");
                    profile_pic.setImageResource(R.drawable.ic_dummy_person);
                    preferences.edit().remove("activateDelivery").remove("activateTaxi").apply();
                    //Toast.makeText(getContext(), "تم تسجيل الخروج بنجاح", Toast.LENGTH_LONG).show();
                    if (getContext() != null) {
                        Helper.showSnackBarMessage("تم تسجيل الخروج بنجاح", (AppCompatActivity) getContext());
                    }
                    user_name.setVisibility(View.GONE);
                    profile_card.setVisibility(View.GONE);
                    chat_card.setVisibility(View.GONE);
                    settings_card.setVisibility(View.GONE);
                    logout_card.setVisibility(View.GONE);
                    mAddProductBtn.setText("تسجيل الدخول");
                } else {
                    //Toast.makeText(getContext(), "انت بالفعل غير مسجل", Toast.LENGTH_LONG).show();
                    if (getContext() != null) {
                        Helper.showSnackBarMessage("انت بالفعل غير مسجل", (AppCompatActivity) getContext());
                    }
                }
                break;

        }
    }

    private void setData(){
        if (util.IsLogged()) {
            user_name.setText(util.GetCurrentUser().getName());
            Picasso.with(getActivity()).load(GMethods.IMAGE_URL + util.GetCurrentUser().getImage()).placeholder(R.drawable.ic_dummy_person).error(R.drawable.ic_dummy_person).into(profile_pic);

        }
    }

}
