package com.zspri.wemocontroller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableRecyclerView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RecyclerViewAdapter extends WearableRecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public class ViewHolder extends WearableRecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public ImageButton toggleButton;
        public Boolean isToggled = false;
        private HttpClient httpClient;
        private Context context;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, Context mContext, HttpClient client) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            httpClient = client;
            context = mContext;

            nameTextView = (TextView) itemView.findViewById(R.id.name);
            toggleButton = (ImageButton) itemView.findViewById(R.id.toggle_button);
            toggleButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                final WemoDeviceItem item = mList.get(pos);

                isToggled = !isToggled;

                httpClient.setDeviceState(item, isToggled ? BinaryState.ON : BinaryState.OFF, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setButtonColor(response.getInt("state") == 1);
                            item.setState(response.getInt("state"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        @Override
        public boolean onLongClick(View view) {
            System.out.println("Long click handler");

            int pos = getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) {
                return false;
            }
            WemoDeviceItem item = mList.get(pos);

            Intent i = new Intent(context, DeviceDetailsActivity.class);
            i.putExtra("device_addr", item.getAddr());
            i.putExtra("device_hash", item.getHash());
            i.putExtra("device_name", item.getName());
            i.putExtra("device_state", item.getState());
            context.startActivity(i);
            return true;
        }

        public void setButtonColor(boolean isToggled) {
            Drawable btnDrawable = this.toggleButton.getBackground();
            btnDrawable = DrawableCompat.wrap(btnDrawable);
            DrawableCompat.setTint(btnDrawable, isToggled ? Color.parseColor("#2196F3") : Color.DKGRAY);
            this.toggleButton.setBackground(btnDrawable);
            this.toggleButton.setImageResource(isToggled ? R.drawable.lightbulb_fill : R.drawable.lightbulb);
        }
    }

    private List<WemoDeviceItem> mList;
    private HttpClient mHttpClient;

    public RecyclerViewAdapter(List<WemoDeviceItem> items, HttpClient client) {
        mList = items;
        mHttpClient = client;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View listView = inflater.inflate(R.layout.item_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(listView, context, mHttpClient);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewHolder, int position) {
        WemoDeviceItem item = mList.get(position);

        TextView textView = viewHolder.nameTextView;
        textView.setText(item.getName());
        viewHolder.isToggled = item.getState() == 1;
        viewHolder.setButtonColor(viewHolder.isToggled);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<WemoDeviceItem> items) {
        mList.addAll(items);
        notifyDataSetChanged();
    }
}
