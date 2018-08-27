package cz.uhk.zemanpe2.semproject;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import cz.uhk.zemanpe2.semproject.adapter.ListAdapter;
import cz.uhk.zemanpe2.semproject.event.monthFinanceOverview.MonthFinanceOverviewRequestEvent;
import cz.uhk.zemanpe2.semproject.event.monthFinanceOverview.MonthFinanceOverviewResponseEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private String accessToken, refreshToken;
    private Long expiresIn;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            accessToken = extras.getString("access_token");
            refreshToken = extras.getString("refresh_token");
            expiresIn = extras.getLong("expires_in");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddActivity.class);
                i.putExtra("access_token", accessToken);
                i.putExtra("refresh_token", refreshToken);
                i.putExtra("expires_in", expiresIn);
                view.getContext().startActivity(i);
            }
        });

    }

    /**
     * A month fragment containing a simple view.
     */
    public static class MonthFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private Bus bus;
        private String accessToken, refreshToken;
        private Long expiresIn;
        private TextView tWStartBalance, tWEndBalance;
        private ListView lW;

        public MonthFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MonthFragment newInstance(int sectionNumber) {
            MonthFragment fragment = new MonthFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);

            tWStartBalance = rootView.findViewById(R.id.tWStartBalance);
            tWEndBalance = rootView.findViewById(R.id.tWEndBalance);
            lW = rootView.findViewById(R.id.lW);
            ListActivity parentActivity = (ListActivity) getActivity();
            if (parentActivity != null) {
                accessToken = parentActivity.getAccessToken();
                refreshToken = parentActivity.getRefreshToken();
                expiresIn = parentActivity.getExpiresIn();

                DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                try {
                    MonthFinanceOverviewRequestEvent monthFinanceOverviewRequestEvent =
                            new MonthFinanceOverviewRequestEvent(df.parse("01-05-2018"), accessToken);
                    getBus().post(monthFinanceOverviewRequestEvent);
                } catch (ParseException e) {
                    Toast.makeText(
                            parentActivity.getApplicationContext(),
                            getString(R.string.error_service_unavailable),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();

            getBus().register(this);
        }

        @Override
        public void onPause() {
            super.onPause();

            getBus().unregister(this);
        }

        @Subscribe
        public void onMonthFinanceOverviewResponse(MonthFinanceOverviewResponseEvent event) {
            tWStartBalance.setText(String.valueOf(event.getStartBalance()));
            tWEndBalance.setText(String.valueOf(event.getEndBalance()));
            lW.setAdapter(new ListAdapter(this.getContext(), event.getFinanceList()));
        }

        public Bus getBus() {
            if (bus == null) {
                bus = TimeIsMoneyApplication.getBus();
            }

            return bus;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a MonthFragment (defined as a static inner class below).
            return MonthFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
