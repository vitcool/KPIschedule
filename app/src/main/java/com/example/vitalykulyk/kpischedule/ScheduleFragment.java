package com.example.vitalykulyk.kpischedule;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Created by Vitaly Kulyk on 23.02.2016.
 */
public class ScheduleFragment extends Fragment {


    //Array adapter for
    ArrayAdapter<Lesson> mScheduleAdapter;

    static ListView mListView;

    List<Lesson> testSchedule;

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_QUERY = "query";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static Bundle[] bundleArray;
    public static ScheduleFragment[] fragmentArray;


    static ScheduleTask.LessonAdapter mLessonAdapter;

    static boolean isSearchq;

    public ScheduleFragment() {
    }

    public static ScheduleFragment newInstance(int sectionNumber, String query, boolean isSearch) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        Log.w("NEW INSTANCE FRAGMENT", query.toUpperCase());
        isSearchq = isSearch;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mListView = (ListView) view.findViewById(R.id.listview_forecast);
        ArrayList<Lesson> testSchedule = new ArrayList<>();
        mLessonAdapter = new ScheduleTask.LessonAdapter(getActivity(), R.layout.list_item, testSchedule);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ScheduleTask task = new ScheduleTask();
        Bundle bundle = getArguments();
        task.execute(getArguments().getString(ARG_QUERY), Integer.toString(bundle.getInt(ARG_SECTION_NUMBER)));
        Log.w("ON RESUME", "SHEDULE FRAGMENT");
//        if (isSearchq){
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.detach(this).attach(this).commit();
//        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static class ScheduleTask extends AsyncTask<String, Void, Lesson[]> {


        private final String LOG_CAT = ScheduleTask.class.getSimpleName();

        private Lesson[] getScheduleDataFromJson(String json, String days)
                throws JSONException {

            Gson gson = new Gson();
            Data m = gson.fromJson(json, Data.class);
            //array of lessons
            List<Lesson> lessons = m.getLesson();

            // list of second week schedule
            List<List<Lesson>> secondWeekSchedule = new ArrayList<>();

            // list of first week schedule
            List<List<Lesson>> firstWeekSchedule = new ArrayList<>();

            for (int i = 0; i < lessons.size(); i++) {
                List<Lesson> day = new ArrayList<>();
                day.add(lessons.get(i));
                String bufferDay = lessons.get(i).getDay_number();
                for (int j = i + 1; j < lessons.size(); j++) {
                    if (bufferDay.equals(lessons.get(j).getDay_number())) {
                        day.add(lessons.get(j));
                        i = j;
                    } else break;
                }
                if (day.get(0).getLesson_week().equals("1")) {
                    firstWeekSchedule.add(day);
                } else {
                    secondWeekSchedule.add(day);
                }
            }
            int daysInweek = 5;
            String nameOfFirstDay;
            try {
                nameOfFirstDay = firstWeekSchedule.get(0).get(0).getDay_number();
                if (firstWeekSchedule.size() == 4){
                    daysInweek = 4;
                }
                int daysInt = dayToInt(days, daysInweek);
                Lesson[] array = firstWeekSchedule.get(daysInt).toArray(new Lesson[firstWeekSchedule.get(daysInt).size()]);
                return array;
            }
            catch (Exception e){
                Log.w("getScheduleDataFromJson", e.getMessage());
                return null;
            }
        }

        int dayToInt(String days, int daysInWeek){
            if (daysInWeek == 4){
                switch (days) {
                    case "2": {
                        return 0;
                    }
                    case "3": {
                        return 1;
                    }
                    case "4": {
                        return 2;
                    }
                    case "5": {
                        return 3;
                    }
                }
            }
            else {
                switch (days) {
                    case "1": {
                        return 0;
                    }
                    case "2": {
                        return 1;
                    }
                    case "3": {
                        return 2;
                    }
                    case "4": {
                        return 3;
                    }
                    case "5": {
                        return 4;
                    }
                }
            }
            return 5;
        }

        @Override
        protected Lesson[] doInBackground(String... params) {
            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String scheduleJsonStr = null;


            try {

                final String FORECAST_BASE_URL = "http://api.rozklad.org.ua/v2/groups/";
                //http://api.rozklad.org.ua/v2/groups/ia-23/lessons?filter={'day_number':3,'lesson_week':1}
                final String LESSONS = "lessons";
                final String FILTERS = "?filter={'day_number':4,'lesson_week':2}";


                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendEncodedPath(params[0])
                        .appendEncodedPath(LESSONS)
                        .build();
                // .appendEncodedPath(FILTER_MUCH).build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_CAT, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                scheduleJsonStr = buffer.toString();
                Log.v(LOG_CAT, "Schedule JSON string - " + scheduleJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                        e.printStackTrace();
                    }
                }
            }
            try {
                return getScheduleDataFromJson(scheduleJsonStr, params[1]);
            } catch (JSONException e) {
                Log.w(LOG_CAT, e.getMessage(), e);
                e.printStackTrace();
            }
            catch (Exception e){
                Log.w("EXECUTE ERROR", e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }



        // method wich make parsing JSON string
        protected void onPostExecute(Lesson[] result) {
            if (result != null) {
                mListView.setAdapter(mLessonAdapter);
                mLessonAdapter.clear();
                mLessonAdapter.addAll(result);
            }

        }



        // Adapter for ListView
        public static class LessonAdapter extends ArrayAdapter<Lesson> {

            ArrayList<Lesson> lesons;


            public LessonAdapter(Context context, int textViewResourceId, ArrayList<Lesson> objects) {
                super(context, textViewResourceId, objects);
                this.lesons = objects;
            }

            public View getView(int position, View convertView, ViewGroup parent) {

                // assign the view we are converting to a local variable
                View v = convertView;

                // first check to see if the view is null. if so, we have to inflate it.
                // to inflate it basically means to render, or show, the view.
                if (v == null) {
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inflater.inflate(R.layout.list_item, null);
                }

		/*
		 * Recall that the variable position is sent in as an argument to this method.
		 * The variable simply refers to the position of the current object in the list. (The ArrayAdapter
		 * iterates through the list we sent it)
		 *
		 * Therefore, i refers to the current Item object.
		 */
                Lesson i = lesons.get(position);

                if (i != null) {

                    // This is how you obtain a reference to the TextViews.
                    // These TextViews are created in the XML files we defined.

                    TextView number_of_lesson_value = (TextView) v.findViewById(R.id.number_of_lesson_value);
                    TextView room_value = (TextView) v.findViewById(R.id.room_value);
                    TextView name_of_subject_value = (TextView) v.findViewById(R.id.name_of_subject_value);
                    TextView teacher_name_value = (TextView) v.findViewById(R.id.teacher_name_value);
                    TextView lesson_type_value = (TextView) v.findViewById(R.id.lesson_type_value);


                    // check to see if each individual textview is null.
                    // if not, assign some text!
                    if (number_of_lesson_value != null) {
                        number_of_lesson_value.setText(i.getLesson_number() + ". ");
                    }
                    if (room_value != null) {
                        room_value.setText(" " + i.getLesson_room());
                    }
                    if (name_of_subject_value != null) {
                        name_of_subject_value.setText(i.getLesson_name());
                    }
                    if (teacher_name_value != null) {
                        teacher_name_value.setText(" " + i.getTeacher_name());
                    }
                    if (lesson_type_value != null) {
                        lesson_type_value.setText(i.getLesson_type());
                    }
                }

                // the view must be returned to our activity
                return v;

            }

        }
    }
}




