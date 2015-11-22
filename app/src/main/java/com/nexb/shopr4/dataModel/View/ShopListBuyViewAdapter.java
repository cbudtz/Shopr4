package com.nexb.shopr4.dataModel.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nexb.shopr4.FireBaseController;
import com.nexb.shopr4.R;

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
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ShopListViewContent content = getItem(position);
        View view = null;

        if(content.getType().equals(ShopListViewContent.contentType.ITEM)){
            view = mInflater.inflate(R.layout.list_item_buy_view, null);

            //add listViewItem
            ((TextView)view.findViewById(R.id.buyItemName)).setText(
                    ((ShopListViewItem) content).getAmount() + " " +
                            ((ShopListViewItem) content).getUnit() + " " +
                            ((ShopListViewItem) content).getName()
            );
           //view.setTag(content.getId());

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


