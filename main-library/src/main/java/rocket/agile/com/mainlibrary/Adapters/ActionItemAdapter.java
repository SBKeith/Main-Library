package rocket.agile.com.mainlibrary.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.joanzapata.iconify.IconDrawable;

import java.util.ArrayList;

import rocket.agile.com.mainlibrary.model.Custom.ActionItemData;
import rocket.agile.com.mainlibrary.model.DataManager;

/**
 * Created by KeithK on 10/12/17.
 */

public class ActionItemAdapter extends BaseAdapter {

    private DataManager dataManager = DataManager.getInstance();
    private Context context;
    private ArrayList<ActionItemData> availableActionItemsList;
    int actionItemIndex;

    public ActionItemAdapter(Context context, ArrayList<ActionItemData> actionItemDataArrayList) {
        this.context = context;
        this.availableActionItemsList = actionItemDataArrayList;
    }

    @Override
    public int getCount() {

        return availableActionItemsList.size();
    }

    @Override
    public Object getItem(int i) {

        return availableActionItemsList.get(i).actionItem;
    }

    @Override
    public long getItemId(int i) {

        // Get index of actionItem stored
        return availableActionItemsList.get(i).actionItemIndex;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {

        ImageView imageView;
        String className = null;

        if(view == null) {

            imageView = new ImageView(this.context);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);

            // Filter out class name from provided string name of each available actionItem class
            String rawName = availableActionItemsList.get(i).actionItem.getClass().getSimpleName();
            if(rawName.endsWith("RealmProxy")) {
                className = rawName.substring(0, rawName.indexOf("RealmProxy"));
            }
            // Get index of actionItem stored
            actionItemIndex = availableActionItemsList.get(i).actionItemIndex;

            switch(className) {
                case "ActionEmail":
                    imageView.setImageDrawable(new IconDrawable(this.context, dataManager.actionEmail.get(actionItemIndex).getFAIcon())
                            .color(Color.RED)
                            .sizeDp(20));
                    break;
                case "ActionCall":
                    imageView.setImageDrawable(new IconDrawable(this.context, dataManager.actionCall.get(actionItemIndex).getFAIcon())
                            .color(Color.RED)
                            .sizeDp(20));
                    break;
                case "ActionStaff":
                    imageView.setImageDrawable(new IconDrawable(this.context, dataManager.actionStaff.get(actionItemIndex).getFAIcon())
                            .color(Color.RED)
                            .sizeDp(20));
                    break;
                default:
                    break;
            }
        } else {
            imageView = (ImageView) view;
        }
        return imageView;
    }


}

