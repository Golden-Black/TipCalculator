package com.example.tipcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText cash_totalAmount;
    private TextView cash_tipAmount;
    private TextView cash_totalWithTip;
    private EditText peopleCount;
    private TextView cash_totalPerPerson;
    private TextView overage;
    double tipRatio = 0;

    double totalWithTip_globe = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cash_totalAmount = findViewById(R.id.cash_totalAmount);
        cash_tipAmount = findViewById(R.id.cash_tipAmount);
        cash_totalWithTip = findViewById(R.id.cash_toalWithTip);
        peopleCount = findViewById(R.id.peopleCount);
        cash_totalPerPerson = findViewById(R.id.cash_totalPerPerson);
        overage = findViewById(R.id.cash_Overage);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void tipChoices(View v){
        double totalAmount = 0;
        double tipAmount = 0;
        double totalWithTip = 0;

        if(v.getId() == R.id.twelve_percent){
            tipRatio = 0.12;
        }else if(v.getId() == R.id.fifthteen_percent){
            tipRatio = 0.15;
        }else if(v.getId() == R.id.eighteen_percent){
            tipRatio = 0.18;
        }else if(v.getId() == R.id.twenty_percent){
            tipRatio = 0.20;
        }

        String text = cash_totalAmount.getText().toString();

        if(!text.trim().isEmpty()) {
            totalAmount = Double.parseDouble(cash_totalAmount.getEditableText().toString());
            if(totalAmount != 0) {
                tipAmount = totalAmount * tipRatio;
                totalWithTip = tipAmount + totalAmount;

                String tipAmount_str = String.format("%.2f", tipAmount);
                cash_tipAmount.setText("$" + tipAmount_str);

                String totalWithTip_str = String.format("%.2f", totalWithTip);
                cash_totalWithTip.setText("$" + totalWithTip_str);

                totalWithTip_globe = totalWithTip;
            }
        }else{
            RadioGroup tipButtons = findViewById(R.id.radioGroup);
            tipButtons.clearCheck();
        }
    }

    @SuppressLint("SetTextI18n")
    public void go(View v){

        String people = peopleCount.getText().toString();

        if(!people.trim().isEmpty()){
            int numberOfPeople = Integer.parseInt(peopleCount.getEditableText().toString());

            double totalPerPerson = totalWithTip_globe / numberOfPeople;
            // truncate to 2 decimals
            @SuppressLint("DefaultLocale") String totalPerPerson_str = String.format("%.2f", totalPerPerson);
            // round the number
            // reference: https://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java
            DecimalFormat df = new DecimalFormat("#.00");
            String totalPerPerson_round = df.format(Double.parseDouble(totalPerPerson_str));

            cash_totalPerPerson.setText("$" + totalPerPerson_round);

            double totalPerPerson_round_d = Double.parseDouble(totalPerPerson_round);
            BigDecimal overage_d =  BigDecimal.valueOf(totalPerPerson_round_d * numberOfPeople).subtract(BigDecimal.valueOf(totalWithTip_globe));

            @SuppressLint("DefaultLocale") String overage_round = String.format("%.2f", overage_d);

            overage.setText("$" + overage_round);
            // Not sure if the overage needs to be rounded
            // The one in use is rounded to the closet two decimal points
            // The version without rounding will be without line 103 && replace line 105 with:
            // overage.setText("$" + overage_d);
        }
    }


    public void clear(View v){
        cash_totalAmount.setText("");
        cash_tipAmount.setText("");
        cash_totalWithTip.setText("");
        cash_totalPerPerson.setText("");
        peopleCount.setText("");
        overage.setText("");

        RadioGroup rg = findViewById(R.id.radioGroup);
        rg.clearCheck();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("TipAmount", cash_tipAmount.getText().toString());
        outState.putString("TotalWithTip", cash_totalWithTip.getText().toString());
        outState.putString("TotalPerPerson", cash_totalPerPerson.getText().toString());
        outState.putString("Overage", overage.getText().toString());
        outState.putString("PeopleCount", peopleCount.getText().toString());
        outState.putDouble("Ratio", tipRatio);
        outState.putDouble("TotalWithTip_g", totalWithTip_globe);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        cash_tipAmount.setText(savedInstanceState.getString("TipAmount"));
        cash_totalWithTip.setText(savedInstanceState.getString("TotalWithTip"));
        cash_totalPerPerson.setText(savedInstanceState.getString("TotalPerPerson"));
        overage.setText(savedInstanceState.getString("Overage"));
        peopleCount.setText(savedInstanceState.getString("PeopleCount"));
        totalWithTip_globe = savedInstanceState.getDouble("TotalWithTip_g");
        tipRatio = savedInstanceState.getDouble("Ratio");

    }
}