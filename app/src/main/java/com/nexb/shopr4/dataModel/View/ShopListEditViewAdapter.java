package com.nexb.shopr4.dataModel.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.nexb.shopr4.FireBaseController;
import com.nexb.shopr4.MainActivity;
import com.nexb.shopr4.R;
import com.nexb.shopr4.dataModel.InstantAutoCompleteTextView;
import com.nexb.shopr4.dataModel.ListItem;
import com.nexb.shopr4.dataModel.ShopList;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by mac on 19/11/15.
 */
public class ShopListEditViewAdapter extends ArrayAdapter {


    private ArrayList<ShopListViewContent> list;
    LayoutInflater mInflater;

    public ShopListEditViewAdapter(Context context, int resource, ArrayList<ShopListViewContent> list) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ShopListViewContent content = getItem(position);
        final ListItem newItem = new ListItem();
        View view = null;


        if(content.getType().equals(ShopListViewContent.contentType.ITEM)){
            view = mInflater.inflate(R.layout.list_item_edit_view, null);


            //add listViewItem
            ((TextView)view.findViewById(R.id.itemName)).setText(((ShopListViewItem) content).getName());
            ((TextView) view.findViewById(R.id.itemAmount)).setText(String.valueOf(((ShopListViewItem) content).getAmount()));
            ((TextView)view.findViewById(R.id.itemType)).setText(((ShopListViewItem) content).getUnit());
            //view.setTag(content.getId());
            (view.findViewById(R.id.itemName)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final PopupMenu popUpCatMenu = new PopupMenu(getContext(), v);
                    for(int i = 0; i < list.size(); i++) {
                        if(list.get(i).getType() == ShopListViewContent.contentType.CATEGORY) {
                            popUpCatMenu.getMenu().add(((ShopListViewCategory) list.get(i)).getCatId(), 0, 0, ((ShopListViewCategory) list.get(i)).getName());
                        }

                    }
                    popUpCatMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            newItem.setName(((ShopListViewItem) content).getName());
                            newItem.setUnit(((ShopListViewItem) content).getUnit());
                            newItem.setAmount(((ShopListViewItem) content).getAmount());
                            FireBaseController.getI().insertItem(item.getGroupId(), ((ShopListViewItem) content).getItemId(), newItem);
                            FireBaseController.getI().deleteItem(((ShopListViewItem) content).getCategoryID(), ((ShopListViewItem) content).getItemId());

                            return true;
                        }
                    });
                 popUpCatMenu.show();
                }


            });

            // DELETE BUTTON
            view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FireBaseController.getI().deleteItem(((ShopListViewItem) content).getCategoryID(), ((ShopListViewItem) content).getItemId());
                }
            });
            // MINUS ONE BUTTON
            view.findViewById(R.id.minusOne).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newItem.setName(((ShopListViewItem) content).getName());
                    newItem.setUnit(((ShopListViewItem) content).getUnit());
                    newItem.setAmount(((ShopListViewItem) content).getAmount() - 1);
                    FireBaseController.getI().updateItem(((ShopListViewItem) content).getCategoryID(), ((ShopListViewItem) content).getItemId(), newItem);
                }
            });
            // PLUS ONE BUTTON
            view.findViewById(R.id.plusOne).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newItem.setName(((ShopListViewItem) content).getName());
                    newItem.setUnit(((ShopListViewItem) content).getUnit());
                    newItem.setAmount(((ShopListViewItem) content).getAmount() + 1);
                    FireBaseController.getI().updateItem(((ShopListViewItem) content).getCategoryID(),((ShopListViewItem) content).getItemId(),newItem);
                }
            });
            // item type edit text
            view.findViewById(R.id.itemType).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            // item amount edit text
            view.findViewById(R.id.itemAmount).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText editAmountText = (EditText) v.findViewById(R.id.itemAmount);
                    newItem.setName(((ShopListViewItem) content).getName());
                    newItem.setUnit(((ShopListViewItem) content).getUnit());
                    newItem.setAmount(Double.parseDouble(editAmountText.getText().toString()));
                    FireBaseController.getI().updateItem(((ShopListViewItem) content).getCategoryID(), ((ShopListViewItem) content).getItemId(),newItem);
                }
            });


        }
        else if(content.getType().equals(ShopListViewContent.contentType.CATEGORY)){
            view = mInflater.inflate(R.layout.list_category_view, null);
            ((TextView)view.findViewById(R.id.catName)).setText(((ShopListViewCategory) content).getName());
        }
        else if(content.getType().equals(ShopListViewContent.contentType.HEADER)){
            view = mInflater.inflate(R.layout.list_header_view, null);
            ((TextView)view.findViewById(R.id.headName)).setText(FireBaseController.getI().getActiveShopListName());
        }
        else if(content.getType().equals(ShopListViewContent.contentType.FOOTER)) {
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



