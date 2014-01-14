/**
 * 
 */
package com.freyasystems.maintain2.dao;

/**
 * @author shroffh Freya Systems, LLC.
 */
public class StepContentVO {
	private long id;
	private long stepID;
	private String content = null;
	private long sequence;
	private long mediaItemID;
	private MediaItemVO mediaItem = null;
	private String stepName;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the mediaItemID
	 */
	public long getMediaItemID() {
		return mediaItemID;
	}

	/**
	 * @param mediaItemID
	 *            the mediaItemID to set
	 */
	public void setMediaItemID(long mediaItemID) {
		this.mediaItemID = mediaItemID;
	}

	/**
	 * @return the stepId
	 */
	public long getStepID() {
		return stepID;
	}

	/**
	 * @param stepId
	 *            the stepId to set
	 */
	public void setStepID(long stepId) {
		this.stepID = stepId;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the sequence
	 */
	public long getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the mediaItem
	 */
	public MediaItemVO getMediaItem() {
		return mediaItem;
	}

	/**
	 * @param mediaItem
	 *            the mediaItem to set
	 */
	public void setMediaItem(MediaItemVO mediaItem) {
		this.mediaItem = mediaItem;
	}

	/**
	 * @return the stepName
	 */
	public String getStepName() {
		return stepName;
	}

	/**
	 * @param stepName
	 *            the stepName to set
	 */
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
}
