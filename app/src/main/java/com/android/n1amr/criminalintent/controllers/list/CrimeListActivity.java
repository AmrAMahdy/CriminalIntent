package com.android.n1amr.criminalintent.controllers.list;

import android.app.Fragment;

import com.android.n1amr.criminalintent.controllers.SingleFragmentActivity;

/**
 * Created by N1amr on 2015/06/19.
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}