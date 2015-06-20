package com.android.n1amr.criminalintent.controllers.details.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.android.n1amr.criminalintent.R;

public class DatePickerFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_title).setPositiveButton(android.R.string.ok, null).setNegativeButton(android.R.string.cancel,
                null).create();
    }
}
