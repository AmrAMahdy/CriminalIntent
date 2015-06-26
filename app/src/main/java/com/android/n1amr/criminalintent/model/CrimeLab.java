package com.android.n1amr.criminalintent.model;


import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;

    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mCrimes = new ArrayList<Crime>();
//        for (int i = 0; i < 5; i++) {
//            Crime crime = new Crime();
//            crime.setTitle("Crime #" + i);
//            crime.setSolved(i % 2 == 0);
//            addCrime(crime);
//        }
    }

    public static CrimeLab get(Context c) {
        if (sCrimeLab == null)
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID uuid) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(uuid))
                return crime;
        }
        return null;
    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

}
