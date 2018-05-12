package com.nadernabil216.wlaashal.UI.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nadernabil216.wlaashal.Presenters.LoginPresenter;
import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.UI.Activities.LoginActivity;
import com.nadernabil216.wlaashal.Utils.GMethods;
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
    TextInputLayout ed_user_name_layout, ed_phone_number_layout, ed_promo_code_layout, ed_password_layout, ed_re_password_layout;
    EditText ed_user_name, ed_phone_number, ed_promo_code, ed_password, ed_re_password;
    String username, phone_number, promocode, password, repassword;
    Button btn_sign_up;
    ImageView profile_image;
    File SelectedImage ;
    LoginPresenter presenter;
    LoginActivity mActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_user_sign_up, container, false);
        mActivity = (LoginActivity) getActivity();
        presenter=new LoginPresenter();
        GMethods.ChangeViewFont(view);
        InitViews(view);
        return view;
    }

    private void InitViews(View view) {
        ed_user_name_layout = view.findViewById(R.id.ed_user_name_layout);
        ed_phone_number_layout = view.findViewById(R.id.ed_phone_number_layout);
        ed_promo_code_layout = view.findViewById(R.id.ed_promo_code_layout);
        ed_password_layout = view.findViewById(R.id.ed_password_layout);
        ed_re_password_layout = view.findViewById(R.id.ed_re_password_layout);
        ed_user_name = view.findViewById(R.id.ed_user_name);
        ed_phone_number = view.findViewById(R.id.ed_phone_number);
        ed_promo_code = view.findViewById(R.id.ed_promo_code);
        ed_password = view.findViewById(R.id.ed_password);
        ed_re_password = view.findViewById(R.id.ed_re_password);
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
        phone_number = ed_phone_number.getText().toString().trim();
        promocode = ed_promo_code.getText().toString().trim();
        password = ed_password.getText().toString().trim();
        repassword = ed_re_password.getText().toString().trim();

        ed_user_name_layout.setError("");
        ed_phone_number_layout.setError("");
        ed_promo_code_layout.setError("");
        ed_password_layout.setError("");
        ed_re_password_layout.setError("");

        if (username.isEmpty()) {
            ed_user_name_layout.setError(getString(R.string.please_enter_username));
        } else if (password.isEmpty()) {
            ed_password_layout.setError(getString(R.string.please_enter_password));
        } else if (repassword.isEmpty()) {
            ed_re_password_layout.setError(getString(R.string.please_enter_password));
        } else if (repassword != password) {
            ed_re_password_layout.setError(getString(R.string.password_not_match));
        } else if (!phone_number.isEmpty() && ((phone_number.length() < 10) || (phone_number.length() > 10))) {
            ed_phone_number_layout.setError(getString(R.string.enter_phone_number_correctely));
        } else {
            SignUp();
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
                .start(); // start image picker activity with request code
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
                SelectedImage = BitmapToFile(img.getName(), checkImage(img.getPath(), getBitmap(img.getPath(), 600))) ;
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


    private void SignUp() {

    }

}
