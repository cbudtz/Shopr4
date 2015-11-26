package com.nexb.shopr4.dataModel.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nexb.shopr4.FireBaseController;
import com.nexb.shopr4.MainActivity;
import com.nexb.shopr4.R;
import com.nexb.shopr4.dataModel.ListItem;

import java.util.ArrayList;

/**
 * Created by mac on 19/11/15.
 */
public class ShopListBuyViewAdapter extends ArrayAdapter {


    private ArrayList<ShopListViewContent> list;
    LayoutInflater mInflater;

    public ShopListBuyViewAdapter(Context context, int resource, ArrayList<ShopListViewContent> list) {
        super(context, resource);
        this.list = list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public void notifyDataSetChanged(){
        this.list = FireBaseController.getI().getShoplistViewContents();
        super.notifyDataSetChanged();
        Toast toast = Toast.makeText(getContext(), "hejHEJEJEHEJE", Toast.LENGTH_SHORT);
                        toast.show();

    }

    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ShopListViewContent content = getItem(position);
        final ListItem newItem = new ListItem();
        View view = null;
        final boolean[] states = {false};



        if(content.getType().equals(ShopListViewContent.contentType.ITEM)){
            view = mInflater.inflate(R.layout.list_item_buy_view, null);
            newItem.setName(((ShopListViewItem) content).getName());
            newItem.setUnit(((ShopListViewItem) content).getUnit());
            newItem.setAmount(((ShopListViewItem) content).getAmount());

            //add listViewItem
            ((TextView)view.findViewById(R.id.buyItemName)).setText(
                    ((ShopListViewItem) content).getAmount() + " " +
                            ((ShopListViewItem) content).getUnit() + " " +
                            ((ShopListViewItem) content).getName()
            );

            if(((ShopListViewItem) content).getState().equals(ListItem.ListItemState.FOUND)){
                view.setBackgroundColor(Color.argb(90,0,200,0));
                view.setPadding(20, -10, 20, -10);
            }
            else if(((ShopListViewItem) content).getState().equals(ListItem.ListItemState.NOT_FOUND)){
                view.setBackgroundColor(Color.argb(90,200,0,0));
                view.setPadding(20, -10, 20, -10);
            }


                final View finalView = view;
            view.findViewById(R.id.got_it).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (((ShopListViewItem) content).getState().equals(ListItem.ListItemState.FOUND)) {
                        finalView.setBackgroundColor(Color.WHITE);
                        finalView.setPadding(20, 20, 20, 20);
                        newItem.setState(ListItem.ListItemState.DEFAULT);
                        FireBaseController.getI().updateItem(((ShopListViewItem) content).getCategoryID(), ((ShopListViewItem) content).getItemId(), newItem);
                    } else {
                        finalView.setBackgroundColor(Color.argb(90,0,200,0));
                        finalView.setPadding(20, -10, 20, -10);
                        newItem.setState(ListItem.ListItemState.FOUND);
                        FireBaseController.getI().updateItem(((ShopListViewItem) content).getCategoryID(), ((ShopListViewItem) content).getItemId(), newItem);
                    }
//                    Toast toast = Toast.makeText(getContext(), "hej", Toast.LENGTH_SHORT);
//                        toast.show();
                }
            });
            view.findViewById(R.id.dont_got_it).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((ShopListViewItem) content).getState().equals(ListItem.ListItemState.NOT_FOUND)) {
                        finalView.setBackgroundColor(Color.WHITE);
                        finalView.setPadding(20, 20, 20, 20);
                        newItem.setState(ListItem.ListItemState.DEFAULT);
                        FireBaseController.getI().updateItem(((ShopListViewItem) content).getCategoryID(), ((ShopListViewItem) content).getItemId(), newItem);
                    } else {
                        finalView.setBackgroundColor(Color.argb(90,200,0,0));
                        finalView.setPadding(20, -10, 20, -10);
                        newItem.setState(ListItem.ListItemState.NOT_FOUND);
                        FireBaseController.getI().updateItem(((ShopListViewItem) content).getCategoryID(), ((ShopListViewItem) content).getItemId(), newItem);
                    }

                }
            });

        }
        else if(content.getType().equals(ShopListViewContent.contentType.CATEGORY)){
            view = mInflater.inflate(R.layout.list_category_view, null);
            ((TextView)view.findViewById(R.id.catName)).setText(((ShopListViewCategory) content).getName());
        }
        else if(position == getCount()){
            //add last element
        }
        else {
        }

        return view;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ShopListViewContent getItem(int position) {
        return list.get(position);
    }


}



