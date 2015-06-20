package com.android.n1amr.criminalintent.controllers.details;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.android.n1amr.criminalintent.R;
import com.android.n1amr.criminalintent.controllers.details.dialog.DatePickerFragment;
import com.android.n1amr.criminalintent.model.Crime;
import com.android.n1amr.criminalintent.model.CrimeLab;

import java.util.UUID;


public class CrimeFragment extends Fragment {
    public static final String EXTRA_CRIME_ID = "";
    private static final String DIALOG_DATE = "date";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(CrimeFragment.EXTRA_CRIME_ID, crimeId);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);

        return crimeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID) getArguments().getSerializable(CrimeFragment.EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(
                    CharSequence c, int start, int before, int count) {
                mCrime.setTitle(c.toString());
            }

            @Override
            public void beforeTextChanged(
                    CharSequence c, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void afterTextChanged(Editable c) {
                // This one too
            }
        });

        mDateButton = (Button) view.findViewById(R.id.crime_date);
        mDateButton.setText(DateFormat.format("EEEE, MMM d, yyyy", mCrime.getDate()).toString());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getActivity().getFragmentManager(), DIALOG_DATE);
            }
        });
        ;


        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return view;
    }


}
