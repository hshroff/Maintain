/**
 * 
 */
package com.freyasystems.maintain2.tabs;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.freyasystems.maintain2.R;
import com.freyasystems.maintain2.adapters.ResourceListAdapter;
import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.ResourceVO;
import com.freyasystems.maintain2.dao.TaskVO;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class TaskToolsTabFragment extends Fragment {
	private String TAG = "TaskToolsTabFragment";
	private MaintainDS maintainDS;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        
        View taskToolsTabView = (LinearLayout)inflater.inflate(R.layout.tab_task_tools_layout, container, false);
        
        Bundle b = getArguments();
        final long taskId = b.getLong("TaskID", 1);
		Log.d(TAG, "got taskId of " + taskId);

		maintainDS = new MaintainDS(container.getContext());
		maintainDS.open();

		TaskVO task = maintainDS.getTask(taskId);
		
		
		if (task == null) {
			Log.d(TAG, "No task found with id of " + taskId);
		}

		List<ResourceVO> tools = maintainDS.getResources(task, "TOOLS");
		maintainDS.close();
		
		ResourceListAdapter rAdapter = new ResourceListAdapter(container.getContext(), android.R.layout.simple_list_item_1, tools);
		ListView toolsListView = (ListView) taskToolsTabView.findViewById(R.id.TaskTools_ListView);
		toolsListView.setAdapter(rAdapter);
        
        return taskToolsTabView ;

    }
}
