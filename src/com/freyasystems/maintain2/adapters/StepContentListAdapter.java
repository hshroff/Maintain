/**
 * 
 */
package com.freyasystems.maintain2.adapters;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.freyasystems.maintain2.FullScreenImageActivity;
import com.freyasystems.maintain2.R;
import com.freyasystems.maintain2.dao.MediaItemVO;
import com.freyasystems.maintain2.dao.StepContentVO;
import com.freyasystems.maintain2.util.MediaHelper;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class StepContentListAdapter extends ArrayAdapter<StepContentVO> {
	private final Context context;
	private final List<StepContentVO> contents;
	private String TAG = "StepContentListAdapter";
	
	/**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public StepContentListAdapter(Context context,
			int textViewResourceId, List<StepContentVO> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.contents = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View rowView = inflater.inflate(R.layout.template_stepcontent, parent, false);
			StepContentVO content = contents.get(position);
			
			//TextView stepName = (TextView)rowView.findViewById(R.id.StepContent_StepName);
			//stepName.setText(content.getStepName());
			
			TextView stepContent = (TextView)rowView.findViewById(R.id.StepContent_Content); 
			ImageView image = (ImageView) rowView.findViewById(R.id.StepContent_Image);
			VideoView video = (VideoView) rowView.findViewById(R.id.StepContent_Video);
			
			if(content.getContent() != null && content.getContent() != "" && content.getContent().length() > 0){
				Log.d(TAG, "content is >" + content.getContent() + "<");
				stepContent.setText(content.getContent());
				stepContent.setVisibility(View.VISIBLE);
			}else if(content.getMediaItemID() > 0){
				Log.d(TAG, "Content " + content.getStepName() + " has media item id " + content.getMediaItemID());
				
				MediaItemVO media = content.getMediaItem();
				Log.d(TAG, "media file name is " + media.getFileName());
				
				MediaHelper mediaHelper = new MediaHelper(getContext());
				
				if(mediaHelper.validImageFormats.contains(media.getType())){
					image.setImageBitmap(mediaHelper.getImage(media.getFileName()));
					image.setTag(media.getFileName());
					
					//Full Screen Image View
					image.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ImageView imageView = (ImageView)v;
							
							Object tag = imageView.getTag();                  
							//int id = tag == null ? -1 : Integer.parseInt(tag.toString());
							String fileName = tag.toString();
							
							Intent fullScreenIntent = new Intent(context, FullScreenImageActivity.class);
							Bundle b = new Bundle();
							b.putString("fileName", fileName);
					        fullScreenIntent.putExtras(b);
					        context.startActivity(fullScreenIntent);			        
						}
					});
					
					image.setVisibility(View.VISIBLE);
				}else if(mediaHelper.validVideoFormats.contains(media.getType())){
					String videoPath = mediaHelper.getAlbumDir().getPath() + File.separator
							+ media.getFileName();
					video.setVideoPath(videoPath);
					/*Uri videoUri = Uri.parse(videoPath);
					video.setVideoURI(videoUri);*/
					Log.d(TAG, "video path is " + videoPath);
					video.setTag(media.getFileName());
					MediaController mediaController = new MediaController(getContext());
			        mediaController.setAnchorView(video);
			        mediaController.setMediaPlayer(video);
			        video.setMediaController(mediaController);
					
					video.setVisibility(View.VISIBLE);
					video.requestFocus();
					video.setZOrderOnTop(true);
					video.start();
				}
			}
			
			return rowView;
	}

}
