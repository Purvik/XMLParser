package com.purvik.xmlparser.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.purvik.xmlparser.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Purvik Rana on 16-03-2016.
 *
 */
public class NewActivity extends AppCompatActivity{

    private static final String TAG = "New Activity" ;
    Spinner semSpinner, enrollNoSpinner;
    JSONObject enrollNo = null;
    TextView resultDAta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get instance
        resultDAta = (TextView)findViewById(R.id.resultData);
        enrollNoSpinner = (Spinner) findViewById(R.id.enrollNoSpinner);

        // get the spinner
        semSpinner = (Spinner) findViewById(R.id.semSpinner);


/*  Load the Semester List from the Resource and Set the Adapter and Selection of type Single Choice Selection

        // Create ArrayAdapter of enrollNoList
        ArrayAdapter<String> enrollNoAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, R.array.semList);

        // Set DisplayDropDown as Single Choice Selection
        enrollNoAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
*/


        semSpinner.setSelection(0, false);

        // set the Item Selection Listener
        semSpinner.post(new Runnable() {
            @Override
            public void run() {
                semSpinner.setOnItemSelectedListener(new semSpippnerItemListener());
            }
        });
    }


    public class semSpippnerItemListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            semSpinner.setSelected(true);
            // Disable EnrollNoSpinner Item Selection Listener
            enrollNoSpinner.setOnItemSelectedListener(null);

            // on Semester Change Clear Displayed Result Content
            resultDAta.setText(" ");

            // Get Selected Semester Item String
            String selectedSem = semSpinner.getSelectedItem().toString();
//            Toast.makeText(getApplicationContext(), "Position >>" + position + "\tItem>>" + selectedSem, Toast.LENGTH_LONG).show();

            // Method to get list of Enrollment Number
            parseSelectedSemData(selectedSem);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

            //When no item selected

        }
    }


    /*
    * Method that will Parse Enrollment Number on Semester Selection
    * @para enrollNoList - contain list of enrollNo (parsed Enrollment Number)
    * @para jsonData - JSONString of all the semester data
    * @para loadJSONFromAssets() - method to fetch string of JSON from raw resources
    * */
    private void parseSelectedSemData(String selectedSem) {
        try {

            ArrayList<String> enrollNoList;
            JSONObject jsonData = new JSONObject(loadJSONFromAssets());

            //  Log.d(TAG, "parseSelectedSemData: >> JSONObject >>" + jsonData);

            if(jsonData == null){
                // What if jsonData not Found
                Toast.makeText(getApplicationContext(),"No Data Found Try Again",Toast.LENGTH_SHORT).show();
            }else {

                // Log.d(TAG, "parseSelectedSemData: jSONObject>>" + enrollNo);

                // get enrollNo node from the JSON string
                enrollNo = jsonData.getJSONObject(selectedSem).getJSONObject("enrollNo");
                if (enrollNo == null) {

                    //What if enrollNo node not found
                    Toast.makeText(getApplicationContext(), "No Enrollment No Found. Try Again", Toast.LENGTH_SHORT).show();

                } else {

                    // If enrollNo Node Found parse the List of Enrollnment Numbers
                    enrollNoList = new ArrayList<>();

                    // Fetch the Enrollment Numbers for enrollNoSpinner
                    Iterator keys = enrollNo.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        enrollNoList.add(key);
                    }

                    // Create ArrayAdapter of enrollNoList
                    ArrayAdapter<String> enrollNoAdapter = new ArrayAdapter<>(this,
                            android.R.layout.simple_spinner_dropdown_item, enrollNoList);

                    // Set DisplayDropDown as Single Choice Selection
                    enrollNoAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

                    enrollNoSpinner.setAdapter(enrollNoAdapter);
                    enrollNoSpinner.setSelection(0,false);          // set first selection Disable
                    enrollNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            try {

                                enrollNoSpinner.setSelected(true);
                                String selectedEnrollNo = enrollNoSpinner.getSelectedItem().toString();
                                JSONObject selectedNode = enrollNo.getJSONObject(selectedEnrollNo);

                                StringBuilder sb = new StringBuilder();
                                sb.append("Enroll No:\t\t"+selectedEnrollNo+"\n");

                                Iterator keys = selectedNode.keys();
                                while (keys.hasNext()){

                                    String key = keys.next().toString();
                                    sb.append(key + ":\t\t"+selectedNode.getString(key)+"\n");
                                }

                                /*  Display Result As Dialog Window

                                AlertDialog.Builder dialog = new AlertDialog.Builder(getBaseContext());
                                        dialog.setTitle("Result Data:\t" + selectedEnrollNo)
                                        .setMessage(sb.toString())
                                        .setMessage(sb.toString())
                                        .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                dialog.show();*/

                                resultDAta.setText(sb.toString());

//                                Toast.makeText(getApplicationContext(),"Enroll" + selectedEnrollNo + "Data: >>"+sb.toString(),Toast.LENGTH_LONG).show();
                                Log.d(TAG, "onItemClick: >> Enroll" + selectedEnrollNo + "Data: >>" + sb.toString());



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromAssets() {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = getResources().openRawResource(R.raw.temp_data); //open("animals.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
