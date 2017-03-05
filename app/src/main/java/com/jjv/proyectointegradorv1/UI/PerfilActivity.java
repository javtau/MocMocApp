package com.jjv.proyectointegradorv1.UI;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jjv.proyectointegradorv1.R;
import com.squareup.picasso.Picasso;
import java.io.File;
import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {
    //variables para fireba storage
    public static final String URL_STORAGE_REFERENCE = "gs://logginpi.appspot.com";
    public static final String FOLDER_STORAGE_IMG = "Userimage";
    private static final int CAPTURE_IMAGE = 10;
    private static final int PICK_IMAGE = 20;
    private static final String TAG = PerfilActivity.class.getSimpleName();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl(URL_STORAGE_REFERENCE).child(FOLDER_STORAGE_IMG);
    private ActionBar actBar;
    private CircleImageView imgView;
    private FirebaseAuth mAuth;
    private Uri downlodUrl;
    private Uri userimage;
    private File filePathImageCamera;
    private Intent mainactivityIntent;

    private EditText etNombreUsuario, etEmailUsuario;
    private ImageView ivEditarNombre, ivEditarEmail;
    private FirebaseUser firebaseUser;
    private RelativeLayout relativeLayout;
    private Button btnActualizarPerfil;

    // controla si el usuario ha introducido nuevos valores para su nombre de usuario y el email
    private boolean nombreChanged = false;
    private boolean emailChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        etNombreUsuario = (EditText) findViewById(R.id.lbl_username);
        etEmailUsuario = (EditText) findViewById(R.id.lbl_useremail);
        ivEditarEmail = (ImageView) findViewById(R.id.iv_editar_email);
        ivEditarNombre = (ImageView) findViewById(R.id.iv_editar_nombre);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_perfil);
        btnActualizarPerfil = (Button) findViewById(R.id.btn_actualizar);

        etNombreUsuario.setFocusable(false);
        etEmailUsuario.setFocusable(false);

        mainactivityIntent = getIntent();
        userimage = Uri.parse(mainactivityIntent.getStringExtra(MainActivity.FB_AVATAR));
        downlodUrl = userimage;
        mAuth = FirebaseAuth.getInstance();

        actBar = getSupportActionBar();
        actBar.setDisplayHomeAsUpEnabled(true);
        actBar.setTitle(getString(R.string.perfil));
        imgView = (CircleImageView) findViewById(R.id.rimg_userimage);
        Picasso.with(this).load(userimage).into(imgView);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicker();
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNombreUsuario.setFocusable(false);
                etEmailUsuario.setFocusable(false);
                esconderTeclado(PerfilActivity.this);
            }
        });

        // los edit text empiezan sin foco, cuando el usuario pincha sobre los iconos, consigue los focos
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            etNombreUsuario.setText(firebaseUser.getDisplayName());
            etEmailUsuario.setText(firebaseUser.getEmail());

            ivEditarNombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etNombreUsuario.setFocusableInTouchMode(true);
                    etNombreUsuario.requestFocus();
                    etNombreUsuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(hasFocus){
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(etNombreUsuario, InputMethodManager.SHOW_IMPLICIT);
                            }
                        }
                    });
                }
            });
            ivEditarEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etEmailUsuario.setFocusableInTouchMode(true);
                    etEmailUsuario.requestFocus();
                    etEmailUsuario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(hasFocus){
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(etEmailUsuario, InputMethodManager.SHOW_IMPLICIT);
                            }
                        }
                    });
                }
            });
        }

        // actualiza el perfil
        btnActualizarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!firebaseUser.getDisplayName().equals(etNombreUsuario.getText().toString())){
                    nombreChanged = true;
                }

                if(!firebaseUser.getEmail().equals(etEmailUsuario.getText().toString())){
                    emailChanged = true;
                }

                if(!nombreChanged && !emailChanged){
                    Toast.makeText(PerfilActivity.this, "¡Ningún cambio que guardar!", Toast.LENGTH_SHORT).show();
                }else{
                    if(nombreChanged){
                        UserProfileChangeRequest perfilUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(etNombreUsuario.getText().toString())
                                .build();

                        // TODO: EL NOMBRE SE ACTUALIZA PERO EL USUARIO TIENE QUE SALIR Y VOLVER A LOGUEARSE PARA VER LOS CAMBIOS
                        firebaseUser.updateProfile(perfilUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(PerfilActivity.this, "Nombre de perfil actualizado", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                    if(emailChanged){
                        firebaseUser.updateEmail(etEmailUsuario.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(PerfilActivity.this, "Email actualizado", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });

    }
    // Mostramos un dialogo para dara elgir entre seleccionar la imagen de la galeria
    // o hacer una foto desde la camara
    private void showImagePicker() {
        final Dialog picker = new Dialog(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogview = inflater.inflate(R.layout.imagepicker_dialog, null);
        picker.setContentView(dialogview);
        picker.setCancelable(true);
        picker.setCanceledOnTouchOutside(true);
        final boolean result = Utility.checkPermission(PerfilActivity.this);
        if (result) {
            final Button btn_camara = (Button) dialogview.findViewById(R.id.btn_camara);
            Log.d("EN show imagepicker: ", "pasamos la parte de permisos");
            btn_camara.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Pulsado el btn camara: ", "en on click");
                    cameraIntent();
                    picker.dismiss();
                }
            });
            final Button btn_galeria = (Button) dialogview.findViewById(R.id.btn_galeria);

            btn_galeria.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Pulsado el btngaleria: ", "en on click");
                    galleryIntent();
                    picker.dismiss();
                }
            });
        } else {
            picker.dismiss();
        }
        picker.show();
    }

    //Iniciamos un nuevo intent que nos abrira la camara
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
            Uri ImageUri = Uri.fromFile(filePathImageCamera);
            imgView.setImageURI(ImageUri);
            sendFileFirebase(storageRef, ImageUri);
        } else {
            //IS NULL
        }
    }

    public void sendFileFirebase(StorageReference storageReference, final Uri file) {
        if (storageReference != null) {
            final String name = mAuth.getCurrentUser().getUid();
            UploadTask uploadTask = storageReference.child(name).putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downlodUrl = taskSnapshot.getDownloadUrl();
                    mainactivityIntent.putExtra(MainActivity.FB_AVATAR, downlodUrl.toString());
                    setResult(-1, mainactivityIntent);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mainactivityIntent.putExtra(MainActivity.FB_AVATAR,downlodUrl.toString());
            setResult(-1, mainactivityIntent);
            FirebaseUser user = mAuth.getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(downlodUrl).build();
            user.updateProfile(profileUpdates);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
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

    public static void esconderTeclado(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
