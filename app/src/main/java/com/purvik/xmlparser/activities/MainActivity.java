package com.purvik.xmlparser.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.purvik.xmlparser.R;
import com.purvik.xmlparser.appUtilities.XmlValuesModel;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    List<XmlValuesModel> myData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        final TextView output = (TextView) findViewById(R.id.output);
        final Button bparsexml = (Button) findViewById(R.id.bparsexml);

        //Static XML data which we will parse
        final String XMLData = "<?xml version=\"1.0\"?>\n" +
                "<login>\n" +
                "    <status>OK</status>\n" +
                "    <jobs>\n" +
                "        <job>\n" +
                "            <id>4</id>\n" +
                "            <companyid>4</companyid>\n" +
                "            <company>Android Example</company>\n" +
                "            <address>Parse XML Android</address>\n" +
                "            <city>Tokio</city>\n" +
                "            <state>Xml Parsing Tutorial</state>\n" +
                "            <zipcode>601301</zipcode>\n" +
                "            <country>Japan</country>\n" +
                "            <telephone>9287893558</telephone>\n" +
                "            <fax>1234567890</fax>\n" +
                "            <date>2012-03-15 12:00:00</date>\n" +
                "        </job>\n" +
                "        <job>\n" +
                "            <id>5</id>\n" +
                "            <companyid>6</companyid>\n" +
                "            <company>Xml Parsing In Java</company>\n" +
                "            <address>B-22</address>\n" +
                "            <city>Cantabill</city>\n" +
                "            <state>XML Parsing Basics</state>\n" +
                "            <zipcode>201301</zipcode>\n" +
                "            <country>America</country>\n" +
                "            <telephone>9287893558</telephone>\n" +
                "            <fax>1234567890</fax>\n" +
                "            <date>2012-05-18 13:00:00</date>\n" +
                "        </job>\n" +
                "    </jobs>\n" +
                "</login>";

        String dataToBeParsed = "Click on button to parse XML.\n\n XML DATA : \n\n"+XMLData;
        output.setText(dataToBeParsed);

        /****  Button Click Listener ********/
        bparsexml.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {

                try {

                    /************** Read XML *************/

                    BufferedReader br = new BufferedReader(new StringReader(XMLData));
                    InputSource is = new InputSource(br);

                    /************  Parse XML **************/

                    XMLParser parser = new XMLParser();
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    SAXParser sp = factory.newSAXParser();
                    XMLReader reader = sp.getXMLReader();
                    reader.setContentHandler(parser);
                    reader.parse(is);

                    /************* Get Parse data in a ArrayList **********/
                    myData = parser.list;

                    if (myData != null) {

                        String OutputData = "";

                        /*************** Get Data From ArrayList *********/

                        for (XmlValuesModel xmlRowData : myData) {
                            if (xmlRowData != null) {


                                int id = xmlRowData.getid();
                                int companyid = xmlRowData.getcompanyid();
                                String company = xmlRowData.getcompany();
                                String address = xmlRowData.getaddress();
                                String city = xmlRowData.getcity();
                                String state = xmlRowData.getstate();
                                String zipcode = xmlRowData.getzipcode();
                                String country = xmlRowData.getcountry();
                                String telephone = xmlRowData.gettelephone();
                                String date = xmlRowData.getdate();


                                OutputData += "Job Node : \n\n " + company + " | "
                                        + address + " | "
                                        + city + " | "
                                        + state + " | "
                                        + zipcode + " | "
                                        + country + " | "
                                        + telephone + " | "
                                        + date + " \n\n "

                                ;

                            } else
                                Log.e("Jobs", "Jobs value null");
                        }

                        /******** Show Data *************/
                        output.setText(OutputData);
                    }
                } catch (Exception e) {
                    Log.e("Jobs", "Exception parse xml :" + e);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
