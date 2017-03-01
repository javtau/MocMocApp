package com.jjv.proyectointegradorv1.UI;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jjv.proyectointegradorv1.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.name;
import static android.graphics.BitmapFactory.decodeFile;

public class PerfilActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE = 10;
    private static final int PICK_IMAGE = 20;
    public static final String URL_STORAGE_REFERENCE = "gs://logginpi.appspot.com";
    public static final String FOLDER_STORAGE_IMG = "Userimage";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl(URL_STORAGE_REFERENCE).child(FOLDER_STORAGE_IMG);
    private static final String TAG = PerfilActivity.class.getSimpleName() ;
    private ActionBar actBar;
    private CircleImageView imgView;
    private String imgPath;
    private String selectedImagePath = "";
    private FirebaseAuth mAuth;
    private Uri downlodUrl;
    private Uri userimage;
    private DatabaseReference root;
    private String temp_key;
    private File filePathImageCamera;
    private Intent mainactivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mainactivity =getIntent();
        userimage = Uri.parse(mainactivity.getStringExtra(MainActivity.FB_AVATAR));
        Log.e("uri recivida:  ",userimage.toString());
        actBar = getSupportActionBar();
        //getSupportActionBar().setTitle(null);
        actBar.setTitle(getString(R.string.perfil));
        imgView = (CircleImageView) findViewById(R.id.rimg_userimage);
        Picasso.with(this).load(userimage).into(imgView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicker();
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }

    private void showImagePicker() {
        final Dialog picker = new Dialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogview = inflater.inflate(R.layout.imagepicker_dialog, null);
        picker.setContentView(dialogview);
        picker.setTitle(getString(R.string.fotoperfil));
        picker.setCancelable(true);
        picker.setCanceledOnTouchOutside(true);
        final Button btn_camara = (Button) dialogview.findViewById(R.id.btn_camara);
        final boolean result = Utility.checkPermission(PerfilActivity.this);
        if (result) {
            btn_camara.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cameraIntent();
                    picker.dismiss();
                }
            });
            final Button btn_galeria = (Button) dialogview.findViewById(R.id.btn_galeria);

            btn_galeria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    galleryIntent();
                    picker.dismiss();
                }
            });
        } else {
            picker.dismiss();
        }
        picker.show();
    }

    private void cameraIntent() {
        String nomeFoto = mAuth.getCurrentUser().getUid();
        filePathImageCamera = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), nomeFoto + ".jpg");
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePathImageCamera));
        startActivityForResult(it, CAPTURE_IMAGE);

    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.selectPicture)), PICK_IMAGE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE)
                onSelectFromGalleryResult(data);
            else if (requestCode == CAPTURE_IMAGE)
                onCaptureImageResult(data);
        }


    }

    private void onSelectFromGalleryResult(Intent data) {


        Uri selectedImageUri = data.getData();
        if (selectedImageUri != null) {
            sendFileFirebase(storageRef, selectedImageUri);
            imgView.setImageURI(selectedImageUri);


        } else {
            //IS NULL
        }


    }

    private void onCaptureImageResult(Intent data) {

        if (filePathImageCamera != null && filePathImageCamera.exists()) {
            Uri ImageUri =  Uri.fromFile(filePathImageCamera);
            Log.e("rutasss",filePathImageCamera.toString()+"       "+ImageUri);
            imgView.setImageURI(ImageUri);
            sendFileFirebase(storageRef, ImageUri);

        } else {
            //IS NULL
        }
    }

    public  void sendFileFirebase(StorageReference storageReference, final Uri file) {
        //downlodUrl = Uri.parse("https://firebasestorage.googleapis.com/v0/b/logginpi.appspot.com/o/Userimage%2Fdefault.png?alt=media&token=3791a8b6-c7d0-45fe-b04b-cd0b90ffb6fd");
        if (storageReference != null) {
            final String name = mAuth.getCurrentUser().getUid();
            StorageReference imageGalleryRef = storageReference.child(name);
            UploadTask uploadTask = imageGalleryRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure sendFileFirebase " + e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i(TAG, "onSuccess sendFileFirebase");
                    downlodUrl = taskSnapshot.getDownloadUrl();
                    Log.d("uploadefd URIIMAGE", downlodUrl.toString());
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(downlodUrl).build();
                    user.updateProfile(profileUpdates);
                    /*Map<String, Object> map = new HashMap<String, Object>();
                    temp_key = root.push().getKey();
                    root.updateChildren(map);
                    DatabaseReference message_root = root.child(temp_key);
                    Map<String, Object> map2 = new HashMap<String, Object>();
                    map2.put("user_name", mUserName);
                    map2.put("message", downloadUrl.toString());
                    message_root.updateChildren(map2);*/
                }
            });
        } else {
//IS NULL
        }

    }

    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mainactivity.putExtra(MainActivity.FB_AVATAR,downlodUrl.toString());
        setResult(RESULT_OK,mainactivity);
        finish();
    }
}
