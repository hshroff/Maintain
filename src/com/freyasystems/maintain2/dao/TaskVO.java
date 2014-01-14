/**
 * 
 */
package com.freyasystems.maintain2.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class TaskVO {
	private String workOrderNumber;
	private String tailNumber;
	private Long taskID;
	private String taskCode = "";
	private String shortDescription = "";
	private String longDescription = "";
	private String workDone = "";
	private String state = "";
	private Long sequenceNumber;
	private Long percentComplete;
	private Long numberStepsComplete;
	private Date taskStartDateTime;
	private Date taskEndDateTime;
	private Date taskPauseDateTime;
	private Date taskPauseTime;
	private String displayType = "Swipe List";
	private String assignedTo = "";
	private Long templateId = new Long(0);
	
	private List<StepVO> steps = new ArrayList<StepVO>();
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("Task: "+ shortDescription);
		
		//sb.append("\nStatus: ").append(Status);
		//sb.append("\n").append(Description);
		
		return sb.toString();
	}
	
	/**
	 * @return the assignedTo
	 */
	public String getAssignedTo() {
		return assignedTo;
	}

	/**
	 * @param assignedTo the assignedTo to set
	 */
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	/**
	 * @return the templateId
	 */
	public Long getTemplateId() {
		return templateId;
	}

	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	/**
	 * @return the workOrderNumber
	 */
	public String getWorkOrderNumber() {
		return workOrderNumber;
	}
	/**
	 * @param workOrderNumber the workOrderNumber to set
	 */
	public void setWorkOrderNumber(String workOrderNumber) {
		this.workOrderNumber = workOrderNumber;
	}
	/**
	 * @return the tailNumber
	 */
	public String getTailNumber() {
		return tailNumber;
	}
	/**
	 * @param tailNumber the tailNumber to set
	 */
	public void setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
	}
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
	 * @return the taskCode
	 */
	public String getTaskCode() {
		return taskCode;
	}
	/**
	 * @param taskCode the taskCode to set
	 */
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	/**
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}
	/**
	 * @param shortDescription the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	/**
	 * @return the longDescription
	 */
	public String getLongDescription() {
		return longDescription;
	}
	/**
	 * @param longDescription the longDescription to set
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	/**
	 * @return the workDone
	 */
	public String getWorkDone() {
		return workDone;
	}
	/**
	 * @param workDone the workDone to set
	 */
	public void setWorkDone(String workDone) {
		this.workDone = workDone;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the sequenceNumber
	 */
	public Long getSequenceNumber() {
		return sequenceNumber;
	}
	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	/**
	 * @return the percentComplete
	 */
	public Long getPercentComplete() {
		return percentComplete;
	}
	/**
	 * @param percentComplete the percentComplete to set
	 */
	public void setPercentComplete(Long percentComplete) {
		this.percentComplete = percentComplete;
	}
	/**
	 * @return the numberStepsComplete
	 */
	public Long getNumberStepsComplete() {
		return numberStepsComplete;
	}
	/**
	 * @param numberStepsComplete the numberStepsComplete to set
	 */
	public void setNumberStepsComplete(Long numberStepsComplete) {
		this.numberStepsComplete = numberStepsComplete;
	}
	/**
	 * @return the taskStartDateTime
	 */
	public Date getTaskStartDateTime() {
		return taskStartDateTime;
	}
	/**
	 * @param taskStartDateTime the taskStartDateTime to set
	 */
	public void setTaskStartDateTime(Date taskStartDateTime) {
		this.taskStartDateTime = taskStartDateTime;
	}
	/**
	 * @return the taskEndDateTime
	 */
	public Date getTaskEndDateTime() {
		return taskEndDateTime;
	}
	/**
	 * @param taskEndDateTime the taskEndDateTime to set
	 */
	public void setTaskEndDateTime(Date taskEndDateTime) {
		this.taskEndDateTime = taskEndDateTime;
	}
	/**
	 * @return the taskPauseDateTime
	 */
	public Date getTaskPauseDateTime() {
		return taskPauseDateTime;
	}
	/**
	 * @param taskPauseDateTime the taskPauseDateTime to set
	 */
	public void setTaskPauseDateTime(Date taskPauseDateTime) {
		this.taskPauseDateTime = taskPauseDateTime;
	}
	/**
	 * @return the taskPauseTime
	 */
	public Date getTaskPauseTime() {
		return taskPauseTime;
	}
	/**
	 * @param taskPauseTime the taskPauseTime to set
	 */
	public void setTaskPauseTime(Date taskPauseTime) {
		this.taskPauseTime = taskPauseTime;
	}

	/**
	 * @return the displayType
	 */
	public String getDisplayType() {
		return displayType;
	}

	/**
	 * @param displayType the displayType to set
	 */
	public void setDisplayType(String displayType) {
		this.displayType = displayType;
	}

	/**
	 * @return the steps
	 */
	public List<StepVO> getSteps() {
		return steps;
	}

	/**
	 * @param steps the steps to set
	 */
	public void setSteps(List<StepVO> steps) {
		this.steps = steps;
	}
	
}
