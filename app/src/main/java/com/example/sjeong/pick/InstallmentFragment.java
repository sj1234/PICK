package com.example.sjeong.pick;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by mijin on 2017-08-14.
 */

public class InstallmentFragment extends Fragment {

    EditText won, dur, rate;
    TextView normal_pay, normal_rat, prime_pay, prime_rat, free_pay, free_rat;
    TableLayout result;
    RadioGroup radio;
    Button cal;
    double w, d, r, temp, norm, prime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_installment, container, false);

        won = (EditText) rootView.findViewById(R.id.swon);
        dur = (EditText) rootView.findViewById(R.id.sdur);
        rate = (EditText) rootView.findViewById(R.id.srate);

        normal_pay = (TextView) rootView.findViewById(R.id.snormal_pay);
        normal_rat = (TextView) rootView.findViewById(R.id.snormal_rat);
        prime_pay = (TextView) rootView.findViewById(R.id.sprime_pay);
        prime_rat = (TextView) rootView.findViewById(R.id.sprime_rat);
        free_pay = (TextView) rootView.findViewById(R.id.sfree_pay);
        free_rat = (TextView) rootView.findViewById(R.id.sfree_rat);

        radio = (RadioGroup) rootView.findViewById(R.id.sradio);
        radio.check(R.id.ssim);

        cal = (Button) rootView.findViewById(R.id.scal);

        result = (TableLayout) rootView.findViewById(R.id.sresult);


        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(won.getText()!=null&&dur.getText()!=null&&rate.getText()!=null) {
                    w = Double.parseDouble(won.getText().toString());
                    d = Double.parseDouble(dur.getText().toString());
                    r = Double.parseDouble(rate.getText().toString());

                    switch (radio.getCheckedRadioButtonId()) {
                        case R.id.ssim:
                            temp = (w * d * (d + 1) / 2 * (r / 100) / 12);
                            break;
                        case R.id.scom:
                            temp = (w * (1 + (r / 100) / 12) * (Math.pow(1 + (r / 100) / 12, d) - 1) / ((r / 100) / 12)) - w * d;
                            break;
                    }

                    norm = Math.ceil(temp * (1 - 15.4 / 100));
                    prime = Math.ceil(temp * (1 - 9.5 / 100));

                    normal_pay.setText(String.valueOf(norm + w));
                    normal_rat.setText(String.valueOf(norm));

                    prime_pay.setText(String.valueOf(prime + w));
                    prime_rat.setText(String.valueOf(prime));

                    free_pay.setText(String.valueOf(Math.ceil(temp) + w));
                    free_rat.setText(String.valueOf(Math.ceil(temp)));

                    result.setVisibility(View.VISIBLE);

                }else{
                    result.setVisibility(View.GONE);
                }

            }
        });

        return rootView;
    }
}
