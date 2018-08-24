package com.smatech.wlaashal.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.smatech.wlaashal.CallBacks.SuccessCallBack;
import com.smatech.wlaashal.CallBacks.UploadImageCallBack;
import com.smatech.wlaashal.Model.Objects.User;
import com.smatech.wlaashal.Presenters.MainPresenter;
import com.smatech.wlaashal.R;
import com.smatech.wlaashal.Utils.AsteriskPasswordTransformationMethod;
import com.smatech.wlaashal.Utils.GMethods;
import com.smatech.wlaashal.Utils.Helper;
import com.smatech.wlaashal.Utils.StorageUtil;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class SettingsActivity extends AppCompatActivity {
    TextInputLayout ed_user_name_layout, ed_phone_number_layout, ed_promo_code_layout, ed_password_layout, ed_re_password_layout;
    EditText ed_user_name, ed_phone_number, ed_promo_code, ed_password, ed_re_password,ed_email;
    String username, phone_number, promocode, password, repassword,email;
    Button btn_save;
    ImageView profile_image , ic_back;
    File SelectedImage;
    String selected_image_path = "";
    MainPresenter presenter;
    StorageUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        GMethods.ChangeFont(this);
        util = StorageUtil.getInstance().doStuff(this);
        presenter = new MainPresenter();
        InitViews();

        if (util.IsLogged()){
            ed_email.setText(util.GetCurrentUser().getUsername());
            ed_user_name.setText(util.GetCurrentUser().getName());
            ed_phone_number.setText(util.GetCurrentUser().getMobile());
            ed_promo_code.setText(util.GetCurrentUser().getCode());
            Picasso.with(this).load(GMethods.IMAGE_URL + util.GetCurrentUser().getImage()).fit().error(R.drawable.ic_dummy_person).into(profile_image);
            selected_image_path = util.GetCurrentUser().getImage();
        }

    }

    private void InitViews() {
        ed_user_name_layout = findViewById(R.id.ed_user_name_layout);
        ed_phone_number_layout = findViewById(R.id.ed_phone_number_layout);
        ed_promo_code_layout = findViewById(R.id.ed_promo_code_layout);

        ed_user_name = findViewById(R.id.ed_user_name);
        ed_phone_number = findViewById(R.id.ed_phone_number);
        ed_promo_code = findViewById(R.id.ed_promo_code);

        ed_password = findViewById(R.id.ed_password);
        ed_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        ed_re_password = findViewById(R.id.ed_re_password);
        ed_re_password.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        ed_email = findViewById(R.id.ed_email);

        btn_save = findViewById(R.id.btn_save);
        profile_image = findViewById(R.id.profile_image);
        ic_back=findViewById(R.id.back_btn);

        btn_save.setOnClickListener(new View.OnClickListener() {
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

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void Checker() {
        username = ed_user_name.getText().toString().trim();
        phone_number = ed_phone_number.getText().toString().trim();
        promocode = ed_promo_code.getText().toString().trim();
        password = ed_password.getText().toString().trim();
        repassword = ed_re_password.getText().toString().trim();
        email = ed_email.getText().toString().trim();


        ed_user_name_layout.setError("");
        ed_phone_number_layout.setError("");
        ed_promo_code_layout.setError("");


        if (username.isEmpty()) {
            //Toast.makeText(this, getString(R.string.please_enter_username), Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("برجاء ادخال اسم المستخدم",SettingsActivity.this);
        } else if (!phone_number.isEmpty() && ((phone_number.length() < 10) || (phone_number.length() > 10))) {
            //Toast.makeText(this, getString(R.string.enter_phone_number_correctely), Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("برجاء ادخال قم الجوال صحيحا",SettingsActivity.this);
        } else if (!util.IsLogged()){
            //Toast.makeText(this, "انت غير مسجل", Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("انت غير مسجل",SettingsActivity.this);
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //Toast.makeText(this, "برجاء ادخال البريد الالكتروني صحيح", Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("برجاء ادخال البريد الالكتروني صحيح",SettingsActivity.this);
        } else if (!password.equals(repassword)){
            //Toast.makeText(this, "كلمة المرور يجب ان تكون متطابقه", Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("كلمة المرور يجب ان تكون متطابقه",SettingsActivity.this);
        } else if (TextUtils.isEmpty(email)){
            //Toast.makeText(this, "من فضلك ادخل البريد الالكتروني", Toast.LENGTH_SHORT).show();
            Helper.showSnackBarMessage("من فضلك ادخل البريد الالكتروني",SettingsActivity.this);
        }
        else {
            SaveChanges();
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
        File f = new File(getExternalCacheDir().getAbsolutePath() + "/" + name);
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

    private void SaveChanges() {
        final ProgressDialog progressDialog = GMethods.show_progress_dialoug(this,
                "جاري التحميل",
                false);


        presenter.editProfile(util.GetCurrentUser().getId(),username,email,selected_image_path,phone_number,password, new SuccessCallBack() {
            @Override
            public void OnSuccess() {
                progressDialog.dismiss();
                //Toast.makeText(SettingsActivity.this, "تم تعديل البيانات بنجاح", Toast.LENGTH_LONG).show();
                Helper.showSnackBarMessage("تم تعديل البيانات بنجاح",SettingsActivity.this);
                User user = util.GetCurrentUser();
                user.setImage(selected_image_path);
                user.setName(username);
                user.setUsername(email);
                if (!TextUtils.isEmpty(phone_number)) {
                    user.setMobile(phone_number);
                }
                util.SetCurrentUser(user);
                SettingsActivity.this.finish();
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(SettingsActivity.this,
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

            }
        });
    }


    private void UploadImage() {
        final ProgressDialog progressDialog = GMethods.show_progress_dialoug(this,
                "جاري رفع الصورة",
                false);
        presenter.AddImage(SelectedImage, new UploadImageCallBack() {
            @Override
            public void OnSuccess(String image_path) {
                selected_image_path = image_path;
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
