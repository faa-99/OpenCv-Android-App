package com.farah.ele599androidopencvtutorial;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity implements CvCameraViewListener2 {

    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat mRgba, bgr;
    boolean blur_flag = false;
    boolean cartoon_flag = false;
    boolean invert_flag = false;
    boolean hdr_flag = false;
    boolean summer_flag = false;

    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (OpenCVLoader.initDebug()) {
            Toast.makeText(getApplicationContext(), "OpenCV loaded successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "OpenCV not loaded successfully", Toast.LENGTH_SHORT).show();
        }
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.opencv_tutorial_activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        bgr = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        bgr.release();

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        Imgproc.cvtColor(mRgba, bgr, Imgproc.COLOR_RGB2BGR);

        if(blur_flag==true){
            Imgproc.GaussianBlur(mRgba,bgr, new Size(21,21),0);
            Imgproc.cvtColor(bgr, bgr, Imgproc.COLOR_BGR2RGB);
        }

        if(cartoon_flag==true){
            Mat gray = new Mat();
            Mat blur = new Mat();
            Mat edges = new Mat();
            Mat colorImg = new Mat();
            Imgproc.cvtColor(mRgba, gray, Imgproc.COLOR_BGR2GRAY);
            Imgproc.medianBlur(gray, blur, 5);
            Imgproc.adaptiveThreshold(blur,edges, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
                    Imgproc.THRESH_BINARY, 9, 9);

            Imgproc.bilateralFilter(edges, colorImg, 9, 250, 250);
            Core.bitwise_and(colorImg, edges, bgr);
        }

        if(invert_flag==true){
            Core.bitwise_not(mRgba, bgr);
            Imgproc.cvtColor(bgr, bgr, Imgproc.COLOR_BGR2RGB);
        }

        if(hdr_flag==true){
            Photo.detailEnhance(mRgba, bgr);
            Imgproc.cvtColor(bgr, bgr, Imgproc.COLOR_BGR2RGB);
        }

        if(summer_flag==true){
            ArrayList<Mat> dst = new ArrayList<>(3);
            Core.split(mRgba, dst);
            Mat blue = dst.get(0);
            Mat green = dst.get(1);
            Mat red = dst.get(2);

        }
        return bgr;
    }

    public void blur(View view) {
        blur_flag = !blur_flag;
    }

    public void cartoonize(View view){
        cartoon_flag = !cartoon_flag;
    }

    public void invert(View view){
        invert_flag = !invert_flag;
    }

    public void hdr(View view){
       hdr_flag = !hdr_flag;
    }

    public void summer(View view){
        summer_flag = !summer_flag;
    }

}