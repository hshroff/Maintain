/**
 * 
 */
package com.freyasystems.maintain2.dao;

import java.util.Date;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class ActivityVO {

	private Long taskID = new Long(0);
	private String activity = "";
	private Date activtyTime;
	private String actor = "";
	/**
	 * @return the taskID
	 */
	public Long getTaskID() {
		return taskID;
	}
	/**
	 * @param taskID the taskID to set
	 */
	public void setTaskID(Long taskID) {
		this.taskID = taskID;
	}
	/**
	 * @return the activity
	 */
	public String getActivity() {
		return activity;
	}
	/**
	 * @param activity the activity to set
	 */
	public void setActivity(String activity) {
		this.activity = activity;
	}
	/**
	 * @return the activtyTime
	 */
	public Date getActivtyTime() {
		return activtyTime;
	}
	/**
	 * @param activtyTime the activtyTime to set
	 */
	public void setActivtyTime(Date activtyTime) {
		this.activtyTime = activtyTime;
	}
	/**
	 * @return the actor
	 */
	public String getActor() {
		return actor;
	}
	/**
	 * @param actor the actor to set
	 */
	public void setActor(String actor) {
		this.actor = actor;
	}
}
