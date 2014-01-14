/**
 * 
 */
package com.freyasystems.maintain2.dao;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class StepDataRuleVO {
	private String WorkOrderNumber;
	private String TailNumber;
	private Long TaskID;
	private Long StepID;
	private Long id;
	private String ruleCaption;
	private String ruleName;
	private NoteVO Note; //Display only, updates are not saved in the database.
	
	/**
	 * @return the workOrderNumber
	 */
	public String getWorkOrderNumber() {
		return WorkOrderNumber;
	}
	/**
	 * @param workOrderNumber the workOrderNumber to set
	 */
	public void setWorkOrderNumber(String workOrderNumber) {
		WorkOrderNumber = workOrderNumber;
	}
	/**
	 * @return the tailNumber
	 */
	public String getTailNumber() {
		return TailNumber;
	}
	/**
	 * @param tailNumber the tailNumber to set
	 */
	public void setTailNumber(String tailNumber) {
		TailNumber = tailNumber;
	}
	/**
	 * @return the taskID
	 */
	public Long getTaskID() {
		return TaskID;
	}
	/**
	 * @param taskID the taskID to set
	 */
	public void setTaskID(Long taskID) {
		TaskID = taskID;
	}
	/**
	 * @return the stepID
	 */
	public Long getStepID() {
		return StepID;
	}
	/**
	 * @param stepID the stepID to set
	 */
	public void setStepID(Long stepID) {
		StepID = stepID;
	}
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the ruleCaption
	 */
	public String getRuleCaption() {
		return ruleCaption;
	}
	/**
	 * @param ruleCaption the ruleCaption to set
	 */
	public void setRuleCaption(String ruleCaption) {
		this.ruleCaption = ruleCaption;
	}
	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}
	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	/**
	 * @return the note
	 */
	public NoteVO getNote() {
		return Note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(NoteVO note) {
		Note = note;
	}
}
