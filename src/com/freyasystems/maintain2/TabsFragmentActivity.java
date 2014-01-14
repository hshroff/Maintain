package com.freyasystems.maintain2;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

import com.freyasystems.maintain2.tabs.TaskDetailTabFragment;
import com.freyasystems.maintain2.tabs.TaskPartsTabFragment;
import com.freyasystems.maintain2.tabs.TaskPeopleTabFragment;
import com.freyasystems.maintain2.tabs.TaskToolsTabFragment;
import com.freyasystems.maintain2.tabs.TaskWorkDoneTabFragment;

/**
 * @author mwho
 * 
 */
public class TabsFragmentActivity extends FragmentActivity implements
		TabHost.OnTabChangeListener {

	private String TAG = "TabsFragmentActivity";
	private TabHost mTabHost;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabsFragmentActivity.TabInfo>();
	private TabInfo mLastTab = null;

	/**
	 * 
	 * @author mwho
	 * 
	 */
	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}

	}

	/**
	 * 
	 * @author mwho
	 * 
	 */
	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
		 */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_tabs_layout);

		Bundle b = getIntent().getExtras();

		long taskId = b.getLong("TaskID", 1);
		Log.d(TAG, "got taskId of " + taskId);

		if (savedInstanceState == null) {
			savedInstanceState = new Bundle();
		}
		savedInstanceState.putLong("TaskID", taskId);
		// setContentView(R.layout.activity_tasks);

		// Show the Up button in the action bar.
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		/*
		 * View tabView = findViewById(R.id.taskdetail_tabs);
		 * setContentView(tabView);
		 */
		initialiseTabHost(savedInstanceState);
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); // set
																				// the
																				// tab
																				// as
																				// per
																				// the
																				// saved
																				// state
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
																// selected
		super.onSaveInstanceState(outState);
	}

	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("Tab1").setIndicator("Detail"),
				(tabInfo = new TabInfo("Tab1", TaskDetailTabFragment.class,
						args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		TabsFragmentActivity.addTab(this, this.mTabHost, this.mTabHost
				.newTabSpec("Tab2").setIndicator("People"),
				(tabInfo = new TabInfo("Tab2", TaskPeopleTabFragment.class,
						args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		TabsFragmentActivity
				.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3")
						.setIndicator("Parts"), (tabInfo = new TabInfo("Tab3",
						TaskPartsTabFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		TabsFragmentActivity
				.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab4")
						.setIndicator("Tools"), (tabInfo = new TabInfo("Tab4",
						TaskToolsTabFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		TabsFragmentActivity
		.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab5")
				.setIndicator("Work Done"), (tabInfo = new TabInfo("Tab5",
				TaskWorkDoneTabFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		
		// Default to first tab
		this.onTabChanged("Tab1");
		//
		mTabHost.setOnTabChangedListener(this);
	}

	/**
	 * @param activity
	 * @param tabHost
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	private static void addTab(TabsFragmentActivity activity, TabHost tabHost,
			TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(activity.new TabFactory(activity));
		String tag = tabSpec.getTag();

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state. If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		tabInfo.fragment = activity.getSupportFragmentManager()
				.findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction ft = activity.getSupportFragmentManager()
					.beginTransaction();
			ft.detach(tabInfo.fragment);
			ft.commit();
			activity.getSupportFragmentManager().executePendingTransactions();
		}

		tabHost.addTab(tabSpec);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	public void onTabChanged(String tag) {
		TabInfo newTab = this.mapTabInfo.get(tag);
		if (mLastTab != newTab) {
			FragmentTransaction ft = this.getSupportFragmentManager()
					.beginTransaction();
			if (mLastTab != null) {
				if (mLastTab.fragment != null) {
					ft.detach(mLastTab.fragment);
				}
			}
			if (newTab != null) {
				if (newTab.fragment == null) {
					newTab.fragment = Fragment.instantiate(this,
							newTab.clss.getName(), newTab.args);
					ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
				} else {
					ft.attach(newTab.fragment);
				}
			}

			mLastTab = newTab;
			ft.commit();
			this.getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this, TasksActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 */
	public void doPositiveClick() {
		// TODO Auto-generated method stub
		Log.d(TAG, "Not sure whats going on, but Im in doPositiveClick() method");
		onTabChanged("Tab1");
	}
}
