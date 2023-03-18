package be.hvwebsites.myhealth.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.hvwebsites.myhealth.R;
import be.hvwebsites.myhealth.UpdateMeasurementActivity;
import be.hvwebsites.myhealth.constants.GlobalConstant;
import be.hvwebsites.myhealth.helpers.BloodPressureM;
import be.hvwebsites.myhealth.entities.Measurement;
import be.hvwebsites.myhealth.helpers.MListLine;

public class MeasurementListAdapter extends RecyclerView.Adapter<MeasurementListAdapter.MeasurementViewHolder>{

    private final LayoutInflater inflater;
    private List<MListLine> lineList;
    private Context mContext;


    public MeasurementListAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    class MeasurementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView measurementItemView;

        private MeasurementViewHolder(View itemView){
            super(itemView);
            measurementItemView = itemView.findViewById(R.id.mListRecycler);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // er is geclicked op een item, dit betekent dat er nr detail vd item vr evt update wordt gegaan
            // daarvoor gaan we nr de update activity
            int indexToUpdate = getAdapterPosition();
            MListLine current = lineList.get(indexToUpdate);

            Intent intent = new Intent(mContext, UpdateMeasurementActivity.class);
            intent.putExtra(Measurement.EXTRA_INTENT_KEY_ACTION, GlobalConstant.ACTION_UPDATE);
            intent.putExtra(Measurement.EXTRA_INTENT_KEY_TYPE, current.getmType());
            intent.putExtra(Measurement.EXTRA_INTENT_KEY_DATE, current.getFormatDate());
            intent.putExtra(Measurement.EXTRA_INTENT_KEY_INDEX, indexToUpdate);
            switch (current.getmType()){
                case GlobalConstant.CASE_BELLY:
                    intent.putExtra(Measurement.EXTRA_INTENT_KEY_VALUE, current.getmValue1());
                    break;
                case GlobalConstant.CASE_BLOOD:
                    intent.putExtra(BloodPressureM.EXTRA_INTENT_KEY_UPPER, current.getmValue2());
                    intent.putExtra(BloodPressureM.EXTRA_INTENT_KEY_LOWER, current.getmValue3());
                    intent.putExtra(BloodPressureM.EXTRA_INTENT_KEY_HEARTB, current.getmValue4());
            }
            mContext.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public MeasurementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_mlist_item, parent, false);
        return new MeasurementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasurementViewHolder holder, int position) {
        if (lineList != null){
            MListLine current = lineList.get(position);
            String listTextLine = "";
            if (current.getmType() == GlobalConstant.CASE_BELLY){
                listTextLine = current.getFormatDate()
                        + " : "
                        + current.getmValue1()
                        + " cm ";
            }else if (current.getmType() == GlobalConstant.CASE_BLOOD){
                listTextLine = current.getFormatDate()
                        + " : "
                        + current.getmValue2()
                        + " mmHg ; "
                + current.getmValue3()
                        + " mmHg ; "
                        + current.getmValue4()
                        + " b/min ";
            }
            holder.measurementItemView.setText(listTextLine);
        } else {
        holder.measurementItemView.setText("No measurements !");
        }
    }

    public void setLineList(List<MListLine> lineList) {
        this.lineList = lineList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (lineList != null) return lineList.size();
        else return 0;
    }

    // Om te weten welke measurement is gekozen uit de lijst
    public MListLine getLineAtPosition(int position){
        return lineList.get(position);
    }

}
