/**
 * 
 */
package com.freyasystems.maintain2.dao;

/**
 * @author shroffh Freya Systems, LLC.
 */
public class MediaItemVO {
	private Long mediaItemID = new Long(0);
	private Long noteID;
	private String fileName;
	private String type;

	/**
	 * @return the mediaItemID
	 */
	public Long getMediaItemID() {
		return mediaItemID;
	}

	/**
	 * @param mediaItemID
	 *            the mediaItemID to set
	 */
	public void setMediaItemID(Long mediaItemID) {
		this.mediaItemID = mediaItemID;
	}

	/**
	 * @return the noteID
	 */
	public Long getNoteID() {
		return noteID;
	}

	/**
	 * @param noteID
	 *            the noteID to set
	 */
	public void setNoteID(Long noteID) {
		this.noteID = noteID;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the itemType
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param itemType
	 *            the itemType to set
	 */
	public void setType(String type) {
		this.type = type;
	}
}
