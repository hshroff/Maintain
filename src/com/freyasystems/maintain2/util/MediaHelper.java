/**
 * 
 */
package com.freyasystems.maintain2.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.freyasystems.maintain2.R;
import com.freyasystems.maintain2.dao.MediaItemVO;
import com.freyasystems.maintain2.photo.BaseAlbumDirFactory;
import com.freyasystems.maintain2.photo.FroyoAlbumDirFactory;

/**
 * @author shroffh Freya Systems, LLC.
 */
public class MediaHelper {
	private Context context;
	private String TAG = "MediaHelper";
	
	public final Set<String> validVideoFormats = new HashSet<String>(Arrays.asList(
			new String[] {"MP4", "MPEG"}
 			));
	
	public final Set<String> validImageFormats = new HashSet<String>(Arrays.asList(
			new String[] {"PNG", "JPG", "JPEG", "BITMAP", "GIF"}
 			));
	
	
	
	public MediaHelper(Context context) {
		this.context = context;
	}

	/**
	 * Save provided @image to the local storage of the device
	 * 
	 * @param name
	 *            - Image name
	 * @param image
	 * @return
	 */
	public String saveImage(MediaItemVO mediaItem, Bitmap image) {
		
		File mediaStorageDir = getAlbumDir();
		File imageF = new File(mediaStorageDir.getPath() + File.separator
				+ mediaItem.getFileName());

		Log.d(TAG, "imageF is " + imageF.getAbsolutePath());

		if (imageF.exists())
			imageF.delete();
		
		//Use PNG compression for PNG, bitmap, & gif
		Bitmap.CompressFormat compressionFormat = Bitmap.CompressFormat.PNG;

		if(mediaItem.getType().equalsIgnoreCase("JPG") || mediaItem.getType().equalsIgnoreCase("JPEG")){
			compressionFormat = Bitmap.CompressFormat.JPEG;
		}
		
		try {
			FileOutputStream out = new FileOutputStream(imageF);
			image.compress(compressionFormat, 90, out);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}

		return imageF.getAbsolutePath();
	}

	/**
	 * Retrieve image stored on the local storage of the device.
	 * 
	 * @param imageName
	 * @return
	 */
	public Bitmap getImage(String imageName) {
		File mediaStorageDir = getAlbumDir();
		File imageF = new File(mediaStorageDir.getPath() + File.separator
				+ imageName);

		Log.d(TAG, "imageF is " + imageF.getAbsolutePath());

		if (!imageF.exists())
			return null;

		FileInputStream in = null;
		try {
			try {
				in = new FileInputStream(imageF);
				Bitmap bitmap = BitmapFactory.decodeStream(in);
				return bitmap;
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
		return null;
	}

	private String getAlbumName() {
		return context.getString(R.string.album_name);
	}

	public File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				FroyoAlbumDirFactory mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
				storageDir = mAlbumStorageDirFactory
						.getAlbumStorageDir(getAlbumName());
			} else {
				BaseAlbumDirFactory mAlbumStorageDirFactory = new BaseAlbumDirFactory();
				storageDir = mAlbumStorageDirFactory
						.getAlbumStorageDir(getAlbumName());
			}

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d(TAG, "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(context.getString(R.string.app_name),
					"External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}
	
	/**
	 * Save provided byteArray to the local storage of the device
	 * 
	 * @param mediaItem - Media Item value object
	 * @param byteArray - Contents in byte array form
	 * @return
	 */
	public String save(MediaItemVO mediaItem, byte[] byteArray) {
		
		File mediaStorageDir = getAlbumDir();
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ mediaItem.getFileName());

		Log.d(TAG, "mediaFile is " + mediaFile.getAbsolutePath());

		if (mediaFile.exists())
			mediaFile.delete();
		
		try{
			FileOutputStream out = new FileOutputStream(mediaFile);
			out.write(byteArray);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}

		return mediaFile.getAbsolutePath();
	}
	
	/**
	 * Retrieve media item stored on the local storage of the device.
	 * 
	 * @param name
	 * @return
	 */
	public Bitmap get(String imageName) {
		File mediaStorageDir = getAlbumDir();
		File imageF = new File(mediaStorageDir.getPath() + File.separator
				+ imageName);

		Log.d(TAG, "imageF is " + imageF.getAbsolutePath());

		if (!imageF.exists())
			return null;

		FileInputStream in = null;
		try {
			try {
				in = new FileInputStream(imageF);
				Bitmap bitmap = BitmapFactory.decodeStream(in);
				return bitmap;
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
		return null;
	}
	
	public String isImageOrVideo(MediaItemVO mediaItem){
		
		String type = mediaItem.getType();
		
		if(validVideoFormats.contains(type.toUpperCase())){
			return "Video";
		}else if(validImageFormats.contains(type.toUpperCase())){
			return "Image";
		}
		
		return "Invalid";
	}
}
