package com.example.razvan.googlemapsdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;

import java.util.ArrayList;


public class CityList extends ListFragment implements OnAsynckComplete{

    ViewHolder viewHolder;
    ArrayList<CityData> cities;
    CityLocationAdapter adapter;
    String textToSearch;
    Button show;

    public static CityList newInstance(String textToSearch) {
        CityList fragment = new CityList();
        Bundle args = new Bundle();
        args.putString("textToSearch" , textToSearch);
        fragment.setArguments(args);
        return fragment;
    }

    public CityList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cities=new ArrayList<>();
        if (getArguments() != null) {
            textToSearch=getArguments().getString("textToSearch");
        }
        


        new CityService(cities,this).execute(textToSearch);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_city_list, container, false);
        show=(Button) v.findViewById(R.id.btn_show_all);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<cities.size();i++)
                    ((MainActivity)getActivity()).addIconToMap(cities.get(i));
                removeFragment();
            }
        });
        return v;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        ((MainActivity)getActivity()).addIconToMapOneSelection(cities.get(position));
       removeFragment();
    }

    @Override
    public void OnTaskComplete() {
        adapter = new CityLocationAdapter(cities);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    class CityLocationAdapter extends ArrayAdapter<CityData> {
        public CityLocationAdapter(ArrayList<CityData> cities) {
            super(getActivity(), android.R.layout.list_content, cities);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            viewHolder = new ViewHolder();
            CityData city=cities.get(position);



                if (convertView == null) {

                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.list_layout, parent, false);
                    viewHolder.cityName = (TextView) convertView.findViewById(R.id.txt_city_name);
                    viewHolder.countryCode = (TextView) convertView.findViewById(R.id.txt_country_code);

                    convertView.setTag(viewHolder);

                } else {

                    viewHolder = (ViewHolder) convertView.getTag();

                }
                viewHolder.cityName.setText(city.cityName);
                viewHolder.countryCode.setText(city.countryCode);

                return convertView;



        }


        @Override
        public CityData getItem(int position) {
            return cities.get(position);
        }

        @Override

        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }


    }


    static class ViewHolder {
        TextView cityName, countryCode;

    }


    private void removeFragment(){


        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
    }


}

