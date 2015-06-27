package com.android.n1amr.criminalintent.controllers.list;

import android.app.Fragment;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.android.n1amr.criminalintent.CriminalIntentJSONSerializer;
import com.android.n1amr.criminalintent.controllers.SingleFragmentActivity;
import com.android.n1amr.criminalintent.model.CrimeLab;

/**
 * Created by N1amr on 2015/06/19.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}