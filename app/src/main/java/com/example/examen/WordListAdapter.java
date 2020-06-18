package com.example.examen;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {
    private Cursor cu;
    private Context con;
    private ArrayList<Task> list;

    public WordListAdapter(ArrayList<Task> tasks, Context context, Cursor curs) {
        this.list = tasks;
        this.cu = curs;
        this.con = context;
    }
    public WordListAdapter(ArrayList<Task> tasks, Context c){
        this.list = tasks;
        this.con = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(con);
        View mItemView = inflater.inflate(R.layout.item, parent, false);
        return new ViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.set(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<Task> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
    }

    public void removeItem(int pos){
        list.remove(pos);
        notifyItemRemoved(pos);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv1, tv2;
        private CheckBox cb;

        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.word);
            tv2 = itemView.findViewById(R.id.date);
            cb = itemView.findViewById(R.id.checkBox);
        }

        public void set(final Task t) {
            tv1.setText(t.getName());
            tv2.setText(t.getDate());
            if (t.getComplete().equals("true")) {
                cb.setChecked(true);
                cb.setEnabled(false);
            } else {
                cb.setChecked(false);
            }
            ((CheckBox) cb).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    MyDB.updateItem(t.getId(),t.get_img(), t.getName(), t.getDate(), "true");

                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(con, TaskDetail.class);
                    i.putExtra("ID", t.getId());
                    i.putExtra("IMAGE",t.get_img());
                    i.putExtra("NAME", t.getName());
                    i.putExtra("DATE", t.getDate());
                    i.putExtra("COMPLETE", t.getComplete());
                    con.startActivity(i);
                }
            });
        }

    }
}