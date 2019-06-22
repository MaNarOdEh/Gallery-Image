package com.example.galleryimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImagesInEachGroup extends AppCompatActivity {
    private static int   id_images[]={
            R.id.card_first_img,R.id.card_second_img,R.id.card_third_img,
            R.id.card_fourth_img,R.id.card_fifth_img,R.id.card_six_img,
            R.id.card_seven_img,R.id.card_eight_img,R.id.add_new_category
    };
    private FloatingActionButton add_new_image;
    private TextView txt_title;
    public static  final int RESULT_UPLOPAD_FROM_GALLERY=44;
    static final int RESULT_IMAGE_CAPTURE = 22;
     Dialog dialog,dialog_confirm;
    private  ImageView img_show_result;
    private Button btn_confirm_upload,btn_cancel_confirm,btn_cancel,btn_capture_image,download_image;
    private Bitmap bitmap;
    private View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_in_each_group);
        initializeAllWighet();
        mnakeNecessaryEvents();

    }

    private void mnakeNecessaryEvents() {
        add_new_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_confirm_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable d=new BitmapDrawable(getApplication().getResources(),bitmap);
                view.setBackground(d);
                dialog_confirm.dismiss();
            }
        });
        btn_cancel_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap=null;
                dialog_confirm.dismiss();;
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if ( ContextCompat.checkSelfPermission( ImagesInEachGroup.this, android.Manifest.permission.ACCESS_COARSE_LOCATION )
                        != PackageManager.PERMISSION_GRANTED ) {

                  /*  ActivityCompat.requestPermissions(Main2Activity.this, new String[] {
                            android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                            LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION);*/
                    ActivityCompat.requestPermissions(ImagesInEachGroup.this, new String[]{Manifest.permission.CAMERA},1);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, RESULT_IMAGE_CAPTURE);

                }else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, RESULT_IMAGE_CAPTURE);
                    //startActivityForResult(
                    //      new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                }

            }

        });download_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void initializeAllWighet() {
        txt_title=findViewById(R.id.txt_title);
        add_new_image=findViewById(R.id.add_new_image);
        dialog  = new Dialog(ImagesInEachGroup.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.option_dialog);
        dialog.findViewById(R.id.btn_upload_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), RESULT_UPLOPAD_FROM_GALLERY);
                 dialog.dismiss();
            }
        });
        btn_capture_image=dialog.findViewById(R.id.btn_capture_image);
        btn_cancel=dialog.findViewById(R.id.btn_cancel);
        download_image=dialog.findViewById(R.id.download_image);
        dialog_confirm= new Dialog(ImagesInEachGroup.this);
        dialog_confirm.setCancelable(false);
        dialog_confirm.setContentView(R.layout.confirm_upload);

        img_show_result=dialog_confirm.findViewById(R.id.img_show_result);
        btn_confirm_upload=dialog_confirm.findViewById(R.id.btn_confirm_upload);
        btn_cancel_confirm=dialog_confirm.findViewById(R.id.btn_cancel_confirm);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==RESULT_UPLOPAD_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
             bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                dialog_confirm.show();
                if(bitmap!=null) {
                    img_show_result.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(requestCode== RESULT_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            if(bitmap!=null){
                dialog_confirm.show();
                if(bitmap!=null) {
                    img_show_result.setImageBitmap(bitmap);
                }
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String s= getIntent().getStringExtra("TYPE");
        txt_title.setText(s);
        //gets the images from dataBase..
        switch(s){
            case "Friend":
                break;
            case "Family":
                break;
            case "ScreenShot":
                break;
            case "Camera":
                break;
            case "Download":
                break;
            case "FunnyImage":
                break;

        }

    }

    public void openDialog(View view) {
        this.view=view;
        dialog.show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog.isShowing()) {
            dialog.dismiss();
        }
        if(dialog_confirm.isShowing()){
            dialog_confirm.dismiss();
        }
    }
}
