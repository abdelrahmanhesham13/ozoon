package com.ozoon.ozoon;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.ozoon.ozoon.Adapters.CategoryAdsAdapter;
import com.ozoon.ozoon.Adapters.GridSpacingItemDecoration;
import com.ozoon.ozoon.CallBacks.AllProductsCallBack;
import com.ozoon.ozoon.CallBacks.LoginCallBack;
import com.ozoon.ozoon.CallBacks.SuccessCallBack;
import com.ozoon.ozoon.Model.Objects.ChatModel;
import com.ozoon.ozoon.Model.Objects.Product;
import com.ozoon.ozoon.Model.Objects.User;
import com.ozoon.ozoon.Presenters.MainPresenter;
import com.ozoon.ozoon.UI.Activities.HomeActivity;
import com.ozoon.ozoon.Utils.Connector;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {


    @BindView(R.id.services_parent)
    View mServicesParent;
    @BindView(R.id.taxi_switch)
    Switch taxiSwitch;
    @BindView(R.id.delivery_switch)
    Switch deliverySwitch;
    @BindView(R.id.services_line)
    View servicesLine;
    String Category_id = "";
    MainPresenter presenter;
    ProgressDialog progressDialog;
    CategoryAdsAdapter adapter;
    StorageUtil util;
    RecyclerView recyclerView;

    public TextView numOfFollowers, numOfFollows, personName, numOfPoints, numOfAds,followText;
    ImageView profilePic, callBtn, chatBtn,mBackButton,mNotificationButton,mChatButton;

    public Button deleteAll, rate;

    float rateNumber = 0;

    View num_of_points_parent, follow_parent;

    Map<String, String> mMapSendMessage;

    ArrayList<Product> products;

    Connector mConnectorSendMessage;

    RatingBar ratingBar;

    User user;

    ChatModel mChatModel = null;

    private String TAG = "Profile";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String taxiStatus;
    String deliveryStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Fresco.initialize(this);
        ButterKnife.bind(this);
        GMethods.ChangeFont(this);
        presenter = new MainPresenter();
        util = StorageUtil.getInstance().doStuff(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        followText = findViewById(R.id.follow);
        mMapSendMessage = new HashMap<>();

        mMapSendMessage = new HashMap<>();
        mConnectorSendMessage = new Connector(ProfileActivity.this, new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                if (Connector.checkStatus(response)) {
                    mChatModel = Connector.getChatModelJson(response, user.getName(), user.getId(), util.GetCurrentUser().getId());
                    //Intent returnIntent = new Intent();
                    //returnIntent.putExtra("chat",mChatModel);

                    startActivity(new Intent(ProfileActivity.this, HomeActivity.class).putExtra("chat", mChatModel));
                    //setResult(Activity.RESULT_OK,returnIntent);
                    //finish();
                } else {
                    Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", ProfileActivity.this);
                }
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله", ProfileActivity.this);
            }
        });
        /*if (util.IsLogged()) {
            GetData(1, Category_id,util.GetCurrentUser().getId());
        }*/
        InitViews();
        Intent intent = getIntent();
        if (intent.hasExtra("user_id")) {
            Category_id = intent.getStringExtra("user_id");
            if (util.IsLogged()) {
                GetData(0, Category_id, util.GetCurrentUser().getId());
            } else {
                GetData(0, Category_id, "");
            }
        }
        if (util.IsLogged()) {
            if (Category_id.equals(util.GetCurrentUser().getId())) {
                rate.setText("الكود الترويجي"+ "\n" + util.GetCurrentUser().getCode());
                num_of_points_parent.setVisibility(View.VISIBLE);
                follow_parent.setVisibility(View.GONE);
                //numOfPoints.setText(util.GetCurrentUser().getPoints());
                //numOfFollows.setText(util.GetCurrentUser().getFollowing()+ "");
                //numOfFollowers.setText(util.GetCurrentUser().getFollower()+"");


                if (util.getDelivery() && util.getTaxi()){
                    servicesLine.setVisibility(View.VISIBLE);
                    mServicesParent.setVisibility(View.VISIBLE);
                    deliverySwitch.setChecked(preferences.getBoolean("activateDelivery",false));
                    taxiSwitch.setChecked(preferences.getBoolean("activateTaxi",false));
                } else if (util.getDelivery()) {
                    servicesLine.setVisibility(View.VISIBLE);
                    mServicesParent.setVisibility(View.VISIBLE);
                    deliverySwitch.setChecked(preferences.getBoolean("activateDelivery",false));
                    taxiSwitch.setVisibility(View.GONE);
                } else if (util.getTaxi()){
                    servicesLine.setVisibility(View.VISIBLE);
                    mServicesParent.setVisibility(View.VISIBLE);
                    deliverySwitch.setVisibility(View.GONE);
                    taxiSwitch.setChecked(preferences.getBoolean("activateTaxi",false));

                }
                deliverySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editor = preferences.edit().putBoolean("activateDelivery",isChecked);
                        editor.apply();
                        if (isChecked)
                            deliveryStatus = "1";
                        else
                            deliveryStatus = "2";
                        updateDelivery();
                    }
                });
                taxiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editor = preferences.edit().putBoolean("activateTaxi",isChecked);
                        editor.apply();
                        if (isChecked)
                            taxiStatus = "1";
                        else
                            taxiStatus = "2";
                        updateTaxi();
                    }
                });
                //ratingBar.setRating(Float.valueOf(util.GetCurrentUser().getRate()));
                //personName.setText(util.GetCurrentUser().getName());
                //Picasso.with(this).load(GMethods.IMAGE_URL + util.GetCurrentUser().getImage()).fit().error(R.drawable.ic_dummy_person).into(profilePic);
                Login();
            } else {
                deleteAll.setVisibility(View.GONE);
                rate.setText("تقييم");
                num_of_points_parent.setVisibility(View.GONE);
                follow_parent.setVisibility(View.VISIBLE);
                callBtn.setVisibility(View.VISIBLE);
                chatBtn.setVisibility(View.VISIBLE);
                Login();
            }
        } else {
            deleteAll.setVisibility(View.GONE);
            rate.setText("تقييم");
            num_of_points_parent.setVisibility(View.GONE);
            follow_parent.setVisibility(View.VISIBLE);
            callBtn.setVisibility(View.VISIBLE);
            chatBtn.setVisibility(View.VISIBLE);
            Login();
        }


        follow_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (util.IsLogged()) {
                    presenter.followUser(util.GetCurrentUser().getId(), user.getId(), new SuccessCallBack() {
                        @Override
                        public void OnSuccess() {
                            //Toast.makeText(ProfileActivity.this, "خطأ من فضلك اعد المحاوله", Toast.LENGTH_LONG).show();
                            Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله",ProfileActivity.this);

                        }

                        @Override
                        public void OnFailure(String message) {
                            //Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();
                            Helper.showSnackBarMessage(message,ProfileActivity.this);
                            if (message.trim().equals("تم المتابعة")){
                                followText.setText("لقد قمت بالمتابعة");
                            } else {
                                followText.setText("متابعة");
                            }
                            Login();
                        }

                        @Override
                        public void OnServerError() {

                        }
                    });
                } else {
                    //Toast.makeText(ProfileActivity.this, "انت غير مسجل", Toast.LENGTH_LONG).show();
                    Helper.showSnackBarMessage("انت غير مسجل",ProfileActivity.this);
                }
            }
        });


        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rate.getText().equals("تقييم")) {
                    if (util.IsLogged()) {
                        show();
                    } else {
                        //Toast.makeText(ProfileActivity.this, "انت غير مسجل", Toast.LENGTH_SHORT).show();
                        Helper.showSnackBarMessage("انت غير مسجل",ProfileActivity.this);
                    }
                }
            }
        });


        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAll();
            }
        });

    }


    private void GetData(int type, String param, String userId) {
        progressDialog.show();
        presenter.GetAllProducts(type, param, new AllProductsCallBack() {
            @Override
            public void OnSuccess(ArrayList<Product> products) {
                progressDialog.dismiss();
                Helper.writeToLog("Size :"+ products.isEmpty()+"");
                SetData(products);
            }

            @Override
            public void OnFailure(String message) {

                progressDialog.dismiss();
                numOfAds.setText("0");
                SetData(new ArrayList<Product>());
            }

            @Override
            public void OnServerError() {
                progressDialog.dismiss();
            }
        });

    }


    private void Login() {
        String user_id = "";
        if (util.IsLogged()){
            user_id = util.GetCurrentUser().getId();
        }
        presenter.getUser(Category_id,user_id, new LoginCallBack() {
            @Override
            public void OnSuccess(final User user) {
                numOfPoints.setText(user.getPoints());
                Helper.writeToLog(user.getId());
                ratingBar.setRating(Float.valueOf(user.getRate()));
                personName.setText(user.getName());
                numOfFollowers.setText(user.getFollower()+"");
                numOfFollows.setText(user.getFollowing()+"");
                Helper.writeToLog(user.isFollowed()+"");

                Helper.writeToLog("Profile Id : " + user.getId());
                if (user.isFollowed().equals("true")){
                    followText.setText("لقد قمت بالمتابعه");
                }
                Picasso.with(ProfileActivity.this).load(GMethods.IMAGE_URL + user.getImage()).fit().error(R.drawable.ic_dummy_person).into(profilePic);
                callBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialPhoneNumber(user.getMobile());
                    }
                });

                ProfileActivity.this.user = user;

                final ArrayList<String> strings = new ArrayList<>();
                strings.add(GMethods.IMAGE_URL + user.getImage());

                profilePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (strings.size() > 0) {
                            //startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
                            final ImageViewer.Builder builder = new ImageViewer.Builder(ProfileActivity.this, strings);
                            //builder.setStartPosition(sliderLayout.getCurrentPosition());
                            builder.show();
                        }
                    }
                });

                chatBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (util.IsLogged()) {
                            if (!util.GetCurrentUser().getId().equals(user.getId())) {
                                mMapSendMessage.put("message", "");
                                mMapSendMessage.put("user_id", util.GetCurrentUser().getId());
                                mMapSendMessage.put("to_id", user.getId());
                                mConnectorSendMessage.setMap(mMapSendMessage);
                                String url = Connector.createStartChatUrl() + "?message=&user_id=" + util.GetCurrentUser().getId() + "&to_id=" + user.getId();
                                Helper.writeToLog(url);
                                mConnectorSendMessage.getRequest(TAG, url);
                            } else {
                                Helper.showSnackBarMessage("لايمكنك مراسلة نفسك", ProfileActivity.this);
                            }
                        } else {
                            Helper.showSnackBarMessage("برجاء تسجيل الدخول", ProfileActivity.this);
                        }
                    }
                });
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(ProfileActivity.this,
                        message,
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);
            }

            @Override
            public void OnServerError() {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(ProfileActivity.this,
                        getString(R.string.server_error),
                        getString(R.string.app_name),
                        true,
                        "",
                        "",
                        null,
                        null);
            }
        });
    }


    private void InitViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("جاري جلب البيانات");

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(this, 2, true));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        numOfAds = findViewById(R.id.number_of_ads);
        numOfFollowers = findViewById(R.id.num_of_followers);
        numOfFollows = findViewById(R.id.num_of_follows);
        numOfPoints = findViewById(R.id.num_of_points);
        personName = findViewById(R.id.person_name);
        profilePic = findViewById(R.id.profile_image);

        deleteAll = findViewById(R.id.delete_all);
        rate = findViewById(R.id.btn_rate);
        num_of_points_parent = findViewById(R.id.num_of_points_parent);
        follow_parent = findViewById(R.id.follow_parent);
        ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (user != null)
                showClientsRatingDialog(user.getId());
                return false;
            }
        });
        callBtn = findViewById(R.id.call_btn);
        chatBtn = findViewById(R.id.chat_btn_menu);
        mBackButton = findViewById(R.id.back_btn);
        mNotificationButton = findViewById(R.id.notification_btn);
        mChatButton = findViewById(R.id.chat_btn);

        mChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,HomeActivity.class).putExtra("goToChat",true));
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra("chat")){
                    setResult(RESULT_OK,new Intent().putExtra("chat","chat"));
                    finish();
                } else {
                    finish();
                }
            }
        });

        mNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,HomeActivity.class).putExtra("goToNotification",true));
            }
        });

    }


    private void SetData(ArrayList<Product> products) {
        this.products = products;
        Helper.writeToLog("Size : " + products.size());
        if (products.isEmpty()) {
            numOfAds.setText("0");
        } else {
            numOfAds.setText(products.size() + "");
        }
        if (!products.isEmpty()){
            if (util.IsLogged()){
                if (Category_id.equals(util.GetCurrentUser().getId()))
                deleteAll.setVisibility(View.VISIBLE);
            }
        }
        if (util.IsLogged()) {
            if (Category_id.equals(util.GetCurrentUser().getId())) {
                adapter = new CategoryAdsAdapter(products, ProfileActivity.this, 1);
            } else {
                adapter = new CategoryAdsAdapter(products, ProfileActivity.this, 0);
            }
        } else {
            adapter = new CategoryAdsAdapter(products, ProfileActivity.this, 0);
        }
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();


    }


    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private void show() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_rating, null);
        dialogBuilder.setView(dialogView);
        final RatingBar rating = dialogView.findViewById(R.id.rating_bar_2);
        final Button rate = dialogView.findViewById(R.id.btn_rate);
        final EditText comment = dialogView.findViewById(R.id.comment);
        rating.setIsIndicator(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        alertDialog.show();
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateNumber = rating;
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = comment.getText().toString();
                if (TextUtils.isEmpty(commentText)) {
                    //Toast.makeText(ProfileActivity.this, "من فضلك ادخل التعليق", Toast.LENGTH_LONG).show();
                    Helper.showSnackBarMessage("من فضلك ادخل التعليق",ProfileActivity.this);
                } else {
                    presenter.addReview(String.valueOf(rateNumber), commentText, user.getId(), util.GetCurrentUser().getId(), new SuccessCallBack() {
                        @Override
                        public void OnSuccess() {
                            //Toast.makeText(ProfileActivity.this, "خطأ من فضلك اعد المحاوله", Toast.LENGTH_SHORT).show();
                            Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله",ProfileActivity.this);
                        }

                        @Override
                        public void OnFailure(String message) {
                            //Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();
                            Helper.showSnackBarMessage(message,ProfileActivity.this);
                            alertDialog.dismiss();
                        }

                        @Override
                        public void OnServerError() {

                        }
                    });
                }
            }
        });
        /*dialogView.findViewById(R.id.btn_last).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productString = product.getText().toString();
                priceString = price.getText().toString();
                if (TextUtils.isEmpty(productString)){
                    Toast.makeText(context,"من فضلك ادخل ماتريد شرائه",Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(priceString)){
                    Toast.makeText(context,"من فضلك ادخل السعر",Toast.LENGTH_LONG).show();
                } else {
                    progressDialog = GMethods.show_progress_dialoug(context,"جاري اضافة طلبك...",false);
                    progressDialog.cancel();
                    getLocation();
                }
            }
        });*/




    }



    public void deleteAll(){
        presenter.deleteAllProducts(util.GetCurrentUser().getId(), new SuccessCallBack() {
            @Override
            public void OnSuccess() {

            }

            @Override
            public void OnFailure(String message) {
                //Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage(message,ProfileActivity.this);
                GetData(0, Category_id, util.GetCurrentUser().getId());
                deleteAll.setVisibility(View.GONE);
            }

            @Override
            public void OnServerError() {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().hasExtra("user_id")) {
            Category_id = getIntent().getStringExtra("user_id");
            if (util.IsLogged()) {
                GetData(0, Category_id, util.GetCurrentUser().getId());
            } else {
                GetData(0, Category_id, "");
            }
        }
    }


    public void updateTaxi(){
        presenter.updateTaxi(util.GetCurrentUser().getId(), taxiStatus, new SuccessCallBack() {
            @Override
            public void OnSuccess() {
                Helper.showSnackBarMessage("تم تعديل الحاله بنجاح",ProfileActivity.this);
            }

            @Override
            public void OnFailure(String message) {
                Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله",ProfileActivity.this);
            }

            @Override
            public void OnServerError() {
                Helper.showSnackBarMessage("خطأ بالسيرفر",ProfileActivity.this);
            }
        });
    }


    public void updateDelivery(){
        presenter.updateDelivery(util.GetCurrentUser().getId(), deliveryStatus, new SuccessCallBack() {
            @Override
            public void OnSuccess() {
                Helper.showSnackBarMessage("تم تعديل الحاله بنجاح",ProfileActivity.this);
            }

            @Override
            public void OnFailure(String message) {
                Helper.showSnackBarMessage("خطأ من فضلك اعد المحاوله",ProfileActivity.this);
            }

            @Override
            public void OnServerError() {
                Helper.showSnackBarMessage("خطأ بالسيرفر",ProfileActivity.this);
            }
        });
    }


    public void showClientsRatingDialog(String id) {

        if (!NetworkUtils.isNetworkConnected(this)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاولة",ProfileActivity.this);
            return ;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ShowRatingDialog newFragment = new ShowRatingDialog();
        newFragment.setProduct_id(id);

        newFragment.show(this.getSupportFragmentManager(), "");
    }


    public void showFollowersDialog(String id) {

        if (!NetworkUtils.isNetworkConnected(this)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاولة",ProfileActivity.this);
            return ;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ShowFollowersDialog newFragment = new ShowFollowersDialog();
        newFragment.setProduct_id(id);

        newFragment.show(this.getSupportFragmentManager(), "");
    }


    public void showFollowingDialog(String id) {

        if (!NetworkUtils.isNetworkConnected(this)) {
            GMethods.showSnackBarMessage("تأكد من الاتصال بالانترنت اولا واعد المحاولة",ProfileActivity.this);
            return ;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        ShowFollowingDialog newFragment = new ShowFollowingDialog();
        newFragment.setProduct_id(id);

        newFragment.show(this.getSupportFragmentManager(), "");
    }

    public void numOfFollowers(View view) {

        if (user != null)
            showFollowersDialog(user.getId());

    }

    public void numOfFollowing(View view) {

        if (user != null)
            showFollowingDialog(user.getId());
    }


    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("chat")){
            setResult(RESULT_OK,new Intent().putExtra("chat","chat"));
            finish();
        } else {
            finish();
        }

    }
}
