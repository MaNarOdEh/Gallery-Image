package com.example.galleryimage;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/** A basic Camera preview class */
public class ShowCamerPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public ShowCamerPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
         //   Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        mCamera.stopPreview();
        mCamera.release();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size>sizes=parameters.getSupportedPictureSizes();
        Camera.Size mSize=null;
        for(Camera.Size size:sizes){
            mSize=size;
        }
        if(this.getResources().getConfiguration().orientation!= Configuration.ORIENTATION_LANDSCAPE){
            parameters.set("orientation","portrait");
            mCamera.setDisplayOrientation(90);
            parameters.setRotation(90);
        }else{
            parameters.set("orientation","landscape");
            mCamera.setDisplayOrientation(0);
            parameters.setRotation(0);
        }
        parameters.setPictureSize(mSize.width,mSize.height);
        mCamera.setParameters(parameters);
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            //Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}