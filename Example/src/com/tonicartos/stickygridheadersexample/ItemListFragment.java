/*
 Copyright 2013 Tonic Artos

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.tonicartos.stickygridheadersexample;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleArrayAdapter;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 *
 * @author Tonic Artos
 */
public class ItemListFragment extends SherlockFragment implements OnItemClickListener {
    private static final String KEY_LIST_POSITION = "key_list_position";

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(int id) {
        }
    };

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private int mFirstVisible;
    private GridView mGridView;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    private Menu mMenu;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_grid, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onItemClick(AdapterView<?> gridView, View view, int position, long id) {
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGridView = (GridView) view.findViewById(R.id.asset_grid);
        mGridView.setOnItemClickListener(this);
        mGridView.setColumnWidth((int) calculatePixelsFromDips(100));
        mGridView.setNumColumns(-1);
        mGridView.setAdapter(new StickyGridHeadersSimpleArrayAdapter<String>(getActivity().getApplicationContext(), getResources().getStringArray(R.array.countries), R.layout.header, R.layout.item));

        if (savedInstanceState != null) {
            mFirstVisible = savedInstanceState.getInt(KEY_LIST_POSITION);
        }

        mGridView.setSelection(mFirstVisible);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }

        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_toggle_sticky:
            item.setChecked(!item.isChecked());
            ((StickyGridHeadersGridView) mGridView).setAreHeadersSticky(!((StickyGridHeadersGridView) mGridView).areHeadersSticky());

            return true;
        case R.id.menu_use_list_adapter:
            mGridView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item, getResources().getStringArray(R.array.countries)));
            mMenu.findItem(R.id.menu_use_list_adapter).setVisible(false);
            mMenu.findItem(R.id.menu_use_sticky_adapter).setVisible(true);
            mMenu.findItem(R.id.menu_toggle_sticky).setVisible(false);
            return true;
        case R.id.menu_use_sticky_adapter:
            mGridView.setAdapter(new StickyGridHeadersSimpleArrayAdapter<String>(getActivity().getApplicationContext(), getResources().getStringArray(R.array.countries), R.layout.header, R.layout.item));
            mMenu.findItem(R.id.menu_use_list_adapter).setVisible(true);
            mMenu.findItem(R.id.menu_toggle_sticky).setVisible(true);
            mMenu.findItem(R.id.menu_use_sticky_adapter).setVisible(false);
            return true;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_item_list, menu);
        mMenu = menu;
        menu.findItem(R.id.menu_toggle_sticky)
            .setChecked(((StickyGridHeadersGridView) mGridView).areHeadersSticky());
    }

    private float calculatePixelsFromDips(float dips) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dips, getResources().getDisplayMetrics());
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mGridView.setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
        }
    }

    @SuppressLint("NewApi")
    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            mGridView.setItemChecked(mActivatedPosition, false);
        } else {
            mGridView.setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(int position);
    }
}
