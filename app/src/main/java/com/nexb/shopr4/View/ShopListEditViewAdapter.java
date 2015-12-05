package com.nexb.shopr4.View;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.nexb.shopr4.FireBaseController;
import com.nexb.shopr4.MainActivity;
import com.nexb.shopr4.R;
import com.nexb.shopr4.dataModel.ListItem;

import java.util.ArrayList;

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
            view = mInflater.inflate(R.layout.list_item_edit_view, parent, false);


            //add listViewItem
            ((TextView)view.findViewById(R.id.itemName)).setText(((ShopListViewItem) content).getName());
            ((TextView) view.findViewById(R.id.itemAmount)).setText(String.valueOf(((ShopListViewItem) content).getAmount()));
            ((TextView)view.findViewById(R.id.itemType)).setText(((ShopListViewItem) content).getUnit());

            (view.findViewById(R.id.itemName)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final PopupMenu popUpCatMenu = new PopupMenu(getContext(), v);
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getType() == ShopListViewContent.contentType.CATEGORY) {
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
                            return false;
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

                    if(((ShopListViewItem) content).getAmount()>= 1) {
                        newItem.setAmount(((ShopListViewItem) content).getAmount() - 1);
                    }else{
                        newItem.setAmount(((ShopListViewItem) content).getAmount());
                    }

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
                    FireBaseController.getI().updateItem(((ShopListViewItem) content).getCategoryID(), ((ShopListViewItem) content).getItemId(), newItem);
                }
            });
            // item type edit text
            view.findViewById(R.id.itemType).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final PopupMenu popUpTypeMenu = new PopupMenu(getContext(), v);
                    popUpTypeMenu.getMenuInflater().inflate(R.menu.pop_up_type_menu, popUpTypeMenu.getMenu());
                    popUpTypeMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            newItem.setName(((ShopListViewItem) content).getName());
                            newItem.setAmount(((ShopListViewItem) content).getAmount());
                            newItem.setUnit(item.getTitle().toString());
                            ((EditText)v.findViewById(R.id.itemType)).setText(item.getTitle());
                            FireBaseController.getI().updateItem(((ShopListViewItem) content).getCategoryID(), ((ShopListViewItem) content).getItemId(), newItem);
                            return false;
                        }
                    });
                    popUpTypeMenu.show();
                }
            });
            ((EditText) view.findViewById(R.id.itemType)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    newItem.setName(((ShopListViewItem) content).getName());
                    newItem.setUnit(((EditText) v.findViewById(R.id.itemType)).getText().toString());
                    newItem.setAmount(((ShopListViewItem) content).getAmount());
                    FireBaseController.getI().updateItem(((ShopListViewItem) content).getCategoryID(), ((ShopListViewItem) content).getItemId(), newItem);
                    //MainActivity.hideKeyboard();
                    return true;
                }
            });

            // item amount edit text
            ((TextView) view.findViewById(R.id.itemAmount)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    EditText editAmountText = (EditText) v.findViewById(R.id.itemAmount);
                    newItem.setName(((ShopListViewItem) content).getName());
                    newItem.setUnit(((ShopListViewItem) content).getUnit());
                    newItem.setAmount(Double.parseDouble(editAmountText.getText().toString()));
                    FireBaseController.getI().updateItem(((ShopListViewItem) content).getCategoryID(), ((ShopListViewItem) content).getItemId(), newItem);
                   // MainActivity.hideKeyboard();
                    return true;
                }
            });


        }
        else if(content.getType().equals(ShopListViewContent.contentType.CATEGORY)){
            view = mInflater.inflate(R.layout.list_category_edit_view, parent, false);

            ((EditText)view.findViewById(R.id.catEditName)).setText(((ShopListViewCategory) content).getName());
            ((EditText)view.findViewById(R.id.catEditName)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    FireBaseController.getI().updateCategoryname(((ShopListViewCategory) content).getCatId(),((EditText)v.findViewById(R.id.catEditName)).getText().toString());
                    return true;
                }
            });


            view.findViewById(R.id.catEditDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FireBaseController.getI().deleteCategory(((ShopListViewCategory) content).getCatId());
                }
            });

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



