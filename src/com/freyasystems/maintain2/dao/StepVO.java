/**
 * 
 */
package com.freyasystems.maintain2.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class StepVO {
	private String workOrderNumber;
	private String tailNumber;
	private Long taskID;
	private Long stepID;
	private String state;
	private Long sequence;
	private String warning;
	
	private List<StepContentVO> stepcontents = new ArrayList<StepContentVO>();
	private List<StepDataRuleVO> stepdatarules = new ArrayList<StepDataRuleVO>();
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("Step: "+ stepID);
		
		//sb.append("\nStatus: ").append(Status);
		//sb.append("\n").append(Description);
		
		return sb.toString();
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
	 * @return the stepID
	 */
	public Long getStepID() {
		return stepID;
	}

	/**
	 * @param stepID the stepID to set
	 */
	public void setStepID(Long stepID) {
		this.stepID = stepID;
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
	 * @return the sequence
	 */
	public Long getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the warning
	 */
	public String getWarning() {
		return warning;
	}

	/**
	 * @param warning the warning to set
	 */
	public void setWarning(String warning) {
		this.warning = warning;
	}

	/**
	 * @return the stepcontents
	 */
	public List<StepContentVO> getStepcontents() {
		return stepcontents;
	}

	/**
	 * @param stepcontents the stepcontents to set
	 */
	public void setStepcontents(List<StepContentVO> stepcontents) {
		this.stepcontents = stepcontents;
	}

	/**
	 * @return the stepdatarules
	 */
	public List<StepDataRuleVO> getStepdatarules() {
		return stepdatarules;
	}

	/**
	 * @param stepdatarules the stepdatarules to set
	 */
	public void setStepdatarules(List<StepDataRuleVO> stepdatarules) {
		this.stepdatarules = stepdatarules;
	}
}
