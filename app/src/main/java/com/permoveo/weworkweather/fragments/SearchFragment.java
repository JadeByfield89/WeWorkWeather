package com.permoveo.weworkweather.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.permoveo.weworkweather.R;
import com.permoveo.weworkweather.interfaces.OnAutoCompleteRequestListener;
import com.permoveo.weworkweather.interfaces.OnLocationSelectedListener;
import com.permoveo.weworkweather.models.AutoCompleteResult;
import com.permoveo.weworkweather.network.GoogleAutoCompleteRequest;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by byfieldj on 3/21/16.
 */
public class SearchFragment extends DialogFragment {

    @Bind(R.id.atv_query)
    AutoCompleteTextView autoCompleteTextView;

    ArrayList<String> mPredictions = new ArrayList<String>();

    private OnLocationSelectedListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, v);

        autoCompleteTextView.addTextChangedListener(autoCompleteListener);


        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            mListener = (OnLocationSelectedListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
            Log.e("SearchFragment", "This Activity must implement OnLocationSelectedListener!");
        }
        super.onAttach(activity);
    }


    // We want to execute a GooglePlaces autocomplete search request each time the user makes a
    // Change to the search query string, and display the prediction results
    private TextWatcher autoCompleteListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!TextUtils.isEmpty(s)) {

                final GoogleAutoCompleteRequest request = new GoogleAutoCompleteRequest();
                request.setListener(new OnAutoCompleteRequestListener() {
                    @Override
                    public void onAutoCompleteRequestComplete(AutoCompleteResult result) {

                        mPredictions = result.getPredictions();

                        ArrayAdapter<String> adapter = new ArrayAdapter<>
                                (getContext(), android.R.layout.select_dialog_item, mPredictions);

                        autoCompleteTextView.setAdapter(adapter);
                        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Toast.makeText(getContext(), "" + mPredictions.get(position), Toast.LENGTH_SHORT).show();
                                mListener.OnLocationSelected(mPredictions.get(position));
                                Log.d("SearchFragment", "Selected -> " + mPredictions.get(position));
                                dismiss();
                            }
                        });
                    }

                    @Override
                    public void onAutoCompleteRequestFailed() {

                    }
                });

                request.getCityNames(autoCompleteTextView.getText().toString());

            }

        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };


    @Override
    public void onResume() {
        super.onResume();

        int width = getResources().getDimensionPixelSize(R.dimen.search_dialog_widith);
        int height = getResources().getDimensionPixelSize(R.dimen.search_dialog_height);
        getDialog().getWindow().setLayout(width, height);
    }
}
