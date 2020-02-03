package com.example.testnoteapp;

import android.content.Context;
import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter implements Filterable {

    public ArrayList<Note> dataSet, filter, filterList;
    private Context context;
    private NoteFilter noteFilter;

    public NoteAdapter(Context context, ArrayList<Note>objs){
        this.context = context;
        this.dataSet = objs ;
        this.filter = objs;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Note getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public Filter getFilter() {
        if(noteFilter == null)
            noteFilter = new NoteFilter();
        return noteFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        }

        Note note =(Note)getItem(position);

        TextView tit =(TextView)convertView.findViewById(R.id.tvTit);
        TextView content = (TextView)convertView.findViewById(R.id.tvContent);
        TextView date = (TextView)convertView.findViewById(R.id.tvDate);
        TextView tag = (TextView)convertView.findViewById(R.id.tvTag);

        tit.setText(note.getTitle());
        content.setText(note.getContent());
        date.setText(note.getDate());
        tag.setText(note.getTag());

        return convertView;
    }

    public class NoteFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            MainActivity.oldPos.clear();
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length()>0){
                filterList = new ArrayList<Note>();
                for (int i =0 ; i < filter.size();i++){
                    if(filter.get(i).getTitle().toUpperCase().contains(constraint.toString().toUpperCase())||
                    filter.get(i).getTag().toUpperCase().contains(constraint.toString().toUpperCase())){
                        Note newNote = filter.get(i);
                        MainActivity.oldPos.add(i);
                        filterList.add(newNote);
                    }
                }

                results.values = filterList;
                results.count = filterList.size();
            }
            else{

                results.values=filter;
                results.count=filter.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataSet = (ArrayList<Note>)results.values;
            notifyDataSetChanged();
        }
    }

    public void deleteItem(int position){
        MainActivity.dataSet.remove(position);
        if (MainActivity.oldPos.size()!=0){
            filterList.remove(MainActivity.delItemOldPos);
        }
        else{
        }
        notifyDataSetChanged();
    }
}
