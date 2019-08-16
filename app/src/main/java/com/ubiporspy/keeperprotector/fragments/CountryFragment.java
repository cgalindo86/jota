package com.ubiporspy.keeperprotector.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;

import com.ubiporspy.keeperprotector.R;
import com.ubiporspy.keeperprotector.bean.CountryBean;
import com.ubiporspy.keeperprotector.util.CSVFile;
import com.ubiporspy.keeperprotector.view.adapter.CountryAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CountryFragment extends DialogFragment {

    @BindView(R.id.svSearch) SearchView svSearch;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private LinearLayoutManager linearLayout;
    private List<CountryBean> original = new ArrayList<>();
    private List<CountryBean> lista = new ArrayList<>();
    private CountryAdapter adapter;

    public interface CountryListener {
        void setCountry(String name, String code);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_country, null);

        ButterKnife.bind(this, view);

        InputStream inputStream = null;
        try {
            inputStream = getResources().getAssets().open("country.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSVFile csvFile = new CSVFile(inputStream);
        ArrayList<String[]> scoreList = csvFile.read();

        for(int i = 1; i < scoreList.size()-1; i++) {
            String[] row = scoreList.get(i);
            lista.add(new CountryBean(row[1].replace("\"", ""), row[5].replace("\"", "")));
            original.add(new CountryBean(row[1].replace("\"", ""), row[5].replace("\"", "")));
        }

        svSearch.requestFocus();

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                lista.clear();

                for(CountryBean bean : original) {
                    if(bean.getName().toLowerCase().contains(newText.toLowerCase())) {
                        lista.add(bean);
                    }
                }

                adapter.notifyDataSetChanged();
                return false;
            }
        });

        linearLayout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), linearLayout.getOrientation()));

        adapter = new CountryAdapter(getContext(), lista);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CountryAdapter.ClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                CountryListener dialogListener = (CountryListener) getActivity();
                dialogListener.setCountry(lista.get(position).getName(), lista.get(position).getPhoneCode());
                dismiss();
            }
        });

        return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setTitle("Paises")
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .setView(view)
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}