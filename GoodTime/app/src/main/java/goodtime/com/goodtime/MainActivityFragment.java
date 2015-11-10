package goodtime.com.goodtime;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.achartengine.GraphicalView;

import java.util.ArrayList;
import java.util.List;

import core.GoodTimeCalculator;
import core.HoraRange;
import core.InfoHolder;
import core.Utils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks , GoogleApiClient.OnConnectionFailedListener {

    private static String LOG_TAG="MainActivityFragment";

    private GoogleApiClient mGoogleApiClient;

    private HoraAdapter  mHoraAdapter;

    private Location mLastLocation;
    private TextView mLatitudeText;
    private TextView mLongitudeText;

    private TextView mReasonText;

    private TextView mYourStarText;

    private TextView mForDate;
    private TextView mSunriseText;
    private TextView mSunsetText;

    private TextView mCurrentStar;

    private TextView mCurrentStatusText;

    private ImageView mCurrentStatus;

    private TextView mCurrentHora;

    private TextView mAdress;

    private String[] mStarsArray;

    private String[] mPlanetsArray;

    private LinearLayout mChartContainer;

//    private ScrollView mChartScrollContainer;

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
//        mHoraAdapter =
//                new ArrayAdapter<String>(
//                        getActivity(), // The current context (this activity)
//                        R.layout.list_item_hora, // The name of the layout ID.
//                        R.id.list_item_hora_textview, // The ID of the textview to populate.
//                        new ArrayList<String>());
        List<HoraRange> horaList = new ArrayList<>();

        mHoraAdapter = new HoraAdapter(getActivity().getApplicationContext(), horaList);
        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_horas);
        listView.setAdapter(mHoraAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String forecast = mHoraAdapter.getItem(position);
//                Intent intent = new Intent(getActivity(), DetailActivity.class)
//                        .putExtra(Intent.EXTRA_TEXT, forecast);
//                startActivity(intent);
//            }
//        });

//        mForDate = (TextView) rootView.findViewById(R.id.fordate);
//        mLatitudeText = (TextView) rootView.findViewById(R.id.latitude);
//        mLongitudeText= (TextView) rootView.findViewById(R.id.longitude);
//        mAdress= (TextView) rootView.findViewById(R.id.address);
//
//        mSunriseText  = (TextView) rootView.findViewById(R.id.sunrise);
//        mSunsetText  = (TextView) rootView.findViewById(R.id.sunset);

        mCurrentStatus  = (ImageView) rootView.findViewById(R.id.currentStatus);

        mCurrentStatusText = (TextView) rootView.findViewById(R.id.currentStatusText);

        mCurrentStar = (TextView) rootView.findViewById(R.id.currentStar);

        mCurrentHora = (TextView) rootView.findViewById(R.id.currentHora);

        mStarsArray = getResources().getStringArray(R.array.stars_array);

        mPlanetsArray = getResources().getStringArray(R.array.planets_array);

        mYourStarText = (TextView) rootView.findViewById(R.id.yourStarTextView);
        mReasonText= (TextView) rootView.findViewById(R.id.reasonText);

        mChartContainer = (LinearLayout) rootView.findViewById(R.id.chart_container);

//        mChartScrollContainer = (ScrollView) rootView.findViewById(R.id.chart_scroll_container);


//        FetchSunInfoTask weatherTask = new FetchSunInfoTask();
//        weatherTask.execute("dd");

        buildGoogleApiClient();
        mGoogleApiClient.connect();
        return rootView;
    }

    //for onconnected to work, we should have google map open at least once.
    @Override
    public void onConnected(Bundle connectionHint) {

        refreshView();

    }

    private void refreshView() {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
//            mLatitudeText.setText(getResources().getString(R.string.longitude) + " : " +  String.valueOf(mLastLocation.getLongitude()));
//            mLongitudeText.setText(getResources().getString(R.string.latitude)+ " : " +  String.valueOf(mLastLocation.getLatitude()));

            InfoHolder info = GoodTimeCalculator.calculateMoon(mLastLocation.getLongitude(), mLastLocation.getLatitude(), 0);

//                   mSunriseText.setText(getResources().getString(R.string.sunrise) + " : " +  String.valueOf(info.getSunriseTime().getTime()));
//            mSunsetText.setText(getResources().getString(R.string.sunset) + " : " + String.valueOf(info.getSunsetTime().getTime()));
//
//            mForDate.setText(getResources().getString(R.string.fordate) + " : " + Calendar.getInstance().getTime());
            int curStarIndex =  Utils.getCurrentStar(info);

            mCurrentStar.setText(mStarsArray[curStarIndex]);

            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String yourStar = sharedPrefs.getString(
                    getString(R.string.your_star_key),
                    getString(R.string.your_star_default));

            mYourStarText.setText(yourStar);

            boolean isGoodStar = Utils.isGoodStar(mStarsArray, curStarIndex, yourStar);

            boolean isGoodTime  = Utils.isGoodTime(info);
            if( isGoodTime && isGoodStar) {

                mCurrentStatus.setImageResource(R.drawable.happy_smiley);
                mCurrentStatusText.setText(getResources().getString(R.string.goodStatusText));
                mReasonText.setText(getResources().getString(R.string.allGood));
            }else{

                mCurrentStatus.setImageResource(R.drawable.sad_smiley);
                mCurrentStatusText.setText(getResources().getString(R.string.badStatusText));

                if(isGoodTime && !isGoodStar){
                    mReasonText.setText(getResources().getString(R.string.horaGood));
                }else if(!isGoodTime && isGoodStar){
                    mReasonText.setText(getResources().getString(R.string.starGood));
                }else{
                    mReasonText.setText(getResources().getString(R.string.noneGood));
                }


            }
            mHoraAdapter.setValues(info.getHoras());

            int curHoraIndex =  Utils.getCurrentHora(info);

            String currentHoraPlanet = mPlanetsArray[info.getHoras().get(curHoraIndex).getPlanet()];

            mCurrentHora.setText(currentHoraPlanet);

            int startAngle = Utils.getStartingAngle(info.getSunriseTime());

            String[] labels = Utils.getLabels(info);

            ClockBuilder bpc = new ClockBuilder();
            GraphicalView pieChartView = bpc.buildClock(getActivity(), getResources().getString(R.string.dayChart), startAngle, info.getHoraTime(), labels );

            pieChartView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));

            mChartContainer.removeAllViews();

            mChartContainer.addView(pieChartView);

            startAngle = Utils.getStartingAngle(info.getSunsetTime());

            GraphicalView secondChartView = bpc.buildClock(getActivity(), getResources().getString(R.string.nightChart), startAngle, info.getHoraTime(), labels);

            secondChartView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));

            mChartContainer.addView(secondChartView);

//            for(HoraRange horaRange : info.getHoras()) {
//                mHoraAdapter.add( horaRange );
//            }

//            mAdress.setText(info.toString());
//            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//            String result = null;
//            try {
//                List<Address> addressList = geocoder.getFromLocation(
//                        mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
//                if (addressList != null && addressList.size() > 0) {
//                    Address address = addressList.get(0);
//                    StringBuilder sb = new StringBuilder();
//                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                        sb.append(address.getAddressLine(i)).append("\n");
//                    }
//                    sb.append(address.getLocality()).append("\n");
//                    sb.append(address.getPostalCode()).append("\n");
//                    sb.append(address.getCountryName());
//
//                    result = sb.toString();
//                    mAdress.setText(result);
//                }
//            } catch (IOException e) {
//                Log.e(LOG_TAG, "Unable connect to Geocoder", e);
//            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("from fragment","test");
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("from fragment", "test");
    }

}
