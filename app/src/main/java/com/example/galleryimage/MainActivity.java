package com.example.galleryimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridLayout grid_main_gallery;
    private CardView cardViewFriends,cardViewFamily,cardViewScreenShot,
            cardViewCamera,cardViewDownload,cardViewFunnyImage;
    private FloatingActionButton add_new_category;
    private ArrayList<String> group_name; //assumes this will have all group of image from database
    Dialog dialog;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeAllWidget();
        makeAllNecessaryEvent();
        createAllGroupViews();

        //suppose this group name is storing in the database..
        group_name=new ArrayList<>();
        group_name.add("Friend");
        group_name.add("Family");
        group_name.add("ScreenShot");
        group_name.add("Camera");
        group_name.add("FunnyImage");
        group_name.add("Download");


    }

    private void createAllGroupViews() {
        //View view=getLayoutInflater().inflate(R.layout.group_image, null);
        //grid_main_gallery.invalidate();

         //int childCount = grid_main_gallery.getChildCount();
        //grid_main_gallery.addView(view,childCount-2);

        /*View view1=getLayoutInflater().inflate(R.layout.group_image, null);
        grid_main_gallery.addView(view1);*/
    }

    private void initializeAllWidget() {
        grid_main_gallery=findViewById(R.id.grid_main_gallery);
        cardViewFriends=grid_main_gallery.findViewById(R.id.cardViewFriends);
        cardViewFamily=grid_main_gallery.findViewById(R.id.cardViewFamily);
        cardViewScreenShot=grid_main_gallery.findViewById(R.id.cardViewScreenShot);
        cardViewCamera=grid_main_gallery.findViewById(R.id.cardViewCamera);
        cardViewDownload=grid_main_gallery.findViewById(R.id.cardViewDownload);
        cardViewFunnyImage=grid_main_gallery.findViewById(R.id.cardViewFunnyImage);
        add_new_category=findViewById(R.id.add_new_category);
         dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.confirm_delete_dialog);
        dialog.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grid_main_gallery.removeView(view);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private void makeAllNecessaryEvent() {
        //which is the best put onlCick methods on xml file or make onclick methods in the controller..?
        //normal press to open the gallery..

        // Recalculate gridlayout child count again.

        // Insert password input box at last third position again.
       // gridLayout.addView(passwordEditText, childCount - 2);
      cardViewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  v.findViewById(R.id."mm");

                Intent intent=new Intent(MainActivity.this,ImagesInEachGroup.class);
                intent.putExtra("TYPE","Friend");
                startActivity(intent);
            }
        });
        cardViewFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ImagesInEachGroup.class);
                intent.putExtra("TYPE","Family");
                startActivity(intent);
            }
        });
        cardViewScreenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ImagesInEachGroup.class);
                intent.putExtra("TYPE","ScreenShot");
                startActivity(intent);

            }
        });
        cardViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ImagesInEachGroup.class);
                intent.putExtra("TYPE","Camera");
                startActivity(intent);
            }
        });
        cardViewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ImagesInEachGroup.class);
                intent.putExtra("TYPE","Download");
                startActivity(intent);

            }
        });


        cardViewFunnyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ImagesInEachGroup.class);
                intent.putExtra("TYPE","FunnyImage");
                startActivity(intent);
            }
        });
        //long press to delete the gallery..
        cardViewFriends.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                // View view_confirm_delete = getLayoutInflater().inflate(R.layout.confirm_delete_dialog, null);
                view=v;
                dialog.show();
                return false;
            }
        });
        cardViewFamily.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v){
                // View view_confirm_delete = getLayoutInflater().inflate(R.layout.confirm_delete_dialog, null);
                 view=v;
                dialog.show();
                return false;
            }
        });
        cardViewScreenShot.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                // View view_confirm_delete = getLayoutInflater().inflate(R.layout.confirm_delete_dialog, null);
                view=v;
                dialog.show();
                return false;
            }
        });

        cardViewCamera.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                view=v;
                dialog.show();
                return false;
            }
        });

        cardViewDownload.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                // View view_confirm_delete = getLayoutInflater().inflate(R.layout.confirm_delete_dialog, null);
                view=v;
                dialog.show();
                return false;
            }
        });

        cardViewFunnyImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                // View view_confirm_delete = getLayoutInflater().inflate(R.layout.confirm_delete_dialog, null);
                view=v;
                dialog.show();
                return false;
            }
        });

        add_new_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view=v;
                dialog.show();

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
