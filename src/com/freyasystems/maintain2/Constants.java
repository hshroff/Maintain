/**
 * 
 */
package com.freyasystems.maintain2;

import com.freyasystems.maintain2.dao.TaskVO;

/**
 * Helper class that contains all static values used throughout the application.
 * @author shroffh
 * Freya Systems, LLC.
 */
public class Constants {
	//Task states
	public static String TASK_PENDING = "Pending"; //all untouched tasks
	public static String TASK_IN_PROGRESS = "In Progress"; //when task is opened
	public static String TASK_SIGNED = "Signed"; //when signed
	public static String TASK_VERIFIED = "Verified"; //when verified
	public static String TASK_COMPLETED = "Complete";
	
	public static String TASK_ACTION_SIGN = "Sign";
	public static String TASK_ACTION_VERIFY = "Verify";
	public static String TASK_ACTION_COMPLETE = "Complete";
	
	//Step states
	public static String STEP_PENDING= "Pending"; //all untouched steps
	public static String STEP_IN_PROGRESS = "In Progress"; //when step is first displayed
	public static String STEP_COMPLETED = "Complete"; //when step is marked complete via checkbox
	
	public static String MEDIA_TYPE_IMG = "IMG";
	public static String MEDIA_TYPE_VIDEO = "MOV";
	
	public static String signatureFileName(TaskVO task){
		StringBuilder sig = new StringBuilder("sig")
		.append(task.getWorkOrderNumber()).append("_")
		.append(task.getTailNumber()).append("_")
		.append(task.getTaskID())
		.append(".png");
		
		return sig.toString();
	}
	
	/**
	 * This function needs to be re-engineered to support multiple pictures/videos per task (and potentially step).
	 * @param task
	 * @return
	 */
	public static String pictureFileName(TaskVO task){
		StringBuilder pic = new StringBuilder("pic")
		.append(task.getWorkOrderNumber()).append("_")
		.append(task.getTailNumber()).append("_")
		.append(task.getTaskID())
		.append(".jpg");
		return pic.toString();
	}
}
