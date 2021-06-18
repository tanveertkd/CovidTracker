package com.covidassam

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    val LOG_TAG = MainActivity::class.java.name;
    lateinit var currentTally: TextView
    lateinit var selectedStateAbb: String
    lateinit var selectedTotalCases: TextView
    lateinit var selectedTotalActive: TextView
    lateinit var selectedTotalRecovered: TextView
    lateinit var selectedTotalDeceased: TextView
    lateinit var selectedTotalTested: TextView
    lateinit var selectedOther: String

    private val selectedStateName: TextView
    get() = findViewById(R.id.stateName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.black)
        }

        currentTally = findViewById(R.id.actualDailyTotalView)
        selectedTotalCases = findViewById(R.id.stateTotalCount)
        selectedTotalActive = findViewById(R.id.stateTotalActiveCount)
        selectedTotalRecovered = findViewById(R.id.stateTotalRecoveredCount)
        selectedTotalDeceased = findViewById(R.id.stateTotalDeceasedCount)
        selectedTotalTested = findViewById(R.id.stateTotalTestedCount)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val today = LocalDateTime.now()
//            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSS")
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            val formatted = today.format(formatter)
            val currentTimeView: TextView = findViewById(R.id.actualDateView)
            currentTimeView.text = formatted.toString()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val stateSpinner: Spinner = findViewById(R.id.stateSpinner)
        stateSpinner.onItemSelectedListener = this
        ArrayAdapter.createFromResource(this, R.array.states_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                stateSpinner.adapter = adapter
            }

