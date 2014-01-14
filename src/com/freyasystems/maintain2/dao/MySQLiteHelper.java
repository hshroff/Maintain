/**
 * 
 */
package com.freyasystems.maintain2.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author shroffh Freya Systems, LLC.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

	private static String TAG = "MySQLiteHelper";

	private static String DATABASE_PATH = "/data/data/com.freyasystems.maintain2/databases/";
	private static final String DATABASE_NAME = "maintainApp.db";
	private static final int DATABASE_VERSION = 3;
	private final Context myContext;
	private SQLiteDatabase myDataBase;

	/* Datamodel */
	// WorkOrder //
	public static final String TABLE_WO = "WorkOrder";

	// Task //
	public static final String TABLE_TASK = "task";

	public static String[] TABLE_TASK_COLUMNS = { "TaskID", "WorkOrderNumber",
			"TailNumber", "TaskCode", "ShortDescription", "LongDescription",
			"WorkDone", "State", "SequenceNumber", "PercentComplete",
			"NumberStepsComplete", "TaskStartDateTime", "TaskEndDateTime",
			"TaskPauseDateTime", "TaskPauseTime", "DisplayType" };

	// Step //
	public static final String TABLE_STEP = "step";

	public static String[] TABLE_STEP_COLUMNS = { "WorkOrderNumber",
			"TailNumber", "TaskID", "StepID", "State", "Sequence", "Warning" };

	// Resource //
	public static final String TABLE_RESOURCE = "Resource";
	public static String[] TABLE_RESOURCE_COLUMNS = { "ResourceID",
			"WorkOrderNumber", "TaskID", "Type", "Quantity",
			"ShortDescription", "TailNumber" };

	// Step Content //
	public static final String TABLE_STEP_CONTENT = "StepContent";
	public static String[] TABLE_STEP_CONTENT_COLUMNS = { "StepID", "Sequence",
			"MediaItemID", "StepName", "Content" };

	// Step Data Rule //
	public static final String TABLE_STEP_DATA_RULE = "StepDataRule";
	public static String[] TABLE_STEP_DATA_RULE_COLUMNS = { "WorkOrderNumber",
			"TailNumber", "TaskID", "StepID", "DataRuleID", "RuleCaption",
			"RuleName" };

	// Note //
	public static final String TABLE_NOTE = "Note";
	public static String[] TABLE_NOTE_COLUMNS = { "NoteID", "Note",
			"RecordDateTime" };

	// Note to parent (Task, Step, StepDataRule, MediaItem //
	public static final String TABLE_STEP_DATA_RULE_NOTE = "StepDataRuleNote";
	public static final String TABLE_STEP_NOTE = "StepNote";
	public static final String TABLE_TASK_NOTE = "TaskNote";
	
	public static final String TABLE_MEDIA_ITEM = "MediaItem";
	public static String[] TABLE_MEDIA_ITEM_COLUMNS = { "MediaItemID", "NoteID",
	"FileName", "Type" };

	public static final String TABLE_ACTIVITY_LOG = "Activity_Log";
	public static String[] TABLE_ACTIVITY_LOG_COLUMNS = { "TaskID", "Activity", "Actor", "ActivtyTime" };
	
	// Cursor to VO CONVERTER METHODS...
	public static TaskVO cursorToTaskVO(Cursor c) {

		if (c == null) {
			Log.i(TAG, "Task not found in database");
			return null;
		}else if(!c.moveToFirst()) {
			Log.i(TAG, "Task not found in database");
			return null;
		}

		TaskVO task = new TaskVO();
		task.setWorkOrderNumber(c.getString(c.getColumnIndex("WorkOrderNumber")));
		task.setTailNumber(c.getString(c.getColumnIndex("TailNumber")));
		task.setTaskID(c.getLong(c.getColumnIndex("TaskID")));
		task.setTaskCode(c.getString(c.getColumnIndex("TaskCode")));
		task.setShortDescription(c.getString(c
				.getColumnIndex("ShortDescription")));
		task.setLongDescription(c.getString(c.getColumnIndex("LongDescription")));
		task.setWorkDone(c.getString(c.getColumnIndex("WorkDone")));
		task.setState(c.getString(c.getColumnIndex("State")));
		task.setSequenceNumber(c.getLong(c.getColumnIndex("SequenceNumber")));
		task.setPercentComplete(c.getLong(c.getColumnIndex("PercentComplete")));
		task.setNumberStepsComplete(c.getLong(c
				.getColumnIndex("NumberStepsComplete")));
		// task.setTaskStartDateTime(new
		// Date(c.getString(c.getColumnIndex("TaskStartDateTime"))));
		// task.setTaskEndDateTime(new
		// Date(c.getString(c.getColumnIndex("TaskEndDateTime"))));
		// task.setTaskPauseDateTime(new
		// Date(c.getString(c.getColumnIndex("TaskPauseDateTime"))));
		// task.setTaskPauseTime(new
		// Date(c.getString(c.getColumnIndex("TaskPauseTime"))));

		task.setDisplayType(c.getString(c.getColumnIndex("DisplayType")));
		return task;
	}

	/*
	 * public static TaskVO jsonToTaskVO(JSONObject json){ TaskVO task = new
	 * TaskVO();
	 * 
	 * 
	 * 
	 * 
	 * task.setWorkOrderNumber(c.getString(c.getColumnIndex("WorkOrderNumber")));
	 * task.setTailNumber(c.getString(c.getColumnIndex("TailNumber")));
	 * task.setTaskID(c.getLong(c.getColumnIndex("TaskID")));
	 * task.setTaskCode(c.getString(c.getColumnIndex("TaskCode")));
	 * task.setShortDescription(c.getString(c
	 * .getColumnIndex("ShortDescription")));
	 * task.setLongDescription(c.getString
	 * (c.getColumnIndex("LongDescription")));
	 * task.setWorkDone(c.getString(c.getColumnIndex("WorkDone")));
	 * task.setState(c.getString(c.getColumnIndex("State")));
	 * task.setSequenceNumber(c.getLong(c.getColumnIndex("SequenceNumber")));
	 * task.setPercentComplete(c.getLong(c.getColumnIndex("PercentComplete")));
	 * task.setNumberStepsComplete(c.getLong(c
	 * .getColumnIndex("NumberStepsComplete"))); //
	 * task.setTaskStartDateTime(new //
	 * Date(c.getString(c.getColumnIndex("TaskStartDateTime")))); //
	 * task.setTaskEndDateTime(new //
	 * Date(c.getString(c.getColumnIndex("TaskEndDateTime")))); //
	 * task.setTaskPauseDateTime(new //
	 * Date(c.getString(c.getColumnIndex("TaskPauseDateTime")))); //
	 * task.setTaskPauseTime(new //
	 * Date(c.getString(c.getColumnIndex("TaskPauseTime"))));
	 * 
	 * task.setDisplayType(c.getString(c.getColumnIndex("DisplayType"))); return
	 * task; }
	 */

	public static StepVO cursorToStepVO(Cursor c) {
		if (c.isNull(0)) {
			Log.i("STEP", "Step not found in dababase");
			return null;
		}

		StepVO step = new StepVO();
		step.setStepID(c.getLong(c.getColumnIndex("StepID")));
		step.setWorkOrderNumber(c.getString(c.getColumnIndex("WorkOrderNumber")));
		step.setTailNumber(c.getString(c.getColumnIndex("TailNumber")));
		step.setTaskID(c.getLong(c.getColumnIndex("TaskID")));
		step.setState(c.getString(c.getColumnIndex("State")));
		step.setSequence(c.getLong(c.getColumnIndex("Sequence")));
		step.setWarning(c.getString(c.getColumnIndex("Warning")));

		return step;
	}

	public static ResourceVO cursorToResourceVO(Cursor c) {
		if (c.isNull(0)) {
			Log.i("RESOURCE", "Resource not found in dababase");
			return null;
		}

		/*
		 * for (String s : c.getColumnNames()) { Log.d(TAG, s); }
		 */
		ResourceVO r = new ResourceVO();
		r.setQuantity(c.getLong(c.getColumnIndex("Quantity")));
		r.setResourceID(c.getLong(c.getColumnIndex("ResourceID")));
		r.setShortDescription(c.getString(c.getColumnIndex("ShortDescription")));
		r.setTailNumber(c.getString(c.getColumnIndex("TailNumber")));
		r.setTaskID(c.getLong(c.getColumnIndex("TaskID")));
		r.setType(c.getString(c.getColumnIndex("Type")));
		r.setWorkOrderNumber(c.getString(c.getColumnIndex("WorkOrderNumber")));

		return r;
	}

	public static StepContentVO cursorToStepContentVO(Cursor c) {
		if (c.isNull(0)) {
			Log.i("StepContent", "StepContent not found in dababase");
			return null;
		}

		Log.d(TAG,
				"Retrieved Content: "
						+ c.getString(c.getColumnIndex("Content")));

		StepContentVO content = new StepContentVO();
		content.setStepID(c.getLong(c.getColumnIndex("StepID")));
		content.setSequence(c.getLong(c.getColumnIndex("Sequence")));
		content.setContent(c.getString(c.getColumnIndex("Content")));
		content.setStepName(c.getString(c.getColumnIndex("StepName")));
		// MediaItemID
		if (c.getLong(c.getColumnIndex("MediaItemID")) > 0) {
			content.setMediaItemID(c.getLong(c.getColumnIndex("MediaItemID")));
		}

		return content;
	}

	public static MediaItemVO cursorToMediaItemVO(Cursor c) {
		if (c.isNull(0)) {
			Log.i("MediaItem", "MediaItem not found in dababase");
			return null;
		}

		MediaItemVO content = new MediaItemVO();
		content.setType(c.getString(c.getColumnIndex("Type")));
		content.setMediaItemID(c.getLong(c.getColumnIndex("MediaItemID")));
		content.setFileName(c.getString(c.getColumnIndex("FileName")));
		content.setNoteID(c.getLong(c.getColumnIndex("NoteID")));

		return content;
	}

	public static StepDataRuleVO cursorToStepDataRuleVO(Cursor c) {
		if (c.isNull(2)) {
			Log.i("StepDataRuleVO", "StepDataRule not found in dababase");
			return null;
		}

		for (int i = 0; i < c.getColumnCount(); i++) {
			Log.d(TAG, "column " + c.getColumnName(i));
		}

		StepDataRuleVO vo = new StepDataRuleVO();
		vo.setTailNumber(c.getString(c.getColumnIndex("TailNumber")));
		vo.setTaskID(c.getLong(c.getColumnIndex("TaskID")));
		vo.setWorkOrderNumber(c.getString(c.getColumnIndex("WorkOrderNumber")));
		vo.setStepID(c.getLong(c.getColumnIndex("StepID")));
		vo.setId(c.getLong(c.getColumnIndex("DataRuleID")));
		vo.setRuleCaption(c.getString(c.getColumnIndex("RuleCaption")));
		vo.setRuleName(c.getString(c.getColumnIndex("RuleName")));

		if (c.getColumnIndex("Note") > -1
				&& c.getString(c.getColumnIndex("Note")) != null
				&& c.getString(c.getColumnIndex("Note")) != "") {
			NoteVO note = new NoteVO();
			note.setNote(c.getString(c.getColumnIndex("Note")));
			note.setNoteID(c.getLong(c.getColumnIndex("NoteID")));
			vo.setNote(note);
		}

		return vo;

	}

	// public static final String WO_COLUMN_ID = "_id";
	public static final String WO_COLUMN_WONUMBER = "WorkOrderNumber";
	public static final String WO_COLUMN_TAILNUMBER = "TailNumber";
	// public static final String WO_COLUMN_Complexity = "Complexity";
	public static final String WO_COLUMN_PlannedStartDate = "PlannedStartDate";
	public static final String WO_COLUMN_CompletionDate = "CompletionDate";
	public static final String WO_COLUMN_Asignee = "Asignee";

	// workorder DDL
	/*
	 * private static final String WO_DDL = "create table " + TABLE_WO + "(" +
	 * WO_COLUMN_ID + " integer primary key autoincrement, " +
	 * WO_COLUMN_WONUMBER + " text not null, " + WO_COLUMN_TAILNUMBER +
	 * " text not null, " + WO_COLUMN_Complexity + " text not null, " +
	 * WO_COLUMN_PlannedStartDate + " text, " + WO_COLUMN_CompletionDate +
	 * " text, " + WO_COLUMN_Asignee + " text not null" + ");";
	 */
	// Task

	/*
	 * public static final String TASK_COLUMN_ID = "_id"; public static final
	 * String TASK_COLUMN_TITLE = "ShortDescription"; public static final String
	 * TASK_COLUMN_WO = "WorkOrderNumber"; public static final String
	 * TASK_COLUMN_PROGRESS = "PercentComplete";
	 * 
	 * // task DDL private static final String TASK_DDL = "create table " +
	 * TABLE_TASK + "(" + TASK_COLUMN_ID +
	 * " integer primary key autoincrement, " + TASK_COLUMN_WO +
	 * " text not null, " + TASK_COLUMN_TITLE + " text not null" + ");";
	 */

	// Step
	// private static final String STEP_DDL = "create table " + TABLE_STEP + "("
	// + "_id integer primary key autoincrement, "
	// + "Title text not null, " + "Description text, "
	// + "Status text not null, "
	// + "Complete text, " // this should be boolean
	// + "IETPurl text, " + "taskId integer not null, "
	// + "stepNumber integer not null" + ");";

	public MySQLiteHelper(Context context) throws IOException {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
		MySQLiteHelper();
	}

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @throws IOException
	 */
	public MySQLiteHelper(Context context, String name, CursorFactory factory,
			int version) throws IOException {
		super(context, name, factory, version);
		this.myContext = context;
		MySQLiteHelper();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		/*
		 * Log.w(MySQLiteHelper.class.getName(),
		 * "Upgrading database from version " + oldVersion + " to " + newVersion
		 * + ", which will destroy all old data");
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_WO);
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
		 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP); onCreate(db);
		 */
	}

	public static void resetDB(SQLiteDatabase database) {
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void MySQLiteHelper() throws IOException {

		// Temp hack to always replace device's database with database provided
		// by the app/assets
		/*
		 * this.getWritableDatabase(); try { copyDataBase(); } catch
		 * (IOException e) { throw new Error("Error copying database"); }
		 */

		/*
		 * UNCOMMENT FOLLOWING BLOCK TO ACTIVATE DATABASE CHECK
		 */
		boolean dbExist = checkDataBase();

		if (dbExist) { // do nothing - database already exist

		} else {

			// By calling this method and empty database will be created into //
			// the default system path // of your application so we are gonna be
			// able to overwrite that // database with our database.
			SQLiteDatabase temp = this.getWritableDatabase();
			temp.close();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	public void resetDatabase() {
		Log.d(TAG, "Database Reset");
		SQLiteDatabase temp = this.getWritableDatabase();
		temp.close();

		try {
			copyDataBase();
		} catch (IOException e) {
			throw new Error("Error copying database");
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = DATABASE_PATH + DATABASE_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public SQLiteDatabase openDataBase() throws SQLException {

		// Open the database
		String myPath = DATABASE_PATH + DATABASE_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		return myDataBase;

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

}