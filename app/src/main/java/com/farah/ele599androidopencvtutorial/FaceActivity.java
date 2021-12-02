package com.farah.ele599androidopencvtutorial;

import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FaceActivity extends AppCompatActivity implements CvCameraViewListener2 {

    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat mRgba, bgr, mGray;

    File cascadeFile;
    CascadeClassifier faceDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
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
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseCallback);
        } else {
            try {
                baseCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
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

            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(bgr, faceDetections);

            for(Rect rect: faceDetections.toArray()){
                Imgproc.rectangle(bgr, new Point(rect.x, rect.y), new Point(rect.x+rect.width, rect.y+rect.height), new Scalar(255,0,0));

            }

        return bgr;
    }

    private final BaseLoaderCallback baseCallback = new BaseLoaderCallback(this){
        @Override
        public void onManagerConnected(int status) throws IOException {
            switch(status){
                case LoaderCallbackInterface.SUCCESS:
                {
                    InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
                    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                    cascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
                    FileOutputStream fos = new FileOutputStream(cascadeFile);

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while( (bytesRead = is.read(buffer)) != -1){
                        fos.write(buffer, 0, bytesRead);
                    }

                    is.close();
                    fos.close();

                    faceDetector = new CascadeClassifier(cascadeFile.getAbsolutePath());

                    if(faceDetector.empty()){
                        faceDetector = null;
                    }
                    else
                        cascadeDir.delete();
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