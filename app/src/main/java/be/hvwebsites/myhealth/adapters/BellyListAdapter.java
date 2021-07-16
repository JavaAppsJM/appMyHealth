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
import be.hvwebsites.myhealth.UpdateBellyMActivity;
import be.hvwebsites.myhealth.entities.Belly;

public class BellyListAdapter extends RecyclerView.Adapter<BellyListAdapter.BellyViewHolder>{

    private final LayoutInflater inflater;
    private List<Belly> bellyList;
    private Context mContext;
    public static final int INTENT_REQUEST_CODE = 1;
    public static final String EXTRA_INTENT_KEY_DATE =
            "be.hvwebsites.myhealth.INTENT_KEY_DATE";
    public static final String EXTRA_INTENT_KEY_RADIUS =
            "be.hvwebsites.myhealth.INTENT_KEY_RADIUS";
    public static final String EXTRA_INTENT_KEY_INDEX =
            "be.hvwebsites.myhealth.INTENT_KEY_INDEX";
    public static final String EXTRA_INTENT_KEY_ACTION =
            "be.hvwebsites.myhealth.INTENT_KEY_ACTION";



    public BellyListAdapter(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    class BellyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView bellyItemView;

        private BellyViewHolder(View itemView){
            super(itemView);
            bellyItemView = itemView.findViewById(R.id.textViewBelly);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // er is geclicked op een Belly, dit betekent dat er nr detail vd Belly vr evt update wordt gegaan
            // daarvoor gaan we nr een de update activity
            int indexToUpdate = getAdapterPosition();
            Belly current = bellyList.get(indexToUpdate);

            Intent intent = new Intent(mContext, UpdateBellyMActivity.class);
            intent.putExtra(EXTRA_INTENT_KEY_ACTION, "update");
            intent.putExtra(EXTRA_INTENT_KEY_DATE, current.getFormatDate());
            intent.putExtra(EXTRA_INTENT_KEY_RADIUS, current.getBellyRadius());
            intent.putExtra(EXTRA_INTENT_KEY_INDEX, indexToUpdate);
            mContext.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public BellyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_belly_item, parent, false);
        return new BellyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BellyViewHolder holder, int position) {
        if (bellyList != null){
            Belly current = bellyList.get(position);
            String bellyTextLine = current.getFormatDate()
                    + " : "
                    + current.getBellyRadius()
                    + "cm "
                    + " -- "
                    + current.getDateInt();
            holder.bellyItemView.setText(bellyTextLine);
        } else {
            holder.bellyItemView.setText("No bellys");
        }

    }

    public void setBellyList(List<Belly> bellyList) {
        this.bellyList = bellyList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (bellyList != null) return bellyList.size();
        else return 0;
    }

    // Om te weten welke bellymeasurement is gekozen uit de lijst
    public Belly getBellyAtPosition(int position){
        return bellyList.get(position);
    }

}
