package com.example.sjeong.pick;

import android.support.v7.app.AlertDialog;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by mijin on 2017-11-11.
 */

public class SearchDetailSavingFragment extends Fragment implements View.OnClickListener {

    protected ArrayList<CharSequence> selectedBank = new ArrayList<CharSequence>();
    protected ArrayList<CharSequence> selectedPrimeCond = new ArrayList<CharSequence>();
    protected ArrayList<CharSequence> selectedContTerm = new ArrayList<CharSequence>();
    protected CharSequence[] bankList, primecondList, conttermList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_detail_saving, container, false);

        RadioGroup join_target = (RadioGroup) view.findViewById(R.id.radioButton);
        //Spinner bank = (Spinner) view.findViewById(R.id.spinner);
        Button bank = (Button) view.findViewById(R.id.spinner);
        CheckBox[] join_way = new CheckBox[]{(CheckBox)view.findViewById(R.id.ch1),(CheckBox)view.findViewById(R.id.ch2), (CheckBox)view.findViewById(R.id.ch3), (CheckBox)view.findViewById(R.id.ch4)};
        //Spinner cont_term = (Spinner) view.findViewById(R.id.spinner3);
        //Spinner prime_cond = (Spinner) view.findViewById(R.id.spinner2);
        Button cont_term = (Button) view.findViewById(R.id.spinner3);
        Button prime_cond = (Button) view.findViewById(R.id.spinner2);
        EditText min_intr = (EditText) view.findViewById(R.id.min_rate);
        CheckBox[] prod_type = new CheckBox[]{(CheckBox)view.findViewById(R.id.freeInput), (CheckBox)view.findViewById(R.id.fixInput)};
        CheckBox[] ori_pay_method = new CheckBox[]{(CheckBox)view.findViewById(R.id.orifull), (CheckBox)view.findViewById(R.id.oriyear)};
        CheckBox[] rat_pay_method = new CheckBox[]{(CheckBox)view.findViewById(R.id.ratfull), (CheckBox)view.findViewById(R.id.ratyear)};
        EditText month_limit = (EditText) view.findViewById(R.id.monthInput);


        bankList = getResources().getStringArray(R.array.bank);
        primecondList = getResources().getStringArray(R.array.primelist);
        conttermList = getResources().getStringArray(R.array.cont_term);
        bank.setOnClickListener(this);
        prime_cond.setOnClickListener(this);
        cont_term.setOnClickListener(this);

        return view;
    }
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.spinner:
                showSelectBankDialog();
                break;

            case R.id.spinner2:
                showSelectPrimeCondDialog();
                break;

            case R.id.spinner3:
                showSelectContTermDialog();
                break;

            default:
                break;
        } // switch
    } // onClick()

    protected void showSelectBankDialog()
    {
        boolean[] checkedbankList = new boolean[bankList.length];
        int count = bankList.length;

        for(int i = 0; i < count; i++)
            checkedbankList[i] = selectedBank.contains(bankList[i]);

        DialogInterface.OnMultiChoiceClickListener bankListDialogListener = new DialogInterface.OnMultiChoiceClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked)
            {
                if(isChecked)
                    selectedBank.add(bankList[which]);
                else
                    selectedBank.remove(bankList[which]);

                // onChangeselectedBank();
            } // onClick()
        }; // coloursDialogListener

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select bankList");

        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // Write your code here to execute after dialog closed
                Toast.makeText(getActivity().getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            } // onClick()
        }); // setNeutralButton()

        builder.setMultiChoiceItems(bankList, checkedbankList, bankListDialogListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    } // showSelectColoursDialog()
/*
    protected void onChangeselectedBank()
    {
        String combineYears = "";

        for(int i = 0; i < selectedBank.size(); i++)
        {
            combineYears += selectedBank.get(i);
            if(i < selectedBank.size()-1)
            {
                Log.e("In else...", ">>>>>>>>>>>>");
                combineYears += ", ";
            } // if
        } // for

        selectbankListButton.setText(combineYears);
    } // onChangeSelectedColours()
    */

    protected void showSelectPrimeCondDialog()
    {
        boolean[] checkedbankList = new boolean[primecondList.length];
        int count = primecondList.length;

        for(int i = 0; i < count; i++)
            checkedbankList[i] = selectedPrimeCond.contains(primecondList[i]);

        DialogInterface.OnMultiChoiceClickListener bankListDialogListener = new DialogInterface.OnMultiChoiceClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked)
            {
                if(isChecked)
                    selectedPrimeCond.add(primecondList[which]);
                else
                    selectedPrimeCond.remove(primecondList[which]);

                // onChangeselectedBank();
            } // onClick()
        }; // coloursDialogListener

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select bankList");

        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // Write your code here to execute after dialog closed
                Toast.makeText(getActivity().getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            } // onClick()
        }); // setNeutralButton()

        builder.setMultiChoiceItems(primecondList, checkedbankList, bankListDialogListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void showSelectContTermDialog()
    {
        boolean[] checkedbankList = new boolean[conttermList.length];
        int count = conttermList.length;

        for(int i = 0; i < count; i++)
            checkedbankList[i] = selectedContTerm.contains(conttermList[i]);

        DialogInterface.OnMultiChoiceClickListener bankListDialogListener = new DialogInterface.OnMultiChoiceClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked)
            {
                if(isChecked)
                    selectedContTerm.add(conttermList[which]);
                else
                    selectedContTerm.remove(conttermList[which]);

                // onChangeselectedBank();
            } // onClick()
        }; // coloursDialogListener

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select bankList");

        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // Write your code here to execute after dialog closed
                Toast.makeText(getActivity().getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            } // onClick()
        }); // setNeutralButton()

        builder.setMultiChoiceItems(conttermList, checkedbankList, bankListDialogListener);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
