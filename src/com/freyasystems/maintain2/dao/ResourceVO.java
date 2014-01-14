/**
 * 
 */
package com.freyasystems.maintain2.dao;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class ResourceVO {
	private String WorkOrderNumber;
	private String TailNumber;
	private Long TaskID;
	private Long ResourceID;
	private String Type;
	private Long Quantity;
	private String ShortDescription;
	
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
	 * @return the resourceID
	 */
	public Long getResourceID() {
		return ResourceID;
	}
	/**
	 * @param resourceID the resourceID to set
	 */
	public void setResourceID(Long resourceID) {
		ResourceID = resourceID;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return Type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		Type = type;
	}
	/**
	 * @return the quantity
	 */
	public Long getQuantity() {
		return Quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Long quantity) {
		Quantity = quantity;
	}
	/**
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return ShortDescription;
	}
	/**
	 * @param shortDescription the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		ShortDescription = shortDescription;
	}
}
