package com.cynobit.trialapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Camera camera;
    private CameraPreview preview;
    private Camera.PictureCallback pictureCallback;
    private boolean frontCamera = false;
    private String[] flashModes = {Camera.Parameters.FLASH_MODE_OFF, Camera.Parameters.FLASH_MODE_ON, Camera.Parameters.FLASH_MODE_AUTO};
    private int flashModeIndex = 0;
    public static Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        FrameLayout cameraPreview = findViewById(R.id.cameraPreview);
        preview = new CameraPreview(getApplicationContext(), camera);
        cameraPreview.addView(preview);
        camera.startPreview();
        ConstraintLayout controlPanel = (ConstraintLayout) getLayoutInflater().inflate(R.layout.control_panel,
                cameraPreview, false);
        cameraPreview.addView(controlPanel);
        pictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                String root = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString();
                File myDir = new File(root + "/trial_app");
                //noinspection ResultOfMethodCallIgnored
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fileName = "TrialImage-" + n + ".jpg";
                File file = new File(myDir, fileName);
                if (file.exists()) //noinspection ResultOfMethodCallIgnored
                    file.delete();
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        assert out != null;
                        out.flush();
                        out.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{file.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
                Toast.makeText(getApplicationContext(), "Image Captured!", Toast.LENGTH_SHORT).show();
                Intent pictureIntent = new Intent(getApplicationContext(), PictureActivity.class);
                pictureIntent.putExtra("image", fileName);
                startActivity(pictureIntent);
            }

        };
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(flashModes[flashModeIndex]);
        camera.setParameters(parameters);
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        preview.refreshCamera(camera);
        super.onActivityReenter(resultCode, data);
    }

    public void shiftFlashMode_Click(View view) {
        Camera.Parameters parameters = camera.getParameters();
        if (flashModeIndex == 2) flashModeIndex = -1;
        parameters.setFlashMode(flashModes[++flashModeIndex]);
        camera.setParameters(parameters);
        switch (flashModeIndex) {
            case 0:
                ((ImageButton) view).setImageResource(R.mipmap.ic_flash_off);
                break;
            case 1:
                ((ImageButton) view).setImageResource(R.mipmap.ic_flash_on);
                break;
            case 2:
                ((ImageButton) view).setImageResource(R.mipmap.ic_flash_auto);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    public void openGallery_Click(View view) {
        startActivity(new Intent(this, GalleryActivity.class));
    }

    public void takePicture_Click(View view) {
        camera.takePicture(null, null, pictureCallback);
    }

    public void switchCamera_Click(View view) {
        int cameraCount = Camera.getNumberOfCameras();
        if (cameraCount > 1) {
            releaseCamera();
            chooseCamera();
        }
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    private int getFrontFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                frontCamera = true;
                break;
            }
        }
        return cameraId;
    }

    private int getBackFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                frontCamera = false;
                break;
            }
        }
        return cameraId;
    }

    public void onResume() {
        super.onResume();
        if (camera == null) {
            camera = Camera.open();
            camera.setDisplayOrientation(90);
            preview.refreshCamera(camera);
        }
    }

    public void chooseCamera() {
        if (frontCamera) {
            int cameraId = getBackFacingCamera();
            if (cameraId >= 0) {
                camera = Camera.open(cameraId);
                camera.setDisplayOrientation(90);
                preview.refreshCamera(camera);
                ((ImageButton) findViewById(R.id.cameraImageButton)).setImageResource(R.mipmap.ic_camera_front);
            }
        } else {
            int cameraId = getFrontFacingCamera();
            if (cameraId >= 0) {
                camera = Camera.open(cameraId);
                camera.setDisplayOrientation(90);
                preview.refreshCamera(camera);
                ((ImageButton) findViewById(R.id.cameraImageButton)).setImageResource(R.mipmap.ic_camera_rear);
            }
        }
    }
}
