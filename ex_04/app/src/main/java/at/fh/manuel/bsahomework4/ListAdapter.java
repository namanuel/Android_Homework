package at.fh.manuel.bsahomework4;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends  RecyclerView.Adapter<ListAdapter.ItemViewHolder>{
    private List<String> mItems;

    interface ListItemClicklistener{
        void onListItemClick(String item);
    }
    private ListItemClicklistener mListItemClickListener;

    public void setOnItemClickListener(ListItemClicklistener itemClicklistener){
        mListItemClickListener = itemClicklistener;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mItemTextView;

        ItemViewHolder(View itemView){
            super(itemView);
            mItemTextView = itemView.findViewById(R.id.tv_item);
            itemView.setOnClickListener(this);
        }

        void bind(String item){
            mItemTextView.setText(item);
        }

        @Override
        public void onClick(View view) {
            if(mListItemClickListener != null){
                int clickedPosition = getAdapterPosition();
                mListItemClickListener.onListItemClick(mItems.get(clickedPosition));
            }
        }
    }

    public ListAdapter(List<String> items){
        mItems = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return (mItems == null) ? 0 : mItems.size();
    }

    void swapData(List<String> items){
        mItems = items;
        notifyDataSetChanged();
    }
}
