/**
 * 
 */
package com.freyasystems.maintain2.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * @author shroffh Freya Systems, LLC. This is the DataSource class maintains
 *         the database connection and supports adding new VOs and fetching all
 *         VOs.
 */
public class MaintainDS {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private final String TAG = "MAINTAIN_DATASOURCE";

	public MaintainDS(Context context) {
		try {
			dbHelper = new MySQLiteHelper(context);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("MaintainDS", e.getMessage());
		}
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		// database = dbHelper.openDataBase();
	}

	public void close() {
		database.close();
		dbHelper.close();
	}

	/**
	 * Helper method to reset the database by copying SQLite DB file from assets
	 * folder into data folder. This should only be used by application's first
	 * activity.
	 */
	public void resetDatabase() {
		dbHelper.resetDatabase();
	}

	public List<TaskVO> getAllTasks() {
		List<TaskVO> tasks = new ArrayList<TaskVO>();

		Cursor c = database
				.query(MySQLiteHelper.TABLE_TASK,
						MySQLiteHelper.TABLE_TASK_COLUMNS, null, null, null,
						null, null);

		if(!c.moveToFirst())
			return tasks;
			
			
		while (!c.isAfterLast()) {
			tasks.add(MySQLiteHelper.cursorToTaskVO(c));
			c.moveToNext();
		}
		c.close();
		return tasks;
	}

	public TaskVO getTask(long taskId) {
		Log.d(TAG, "got taskId of " + taskId);
		Cursor c = database.query(MySQLiteHelper.TABLE_TASK,
				MySQLiteHelper.TABLE_TASK_COLUMNS, "TaskID=" + taskId, null,
				null, null, null);
		c.moveToFirst();
		TaskVO task = MySQLiteHelper.cursorToTaskVO(c);
		c.close();
		return task;
	}

	public Long[] getTaskProgress(long taskId) {
		Long[] progress = new Long[2];

		StringBuilder sql = new StringBuilder(
				"select complete.count 'complete', done.count 'done' ")
				.append("from (select count(step._id) as \"count\" ")
				.append("from step step, task task ")
				.append("where step.taskId = task._id ")
				.append("and task._id = ").append(taskId).append(" ")
				.append("and step.status = 'Complete') complete, ")
				.append("(select count(step._id) as \"count\" ")
				.append("from step step, task task ")
				.append("where step.taskId = task._id ")
				.append("and task._id = ").append(taskId).append(") done");
		Log.d(TAG, sql.toString());

		Cursor c = database.rawQuery(sql.toString(), null);
		c.moveToFirst();

		progress[0] = c.getLong(0);
		progress[1] = c.getLong(1);

		return progress;
	}

	public StepVO getStep(long stepId) {

		Cursor c = database.query(MySQLiteHelper.TABLE_STEP,
				MySQLiteHelper.TABLE_STEP_COLUMNS, "StepID=" + stepId, null,
				null, null, null);
		c.moveToFirst();
		StepVO step = MySQLiteHelper.cursorToStepVO(c);
		c.close();
		return step;
	}

	public List<StepVO> getSteps(TaskVO task) {
		List<StepVO> steps = new ArrayList<StepVO>();

		Cursor c = database.query(MySQLiteHelper.TABLE_STEP,
				MySQLiteHelper.TABLE_STEP_COLUMNS,
				"TaskId=" + task.getTaskID(), null, null, null, "Sequence");
		c.moveToFirst();

		while (!c.isAfterLast()) {
			steps.add(MySQLiteHelper.cursorToStepVO(c));
			c.moveToNext();
		}
		c.close();
		return steps;
	}

	public List<ResourceVO> getResources(TaskVO task, String type) {
		List<ResourceVO> resources = new ArrayList<ResourceVO>();

		Cursor c = database.query(MySQLiteHelper.TABLE_RESOURCE,
				MySQLiteHelper.TABLE_RESOURCE_COLUMNS,
				"TaskId=" + task.getTaskID() + " AND Type='" + type + "'",
				null, null, null, null);
		c.moveToFirst();

		while (!c.isAfterLast()) {
			resources.add(MySQLiteHelper.cursorToResourceVO(c));
			c.moveToNext();
		}
		c.close();

		return resources;
	}

