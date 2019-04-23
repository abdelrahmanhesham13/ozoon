package com.ozoon.ozoon.UI.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ozoon.ozoon.CallBacks.LoginCallBack;
import com.ozoon.ozoon.CallBacks.UploadImageCallBack;
import com.ozoon.ozoon.Model.Objects.User;
import com.ozoon.ozoon.Presenters.LoginPresenter;
import com.ozoon.ozoon.R;
import com.ozoon.ozoon.UI.Activities.HomeActivity;
import com.ozoon.ozoon.UI.Activities.LoginActivity;
import com.ozoon.ozoon.Utils.AsteriskPasswordTransformationMethod;
import com.ozoon.ozoon.Utils.GMethods;
import com.ozoon.ozoon.Utils.Helper;
import com.ozoon.ozoon.Utils.StorageUtil;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

/**
 * Created by NaderNabil216@gmail.com on 5/9/2018.
 */
public class UserSignUp extends Fragment {
    EditText ed_user_name, ed_email, ed_phone_number, ed_promo_code, ed_password, ed_re_password;
    String username, email, phone_number, promocode, password, repassword;
    Button btn_sign_up;
    ImageView profile_image;
    File SelectedImage;
    LoginPresenter presenter;
    LoginActivity mActivity;
    String selected_image_path = "";
    StorageUtil util;

    boolean uploading = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_user_sign_up, container, false);
        mActivity = (LoginActivity) getActivity();
        presenter = new LoginPresenter();
        util = StorageUtil.getInstance().doStuff(getActivity());
        GMethods.ChangeViewFont(view);
        InitViews(view);
        return view;
    }

    private void InitViews(View view) {
        ed_user_name = view.findViewById(R.id.ed_user_name);
        ed_email = view.findViewById(R.id.ed_email);
        ed_phone_number = view.findViewById(R.id.ed_phone_number);
        ed_promo_code = view.findViewById(R.id.ed_promo_code);
        ed_password = view.findViewById(R.id.ed_password);
        ed_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        ed_re_password = view.findViewById(R.id.ed_re_password);
        ed_re_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        btn_sign_up = view.findViewById(R.id.btn_sign_up);
        profile_image = view.findViewById(R.id.profile_image);

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Checker();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImage();
            }
        });
    }

    private void Checker() {
        username = ed_user_name.getText().toString().trim();
        email = ed_email.getText().toString().trim();
        phone_number = ed_phone_number.getText().toString().trim();
        promocode = ed_promo_code.getText().toString().trim();
        password = ed_password.getText().toString().trim();
        repassword = ed_re_password.getText().toString().trim();


        if (username.isEmpty()) {
            //Toast.makeText(mActivity, getString(R.string.please_enter_username), Toast.LENGTH_SHORT).show();
            if (getContext() != null) {
                Helper.showSnackBarMessage("برجاء ادخال اسم المستخدم", (AppCompatActivity) getContext());
            }
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //Toast.makeText(mActivity, getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
            if (getContext() != null) {
                Helper.showSnackBarMessage("برجاء ادخال البريد الالكتروني", (AppCompatActivity) getContext());
            }
        } else if (password.isEmpty() || password.length()<6) {
            //Toast.makeText(mActivity, getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
            if (getContext() != null) {
                Helper.showSnackBarMessage("برجاء ادخال كلمة المرور بشكل صحيح", (AppCompatActivity) getContext());
            }
        } else if (repassword.isEmpty()) {
            //Toast.makeText(mActivity, getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
            if (getContext() != null) {
                Helper.showSnackBarMessage("برجاء ادخال تكرار كلمة المرور", (AppCompatActivity) getContext());
            }
        } else if (!repassword. equals(password)) {
            //Toast.makeText(mActivity, getString(R.string.password_not_match), Toast.LENGTH_SHORT).show();
            if (getContext() != null) {
                Helper.showSnackBarMessage("كلمة مرور غير متطابقة", (AppCompatActivity) getContext());
            }
        } else if (!phone_number.isEmpty() && ((phone_number.length() < 10) || (phone_number.length() > 10))) {
            //Toast.makeText(mActivity, getString(R.string.enter_phone_number_correctely), Toast.LENGTH_SHORT).show();
            if (getContext() != null) {
                Helper.showSnackBarMessage("برجاء ادخال رقم الجوال صحيحا", (AppCompatActivity) getContext());
            }
        } else {
            if (!uploading) {
                SignUp();
            } else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SignUp();
                    }
                }, 3000);
            }
        }

    }

    private void PickImage() {
        ImagePicker.with(this)
                .setFolderMode(true) // folder mode (false by default)
                .setFolderTitle(getString(R.string.folder)) // folder selection title
                .setImageTitle(getString(R.string.select_image)) // image selection title
                .setMaxSize(1) //  Max images can be selected
                .setMultipleMode(false) //single mode
                .setShowCamera(true) // show camera or not (true by default)
                .start(); // start image picker activity with Request code
    }

    /**
     * result of pick image will appear here where we start to use it
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            Image img = images.get(0);
            try {
                Bitmap bitmapimage = GMethods.GetBitmap(img.getPath(), 200);
                profile_image.setImageBitmap(bitmapimage);
                SelectedImage = BitmapToFile(img.getName(), checkImage(img.getPath(), getBitmap(img.getPath(), 600)));
                UploadImage();
                uploading = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getBitmap(String path, int size) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        return getResizedBitmap(bitmap, size);
    }

    private Bitmap checkImage(String path, Bitmap bitmap) throws IOException {

        ExifInterface ei = new ExifInterface(path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap rotatedBitmap = null;

        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }


        return rotatedBitmap;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private File BitmapToFile(String name, Bitmap bmap) throws IOException {
        File f = new File(getActivity().getExternalCacheDir().getAbsolutePath() + "/" + name);
        f.createNewFile();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f;
    }

    private void UploadImage() {
        final ProgressDialog progressDialog = GMethods.show_progress_dialoug(getContext(),
                "جاري رفع الصورة",
                false);
        presenter.AddSignUpImage(SelectedImage, new UploadImageCallBack() {
            @Override
            public void OnSuccess(String image_path) {
                selected_image_path = image_path;
                uploading = false;
                progressDialog.dismiss();
                GMethods.writeToLog(selected_image_path);
            }

            @Override
            public void OnFailure(String message) {
            }

            @Override
            public void OnServerError() {

            }
        });
    }


    private void SignUp() {
        final ProgressDialog progressDialog = GMethods.show_progress_dialoug(getActivity(),
                getString(R.string.Signing_up),
                true);

        presenter.SignUp(email, password, username, phone_number, selected_image_path,promocode,util.getToken(), new LoginCallBack() {
            @Override
            public void OnSuccess(User user) {
                progressDialog.dismiss();
                util.setIsLogged(true);
                util.SetCurrentUser(user);
                util.putPassword(password);
                StorageUtil.setAdsCount(getContext(), "0");
                startActivity(new Intent(getActivity(), HomeActivity.class));
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(getActivity(),
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
                GMethods.show_alert_dialoug(getActivity(),
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

}
