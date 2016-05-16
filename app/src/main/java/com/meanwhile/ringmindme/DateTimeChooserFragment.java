package com.meanwhile.ringmindme;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDateTimeFragmentListener} interface
 * to handle interaction events.
 * Use the {@link DateTimeChooserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DateTimeChooserFragment extends Fragment {


    private OnDateTimeFragmentListener mListener;
    private CompactCalendarView mCalendarView;
    private Button mTimeButton;

    private Calendar mSelectedCal;

    public DateTimeChooserFragment() {
    }

    public static DateTimeChooserFragment newInstance() {
        DateTimeChooserFragment fragment = new DateTimeChooserFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_date_time_chooser, container, false);

        mSelectedCal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");

        mTimeButton = (Button) v.findViewById(R.id.time_button);
        mTimeButton.setText(df.format(mSelectedCal.getTime()));

        mCalendarView = (CompactCalendarView) v.findViewById(R.id.cal);
        mCalendarView.setCurrentDate(mSelectedCal.getTime());

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDateTimeFragmentListener) {
            mListener = (OnDateTimeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnDateTimeFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public Date getSelectedTime(){
        return mSelectedCal.getTime();
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
    public interface OnDateTimeFragmentListener {
        void onDateTimeSelected(Date date);
    }
}
