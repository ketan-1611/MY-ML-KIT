package com.example.mymlkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.mymlkit.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    private final static int picId = 11;

    FirebaseVisionImage image;
    FirebaseVisionFaceDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(this);


        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPermission())
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,picId);
                }
                else{
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},picId) ;
                }
            }
        });

    }


    private void MLKit(Bitmap bitmap)
    {
        FirebaseVisionFaceDetectorOptions options = new FirebaseVisionFaceDetectorOptions
                .Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build();


        try {
            image = FirebaseVisionImage.fromBitmap(bitmap);
            detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace> >() {
                    @Override

                    public void onSuccess(
                            List<FirebaseVisionFace>
                                    firebaseVisionFaces)
                    {
                        String resultText = "";
                        int i = 1;
                        for (FirebaseVisionFace face : firebaseVisionFaces) {
                            resultText = resultText.concat("\nFace Number. " + i + ": ")
                                    .concat("\nSmile: " + face.getSmilingProbability() * 100 + "%")
                                    .concat("\nLeft eye open: " + face.getLeftEyeOpenProbability() * 100 + "%")
                                    .concat("\nRight eye open " + face.getRightEyeOpenProbability() * 100 + "%");
                            i++;
                        }


                        if (firebaseVisionFaces.size() == 0) {
                            Toast.makeText(MainActivity.this, "No Face Found", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Bundle bundle = new Bundle();
                            bundle.putString(LCOFaceDetection.resultTxt, resultText);
                            DialogFragment resultDialog = new ResultDialog();
                            resultDialog.setArguments(bundle);
                            resultDialog.setCancelable(true);
                            resultDialog.show(getSupportFragmentManager(), LCOFaceDetection.resDialog);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(MainActivity.this,"Oops, Something wrong",Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)
                ==PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
            return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == picId && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap bitmap = (Bitmap)extra.get("data");
            MLKit(bitmap);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == picId)
        {
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                //checkPermission();
               // Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,picId);
            }
            else
            {
                Toast.makeText(this,"Permission not Granted",Toast.LENGTH_SHORT).show();
            }
        }
    }


}