	public List<StepDataRuleVO> getStepDataRules(StepVO step) {
		List<StepDataRuleVO> rules = new ArrayList<StepDataRuleVO>();

		/*
		 * select rule.ruleName, note.note from StepDataRule rule left outer
		 * join StepDataRuleNote ruleNote on rule.WorkOrderNumber =
		 * ruleNote.WorkOrderNumber and rule.TailNumber = ruleNote.TailNumber
		 * and rule.TaskID = ruleNote.TaskID and rule.StepID = ruleNote.StepID
		 * and rule.DataRuleID = ruleNote.DataRuleID left outer join Note note
		 * on ruleNote.NoteID = note.NoteID where rule.WorkOrderNumber = 'WO123'
		 * and rule.TailNumber = 'TL123' and rule.TaskID = 1 and rule.StepID = 1
		 */

		StringBuilder sql = new StringBuilder("select note.note, note.NoteID ");
		for (String c : MySQLiteHelper.TABLE_STEP_DATA_RULE_COLUMNS) {
			sql.append(", rule.").append(c);
		}

		sql.append(" from ").append(MySQLiteHelper.TABLE_STEP_DATA_RULE)
				.append(" rule");
		// sql.append(" left outer join StepDataRuleNote ruleNote")
		sql.append(" left outer join ")
				.append(MySQLiteHelper.TABLE_STEP_DATA_RULE_NOTE)
				.append(" ruleNote")
				.append(" on rule.WorkOrderNumber = ruleNote.WorkOrderNumber")
				.append(" and rule.TailNumber = ruleNote.TailNumber")
				.append(" and rule.TaskID = ruleNote.TaskID")
				.append(" and rule.StepID = ruleNote.StepID")
				.append(" and rule.DataRuleID = ruleNote.DataRuleID")
				// .append(" left outer join Note note")
				.append(" left outer join ").append(MySQLiteHelper.TABLE_NOTE)
				.append(" note").append(" on ruleNote.NoteID = note.NoteID");
		// where clause
		sql.append(" where rule.WorkOrderNumber = '")
				.append(step.getWorkOrderNumber()).append("'")
				.append(" and rule.TailNumber = '")
				.append(step.getTailNumber()).append("'")
				.append(" and rule.TaskID = ").append(step.getTaskID())
				.append(" and rule.StepID = ").append(step.getStepID());
		sql.append(" order by rule.DataRuleID");

		Log.d(TAG, "sql: >" + sql.toString() + "<");

		Cursor c = database.rawQuery(sql.toString(), null);

		c.moveToFirst();
		while (!c.isAfterLast()) {
			rules.add(MySQLiteHelper.cursorToStepDataRuleVO(c));
			c.moveToNext();
		}
		c.close();

		return rules;
	}

	public StepDataRuleVO getStepDataRule(Long dataRuleID) {
		Cursor c = database.query(MySQLiteHelper.TABLE_STEP_DATA_RULE,
				MySQLiteHelper.TABLE_STEP_DATA_RULE_COLUMNS, "DataRuleID="
						+ dataRuleID, null, null, null, null);
		c.moveToFirst();
		return MySQLiteHelper.cursorToStepDataRuleVO(c);
	}

	public MediaItemVO getMediaItem(Long mediaItemID) {
		Cursor c = database.query(MySQLiteHelper.TABLE_MEDIA_ITEM,
				MySQLiteHelper.TABLE_MEDIA_ITEM_COLUMNS, "MediaItemID="
						+ mediaItemID, null, null, null, null);
		c.moveToFirst();
		return MySQLiteHelper.cursorToMediaItemVO(c);
	}

	public void addStepDataRule(StepDataRuleVO rule) {

		ContentValues values = new ContentValues();

		values.put("WorkOrderNumber", rule.getWorkOrderNumber());
		values.put("TailNumber", rule.getTailNumber());
		values.put("TaskID", rule.getTaskID());
		values.put("StepID", rule.getStepID());
		values.put("DataRuleID", rule.getId());
		values.put("RuleCaption", rule.getRuleCaption());
		values.put("RuleName", rule.getRuleName());

		try {
			long ruleId = database.insertOrThrow(
					MySQLiteHelper.TABLE_STEP_DATA_RULE, null, values);

			Log.d(TAG, ruleId + " step data rule added");
		} catch (SQLException e) {
			// Record already exists, try updating it instead.
			Log.d(TAG,
					"Insert Step Data Rule encountered an error..."
							+ e.getMessage());
		}
	}

	
	
