package com.jjv.proyectointegradorv1.DB;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by javi0 on 04/03/2017.
 */

public class ImageUtils {
    private static StorageReference storageRef;
    public static final Uri DEFAULTIMAGEURI = Uri.parse("https://firebasestorage.googleapis.com/v0/b/logginpi.appspot.com/o/Userimage%2Fdefault.png?alt=media&token=3791a8b6-c7d0-45fe-b04b-cd0b90ffb6fd");
    private static Uri img[] =new Uri[1] ;

    public ImageUtils(StorageReference storageRef) {
        this.storageRef = storageRef;
    }

    public static Uri getUserimageUri(String uid) {

        storageRef.child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                img [0]= uri;
                Log.e("Resultado utils:  ",img [0].toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                img [0] = DEFAULTIMAGEURI;
                Log.e("Resultado utils:  ",img [0].toString());
            }
        });
        Log.e("Resultado utils:  ",img [0].toString());
        return img[0];

    }
}
