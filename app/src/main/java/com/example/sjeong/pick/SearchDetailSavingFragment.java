package com.example.sjeong.pick;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.sjeong.pick.R.id.spinner;


/**
 * Created by mijin on 2017-11-11.
 */

public class SearchDetailSavingFragment extends Fragment implements View.OnClickListener {
    public static int TIME_OUT = 1001;

    private ProgressDialog progressDialog;

    protected ArrayList<CharSequence> selectedBank = new ArrayList<CharSequence>();
    protected ArrayList<CharSequence> selectedPrimeCond = new ArrayList<CharSequence>();
    protected CharSequence[] bankList, primecondList, conttermList;
    View view;
    ArrayAdapter<CharSequence> adspin;
    Button vbank, vprime_cond;
    Spinner vcont_term;
    CheckBox[] vjoin_target, vjoin_way, vprod_type, vori_pay_method, vrat_pay_method;
    EditText vmin_intr, vmonth_limit;
    String cont_term, prime_cond;
    ContentValues values;
    AlertDialog.Builder builder2, builder4;
    int send;


    String json;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_detail_saving, container, false);

        vjoin_target = new CheckBox[]{(CheckBox)view.findViewById(R.id.radioButton1),(CheckBox)view.findViewById(R.id.radioButton2),(CheckBox)view.findViewById(R.id.radioButton3)};
        //Spinner bank = (Spinner) view.findViewById(R.id.spinner);
        vbank = (Button) view.findViewById(spinner);
        vjoin_way = new CheckBox[]{(CheckBox)view.findViewById(R.id.ch1),(CheckBox)view.findViewById(R.id.ch2), (CheckBox)view.findViewById(R.id.ch3), (CheckBox)view.findViewById(R.id.ch4)};
        //Spinner cont_term = (Spinner) view.findViewById(R.id.spinner3);
        //Spinner prime_cond = (Spinner) view.findViewById(R.id.spinner2);
        vcont_term = (Spinner) view.findViewById(R.id.spinner3);
        vprime_cond = (Button) view.findViewById(R.id.spinner2);
        vmin_intr = (EditText) view.findViewById(R.id.min_rate);
        vprod_type = new CheckBox[]{(CheckBox)view.findViewById(R.id.freeInput), (CheckBox)view.findViewById(R.id.fixInput)};
        vori_pay_method = new CheckBox[]{(CheckBox)view.findViewById(R.id.orifull), (CheckBox)view.findViewById(R.id.oriyear)};
        vrat_pay_method = new CheckBox[]{(CheckBox)view.findViewById(R.id.ratfull), (CheckBox)view.findViewById(R.id.ratyear)};
        vmonth_limit = (EditText) view.findViewById(R.id.monthInput);

        bankList = getResources().getStringArray(R.array.bank);
        primecondList = getResources().getStringArray(R.array.primelist);

        vbank.setOnClickListener(this);
        vprime_cond.setOnClickListener(this);


        //ContentValues



        adspin = ArrayAdapter.createFromResource(getActivity(), R.array.cont_term, android.R.layout.simple_spinner_item);

        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vcont_term.setAdapter(adspin);
        vcont_term.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cont_term = parent.getSelectedItem().toString();
                Toast.makeText(getActivity(), cont_term, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Button sss = (Button) view.findViewById(R.id.sss);
        sss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                values = new ContentValues();

                char[] jointarget = new char[]{'0','0','0'};
                if(vjoin_target[0].isChecked()) jointarget[0]='1';
                if(vjoin_target[1].isChecked()) jointarget[1]='1';
                if(vjoin_target[2].isChecked()) jointarget[2]='1';

                values.put("join_target", Integer.valueOf(jointarget[0]+""+jointarget[1]+""+jointarget[2],2));

                int no = 0;

                for(CharSequence c : selectedBank){
                    values.put("bank"+no,c.toString());
                    Log.d("은행"+no, c.toString());
                    no++;
                }
                values.put("no",no);

                char[] primecond = new char[]{'1','1','0','0','0','0','0','0','0','0','0','0'};
                boolean flag = false;
                for (CharSequence c : selectedPrimeCond) {
                    Log.d("우대조건 확인", c.toString());
                    if (c.toString().contains("전체")) {
                        Log.d("전체포함","Y");
                        flag = true;
                        break;
                    }else{
                        for (int i = 1; i < primecondList.length; i++) {
                            if(primecondList[i].toString().contains(c.toString())) primecond[i+1]='1';
                        }
                    }

                }

                Log.d("Flag", String.valueOf(flag));
                if(flag) {
                    prime_cond = "111111111111";
                    values.put("prime_cond", Integer.valueOf("111111111111",2));
                }else{
                    prime_cond = primecond[0]+""+primecond[1]+""+primecond[2]+""+primecond[3]+""+primecond[4]+""+primecond[5]+""+primecond[6]+""+primecond[7]+""+primecond[8]+""+primecond[9]+""+primecond[10]+""+primecond[11];
                    values.put("prime_cond", Integer.valueOf(prime_cond,2));
                }
                send = Integer.valueOf(prime_cond,2)- Integer.valueOf("110000000000",2);

                char[] joinway = new char[]{'0','0','0','0'};
                if(vjoin_way[0].isChecked()) joinway[0]='1';
                if(vjoin_way[1].isChecked()) joinway[1]='1';
                if(vjoin_way[2].isChecked()) joinway[2]='1';
                if(vjoin_way[3].isChecked()) joinway[3]='1';

                values.put("join_way", Integer.valueOf(joinway[0]+""+joinway[1]+""+joinway[2]+""+joinway[3],2));

                if(cont_term.equals("3개월")) values.put("cont_term",3);
                else if(cont_term.equals("6개월")) values.put("cont_term",6);
                else if(cont_term.equals("1년")) values.put("cont_term",12);
                else if(cont_term.equals("2년")) values.put("cont_term",24);
                else if(cont_term.equals("3년")) values.put("cont_term",36);

                values.put("min_intr",vmin_intr.getText().toString());

                char[] prodtype = new char[]{'0','0'};
                if(vprod_type[0].isChecked()) prodtype[0]='1';
                if(vprod_type[1].isChecked()) prodtype[1]='1';
                values.put("prod_type", Integer.valueOf(prodtype[0]+""+prodtype[1],2));

                char[] oripaymethod = new char[]{'0','0'};
                if(vori_pay_method[0].isChecked()) oripaymethod[0]='1';
                if(vori_pay_method[1].isChecked()) oripaymethod[1]='1';
                values.put("ori_pay_method", Integer.valueOf(oripaymethod[0]+""+oripaymethod[1],2));

                char[] ratpaymethod = new char[]{'0','0'};
                if(vrat_pay_method[0].isChecked()) ratpaymethod[0]='1';
                if(vrat_pay_method[1].isChecked()) ratpaymethod[1]='1';
                values.put("rat_pay_method", Integer.valueOf(ratpaymethod[0]+""+ratpaymethod[1],2));

                values.put("month_limit",vmonth_limit.getText().toString());

                Log.d("데이터", values.toString());
                if(values.get("join_target").toString().equals("000")||values.get("no").toString().equals("0")||values.get("prime_cond").toString().equals("000000000000")||values.get("cont_term")==null||values.get("join_way").toString().equals("0000")){
                    AlertDialog.Builder builder5 = new AlertDialog.Builder(getActivity());
                    builder5.setMessage("필수항목을 선택해주세요.");
                    builder5.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder5.show();
                }else{
                    String url = " http://ec2-13-58-182-123.us-east-2.compute.amazonaws.com/getProduct2.php?";
                    NetworkTask networkTask = new NetworkTask(url,values);
                    networkTask.execute();

                }
            }
        });


        return view;
    }
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case spinner:
                showSelectBankDialog();
                break;

            case R.id.spinner2:
                showSelectPrimeCondDialog();
                break;

            default:
                break;
        } // switch
    } // onClick()

    boolean[] checkedbankList;
   // int count;
    protected void showSelectBankDialog()
    {
        //boolean[]
        checkedbankList = new boolean[bankList.length];
        int count = bankList.length;

        for(int i = 0; i < count; i++)
            checkedbankList[i] = selectedBank.contains(bankList[i]);


        //DialogInterface.OnMultiChoiceClickListener bankListDialogListener =

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("Select bankList");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(selectedBank.size()==0){




                }else {
                    dialogInterface.dismiss();
                    Toast.makeText(getActivity().getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();

                }
            }
        });




