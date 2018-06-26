package at.namanuel.bsaweatherapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;


public class ListAdapter extends  RecyclerView.Adapter<ListAdapter.ItemViewHolder>{
    private List<Weather> mItems;
    private final int FIRST_LIST_ITEM = 0;
    public int type_des = 0;
    String $lang = Locale.getDefault().getLanguage();

    interface ListItemClickListener{
        void onListItemClick(Weather item);
    }
    private ListItemClickListener mListItemClickListener;

    public void setOnItemClickListener(ListItemClickListener itemClickListener){
        mListItemClickListener = itemClickListener;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        Context mContext;
        private TextView mDatum;
        private ImageView mIcon;
        private TextView mTemp;
        private  TextView mDes;

        ItemViewHolder(View itemView){
            super(itemView);

            mTemp = itemView.findViewById(R.id.tv_temp);
            mDatum = itemView.findViewById(R.id.tv_datum);
            mIcon = itemView.findViewById(R.id.iv_icon);
            mContext = itemView.getContext();
            if(type_des == 1){
             mDes = itemView.findViewById(R.id.tv_des);
            }
            itemView.setOnClickListener(this);
        }

        void bind(Weather item){

            String uri = "@drawable/a"+item.getIcon();

            int resourceId = mContext.getResources().getIdentifier(uri, null, mContext.getPackageName());
            Drawable imageRes = mContext.getResources().getDrawable(resourceId);

            //mIcon.setImageDrawable(mContext.getResources().getDrawable(Integer.parseInt("a"+item.getIcon()+".png")));
           // String help = "a"+item.getIcon()+".png";
            if(type_des == 1){
                if($lang != "de"){
                    mDes.setText(item.getDescription());
                    type_des =0;
                }else{
                    String wetterbeschreibund_de = getDescirptionForConditionCode(mContext, item.getConditions());
                    mDes.setText(wetterbeschreibund_de);
                    type_des =0;
                }

            }
            mIcon.setImageDrawable(imageRes);
            mDatum.setText(item.getDate());
            mTemp.setText(item.getTemp());

        }

        @Override
        public void onClick(View view) {
            if(mListItemClickListener != null){
                int clickedPosition = getAdapterPosition();
                mListItemClickListener.onListItemClick(mItems.get(clickedPosition));
            }
        }
    }

    public ListAdapter(List<Weather> items){
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        int layoutIdForListItem;
        if(viewType == FIRST_LIST_ITEM){
            layoutIdForListItem = R.layout.list_item_first;
            type_des = 1;
        }else{
            layoutIdForListItem = R.layout.list_item;
        }
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        for(int i = 0; i < 4; i++){
            holder.bind(mItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return (mItems == null) ? 0 : mItems.size();
    }

    void swapData(List<Weather> items){
        mItems = items;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        //wenn position 0 dann 0
        //sonst 1
        //return viewtype

        if(position == 0){
             return 0;
          }else{
            return 1;
        }

        //return super.getItemViewType(position);

    }
    public static String getDescirptionForConditionCode(Context context, int conditionCode){

        return context.getString(context.getResources().getIdentifier("condition" + conditionCode, "string", context.getPackageName()));

    }

}
