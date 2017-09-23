package rocket.agile.com.mainlibrary.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rocket.agile.com.mainlibrary.R;
import rocket.agile.com.mainlibrary.fragments.AboutUsFragment;
import rocket.agile.com.mainlibrary.fragments.CallUsFragment;
import rocket.agile.com.mainlibrary.fragments.WebsiteFragment;
import rocket.agile.com.mainlibrary.model.DataManager;
import rocket.agile.com.mainlibrary.model.actionItems.ActionCall;
import rocket.agile.com.mainlibrary.model.actionItems.ActionEmail;
import rocket.agile.com.mainlibrary.model.actionItems.ActionStaff;

/**
 * Created by keithkowalski on 6/19/17.
 * <p>
 * Purpose:  Present side menu layout view to users
 * Function: A) List of action items presented in side menu; graphics and social media presented on home page
 */

public class LayoutView_SideMenu extends LayoutManager
        implements NavigationView.OnNavigationItemSelectedListener {

    // Call singleton class for data manager
    DataManager dataManager = DataManager.getInstance();
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set FontAwesome Library to be active for this class
        Iconify.with(new FontAwesomeModule());

        setContentView(R.layout.side_menu_activity_nav_drawer_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbarHeader = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbarHeader, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // CUSTOM BUILDS
        pullMenuItemsFromNetworkCall();
        updateView(toolbarHeader);
    }

    private void updateView(Toolbar toolbarHeader) {

        // PRIMARY COLOR
        View primaryBackground = findViewById(R.id.id_main);
        primaryBackground.setBackgroundColor(Color.parseColor(dataManager.primaryBackgroundColor));

        // PRIMARY HEADER COLOR AND TITLE
        toolbarHeader.setBackgroundColor(Color.parseColor(dataManager.primaryHeaderColor));

        // MENU TITLE (App Name)
        TextView appTitle = (TextView) toolbarHeader.findViewById(R.id.toolbar_title);
        appTitle.setText(dataManager.appName);
        appTitle.setTextSize(24);
        appTitle.setTextColor(Color.WHITE);

        // Access drawer header imageView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ImageView imageView = (ImageView) header.findViewById(R.id.drawerHeaderImageView);
        imageView.setImageResource(R.drawable.agile_rocket_logo);
    }

    //    Pass each action item's name, font awesome icon, and actionType value (int)
    public void pullMenuItemsFromNetworkCall() {
        // MENU BUTTONS TO CREATE

        // FIRST ATTEMPT TO TRAVERSE ALL CLASSES -----------------------------------------
//        Realm realm = Realm.getDefaultInstance();
//        ArrayList<Object> actionList = new ArrayList<>();
//
//        for(Class actionClass: dataManager.actionClasses) {
//            RealmResults<RealmObject> actionResults = realm.where(actionClass).findAll();
//            Object[] actions = actionResults.toArray();
//            ArrayList<Object> actionsList = new ArrayList(Arrays.asList(actions));
//            actionList.addAll(actionsList);
//
//            Log.d("actionResults", actionResults.toString());
//        }
//
//        Object[] allActions = actionList.toArray();

//        for(Object obj: allActions) {

//            String rawName = obj.getClass().getSimpleName();
//            if(rawName.endsWith("RealmProxy")) {
        // Filter out class name
//                String className = rawName.substring(0, rawName.indexOf("RealmProxy"));
        // Run switch case over substring of raw name
//        ---------------------------------------------------------------------------------

        for (Class actionClass : dataManager.actionClasses) {
            switch (actionClass.getSimpleName()) {
                case "ActionEmail":
                    if (dataManager.actionEmail.size() > 0) {
                        for (ActionEmail actionEmail : dataManager.actionEmail) {
                            buildMenu(actionEmail.getName(), actionEmail.getFAIcon(), actionEmail.getActionType());
                        }
                        Log.d(dataManager.SIDE_MENU_TAG, "CREATE ACTION EMAIL BUTTONS");
                    }
                    break;
                case "ActionCall":
                    if (dataManager.actionCall.size() > 0) {
                        for (ActionCall actionCall : dataManager.actionCall) {
                            buildMenu(actionCall.getName(), actionCall.getFAIcon(), actionCall.getActionType());
                        }
                        Log.d(dataManager.SIDE_MENU_TAG, "CREATE ACTION CALL BUTTONS");
                    }
                    break;
                case "ActionStaff":
                    if (dataManager.actionStaff.size() > 0) {
                        for (ActionStaff actionStaff : dataManager.actionStaff) {
                            buildMenu(actionStaff.getName(), actionStaff.getFAIcon(), actionStaff.getActionType());
                        }
                        Log.d(dataManager.SIDE_MENU_TAG, "CREATE ACTION STAFF BUTTONS");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    //    Set each button based on data passed in from list data
    public void buildMenu(String title, String icon, int itemID) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Add item to menu
        Menu menu = navigationView.getMenu();
        menu.add(0, itemID, 0, title).setIcon(new IconDrawable(this, icon));
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Home Button ID
        int homeID = item.getItemId();
        // All other buttons IDs
        int menuButtonsID = item.getItemId();
        Bundle bundle = new Bundle();

        FragmentManager manager = getSupportFragmentManager();
        WebsiteFragment websiteFragment = new WebsiteFragment();
        AboutUsFragment aboutUsFragment = new AboutUsFragment();
        CallUsFragment callUsFragment   = new CallUsFragment();

        // Set actionItem title as key that tells our fragment which actionItem name was tapped
        String actionTitle = item.getTitle().toString();    // item is MenuItem passed in to this method
        bundle.putString("title", actionTitle);

        // Home Button
        if (homeID == R.id.nav_home && manager.findFragmentById(R.id.relative_layout_for_fragment) != null) {
            manager.beginTransaction().remove(manager.findFragmentById(R.id.relative_layout_for_fragment)).commit();
        }

        // ** NOTE ** menuButtonsID was created from 'actionType' of each Realm class!
        switch (menuButtonsID) {
            // ActionEmail
            case 0:
                // Email class
                ActionEmail actionEmailSelected = null;
                // Determine which email action item was selected
                for (ActionEmail actionEmail : dataManager.actionEmail) {
                    if (actionEmail.getName().contentEquals(actionTitle)) {
                        // Set action email here, based on title
                        actionEmailSelected = actionEmail;
                    }
                }
                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{actionEmailSelected.getEmailAddress()});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, actionEmailSelected.getSubject());
                this.startActivity(emailIntent);
                break;

            // ActionStaff
            case 1:
                // Send name of actionItem to fragment based on what user tapped
                aboutUsFragment.setArguments(bundle);
                manager.beginTransaction().replace(
                        R.id.relative_layout_for_fragment,
                        aboutUsFragment,
                        aboutUsFragment.getTag()).commit();
                break;
            // ActionCall
            case 2:
                // Send name of actionItem to fragment based on what user tapped
                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
                callUsFragment.setArguments(bundle);
                callUsFragment.show(manager, callUsFragment.getTag());
                break;
            // Default
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}