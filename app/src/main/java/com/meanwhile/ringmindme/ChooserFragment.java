package com.meanwhile.ringmindme;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnChooserFragmentListener} interface
 * to handle interaction events.
 */
public class ChooserFragment extends Fragment {

    private OnChooserFragmentListener mListener;

    private Switch mInOutSwitch;

    public ChooserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_chooser, container, false);

        mInOutSwitch = (Switch) v.findViewById(R.id.inout_switch);
        mInOutSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListener.onSelectionMade(isChecked);
            }
        });

        mInOutSwitch.setChecked(false);
        mListener.onSelectionMade(false);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChooserFragmentListener) {
            mListener = (OnChooserFragmentListener) context;

        } else {
            throw new RuntimeException(context.toString() + " must implement OnChooserFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnChooserFragmentListener {
        void onSelectionMade(boolean alreadyIn);
    }
}
