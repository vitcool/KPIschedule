package com.example.vitalykulyk.kpischedule;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Vitaly Kulyk on 25.02.2016.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

    Button search_button;
    EditText search_query;
    FragmentTransaction mFragmentTransaction;
    FragmentManager mFragmentManager;
    boolean isPressed = false;

    String day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        day = getTodayDay();

        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        search_button = (Button) rootView.findViewById(R.id.search_button);
        search_button.setOnClickListener(this);

        search_query = (EditText) rootView.findViewById(R.id.editText);
        mFragmentManager = getFragmentManager();

        search_query.setOnClickListener(new View.OnClickListener() {

            boolean isFirst = true;
            @Override
            public void onClick(View v) {
                if (isFirst) {
                    search_query.setText("");
                    isFirst = false;
                }
            }
        });

        return rootView;

    }

    @Override
    public void onClick(View v) {
        ScheduleFragment.ScheduleTask scheduleTask = new ScheduleFragment.ScheduleTask();
        String query = String.valueOf(search_query.getText());
        scheduleTask.execute(query, day);
        if (!isPressed) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
            if (v.getId() == R.id.search_button) {
//                ScheduleFragment schFragment = new ScheduleFragment();
//                mFragmentTransaction.add(R.id.schedule_fragment, schFragment);
//                mFragmentTransaction.commit();
            }
            isPressed = true;
        }
    }

    public String getTodayDay(){
        Calendar calendar = Calendar.getInstance();
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
//                case (Calendar.MONDAY) : {
//                    return 1;
//                }
            case (Calendar.TUESDAY) : {
                return "Tuersday";
            }
            case (Calendar.WEDNESDAY) : {
                return "WEDNESDAY";
            }
            case (Calendar.THURSDAY) : {
                return "THURSDAY";
            }
            case (Calendar.FRIDAY) :{
                return "FRIDAY";
            }
            default:{
                return "Tuersday";
            }
        }
    }

}
