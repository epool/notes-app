package mx.eduardopool.notes.fragments;

import android.support.v4.app.Fragment;

import mx.eduardopool.notes.activities.BaseActivity;

/**
 * Base fragment.
 * Created by epool on 11/6/15.
 */
public abstract class BaseFragment extends Fragment {

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

}