/*

        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // Write your code here to execute after dialog closed

            } // onClick()
        }); // setNeutralButton()

*/

        builder.setMultiChoiceItems(bankList, checkedbankList, new DialogInterface.OnMultiChoiceClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked)
            {

                if(which!=0) {
                    if (isChecked) {
                        selectedBank.add(bankList[which]);
                        if (selectedBank.size() == bankList.length - 1) {
                            selectedBank.add(bankList[0]);
                            ((AlertDialog)dialog).getListView().setItemChecked(0,true);
                            checkedbankList[0] = true;
                        }
                    }else
                        selectedBank.remove(bankList[which]);
                        if (selectedBank.size() == bankList.length - 1) {
                            selectedBank.remove(bankList[0]);
                            ((AlertDialog)dialog).getListView().setItemChecked(0,false);
                            checkedbankList[0] = false;
                        }

                }else{
                    if(isChecked) {
                        selectedBank.clear();
                        for (int j=0;j<bankList.length;j++) {
                            selectedBank.add(bankList[j]);
                            ((AlertDialog)dialog).getListView().setItemChecked(j,true);
                            checkedbankList[j] = true;
                        }
                    }else{
                        selectedBank.clear();
                        for (int j=0;j<bankList.length;j++) {
                            ((AlertDialog)dialog).getListView().setItemChecked(j,false);
                            checkedbankList[j] = false;
                        }
                    }
                }
                Log.d("은행", String.valueOf(selectedBank.size()));
                // onChangeselectedBank();
            } // onClick()
        }); // coloursDialogListenerbankListDialogListener);

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean wantToCloseDialog = true;
                        if(selectedBank.size()==0){



                            builder2 = new AlertDialog.Builder(builder.getContext());
                            builder2.setMessage("은행을 선택해주세요.");
                            builder2.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder2.show();
                            wantToCloseDialog=false;

                        }


                        if (wantToCloseDialog)
                            dialog.dismiss();
                    }
                });
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean wantToCloseDialog = false;


                        if (wantToCloseDialog)
                            dialog.dismiss();

                    }
                });


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

    boolean[] checkedprimecondList;
    protected void showSelectPrimeCondDialog()
    {
        //boolean[]
        checkedprimecondList = new boolean[primecondList.length];
        int count = primecondList.length;

        for(int i = 0; i < count; i++)
            checkedprimecondList[i] = selectedPrimeCond.contains(primecondList[i]);

        /*
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
*/
        final AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());
        builder3.setTitle("Select bankList");

        builder3.setNeutralButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // Write your code here to execute after dialog closed
                Toast.makeText(getActivity().getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            } // onClick()
        }); // setNeutralButton()

        builder3.setMultiChoiceItems(primecondList, checkedprimecondList,new DialogInterface.OnMultiChoiceClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked)
            {

                if(which!=0) {
                    if (isChecked) {
                        selectedPrimeCond.add(primecondList[which]);
                        if (selectedPrimeCond.size() == primecondList.length - 1) {
                            selectedPrimeCond.add(primecondList[0]);
                            ((AlertDialog)dialog).getListView().setItemChecked(0,true);
                            checkedprimecondList[0] = true;
                        }
                    }else
                        selectedPrimeCond.remove(primecondList[which]);
                    if (selectedPrimeCond.size() == primecondList.length - 1) {
                        selectedPrimeCond.remove(primecondList[0]);
                        ((AlertDialog)dialog).getListView().setItemChecked(0,false);
                        checkedprimecondList[0] = false;
                    }

                }else{
                    if(isChecked) {
                        selectedPrimeCond.clear();
                        for (int j=0;j<primecondList.length;j++) {
                            selectedPrimeCond.add(primecondList[j]);
                            ((AlertDialog)dialog).getListView().setItemChecked(j,true);
                            checkedprimecondList[j] = true;
                        }
                    }else{
                        selectedPrimeCond.clear();
                        for (int j=0;j<primecondList.length;j++) {
                            ((AlertDialog)dialog).getListView().setItemChecked(j,false);
                            checkedprimecondList[j] = false;
                        }
                    }
                }






                // onChangeselectedBank();
            } // onClick()
        }); // coloursDialogListenerbankListDialogListener);


        final AlertDialog dialog3 = builder3.create();
        dialog3.show();
        dialog3.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean wantToCloseDialog = true;
                        if(selectedPrimeCond.size()==0){



                            builder4 = new AlertDialog.Builder(builder3.getContext());
                            builder4.setMessage("우대조건을 선택해주세요.");
                            builder4.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder4.show();
                            wantToCloseDialog=false;

                        }


                        if (wantToCloseDialog)
                            dialog3.dismiss();
                    }
                });
        dialog3.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Boolean wantToCloseDialog = false;


                        if (wantToCloseDialog)
                            dialog3.dismiss();

                    }
                });
        dialog3.show();
    }


    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        Handler mHandler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == TIME_OUT) { // 타임아웃이 발생하면
                    progressDialog.dismiss(); // ProgressDialog를 종료
                }
            }

        };

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;

        }

        @Override
        protected String doInBackground(Void... params) {


            //String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            String result = requestHttpURLConnection.request(url, values);
            // 해당 URL로 부터 결과물을 얻어온다.


            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            json=result;
            progressDialog = ProgressDialog.show(getContext(), "Title", "Message");
            mHandler.sendEmptyMessageDelayed(TIME_OUT, 2000);


            Intent intent = new Intent(view.getContext(), ProductActivity.class);
            intent.putExtra("json",result);
            intent.putExtra("prime_cond",send);
            Log.d("보내는우대",send+"");
            startActivity(intent);


        }


    }






}
