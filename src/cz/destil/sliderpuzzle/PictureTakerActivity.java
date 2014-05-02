package cz.destil.sliderpuzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cz.destil.sliderpuzzle.ui.*;


public class PictureTakerActivity extends Activity {
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int RESULT_LOAD_IMAGE = 3;
	public static final int TAKE_PICTURE = 100;
	Button takePictureButton, selectPictureButton;
	private Uri fileUri;
	public static Bitmap bm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_taker);
/*		imView1 = (ImageView) findViewById(R.id.imageView1);
		imView2 = (ImageView) findViewById(R.id.imageView2);
		imView3 = (ImageView) findViewById(R.id.imageView3);
		imView4 = (ImageView) findViewById(R.id.imageView4);
		imView5 = (ImageView) findViewById(R.id.imageView5);
		imView6 = (ImageView) findViewById(R.id.imageView6);
		imView7 = (ImageView) findViewById(R.id.imageView7);
		imView8 = (ImageView) findViewById(R.id.imageView8);
		imView9 = (ImageView) findViewById(R.id.imageView9);*/
		takePictureButton = (Button) findViewById(R.id.pictureButton);
		selectPictureButton = (Button) findViewById(R.id.selectPictureButton);
		
		takePictureButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				//in.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(in, TAKE_PICTURE);
			}
		});
		selectPictureButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent inB = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(inB, RESULT_LOAD_IMAGE);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.picture_taker, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == TAKE_PICTURE){
			if(resultCode != RESULT_CANCELED){
				if (resultCode == RESULT_OK){
					try {
						bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
						//bm = (Bitmap) data.getExtras().get("data");
 						Intent in = new Intent(this, MainActivity.class);
						startActivity(in);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}	
		}
		if(requestCode == RESULT_LOAD_IMAGE){
			if(resultCode != RESULT_CANCELED){
				if(resultCode == RESULT_OK){
					try{
						bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
						Intent in = new Intent(this, MainActivity.class);
						startActivity(in);
					}catch(FileNotFoundException e){
						e.printStackTrace();
					}catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	//ANDROID/Media 
	//http://developer.android.com/guide/topics/media/camera.html#saving-media
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}
	/** Create a File for saving an image or video */
	@SuppressLint("NewApi")
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "TestCameraAppPictures");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }
	    //Date d = new Date();
	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(1,4,2007));
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
//	    } else if(type == MEDIA_TYPE_VIDEO) {
//	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}

	

}