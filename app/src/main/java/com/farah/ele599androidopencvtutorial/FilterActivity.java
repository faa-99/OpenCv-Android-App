package com.farah.ele599androidopencvtutorial;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.io.IOException;

public class FilterActivity extends AppCompatActivity implements CvCameraViewListener2 {

    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat mRgba, bgr, mGray;
    boolean blur_flag = false;
    boolean cartoon_flag = false;
    boolean invert_flag = false;
    boolean hdr_flag = false;
    boolean gray_flag = false;


    private final BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) throws IOException {
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
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, baseCallback);
        } else {
            try {
                baseCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            try {
                mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        mGray = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        bgr.release();
        mGray.release();

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        Imgproc.cvtColor(mRgba, bgr, Imgproc.COLOR_RGB2BGR);

        if(blur_flag){
            Imgproc.GaussianBlur(mRgba,bgr, new Size(21,21),0);
            Imgproc.cvtColor(bgr, bgr, Imgproc.COLOR_BGR2RGB);
        }

        if(cartoon_flag){
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

        if(invert_flag){
            Core.bitwise_not(mRgba, bgr);
            Imgproc.cvtColor(bgr, bgr, Imgproc.COLOR_BGR2RGB);
            Imgproc.cvtColor(bgr, bgr, Imgproc.COLOR_RGB2BGR);
        }

        if(hdr_flag){
            Photo.detailEnhance(mRgba, bgr);
            Imgproc.cvtColor(bgr, bgr, Imgproc.COLOR_BGR2RGB);
        }


        if(gray_flag){
            Imgproc.cvtColor(bgr, bgr, Imgproc.COLOR_BGR2GRAY);
        }

        return bgr;
    }

    public void blur(View view) {
        blur_flag = !blur_flag;
        cartoon_flag = false;
        invert_flag = false;
        hdr_flag = false;
        gray_flag = false;
    }

    public void cartoonize(View view){
        cartoon_flag = !cartoon_flag;
        blur_flag = false;
        invert_flag = false;
        hdr_flag = false;
        gray_flag = false;
    }

    public void invert(View view){
        invert_flag = !invert_flag;
        blur_flag = false;
        cartoon_flag = false;
        hdr_flag = false;
        gray_flag = false;
    }

    public void hdr(View view){
        hdr_flag = !hdr_flag;
        invert_flag = false;
        blur_flag = false;
        cartoon_flag = false;
        gray_flag = false;
    }


    public void gray(View view){
        gray_flag = !gray_flag;
        hdr_flag = false;
        invert_flag = false;
        blur_flag = false;
        cartoon_flag = false;
    }

    private BaseLoaderCallback baseCallback = new BaseLoaderCallback(this){
        @Override
        public void onManagerConnected(int status) throws IOException {
            switch(status){
                case LoaderCallbackInterface.SUCCESS:
                {
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

}