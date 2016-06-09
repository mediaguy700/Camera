package com.example.swdjcp.camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 0;
    public static final int REQUEST_TAKE_VIDEO = 1;
    public static final int REQUEST_PICK_PHOTO = 2;
    public static final int REQUEST_PICK_VIDEO = 3;


    public static final int MEDIA_TYPE_IMAGE = 4;
    public static final int MEDIA_TYPE_VIDEO = 5;
    private static final String TAG =MainActivity.class.getSimpleName() ;

    public  String FileName = "";
    public String FileType = "";

    private Uri mMediaUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK) {

                if(resultCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO){


                    if(data != null){

                      mMediaUri = data.getData();

                    }

                    Intent intent = new Intent(this,ViewImageActivity.class);
                    intent.setData(mMediaUri);
                    startActivity(intent);

                }

            else if(requestCode == REQUEST_TAKE_VIDEO){

                    Intent intent = new Intent(Intent.ACTION_VIEW,mMediaUri);
                    intent.setDataAndType(mMediaUri,"video/*");
                    startActivity(intent);

                }


            else if(requestCode == REQUEST_PICK_VIDEO){

                    if(data != null){

                        Log.i(TAG, "Video content URI" + data.getData());
                        Toast.makeText(this,"Video content URI" + data.getData(),Toast.LENGTH_LONG).show();

                    }

                }


        }else if(resultCode != RESULT_CANCELED){

            Toast.makeText(this,"sorry,there was an error", Toast.LENGTH_LONG).show();
        }


    }

    @OnClick(R.id.takePhoto)
    void takePhoto(){
            mMediaUri = getOutputMediaUri(MEDIA_TYPE_IMAGE);

        if(mMediaUri == null){

            Toast.makeText(MainActivity.this ,"Something went wrong", Toast.LENGTH_LONG).show();

        }

        else {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);

        }






    }


    @OnClick(R.id.takeVideo)
    void takeVideo(){


        mMediaUri = getOutputMediaUri(MEDIA_TYPE_VIDEO);

        if(mMediaUri == null){

            Toast.makeText(MainActivity.this ,"Something went wrong", Toast.LENGTH_LONG).show();

        }

        else {

            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT,mMediaUri);
            takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
            startActivityForResult(takeVideoIntent,REQUEST_TAKE_VIDEO);
        }




    }

    @OnClick(R.id.pickVideo)
    void pickVideo(){



        Intent pickVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickVideoIntent.setType("image/*");
        startActivityForResult(pickVideoIntent,REQUEST_PICK_VIDEO);


    }


@OnClick(R.id.pickPhoto)
void pickPhoto(){

    Intent pickPhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
    pickPhotoIntent.setType("image/*");
    startActivityForResult(pickPhotoIntent,REQUEST_PICK_PHOTO);

}


    private Uri getOutputMediaUri(int mediaType) {
        //check for external storage

        if(isExternalStorageAvailable()) {

            //get the URI

            //1.get the external storage directory
            File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            if (mediaType == MEDIA_TYPE_IMAGE) {

                FileName = "IMG_" + timeStamp;
                FileType = ".jpg";

            } else if (mediaType == MEDIA_TYPE_VIDEO) {


                FileName = "VID_" + timeStamp;
                FileType = ".mp4";

            } else {
                return null;
            }


            File mediaFile = null;
            try {
                mediaFile = File.createTempFile(FileName, FileType, mediaStorageDir);
                Log.i(TAG, "File" + Uri.fromFile(mediaFile));
                return Uri.fromFile(mediaFile);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "exception" + mediaStorageDir.getAbsolutePath());
            }


            //2.Create a ungiue file name
            //3.create the file
            //4.Return the files URI


        }

return null;
        }

        // somwthing went wrong



    private boolean isExternalStorageAvailable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){

            return true;

        }else{
                return false;

        }


    }


}
