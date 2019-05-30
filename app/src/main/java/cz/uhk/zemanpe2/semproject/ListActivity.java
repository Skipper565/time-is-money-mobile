package cz.uhk.zemanpe2.semproject;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.otto.Bus;
import cz.uhk.zemanpe2.semproject.adapter.ListAdapter;
import cz.uhk.zemanpe2.semproject.api.TimeIsMoneyApiCalls;
import cz.uhk.zemanpe2.semproject.event.api.ApiErrorEvent;
import cz.uhk.zemanpe2.semproject.event.monthFinanceOverview.MonthFinanceOverviewResponseEvent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private String accessToken, refreshToken;
    private Integer position;
    private Long expiresIn;

    private String getAccessToken() {
        return accessToken;
    }

    private String getRefreshToken() {
        return refreshToken;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    private Long getExpiresIn() {
        return expiresIn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            accessToken = extras.getString("access_token");
            refreshToken = extras.getString("refresh_token");
            position = extras.getInt("position", 5000);
            expiresIn = extras.getLong("expires_in");
        }

        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.notifyDataSetChanged();
        final ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(position, false);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(position, false);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddActivity.class);
                i.putExtra("access_token", accessToken);
                i.putExtra("refresh_token", refreshToken);
                i.putExtra("position", position);
                i.putExtra("expires_in", expiresIn);
                view.getContext().startActivity(i);
            }
        });

    }

    /**
     * A month fragment containing a simple view.
     */
    public static class MonthFragment extends Fragment {

        private Bus bus;
        private String accessToken, refreshToken, date;
        private Long expiresIn;
        private TextView tWStartBalance, tWEndBalance;
        private ListView lW;
        private ListActivity parentActivity;
        private boolean isVisible;
        private TimeIsMoneyApiCalls api;

        public MonthFragment() {
            this.api = TimeIsMoneyApplication.buildApi();
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        static MonthFragment newInstance(int sectionNumber) {
            MonthFragment fragment = new MonthFragment();

            Calendar pagerdate = Calendar.getInstance();
            pagerdate.set(Calendar.DAY_OF_MONTH, 1);
            pagerdate.add(Calendar.MONTH, sectionNumber - 5000);

            Bundle args = new Bundle();
            args.putString("date", getDateFormat("dd-MM-yyyy").format(pagerdate.getTime()));
            args.putInt("position", sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        private static SimpleDateFormat getDateFormat(String format) {
            return new SimpleDateFormat(format, Locale.ENGLISH);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);

            TextView tWDate = rootView.findViewById(R.id.tWDate);
            tWStartBalance = rootView.findViewById(R.id.tWStartBalance);
            tWEndBalance = rootView.findViewById(R.id.tWEndBalance);
            lW = rootView.findViewById(R.id.lW);
            parentActivity = (ListActivity) getActivity();
            if (parentActivity != null && getArguments() != null) {
                accessToken = parentActivity.getAccessToken();
                refreshToken = parentActivity.getRefreshToken();
                expiresIn = parentActivity.getExpiresIn();
                if (isVisible) {
                    parentActivity.setPosition(getArguments().getInt("position"));
                }

                try {
                    Date parsedDate = getDateFormat("dd-MM-yyyy").parse(getArguments().getString("date"));
                    date = getDateFormat("MM-yyyy").format(parsedDate);
                    tWDate.setText(date);
                    callApi();
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
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                isVisible = true;
                if (parentActivity != null && getArguments() != null) {
                    parentActivity.setPosition(getArguments().getInt("position"));
                }
            } else {
                isVisible = false;
            }
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

        Bus getBus() {
            if (bus == null) {
                bus = TimeIsMoneyApplication.getBus();
            }

            return bus;
        }

        private void callApi() {
            Call<MonthFinanceOverviewResponseEvent> monthFinanceApiCall =
                    api.monthFinanceOverview(date, accessToken);
            monthFinanceApiCall.enqueue(new Callback<MonthFinanceOverviewResponseEvent>() {
                @Override
                public void onResponse(Call<MonthFinanceOverviewResponseEvent> call, Response<MonthFinanceOverviewResponseEvent> response) {
                    MonthFinanceOverviewResponseEvent monthFinanceOverview = response.body();
                    if (monthFinanceOverview == null) {
                        bus.post(new ApiErrorEvent("Month finance overview is null"));
                    } else {
                        tWStartBalance.setText(String.valueOf(monthFinanceOverview.getStartBalance()));
                        tWEndBalance.setText(String.valueOf(monthFinanceOverview.getEndBalance()));
                        lW.setAdapter(new ListAdapter(getContext(), monthFinanceOverview.getFinanceList()));
                    }
                }

                @Override
                public void onFailure(Call<MonthFinanceOverviewResponseEvent> call, Throwable throwable) {
                    bus.post(new ApiErrorEvent(throwable.getMessage()));
                }
            });
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MonthFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 10000;
        }
    }
}
