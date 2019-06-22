package com.example.galleryimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.galleryimage.carAPI.CatServices;
import com.example.galleryimage.carAPI.ImageResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImagesInEachGroup extends AppCompatActivity  {
   /* private static int   id_images[]={
            R.id.card_first_img,R.id.card_second_img,R.id.card_third_img,
            R.id.card_fourth_img,R.id.card_fifth_img,R.id.card_six_img,
            R.id.card_seven_img,R.id.card_eight_img,R.id.add_new_category
    };*///this just because i
    private FloatingActionButton add_new_image,btn_back,add_take_img;
    private TextView txt_title;
    private static  final int RESULT_UPLOPAD_FROM_GALLERY=44;
    private static final int RESULT_IMAGE_CAPTURE = 22;
    private static final int REQUEST_IMAGE_API=33;
    private LinearLayout main_layout;
    private  Dialog dialog,dialog_confirm;
    private  ImageView img_show_result;
    private Button btn_confirm_upload,btn_cancel_confirm,btn_cancel,btn_capture_image,download_image,open_camera;
    private Bitmap bitmap;
    private View view;
    public static final  String API_CAT_KEY="0e7b1126-3729-4630-83fe-c063f73e9ddd";
    public static final String BASE_URL = "https://api.thecatapi.com";
    private  String currentPhotoPath;
    private LinearLayout linear_taken_img;
    private GridLayout grid_main_gallery;
    private Camera mCamera;
    private ShowCamerPreview mPreview;
    private Camera.PictureCallback mPicture;
    private FrameLayout camera_preview;
    private boolean success_uplod=false;
    private String url_img;
    Camera.PictureCallback mPitctureCallBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_in_each_group);
        initializeAllWighet();
        mnakeNecessaryEvents();



    }
    private void fetchCats() {
        if (checkInternetConnections()) {
            //Generate  the service && connect to the url
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            CatServices services = retrofit.create(CatServices.class);
            Call<List<ImageResult>> call = services.catsImageServices(API_CAT_KEY);
            call.enqueue(new Callback<List<ImageResult>>() {
                @Override
                public void onResponse(Call<List<ImageResult>> call, Response<List<ImageResult>> response) {
                    if(response.isSuccessful()) {
                        final ImageResult imageResult = response.body().get(0);
                        Toast.makeText(ImagesInEachGroup.this, imageResult.url + "    ", Toast.LENGTH_LONG).show();
                        success_uplod = true;
                        url_img = imageResult.url;
                        check_coorectance();
                    }
                }

                @Override
                public void onFailure(Call<List<ImageResult>> call, Throwable t) {

                }

            });


        }


    }
    private    void check_coorectance(){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    URL url = new URL(url_img);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    Log.d("url_imges_now",url_img);
                    if(bitmap!=null){
                        Drawable d=new BitmapDrawable(getApplication().getResources(),bitmap);
                        view.setBackground(d);
                    }else{
                        Log.d("WrongError","Whhhhhat");
                    }
                    //Your code goes here
                } catch (Exception e) {
                    Log.d("WrongError",e.toString()+"          "+e.getMessage()+"     "+"new Error");
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }
    private void uplod_image_from_url(final String img_url){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    URL url = new URL(img_url);
                    bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    if(bitmap!=null){
                        dialog_confirm.show();
                        if(bitmap!=null) {
                            img_show_result.setImageBitmap(bitmap);
                            Drawable d=new BitmapDrawable(getApplication().getResources(),bitmap);
                            view.setBackground(d);
                        }

                    }
                    //Your code goes here
                } catch (Exception e) {
                    Log.d("WrongError",e.toString()+"          "+e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    private boolean checkInternetConnections(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;

            // Do whatever
        }else{
            Snackbar snackbar = Snackbar
                    .make(main_layout, "You are not connected to the wifi", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        return false;
    }

    private void mnakeNecessaryEvents() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_taken_img.setVisibility(View.GONE);
                grid_main_gallery.setVisibility(View.VISIBLE);
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
                }

            }

        });
        download_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fetchCats();
            }
        });
        add_new_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

            }
        });
        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                show_camera_app();

            }
        });
        add_take_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCamera!=null){
                    mCamera.takePicture(null,null,mPitctureCallBack);
                }
            }
        });
    }
    private void show_camera_app(){
        //Toast.makeText(ImagesInEachGroup.this, checkCameraHardware(), Toast.LENGTH_SHORT).show();
        if(checkCameraHardware()){
            // Create an instance of Camera
            mCamera = getCameraInstance();

            // Create our Preview view and set it as the content of our activity.
            mPreview = new ShowCamerPreview(this, mCamera);
            camera_preview.addView(mPreview);

            linear_taken_img.setVisibility(View.VISIBLE);
            grid_main_gallery.setVisibility(View.GONE);

        }else{
            Snackbar snackbar = Snackbar
                    .make(main_layout, "This device does't have a camera!!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
    /** Check if this device has a camera */
    private boolean checkCameraHardware() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private void initializeAllWighet() {

        camera_preview=findViewById(R.id.camera_preview);
        main_layout=findViewById(R.id.main_layout);
        //FrameLayout preview = findViewById(R.id.camera_preview);
        txt_title=findViewById(R.id.txt_title);
        btn_back=findViewById(R.id.btn_back);
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
        open_camera=dialog.findViewById(R.id.open_camera);

        btn_capture_image=dialog.findViewById(R.id.btn_capture_image);
        btn_cancel=dialog.findViewById(R.id.btn_cancel);
        download_image=dialog.findViewById(R.id.download_image);
        dialog_confirm= new Dialog(ImagesInEachGroup.this);
        dialog_confirm.setCancelable(false);
        dialog_confirm.setContentView(R.layout.confirm_upload);

        img_show_result=dialog_confirm.findViewById(R.id.img_show_result);
        btn_confirm_upload=dialog_confirm.findViewById(R.id.btn_confirm_upload);
        btn_cancel_confirm=dialog_confirm.findViewById(R.id.btn_cancel_confirm);
        linear_taken_img=findViewById(R.id.linear_taken_img);
        grid_main_gallery=findViewById(R.id.grid_main_gallery);
        add_take_img=linear_taken_img.findViewById(R.id.add_take_img);
        mPitctureCallBack=new Camera.PictureCallback(){
            @Override
            public void onPictureTaken(byte[]data,Camera camera){
                File pictureFile=getOutputFile();
                mCamera.startPreview();
                if(pictureFile==null){
                    return;
                }else{
                    try {
                        Bitmap bmp=BitmapFactory.decodeByteArray(data,0,data.length);
                        linear_taken_img.setVisibility(View.GONE);
                        grid_main_gallery.setVisibility(View.VISIBLE);
                        Drawable d=new BitmapDrawable(getApplication().getResources(),bmp);
                        view.setBackground(d);
                        FileOutputStream fileOutputStream=new FileOutputStream(pictureFile);
                        fileOutputStream.write(data);
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

    }

   private  static File  getOutputFile() {
        String state= Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)){
           return null;
        }else{
            File forder_gui=new File(Environment.getExternalStorageDirectory()+File.separator+"GUI");
            if(!forder_gui.exists()){
                forder_gui.mkdir();
            }
            String name="temp"+Math.random()+".jpg";
            File file_ouput=new File(forder_gui,name);
            return  file_ouput;
        }

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
        //url_img="https://cdn2.thecatapi.com/images/MTkzNzkxNQ.jpg";
        //uplod_image_from_url(url_img);
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
