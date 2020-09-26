package com.cgg.pps.view;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import com.cgg.pps.R;
import com.cgg.pps.application.OPMSApplication;
import com.cgg.pps.fragment.ChangePasswordFragment;
import com.cgg.pps.fragment.ConfigFragment;
import com.cgg.pps.fragment.DashboardFragment;
import com.cgg.pps.fragment.DeviceMgmtFragment;
import com.cgg.pps.fragment.FAQFragment;
import com.cgg.pps.fragment.FarRegFragment;
import com.cgg.pps.fragment.FarUpdateFragment;
import com.cgg.pps.fragment.GunnysGivenFragment;
import com.cgg.pps.fragment.MappedPPCFragment;
import com.cgg.pps.fragment.PaddyProcurementFragment;
import com.cgg.pps.fragment.RePrintFragment;
import com.cgg.pps.fragment.RejectedTokenFragment;
import com.cgg.pps.fragment.TCFragment;
import com.cgg.pps.fragment.reports.ReportsFragment;
import com.cgg.pps.interfaces.DashboardFragmentInterface;
import com.cgg.pps.interfaces.PaddyTestInterface;
import com.cgg.pps.model.response.devicemgmt.paddyvalues.PaddyEntity;
import com.cgg.pps.model.response.validateuser.login.PPCUserDetails;
import com.cgg.pps.room.repository.PaddyTestRepository;
import com.cgg.pps.util.ConnectionDetector;
import com.cgg.pps.util.AppConstants;
import com.cgg.pps.util.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.TextUtils;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DashboardFragmentInterface,
        PaddyTestInterface, TCFragment.OnFragmentInteractionListener {
    private PaddyTestRepository paddyTestRepository;
    Fragment selectedFragment = null;
    private PPCUserDetails ppcUserDetails;
    private SharedPreferences sharedPreferences;
    private String cacheDate, currentDate;
    private NavigationView navigationViewPPCDetails;
    private TextView ppcDistrict, ppcMandal, ppcVillage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppConstants.FARG_TAG = null;
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        paddyTestRepository = new PaddyTestRepository(DashboardActivity.this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_version);
        menuItem.setTitle("App Version: " + Utils.getVersionName(this));
        menuItem.setEnabled(false);
        navigationView.setNavigationItemSelectedListener(this);

        getPreferenceData();

        AppConstants.FARG_TAG = DashboardFragment.class.getSimpleName();
        selectedFragment = new DashboardFragment();
        callFragment(selectedFragment, AppConstants.FARG_TAG);

        TextView textView = navigationView.getHeaderView(0).findViewById(R.id.ppcCode);

        //Fatal Exception: java.lang.RuntimeException
        //Unable to start activity ComponentInfo{com.cgg.pps/com.cgg.pps.view.DashboardActivity}:
        // java.lang.NullPointerException: Attempt to invoke virtual method
        // 'java.lang.String com.cgg.pps.model.response.validateuser.login.PPCUserDetails.getPPCCode()' on a null object reference

        //Need to Handle null check on ppcUserDetails
        if (ppcUserDetails != null && ppcUserDetails.getPPCCode()!=null && ppcUserDetails.getPPCName()!=null) {
            textView.setText(ppcUserDetails.getPPCCode() + ", " + ppcUserDetails.getPPCName());
        }

        displayPPCDetails();

    }

    private void getPreferenceData() {
        Gson gson = OPMSApplication.get(this).getGson();
        sharedPreferences = OPMSApplication.get(this).getPreferences();
        String string = sharedPreferences.getString(AppConstants.PPC_DATA, "");
        ppcUserDetails = gson.fromJson(string, PPCUserDetails.class);
        if (ppcUserDetails == null) {
            Utils.ShowDeviceSessionAlert(this,
                    getResources().getString(R.string.home),
                    getString(R.string.ses_expire_re),
                    getSupportFragmentManager());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_ppc_details) {

            if (!(AppConstants.FARG_TAG.equalsIgnoreCase(MappedPPCFragment.class.getSimpleName()))) {
                Utils.callPPCDetailsAlert(this,
                        getResources().getString(R.string.home),
                        getString(R.string.re_ppc_alert)
                        , getSupportFragmentManager());
            }

            return true;
        }

        if (id == R.id.action_re_print) {

            if (!(AppConstants.FARG_TAG.equalsIgnoreCase(RePrintFragment.class.getSimpleName()))) {
                Utils.callRePrintAlert(this,
                        getResources().getString(R.string.home),
                        getString(R.string.re_print_alert)
                        , getSupportFragmentManager());
            }

            return true;
        }

        if (id == R.id.action_logout) {
            Utils.customLogoutAlert(this,
                    getResources().getString(R.string.home),
                    getString(R.string.logout_alert), true, getSupportFragmentManager());
        }
        if (id == R.id.action_ppc) {
            if (ConnectionDetector.isConnectedToInternet(DashboardActivity.this)) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.END);
                return true;
            } else {
                Utils.customAlert(DashboardActivity.this,
                        getResources().getString(R.string.app_name),
                        getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.WARNING), false);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void displayPPCDetails() {
        try {
            navigationViewPPCDetails = (NavigationView) findViewById(R.id.nav_view_ppcDetails);
            TextView ppcName = navigationViewPPCDetails.getHeaderView(0).findViewById(R.id.homepage_userdetails_ppcNameTv);
            TextView ppcID = navigationViewPPCDetails.getHeaderView(0).findViewById(R.id.homepage_userdetails_ppcIdTv);
            TextView operaterName = navigationViewPPCDetails.getHeaderView(0).findViewById(R.id.homepage_userdetails_ppcOparatorName_Tv);
            TextView operaterMobile = navigationViewPPCDetails.getHeaderView(0).findViewById(R.id.homepage_userdetails_ppcOparatorMobile_Tv);
            ppcDistrict = navigationViewPPCDetails.getHeaderView(0).findViewById(R.id.homepage_userdetails_district);
            ppcMandal = navigationViewPPCDetails.getHeaderView(0).findViewById(R.id.homepage_userdetails_ppcmandal);
            ppcVillage = navigationViewPPCDetails.getHeaderView(0).findViewById(R.id.homepage_userdetails_ppcvillage);
            ImageView closeIV = navigationViewPPCDetails.getHeaderView(0).findViewById(R.id.closeIV);
            closeIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closePPC();
                }
            });
            Button homepageUserDetailsChangePwdBtn = navigationViewPPCDetails.getHeaderView(0).
                    findViewById(R.id.homepage_userDetails_changePwdBtn);
            if (ppcUserDetails != null) {
                ppcName.setText("" + ppcUserDetails.getPPCName());
                ppcID.setText("" + ppcUserDetails.getPPCID());
                operaterName.setText("" + ppcUserDetails.getCoordinatorName());
                operaterMobile.setText("" + ppcUserDetails.getCoordinatorMobile());

                paddyTestRepository.getDistrictName(this,
                        ppcUserDetails.getDistrictID().trim());

                paddyTestRepository.getMandalName(this,
                        ppcUserDetails.getDistrictID().trim(),
                        ppcUserDetails.getMandalID().trim());

                paddyTestRepository.getVillageName(this,
                        ppcUserDetails.getDistrictID().trim(),
                        ppcUserDetails.getMandalID().trim(),
                        ppcUserDetails.getVillageID().trim());
            }

            homepageUserDetailsChangePwdBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(AppConstants.FARG_TAG.equalsIgnoreCase(ChangePasswordFragment.class.getSimpleName()))) {
                        AppConstants.FARG_TAG = ChangePasswordFragment.class.getSimpleName();
                        selectedFragment = new ChangePasswordFragment();
                        callFragment(selectedFragment, AppConstants.FARG_TAG);
                        closePPC();
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closePPC() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            AppConstants.FARG_TAG = DashboardFragment.class.getSimpleName();
            selectedFragment = new DashboardFragment();
            callFragment(selectedFragment, AppConstants.FARG_TAG);


        } else if (id == R.id.nav_far_reg) {
            AppConstants.FARG_TAG = FarRegFragment.class.getSimpleName();
            selectedFragment = new FarRegFragment();
            callFragment(selectedFragment, AppConstants.FARG_TAG);


        } else if (id == R.id.nav_faq) {
            AppConstants.FARG_TAG = FAQFragment.class.getSimpleName();
            selectedFragment = new FAQFragment();
            callFragment(selectedFragment, AppConstants.FARG_TAG);


        } else if (id == R.id.nav_gunny) {
            AppConstants.FARG_TAG = GunnysGivenFragment.class.getSimpleName();
            selectedFragment = new GunnysGivenFragment();
            callFragment(selectedFragment, AppConstants.FARG_TAG);


        } else if (id == R.id.nav_procurement) {
            AppConstants.FARG_TAG = PaddyProcurementFragment.class.getSimpleName();
            selectedFragment = new PaddyProcurementFragment();
            callFragment(selectedFragment, AppConstants.FARG_TAG);


        } else if (id == R.id.nav_truckchit) {
            AppConstants.FARG_TAG = TCFragment.class.getSimpleName();
            selectedFragment = new TCFragment();
            callFragment(selectedFragment, AppConstants.FARG_TAG);


        } else if (id == R.id.nav_reports) {
            AppConstants.FARG_TAG = ReportsFragment.class.getSimpleName();
            selectedFragment = new ReportsFragment();
            callFragment(selectedFragment, AppConstants.FARG_TAG);


        } else if (id == R.id.nav_data_sync) {
            AppConstants.FARG_TAG = DeviceMgmtFragment.class.getSimpleName();
            selectedFragment = new DeviceMgmtFragment();
            callFragment(selectedFragment, AppConstants.FARG_TAG);


        } else if (id == R.id.nav_printer_pair) {
            AppConstants.FARG_TAG = ConfigFragment.class.getSimpleName();
            selectedFragment = new ConfigFragment();
            callFragment(selectedFragment, AppConstants.FARG_TAG);


        } else if (id == R.id.nav_rej_faq) {
            AppConstants.FARG_TAG = RejectedTokenFragment.class.getSimpleName();
            selectedFragment = new RejectedTokenFragment();
            callFragment(selectedFragment, AppConstants.FARG_TAG);


        } else if (id == R.id.nav_logout) {

            Utils.customLogoutAlert(this,
                    getResources().getString(R.string.home),
                    getString(R.string.logout_alert), true, getSupportFragmentManager());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void callFragment(Fragment fragment, String name) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.dashboard_container, fragment, name);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {

        try {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {

                Fragment fragment = getSupportFragmentManager().findFragmentByTag(AppConstants.FARG_TAG);

                if (fragment == null) {
                    Utils.customExitAlert(this,
                            getResources().getString(R.string.home),
                            getString(R.string.exit_from));
                } else if (fragment instanceof DashboardFragment) {
                    Utils.customExitAlert(this,
                            getResources().getString(R.string.home),
                            getString(R.string.exit_from));
                } else if (fragment instanceof FarUpdateFragment) {
                    selectedFragment = new FarRegFragment();
                    AppConstants.IS_CANCELLED = true;
                    callFragment(selectedFragment, AppConstants.FARG_TAG);
                    AppConstants.FARG_TAG = null;
                } else if (fragment instanceof ReportsFragment
                        || fragment instanceof ConfigFragment
                        || fragment instanceof RePrintFragment) {
                    selectedFragment = new DashboardFragment();

                    AppConstants.FARG_TAG = DashboardFragment.class.getSimpleName();
                    callFragment(selectedFragment, AppConstants.FARG_TAG);
                } else {
                    Toast.makeText(this, "Please use navigation to redirect", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            DashboardActivity.this.finish();
        }

    }

    @Override
    public void CallFragment(String fname) {
        if (fname.equalsIgnoreCase(getResources().getString(R.string.FarmerRegistration))) {
            AppConstants.FARG_TAG = FarRegFragment.class.getSimpleName();
            selectedFragment = new FarRegFragment();
        } else if (fname.equalsIgnoreCase(getResources().getString(R.string.FAQ))) {
            AppConstants.FARG_TAG = FAQFragment.class.getSimpleName();
            selectedFragment = new FAQFragment();
        } else if (fname.equalsIgnoreCase(getResources().getString(R.string.GunnysGivenToFarmer))) {
            AppConstants.FARG_TAG = GunnysGivenFragment.class.getSimpleName();
            selectedFragment = new GunnysGivenFragment();
        } else if (fname.equalsIgnoreCase(getResources().getString(R.string.PaddyProcurement))) {
            AppConstants.FARG_TAG = PaddyProcurementFragment.class.getSimpleName();
            selectedFragment = new PaddyProcurementFragment();
        } else if (fname.equalsIgnoreCase(getResources().getString(R.string.nav_reports))) {
            AppConstants.FARG_TAG = ReportsFragment.class.getSimpleName();
            selectedFragment = new ReportsFragment();
        } else if (fname.equalsIgnoreCase(getResources().getString(R.string.rejected_faqs))) {
            AppConstants.FARG_TAG = RejectedTokenFragment.class.getSimpleName();
            selectedFragment = new RejectedTokenFragment();
        } else if (fname.equalsIgnoreCase(getResources().getString(R.string.PPCtoMiller))) {
            AppConstants.FARG_TAG = TCFragment.class.getSimpleName();
            selectedFragment = new TCFragment();
        } else if (fname.equalsIgnoreCase(getResources().getString(R.string.DataSync))) {
            AppConstants.FARG_TAG = DeviceMgmtFragment.class.getSimpleName();
            selectedFragment = new DeviceMgmtFragment();
        } else if (fname.equalsIgnoreCase(getResources().getString(R.string.Printersetting))) {
            AppConstants.FARG_TAG = ConfigFragment.class.getSimpleName();
            selectedFragment = new ConfigFragment();
        }

        callFragment(selectedFragment, AppConstants.FARG_TAG);
    }

    public void setActionBarTitle(String title) {
        try {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            setActionBarTitle(getResources().getString(R.string.home));
            boolean isAutomatic = Utils.isTimeAutomatic(this);
            if (!isAutomatic) {
                Utils.customTimeAlert(this,
                        getResources().getString(R.string.home),
                        getString(R.string.date_time));
                return;
            }

            currentDate = Utils.getCurrentDate();
            cacheDate = sharedPreferences.getString(AppConstants.CACHE_DATE, "");

            if (!TextUtils.isEmpty(cacheDate)) {
                if (!cacheDate.equalsIgnoreCase(currentDate)) {
                    Utils.ShowDeviceSessionAlert(this,
                            getResources().getString(R.string.home),
                            getString(R.string.ses_expire_re),
                            getSupportFragmentManager());
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cacheDate = currentDate;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AppConstants.CACHE_DATE, cacheDate);
        editor.commit();
    }

    @Override
    public void getAllPaddyValues(List<PaddyEntity> paddyEntities) {

    }

    @Override
    public void paddyTestCount(int cnt) {

    }

    @Override
    public void getDistName(String distName) {
        ppcDistrict.setText(distName);
    }

    @Override
    public void getManName(String manName) {
        ppcMandal.setText(manName);
    }

    @Override
    public void getVilName(String vilName) {
        ppcVillage.setText(vilName);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