	public List<StepDataRuleVO> getStepDataRuleNotes(StepVO step) {
		List<StepDataRuleVO> notes = new ArrayList<StepDataRuleVO>();
		/*
		 * select rule.ruleName, note.note from StepDataRule rule,
		 * StepDataRuleNote ruleNote, Note note where rule.WorkOrderNumber =
		 * ruleNote.WorkOrderNumber and rule.TailNumber = ruleNote.TailNumber
		 * and rule.TaskID = ruleNote.TaskID and rule.StepID = ruleNote.StepID
		 * and rule.DataRuleID = ruleNote.DataRuleID and ruleNote.NoteID =
		 * note.NoteID and rule.WorkOrderNumber = 'WO123' and rule.TailNumber =
		 * 'TL123' and rule.TaskID = 1 and rule.StepID = 1
		 */
		StringBuilder sql = new StringBuilder("select note.note, note.NoteID ");
		for (String c : MySQLiteHelper.TABLE_STEP_DATA_RULE_COLUMNS) {
			sql.append(", rule.").append(c);
		}

		sql.append(" from ").append(MySQLiteHelper.TABLE_STEP_DATA_RULE)
				.append(" rule, ")
				.append(MySQLiteHelper.TABLE_STEP_DATA_RULE_NOTE)
				.append(" ruleNote, ").append(MySQLiteHelper.TABLE_NOTE)
				.append(" note");

		sql.append(" where")
				.append(" rule.WorkOrderNumber = ruleNote.WorkOrderNumber")
				.append(" and rule.TailNumber = ruleNote.TailNumber")
				.append(" and rule.TaskID = ruleNote.TaskID")
				.append(" and rule.StepID = ruleNote.StepID")
				.append(" and rule.DataRuleID = ruleNote.DataRuleID")
				.append(" and ruleNote.NoteID = note.NoteID");

		sql.append(" and rule.WorkOrderNumber = '")
				.append(step.getWorkOrderNumber()).append("'")
				.append(" and rule.TailNumber = '")
				.append(step.getTailNumber()).append("'")
				.append(" and rule.TaskID = ").append(step.getTaskID())
				.append(" and rule.StepID = ").append(step.getStepID());
		sql.append(" order by rule.DataRuleID");

		Log.d(TAG, "sql: >" + sql.toString() + "<");

		Cursor c = database.rawQuery(sql.toString(), null);

		c.moveToFirst();
		while (!c.isAfterLast()) {
			notes.add(MySQLiteHelper.cursorToStepDataRuleVO(c));
			c.moveToNext();
		}
		c.close();

		return notes;
	}

	public List<MediaItemVO> getStepMediaItem(Long stepId) {
		List<MediaItemVO> notes = new ArrayList<MediaItemVO>();
		/*
		 * select rule.ruleName, note.note from StepDataRule rule,
		 * StepDataRuleNote ruleNote, Note note where rule.WorkOrderNumber =
		 * ruleNote.WorkOrderNumber and rule.TailNumber = ruleNote.TailNumber
		 * and rule.TaskID = ruleNote.TaskID and rule.StepID = ruleNote.StepID
		 * and rule.DataRuleID = ruleNote.DataRuleID and ruleNote.NoteID =
		 * note.NoteID and rule.WorkOrderNumber = 'WO123' and rule.TailNumber =
		 * 'TL123' and rule.TaskID = 1 and rule.StepID = 1
		 */
		StringBuilder sql = new StringBuilder(
				"select MediaItem.MediaItemId, MediaItem.NoteId, MediaItem.FileName, MediaItem.Type from MediaItem, StepContent where MediaItem.MediaItemId = StepContent.MediaItemId and StepContent.StepId =  "
						+ stepId);

		Log.d(TAG, "sql: >" + sql.toString() + "<");

		Cursor c = database.rawQuery(sql.toString(), null);

		c.moveToFirst();
		while (!c.isAfterLast()) {
			notes.add(MySQLiteHelper.cursorToMediaItemVO(c));
			c.moveToNext();
		}
		c.close();

		return notes;
	}

	public void addStep(StepVO step) {
		ContentValues values = new ContentValues();

		values.put("TaskID", step.getTaskID());
		values.put("StepID", step.getStepID());
		values.put("State", step.getState());
		values.put("Sequence", step.getSequence());
		values.put("Warning", step.getWarning());
		values.put("WorkOrderNumber", step.getWorkOrderNumber());
		values.put("TailNumber", step.getTailNumber());

		try {
			long stepId = database.insertOrThrow(MySQLiteHelper.TABLE_STEP,
					null, values);

			Log.d(TAG, stepId + " step added");
		} catch (SQLException e) {
			// Record already exists, try updating it instead.
			Log.d(TAG, "Insert Step encountered an error, trying to update it"
					+ e.getMessage());
			updateStep(step);
		}
	}

