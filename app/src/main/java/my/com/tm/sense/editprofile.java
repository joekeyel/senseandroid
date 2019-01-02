package my.com.tm.sense;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class editprofile extends Fragment {


    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;
    ImageView imageView;
    View myView;

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_editprofile, container, false);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

     String uid = currentFirebaseUser.getUid();
     String email = currentFirebaseUser.getEmail();
     final String query = "?email="+email+"&uid="+uid;

        imageView = (ImageView)myView.findViewById(R.id.imageprofile);
        Button upload = (Button)myView.findViewById(R.id.uploadimagebtn);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectgallerywall1();
            }
        });


        Button cameraupload = (Button)myView.findViewById(R.id.cameraimage);

        cameraupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                selectcamera();
            }
        });

        loadimage(imageView,uid);

        return myView;
    }


    public void selectgallerywall1() {

        requestPermissions(
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                3);



    }

    public void selectcamera() {


        requestPermissions(
                new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                2);






        }






    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {



        switch (requestCode) {
            case 2: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    // Perform action on click
                    Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = getFile(uid );

                    Uri apkURI = FileProvider.getUriForFile(
                            this.getActivity(),
                            this.getActivity()
                                    .getPackageName() + ".provider", file);


                    camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,

                            //photoURI
                            apkURI

                    );

                    startActivityForResult(camera_intent, 2);




                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 3:{


                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();



                // Perform action on click
                Intent camera_intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                File file = getFile(uid );

                Uri apkURI = FileProvider.getUriForFile(
                        this.getActivity(),
                        this.getActivity()
                                .getPackageName() + ".provider", file);


                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT,

                        //photoURI
                        apkURI

                );


                startActivityForResult(camera_intent, 1);

            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }


    public void generatebarcode(String queryitem) {



        try {
            // generate a 150x150 QR code
            Bitmap bm = encodeAsBitmap(queryitem);

            if(bm != null) {
                imageView.setImageBitmap(bm);
            }
        } catch (WriterException e) { //eek }

        }




    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 150, 150, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 150, 0, 0, w, h);
        return bitmap;
    }

    private File getFile(String filename){


        File Folder = new File(Environment.getExternalStorageDirectory() +
                File.separator +"DCIM");

        if(!Folder.exists()){

            Folder.mkdir();
        }

        File image_file = new File(Folder,filename+".jpg");

        return image_file;


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == 1){
            if (data != null) {
                Uri contentURI = data.getData();


                final String path = Environment.getExternalStorageDirectory() +
                        File.separator + "DCIM/" + uid+ ".jpg";

                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), contentURI);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Error image Galery", e.toString());
                }
                Bitmap photo = Bitmap.createScaledBitmap(bmp, 1280, 1024, true);

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                photo.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "DCIM/" + uid+ ".jpg");
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream fo = null;
                try {
                    fo = new FileOutputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }




                    imageView.setImageDrawable(Drawable.createFromPath(path));




            }



            // Create a storage reference from our app
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            File file = getFile(uid);
//                    Uri photoURI = FileProvider.getUriForFile(MyActivity.this,
            //       "my.com.tm.moapps.remoteandroid.fileprovider",
            //        file);

            Uri apkURI = FileProvider.getUriForFile(
                    getActivity(),
                    getActivity().getApplicationContext()
                            .getPackageName() + ".provider", file);
            StorageReference riversRef = storageRef.child("senseprofile" + File.separator + apkURI.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(apkURI);



            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {


                    Toast.makeText(getActivity(), "Failed Upload To Server", Toast.LENGTH_SHORT)
                            .show();

                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Toast.makeText(getActivity(), "Save to server", Toast.LENGTH_SHORT)
                            .show();

                }
            });


        }


        if(requestCode == 2){



            final String path = Environment.getExternalStorageDirectory() +
                    File.separator + "DCIM/" + uid+ ".jpg";

            Bitmap bmp = BitmapFactory.decodeFile(path);

            Bitmap photo = scaleDown(bmp, 800, true);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            photo.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "DCIM/" + uid+ ".jpg");
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fo = null;
            try {
                fo = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }



                imageView.setImageDrawable(Drawable.createFromPath(path));


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            File file = getFile(uid);
//                    Uri photoURI = FileProvider.getUriForFile(MyActivity.this,
            //       "my.com.tm.moapps.remoteandroid.fileprovider",
            //        file);

            Uri apkURI = FileProvider.getUriForFile(
                    getActivity(),
                    getActivity().getApplicationContext()
                            .getPackageName() + ".provider", file);
            StorageReference riversRef = storageRef.child("senseprofile" + File.separator  + uid+".jpg");
            UploadTask uploadTask = riversRef.putFile(apkURI);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {


                    Toast.makeText(getActivity(), "Failed Upload To Server", Toast.LENGTH_SHORT)
                            .show();

                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                    Toast.makeText(getActivity(), "Upload To Server", Toast.LENGTH_SHORT)
                            .show();




                }
            });


        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }


    public void loadimage(final ImageView view, final String imagename1){


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


        storageRef.child("senseprofile" + "/"+imagename1+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // TODO: handle uri

                Context context = view.getContext();

                view.invalidate();

                Picasso.with(context).load(uri).networkPolicy(NetworkPolicy.NO_CACHE).into(view);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Hand
                //
                //
                // le any errors

                Toast.makeText(getActivity(), "Failed  To view image"+ imagename1+" "+exception , Toast.LENGTH_SHORT).show();
            }
        });


    }


}
