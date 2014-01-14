package com.freyasystems.maintain2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.freyasystems.maintain2.util.MediaHelper;
import com.freyasystems.maintain2.util.PinchImageView;

public class FullScreenImageActivity extends Activity {

	private static String IMG_ID = "imageID";
	private static String FILE_NAME = "fileName";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_screen_image);

		PinchImageView imageView = (PinchImageView) findViewById(R.id.fullscreenImageView);

		Bundle b = getIntent().getExtras();
		String fileName = b.getString(FILE_NAME);

		MediaHelper mediaHelper = new MediaHelper(getApplicationContext());
		imageView.setImageBitmap(mediaHelper.getImage(fileName));

		/*
		 * if(imageId < 1){ String imagePath = b.getString(FILE_NAME); Bitmap
		 * imgBitMap = BitmapFactory.decodeFile(imagePath);
		 * imageView.setImageBitmap(imgBitMap); }else{
		 * imageView.setImageResource(imageId); }
		 */

		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	}
}
