/**
 * 
 */
package com.freyasystems.maintain2.dao;

import java.util.Date;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class WorkOrderVO {
	private long id;
	private String WorkOrderNumber;
	private String TailNumber;
	private String Complexity;
	private Date PlannedStartDate;
	private Date CompletionDate;
	private String Asignee;
	
	@Override
	public String toString(){
		return WorkOrderNumber;
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
	 * @return the complexity
	 */
	public String getComplexity() {
		return Complexity;
	}

	/**
	 * @param complexity the complexity to set
	 */
	public void setComplexity(String complexity) {
		Complexity = complexity;
	}

	/**
	 * @return the plannedStartDate
	 */
	public Date getPlannedStartDate() {
		return PlannedStartDate;
	}

	/**
	 * @param plannedStartDate the plannedStartDate to set
	 */
	public void setPlannedStartDate(Date plannedStartDate) {
		PlannedStartDate = plannedStartDate;
	}

	/**
	 * @return the completionDate
	 */
	public Date getCompletionDate() {
		return CompletionDate;
	}

	/**
	 * @param completionDate the completionDate to set
	 */
	public void setCompletionDate(Date completionDate) {
		CompletionDate = completionDate;
	}

	/**
	 * @return the asignee
	 */
	public String getAsignee() {
		return Asignee;
	}

	/**
	 * @param asignee the asignee to set
	 */
	public void setAsignee(String asignee) {
		Asignee = asignee;
	}
}
