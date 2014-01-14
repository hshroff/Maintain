/**
 * 
 */
package com.freyasystems.maintain2.dao;

import java.util.Date;

/**
 * Value Object for table Note
 * 
 * @author shroffh
 * Freya Systems, LLC.
 */
public class NoteVO {
	private Long NoteID;
	private String Note;
	private Date RecordDateTime;
	
	/**
	 * @return the noteID
	 */
	public Long getNoteID() {
		return NoteID;
	}
	/**
	 * @param noteID the noteID to set
	 */
	public void setNoteID(Long noteID) {
		NoteID = noteID;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return Note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		Note = note;
	}
	/**
	 * @return the recordDateTime
	 */
	public Date getRecordDateTime() {
		return RecordDateTime;
	}
	/**
	 * @param recordDateTime the recordDateTime to set
	 */
	public void setRecordDateTime(Date recordDateTime) {
		RecordDateTime = recordDateTime;
	}
}