	public void updateStep(StepVO step) {

		ContentValues values = new ContentValues();
		values.put("State", step.getState());
		values.put("Sequence", step.getSequence());

		StringBuilder whereClause = new StringBuilder("WorkOrderNumber='")
				.append(step.getWorkOrderNumber()).append("'")
				.append(" AND TailNumber='").append(step.getTailNumber())
				.append("'").append(" AND TaskID=").append(step.getTaskID())
				.append(" AND StepID=").append(step.getStepID());

		StringBuilder sql = new StringBuilder("UPDATE ")
				.append(MySQLiteHelper.TABLE_STEP).append(" SET")
				.append(" State='").append(step.getState()).append("'")
				.append(", Sequence=").append(step.getSequence())
				.append(" WHERE ").append(whereClause);

		Log.d(TAG, "Step Update Clause: " + sql.toString());
		// int stepId = database.update(MySQLiteHelper.TABLE_STEP, values,
		// whereClause.toString(), null);
		database.execSQL(sql.toString());
		Log.d(TAG, step.getStepID() + " Step updated");
		StepVO newStep = getStep(step.getStepID());
		Log.d(TAG, "new step state is " + newStep.getState());
	}

	public void updateTask(TaskVO task) {

		ContentValues values = new ContentValues();
		values.put("TaskCode", task.getTaskCode());
		values.put("ShortDescription", task.getShortDescription());
		values.put("LongDescription", task.getLongDescription());
		values.put("WorkDone", task.getWorkDone());
		values.put("State", task.getState());
		values.put("SequenceNumber", task.getSequenceNumber());
		values.put("PercentComplete", task.getPercentComplete());
		values.put("NumberStepsComplete", task.getNumberStepsComplete());
		values.put("DisplayType", task.getDisplayType());
		// values.put("TaskStartDateTime", task.getTaskStartDateTime());
		// values.put("TaskEndDateTime", task.getTaskEndDateTime());
		// values.put("TaskPauseDateTime", task.getTaskPauseDateTime());
		// values.put("TaskPauseTime", task.getTaskPauseTime());

		StringBuilder whereClause = new StringBuilder("WorkOrderNumber='")
				.append(task.getWorkOrderNumber()).append("'")
				.append(" AND TailNumber='").append(task.getTailNumber())
				.append("'").append(" AND TaskID=").append(task.getTaskID());
		Log.d(TAG + "-updateTask", whereClause.toString());

		long taskId = database.update(MySQLiteHelper.TABLE_TASK, values,
				whereClause.toString(), null);
		Log.d(TAG, taskId + " Task updated");
	}

	public void addTask(TaskVO task) {
		ContentValues values = new ContentValues();
		values.put("TaskID", task.getTaskID());
		values.put("WorkOrderNumber", task.getWorkOrderNumber());
		values.put("TailNumber", task.getTailNumber());
		values.put("TaskCode", task.getTaskCode());
		values.put("ShortDescription", task.getShortDescription());
		values.put("LongDescription", task.getLongDescription());
		values.put("WorkDone", task.getWorkDone());
		values.put("State", task.getState());
		values.put("SequenceNumber", task.getSequenceNumber());
		values.put("PercentComplete", task.getPercentComplete());
		values.put("NumberStepsComplete", task.getNumberStepsComplete());
		values.put("DisplayType", task.getDisplayType());

		try {
			long taskId = database.insertOrThrow(MySQLiteHelper.TABLE_TASK,
					null, values);

			Log.d(TAG, taskId + " Task added");
		} catch (SQLException e) {
			// Record already exists, try updating it instead.
			Log.d(TAG,
					"task already exists, trying to update it" + e.getMessage());
			updateTask(task);
		}
	}

	public void recordActivity(ActivityVO a){
		a.setActivtyTime(new Date());
		
		ContentValues values = new ContentValues();
		values.put("TaskID", a.getTaskID());
		values.put("Activity", a.getActivity());
		values.put("Actor", a.getActor());
		values.put("ActivtyTime", a.getActivtyTime().toGMTString());
		
		long aId = database.insert(MySQLiteHelper.TABLE_NOTE, null, values);

		Log.d(TAG, "Activity " + a.getActivity() + " added with id of " + aId);
	}
	