//        getCurrentCasesVolley()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val selectedState: String = parent?.getItemAtPosition(position).toString()
        Log.d(LOG_TAG, "onItemSelected: $selectedState")
        selectedStateName.text = selectedState

        when(selectedState){
            "Andaman and Nicobar Islands" -> selectedStateAbb = "AN"
            "Andhra Pradesh" -> selectedStateAbb = "AP"
            "Arunachal Pradesh" -> selectedStateAbb = "AR"
            "Assam" -> selectedStateAbb = "AS"
            "Bihar" -> selectedStateAbb = "BR"
            "Chandigarh" -> selectedStateAbb = "CH"
            "Chattisgarh" -> selectedStateAbb = "CT"
            "Delhi" -> selectedStateAbb = "DL"
            "Dadra and Nagar Haveli and Daman and Diu" -> selectedStateAbb = "DN"
            "Goa" -> selectedStateAbb = "GA"
            "Gujarat" -> selectedStateAbb = "GJ"
            "Haryana" -> selectedStateAbb = "HR"
            "Himachal Pradesh" -> selectedStateAbb = "HP"
            "Jammu and Kashmir" -> selectedStateAbb = "JK"
            "Jharkhand" -> selectedStateAbb = "JH"
            "Karnataka" -> selectedStateAbb = "KA"
            "Kerala" -> selectedStateAbb = "KL"
            "Ladakh" -> selectedStateAbb = "LA"
            "Lakshadweep" -> selectedStateAbb = "LD"
            "Madhya Pradesh" -> selectedStateAbb = "MP"
            "Maharashtra" -> selectedStateAbb = "MH"
            "Manipur" -> selectedStateAbb = "MN"
            "Meghalaya" -> selectedStateAbb = "ML"
            "Mizoram" -> selectedStateAbb = "MZ"
            "Nagaland" -> selectedStateAbb = "NL"
            "Odisha" -> selectedStateAbb = "OR"
            "Puducherry" -> selectedStateAbb = "PY"
            "Punjab" -> selectedStateAbb = "PB"
            "Rajasthan" -> selectedStateAbb = "RJ"
            "Sikkim" -> selectedStateAbb = "SK"
            "Tamil Nadu" -> selectedStateAbb = "TN"
            "Telangana" -> selectedStateAbb = "TT"
            "Tripura" -> selectedStateAbb = "TR"
            "Uttar Pradesh" -> selectedStateAbb = "UP"
            "Uttarakhand" -> selectedStateAbb = "UT"
            "West Bengal" -> selectedStateAbb = "WB"
        }
        getStateWiseCurrent(selectedStateAbb)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Log.d(LOG_TAG, "onNothingSelected: Defaulting to Assam")
        selectedStateAbb = "AS"
    }

    private fun getStateWiseCurrent(stateAbb: String){
        Log.d(LOG_TAG, "getStateWiseCurrent: $selectedStateAbb")

        val URL = "https://api.covid19india.org/v4/min/data.min.json"
        val queue = Volley.newRequestQueue(this)

        val jsonObjectRequestStateWise = JsonObjectRequest(Request.Method.GET, URL, null, Response.Listener{ response ->
            try {
                val currentState: String = stateAbb
                val state = response.getJSONObject(currentState)
                if(state.has("delta")){
                    val delta = state.getJSONObject("delta")
                    val current = delta.getString("confirmed")
                    currentTally.text = current.toString()
//                    if (delta.has("other")){
//                        val other = delta.getString("other")
//                        val stateTotal = state.getJSONObject("total")
//                        val stateConfirmed = stateTotal.getString("confirmed")
//                        val stateDeceased = stateTotal.getString("deceased")
//                        val stateRecovered = stateTotal.getString("recovered")
//
//                        if(!other.equals(null)){
//                            val stateActive = stateConfirmed.toInt()-stateRecovered.toInt()-stateDeceased.toInt()-other.toInt()
//                            selectedTotalActive.text = stateActive.toString()
//                        } else {
//                            val stateActive = stateConfirmed.toInt()-stateRecovered.toInt()-stateDeceased.toInt()
//                            selectedTotalActive.text = stateActive.toString()
//                        }
//                    }
                } else {
                    currentTally.text = getString(R.string.not_updated_yet)
                }

//                val other:  String
//                if (delta.has("other")){
//                   other = delta.getString("other")
//                }



                val stateTotal = state.getJSONObject("total")
                val stateConfirmed = stateTotal.getString("confirmed")
                val stateDeceased = stateTotal.getString("deceased")
                val stateRecovered = stateTotal.getString("recovered")
                val stateTested = stateTotal.getString("tested")
                if(stateTotal.has("other")){
                    val stateOther = stateTotal.getString("other")
                    val stateActive = stateConfirmed.toInt()-stateRecovered.toInt()-stateDeceased.toInt()-stateOther.toInt()
                    selectedTotalActive.text = stateActive.toString()
                } else {
                    val stateActive = stateConfirmed.toInt()-stateRecovered.toInt()-stateDeceased.toInt()
                    selectedTotalActive.text = stateActive.toString()
                }

//                val stateActive = stateConfirmed.toInt()-stateRecovered.toInt()-stateDeceased.toInt()-other.toInt()

//                if(!other.equals(null)){
//                    val stateActive = stateConfirmed.toInt()-stateRecovered.toInt()-stateDeceased.toInt()-other.toInt()
//                    selectedTotalActive.text = stateActive.toString()
//                } else {
//                    val stateActive = stateConfirmed.toInt()-stateRecovered.toInt()-stateDeceased.toInt()
//                    selectedTotalActive.text = stateActive.toString()
//                }

                Log.d(LOG_TAG, "getStateWiseCurrent: $stateConfirmed")
                selectedTotalCases.text = stateConfirmed.toString()
//                selectedTotalActive.text = stateActive.toString()
                selectedTotalRecovered.text = stateRecovered.toString()
                selectedTotalDeceased.text = stateDeceased.toString()
                selectedTotalTested.text = stateTested.toString()

            }catch (e: JSONException){
                currentTally.text = getString(R.string.not_updated_yet)
                e.printStackTrace()
            }
        },{ error ->
            error.printStackTrace()
        })
        queue.add(jsonObjectRequestStateWise)
    }

//    private fun getCurrentCasesVolley() {
//
//        val url = "https://api.covid19india.org/v4/min/data.min.json"
//        val queue = Volley.newRequestQueue(this)
//
//        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener{ response ->
//            try{
//                val state = response.getJSONObject("AS")
//                val delta = state.getJSONObject("delta")
//                val current = delta.getString("confirmed")
//                Log.d(LOG_TAG, "getCurrentCasesVolley: $current")
//                currentTally.text = current.toString()
//
////                for(i in 0 until state.length()){
////                    val delta = state.getJSONObject("delta")
////                    val current = delta.getString("confirmed")
////                    Log.d(LOG_TAG, "getCurrentCasesVolley: $state")
////                }
//            }catch (e: JSONException){
//                e.printStackTrace()
//            }
//
//            },
//            { error ->
//                error.printStackTrace()
//            }
//        )
//
//        queue.add(jsonObjectRequest)
//    }
}