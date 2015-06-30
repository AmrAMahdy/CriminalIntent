package com.android.n1amr.criminalintent.controllers.list;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.android.n1amr.criminalintent.R;
import com.android.n1amr.criminalintent.controllers.details.CrimeFragment;
import com.android.n1amr.criminalintent.controllers.details.CrimePagerActivity;
import com.android.n1amr.criminalintent.model.Crime;
import com.android.n1amr.criminalintent.model.CrimeLab;

import java.util.ArrayList;

public class CrimeListFragment extends Fragment {
    private static final String TAG = "CrimeListFragment";

    private ArrayList<Crime> mCrimes;
    private boolean mSubtitleVisible;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);

        setRetainInstance(true);
        mSubtitleVisible = false;

        setHasOptionsMenu(true);

        mCrimes = CrimeLab.get(getActivity()).getCrimes();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mListView = (ListView) view.findViewById(R.id.listView);

        //  ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1, mCrimes);
        CrimeAdapter adapter = new CrimeAdapter(mCrimes);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Crime crime = ((CrimeAdapter) mListView.getAdapter()).getItem(position);
                Log.d(TAG, crime.getTitle() + " was pressed");

                Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
                intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());

                startActivity(intent);
            }
        });

        //Old style
        mListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
            }

        });

        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                //((Crime) (mListView.getAdapter().getItem(position))).setSolved(checked);
                CrimeAdapter crimeAdapter = (CrimeAdapter) mListView.getAdapter();
                crimeAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.crime_list_item_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_delete_crime:
                        CrimeAdapter crimeAdapter = ((CrimeAdapter) mListView.getAdapter());
                        CrimeLab crimeLab = CrimeLab.get(getActivity());
                        for (int i = crimeAdapter.getCount() - 1; i >= 0; i--) {
                            if (mListView.isItemChecked(i)) {
                                crimeLab.deleteCrime(crimeAdapter.getItem(i));
                            }
                        }
                        mode.finish();
                        crimeAdapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        //Empty View
        mListView.setEmptyView(view.findViewById(R.id.emptyView));
        Button newCrimeButton = (Button) view.findViewById(R.id.emptyView_add_Button);
        newCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewCrimeFragment();
            }
        });

        if (mSubtitleVisible) {
            getActivity().getActionBar().setSubtitle(R.string.subtitle);
        }

        return view;
    }


    private class CrimeAdapter extends ArrayAdapter<Crime> {
        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_crime, null);
            }

            // Configure the view for this Crime
            Crime c = getItem(position);

            TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            titleTextView.setText(c.getTitle());

            TextView dateTextView =
                    (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getDate().toString());

            CheckBox solvedCheckBox =
                    (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.isSolved());

            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ((CrimeAdapter) mListView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible && showSubtitle != null)
            showSubtitle.setTitle(R.string.hide_subtitle);
    }

    private void openNewCrimeFragment() {
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);

        Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                openNewCrimeFragment();
                return true;
            case R.id.menu_item_show_subtitle:
                if (getActivity().getActionBar().getSubtitle() == null) {
                    getActivity().getActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible = true;
                    item.setTitle(R.string.hide_subtitle);
                } else {
                    getActivity().getActionBar().setSubtitle(null);
                    mSubtitleVisible = false;
                    item.setTitle(R.string.show_subtitle);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //Old style
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        CrimeAdapter adapter = (CrimeAdapter) (mListView.getAdapter());
        Crime crime = adapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(crime);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
