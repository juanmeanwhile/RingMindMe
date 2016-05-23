package com.meanwhile.ringmindme;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.meanwhile.ringmindme.ui.CalendarView;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

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


    private static final String ARG_RING_INSIDE = "RingInside";
    private OnDateTimeFragmentListener mListener;
    private CalendarView mCalendarView;
    private Button mTimeButton;
    private Calendar mSelectedCal;
    private TextView mQuestion;
    private SimpleDateFormat mDf;
    private boolean mRingInside;

    private TimePickerDialog.OnTimeSetListener mTimeChangeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, second);

            mListener.onDateTimeSelected(cal.getTime());
            
            mTimeButton.setText(mDf.format(cal.getTime()));
        }
    };

    public DateTimeChooserFragment() {
    }

    public static DateTimeChooserFragment newInstance(boolean ringIn) {
        DateTimeChooserFragment fragment = new DateTimeChooserFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_RING_INSIDE, ringIn);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRingInside = getArguments().getBoolean(ARG_RING_INSIDE);
        }

        mDf = new SimpleDateFormat("HH:mm");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_date_time_chooser, container, false);

        mSelectedCal = Calendar.getInstance();

        mQuestion = (TextView) v.findViewById(R.id.question);
        mQuestion.setText(mRingInside?R.string.datetime_picker_ring_put: R.string.datetime_picker_ring_take_out);

        mTimeButton = (Button) v.findViewById(R.id.time_button);
        mTimeButton.setText(mDf.format(mSelectedCal.getTime()));
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog dpd = TimePickerDialog.newInstance(
                        mTimeChangeListener,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.SECOND),
                        true
                );
                dpd.show(getActivity().getFragmentManager(), "TimePickerDialog");
            }
        });

        mCalendarView = (CalendarView) v.findViewById(R.id.cal);
        mCalendarView.getCalendarView().setCurrentDate(mSelectedCal.getTime());
        mCalendarView.getCalendarView().setCurrentSelectedDayBackgroundColor(Color.WHITE);
        mCalendarView.getCalendarView().setCurrentDayBackgroundColor(getResources().getColor(R.color.intro_card_bk));
        mCalendarView.setListener(new CalendarView.OnDaySelectedListener() {
            @Override
            public void onDaySelected(Date dateClicked) {
                int min = mSelectedCal.get(Calendar.MINUTE);
                int hour = mSelectedCal.get(Calendar.HOUR);

                mSelectedCal.setTime(dateClicked);
                mSelectedCal.set(Calendar.MINUTE, min);
                mSelectedCal.set(Calendar.HOUR, hour);

                mListener.onDateTimeSelected(mSelectedCal.getTime());
            }
        });

        return v;
    }

    public void updateRingQuestion(boolean inside) {
        mQuestion.setText(inside?R.string.datetime_picker_ring_put: R.string.datetime_picker_ring_take_out);
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
