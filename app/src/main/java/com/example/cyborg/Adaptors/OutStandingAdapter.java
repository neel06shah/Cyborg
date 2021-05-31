package com.example.cyborg.Adaptors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyborg.Models.CheckingModel;
import com.example.cyborg.Models.OutstandingModel;
import com.example.cyborg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class OutStandingAdapter extends RecyclerView.Adapter<OutStandingAdapter.OutStandingViewHolder> implements Filterable {

    private ArrayList<OutstandingModel> outstandingModels;
    private ArrayList<OutstandingModel> outstandingModelsAll;
    private Activity activity;
    private DatabaseReference databaseReference;

    public OutStandingAdapter(Activity activity, ArrayList<OutstandingModel> models){
        this.outstandingModels = models;
        this.outstandingModelsAll = new ArrayList<>(models);
        this.activity = activity;
    }

    public void updateAdapter(ArrayList<OutstandingModel> data){
        outstandingModelsAll.clear();
        outstandingModels.clear();
        outstandingModelsAll.addAll(data);
        outstandingModels.addAll(data);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public OutStandingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.outstanding_view_holder,parent,false);
        return new OutStandingViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OutStandingViewHolder holder, int position) {

        OutstandingModel currentModel = outstandingModels.get(position);
        holder.outstandingLedger.setText(currentModel.getLedgerName());
        holder.outstandingAmount.setText(currentModel.getBalanceAmount());
        holder.outstandingInfo.setText(String.format("%s | %s",currentModel.getVoucherDate(),currentModel.getVoucherRef()));
        holder.outstandingDueOn.setText(String.format("Due On : %s",currentModel.getBalanceDueOn()));
        holder.outstandingOverDue.setText(String.format("Over Due : %s",currentModel.getBalanceOverDue()));
        holder.view.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String date = dateFormat.format(calendar.getTime());
            databaseReference = FirebaseDatabase.getInstance().getReference().child("workingSheet").child(date).child(currentModel.getBalanceArea()).child(currentModel.getLedgerName().replace(".","_dot_").replace("/","_slash_"));
            databaseReference.child("contact").setValue(currentModel.getBalanceMobile());
            databaseReference.child("party_name").setValue(currentModel.getLedgerName());
            databaseReference.child("area").setValue(currentModel.getBalanceArea());
            CheckingModel data = new CheckingModel(
                    currentModel.getBalanceArea(),
                    currentModel.getVoucherDate(),
                    currentModel.getBalanceDueOn(),
                    currentModel.getLedgerName(),
                    currentModel.getBalanceMobile(),
                    currentModel.getVoucherRef(),
                    Long.parseLong(currentModel.getBalanceOverDue()),
                    Long.parseLong(currentModel.getBalanceAmount().replace(".0","")),
                    Long.parseLong("0"));
            databaseReference.child("Bills").child(currentModel.getVoucherRef().replace("/","_slash_")).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(activity, "Data Added", Toast.LENGTH_SHORT).show();
                    outstandingModels.remove(outstandingModels.get(position));
                    notifyDataSetChanged();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return outstandingModels.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<OutstandingModel> temp = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                 temp.addAll(outstandingModelsAll);
            }else{
                String pattern = constraint.toString().trim().toLowerCase();
                for(OutstandingModel outstandingModel : outstandingModelsAll){
                    if(outstandingModel.getLedgerName().toLowerCase().contains(pattern)){
                         temp.add(outstandingModel);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = temp;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            outstandingModels.clear();
            outstandingModels.addAll((ArrayList) results.values);
            notifyDataSetChanged();

        }
    };

    public static class OutStandingViewHolder extends RecyclerView.ViewHolder{

        private AppCompatTextView outstandingLedger;
        private AppCompatTextView outstandingAmount;
        private AppCompatTextView outstandingInfo;
        private AppCompatTextView outstandingDueOn;
        private AppCompatTextView outstandingOverDue;
        private View view;
        private LinearLayout linearLayout;

        public  OutStandingViewHolder(View v){
            super(v);

            view = v;
            outstandingLedger = v.findViewById(R.id.outstandingLedger);
            outstandingAmount = v.findViewById(R.id.outstandingAmount);
            outstandingInfo = v.findViewById(R.id.outstandingInfo);
            outstandingDueOn = v.findViewById(R.id.outstandingDueOn);
            outstandingOverDue = v.findViewById(R.id.outstandingOverDue);
            linearLayout = v.findViewById(R.id.linearLayout);
        }

    }
}