	/**
	 * @param note
	 * @param parent
	 *            - This must be one of TaskVO, StepVO, StepDataRuleVO, or
	 *            MediaItemVO
	 */
	public void addNote(NoteVO note, Object parent) {
		// Insert Note
		note.setRecordDateTime(new Date());

		ContentValues values = new ContentValues();
		values.put("Note", note.getNote());
		values.put("RecordDateTime", note.getRecordDateTime().toGMTString());

		long noteId = database.insert(MySQLiteHelper.TABLE_NOTE, null, values);

		Log.d(TAG, "Note " + note.getNote() + " added with id of " + noteId);

		// Insert relationship record based on what the parent object is
		if (parent == null) {
			// No parent supplied
		} else if (parent instanceof TaskVO) {

		} else if (parent instanceof StepVO) {

		} else if (parent instanceof StepDataRuleVO) {
			StepDataRuleVO vo = (StepDataRuleVO) parent;
			ContentValues ruleValues = new ContentValues();
			ruleValues.put("WorkOrderNumber", vo.getWorkOrderNumber());
			ruleValues.put("TailNumber", vo.getTailNumber());
			ruleValues.put("TaskID", vo.getTaskID());
			ruleValues.put("StepID", vo.getStepID());
			ruleValues.put("DataRuleID", vo.getId());
			ruleValues.put("NoteID", noteId);

			long ruleId = database.insert(
					MySQLiteHelper.TABLE_STEP_DATA_RULE_NOTE, null, ruleValues);
			Log.d(TAG, "StepDataRuleNote added with id of " + ruleId
					+ " as parent of Note " + noteId);
		} else if (parent instanceof MediaItemVO) {

		}
	}

	public void updateNote(NoteVO note) {

		note.setRecordDateTime(new Date());

		ContentValues values = new ContentValues();
		values.put("Note", note.getNote());
		values.put("RecordDateTime", note.getRecordDateTime().toGMTString());

		long rowsUpdated = database.update(MySQLiteHelper.TABLE_NOTE, values,
				"NoteID=" + note.getNoteID(), null);
		Log.d(TAG, rowsUpdated + " rows updated by updateNote()");
	}

	public List<StepContentVO> getStepContent(StepVO step) {
		List<StepContentVO> contentList = new ArrayList<StepContentVO>();

		// StringBuilder a

		Cursor c = database.query(MySQLiteHelper.TABLE_STEP_CONTENT,
				MySQLiteHelper.TABLE_STEP_CONTENT_COLUMNS,
				"StepID=" + step.getStepID(), null, null, null, "Sequence");
		c.moveToFirst();

		while (!c.isAfterLast()) {
			contentList.add(MySQLiteHelper.cursorToStepContentVO(c));
			c.moveToNext();
		}
		c.close();

		List<StepContentVO> loadedContentList = new ArrayList<StepContentVO>();
		for (StepContentVO content : contentList) {
			if (content.getMediaItemID() > 0) {
				content.setMediaItem(getMediaItem(content.getMediaItemID()));
				content.setContent(null);
			}
			loadedContentList.add(content);
		}

		return loadedContentList;
	}

	public void addStepContent(StepContentVO content) {

		Log.d(TAG,
				"StepID: " + content.getStepID() + ", Content: "
						+ content.getContent());

		ContentValues values = new ContentValues();

		values.put("StepID", content.getStepID());
		values.put("Sequence", content.getSequence());
		values.put("StepName", content.getStepName());
		values.put("Content", content.getContent());
		values.put("MediaItemID", content.getMediaItemID());

		try {
			long contentId = database.insertOrThrow(
					MySQLiteHelper.TABLE_STEP_CONTENT, null, values);

			Log.d(TAG, contentId + " step content added");
		} catch (SQLException e) {
			Log.d(TAG,
					"Insert Step Content encountered an error..."
							+ e.getMessage());
		}
	}

	public void addMediaItem(MediaItemVO me) {

		ContentValues values = new ContentValues();
		values.put("MediaItemID", me.getMediaItemID());
		values.put("NoteID", me.getNoteID());
		values.put("FileName", me.getFileName());
		values.put("Type", me.getType());

		try {
			long contentId = database.insertOrThrow(
					MySQLiteHelper.TABLE_MEDIA_ITEM, null, values);

			Log.d(TAG, contentId + " media item added");
		} catch (SQLException e) {
			Log.d(TAG,
					"Insert Media Item encountered an error..."
							+ e.getMessage());
		}
	}

	public void removeLocalData() {
		removeAllStepDataRules();
		removeAllStepContent();
		removeAllSteps();
		removeAllTasks();
	}

	public void removeAllTasks() {
		database.delete(MySQLiteHelper.TABLE_TASK, null, null);
	}

	public void removeAllSteps() {
		database.delete(MySQLiteHelper.TABLE_STEP, null, null);
	}

	public void removeAllStepContent() {
		database.delete(MySQLiteHelper.TABLE_STEP_CONTENT, null, null);
	}

	public void removeAllStepDataRules() {
		database.delete(MySQLiteHelper.TABLE_STEP_DATA_RULE, null, null);
	}
}
