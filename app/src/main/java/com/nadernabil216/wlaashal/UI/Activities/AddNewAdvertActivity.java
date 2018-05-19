package com.nadernabil216.wlaashal.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nadernabil216.wlaashal.CallBacks.AllCategoriesCallBack;
import com.nadernabil216.wlaashal.CallBacks.GetSubCategoriesCallBack;
import com.nadernabil216.wlaashal.CallBacks.UploadImageCallBack;
import com.nadernabil216.wlaashal.Model.Objects.Category;
import com.nadernabil216.wlaashal.Model.Objects.SubCategory;
import com.nadernabil216.wlaashal.Presenters.PublishAdsPresenter;
import com.nadernabil216.wlaashal.R;
import com.nadernabil216.wlaashal.Utils.GMethods;
import com.nadernabil216.wlaashal.Utils.StorageUtil;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddNewAdvertActivity extends AppCompatActivity implements View.OnClickListener {
    private PublishAdsPresenter presenter;
    StorageUtil util;
    private ImageView ic_back, ic_notification, img1, img2, img3, img4;
    private String current_key, img1_key = "images[0]", img2_key = "images[1]", img3_key = "images[2]", img4_key = "images[3]", title, phone_number, description, category_id = "", subcategory_id = "";
    private EditText ed_service_title, ed_phone_number, ed_description;
    private Button publish_btn;
    private Spinner categories_spinner, sub_categories_spinner;
    private HashMap<String, String> SelectedImages = new HashMap<>();

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_advert);
        GMethods.ChangeFont(this);
        presenter = new PublishAdsPresenter();
        util = StorageUtil.getInstance().doStuff(this);
        InitViews();
        GetCategories();
        Setup_SubCategory_Spinner(new ArrayList<SubCategory>());
    }

    private void InitViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("جاري جلب البيانات");

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);

        ed_service_title = findViewById(R.id.ed_service_title);
        ed_phone_number = findViewById(R.id.ed_phone_number);
        ed_description = findViewById(R.id.ed_description);

        publish_btn = findViewById(R.id.btn_publish);
        ic_back = findViewById(R.id.back_btn);

        categories_spinner = findViewById(R.id.main_category_spinner);
        sub_categories_spinner = findViewById(R.id.sub_category_spinner);

        ic_back.setOnClickListener(this);
        publish_btn.setOnClickListener(this);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
    }

    private void GetCategories() {
        progressDialog.show();
        presenter.GetAllCategories(util.GetCurrentUser().getId(), new AllCategoriesCallBack() {
            @Override
            public void OnSuccess(ArrayList<Category> categories) {
                progressDialog.dismiss();
                Setup_Category_Spinner(categories);
            }

            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(AddNewAdvertActivity.this,
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
                GMethods.show_alert_dialoug(AddNewAdvertActivity.this,
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

    private void GetSubCategories(String category_id) {
        progressDialog.show();
        presenter.GetSubCategories(category_id, new GetSubCategoriesCallBack() {
            @Override
            public void OnSuccess(ArrayList<SubCategory> subCategories) {
                progressDialog.dismiss();
                Setup_SubCategory_Spinner(subCategories);
            }


            @Override
            public void OnFailure(String message) {
                progressDialog.dismiss();
                GMethods.show_alert_dialoug(AddNewAdvertActivity.this,
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
                GMethods.show_alert_dialoug(AddNewAdvertActivity.this,
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

    private void Setup_Category_Spinner(final ArrayList<Category> list) {

        // adding string list of categories names
        ArrayList<String> str_list = new ArrayList<>();
        str_list.add("القسم الرئيسي");
        for (int i = 0; i < list.size(); i++) {
            str_list.add(list.get(i).getName());
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str_list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories_spinner.setAdapter(adapter);
        categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    category_id = list.get(position-1).getId();
                    GetSubCategories(category_id);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category_id = "";
            }
        });

    }

    private void Setup_SubCategory_Spinner(final ArrayList<SubCategory> list) {

        // adding string list of categories names
        ArrayList<String> str_list = new ArrayList<>();
        str_list.add("القسم الفرعي");
        for (int i = 0; i < list.size(); i++) {
            str_list.add(list.get(i).getName());
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, str_list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sub_categories_spinner.setAdapter(adapter);
        sub_categories_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    subcategory_id = list.get(position-1).getId();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                subcategory_id = "";
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_publish:
                //   Checker();
                break;
            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.notification_btn:
                // TODO: 5/10/2018 go to notifications
                break;
            case R.id.img1:
                PickImage(img1_key);
                break;
            case R.id.img2:
                PickImage(img2_key);
                break;
            case R.id.img3:
                PickImage(img3_key);
                break;
            case R.id.img4:
                PickImage(img4_key);
                break;
        }
    }

    private void PickImage(String Key) {
        current_key = Key;
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
                Bitmap bitmapimage = GMethods.GetBitmap(img.getPath(), 100);
                UploadImage(img.getName(), GMethods.GetBitmap(img.getPath(), 700));

                if (current_key.equals(img1_key)) {
                    img1.setImageBitmap(bitmapimage);
                } else if (current_key.equals(img2_key)) {
                    img2.setImageBitmap(bitmapimage);
                } else if (current_key.equals(img3_key)) {
                    img3.setImageBitmap(bitmapimage);
                } else if (current_key.equals(img4_key)) {
                    img4.setImageBitmap(bitmapimage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void UploadImage(String name, Bitmap bitmap) {
        try {
            File image = BitmapToFile(name, bitmap);
            presenter.AddImage(image, new UploadImageCallBack() {
                @Override
                public void OnSuccess(String image_path) {
                    SelectedImages.put(current_key, image_path);
                }

                @Override
                public void OnFailure(String message) {

                }

                @Override
                public void OnServerError() {

                }
            });
        } catch (IOException e) {

        }
    }

    private File BitmapToFile(String name, Bitmap bmap) throws IOException {
        File f = new File(this.getExternalCacheDir().getAbsolutePath() + "/" + name);
        f.createNewFile();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
