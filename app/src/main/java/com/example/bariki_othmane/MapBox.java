package com.example.bariki_othmane;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.Property.LINE_JOIN_ROUND;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * When a RecyclerView item is tapped on, display the Mapbox Directions API route
 * associated with the item.
 */
public class MapBox extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, PermissionsListener {

    public ArrayList<UserHelper> user2 = new ArrayList<>();
    private static final String TAG = "RVDirectionsActivity";
    private static final String SYMBOL_ICON_ID = "SYMBOL_ICON_ID";
    private static final String PERSON_ICON_ID = "PERSON_ICON_ID";
    private static final String MARKER_SOURCE_ID = "MARKER_SOURCE_ID";
    private static final String PERSON_SOURCE_ID = "PERSON_SOURCE_ID";
    private static final String DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID = "DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID";
    private static final String LAYER_ID = "LAYER_ID";
    private static final String PERSON_LAYER_ID = "PERSON_LAYER_ID";
    private static final String DASHED_DIRECTIONS_LINE_LAYER_ID = "DASHED_DIRECTIONS_LINE_LAYER_ID";

    public static ArrayList<LatLng> possibleDestinations = new ArrayList<>();
    private final List<DirectionsRoute> directionsRouteList = new ArrayList<>();
    private MapboxMap mapboxMap;
    private MapView mapView;
    private FeatureCollection dashedLineDirectionsFeatureCollection;
    //current position
    private PermissionsManager permissionsManager;
    //Drawer Menu
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    LinearLayout contentView,recycle;
    RecyclerView users_recycler;
    //driver
    static UserHelper driver = new UserHelper();
    //user
    static UserHelperCo user = new UserHelperCo();
    public static String callerV;
    TextView username_;
    DatabaseReference reference;
    RecyclerView.Adapter adapter;
    ImageView img;
    public static MyThread thread = new MyThread();


    // Variables needed to add the location engine
    private LocationEngine locationEngine;
    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    // Variables needed to listen to location updates
    private MainActivityLocationCallback callback = new MainActivityLocationCallback(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, "pk.eyJ1IjoibWF0dXNzZmUiLCJhIjoiY2tiNW8zNzhzMHJnODJ5bzZ4ZTczbzlydCJ9.EE5FCmoOrYFo3jwav2eILA");


        //open Firebase
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("Driver");
        user2.clear();
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                possibleDestinations.clear();
                for (DataSnapshot x : dataSnapshot.getChildren()) {

                    String fullName = dataSnapshot.child(x.getKey()).child("fullName").getValue(String.class);
                    String gender = dataSnapshot.child(x.getKey()).child("gender").getValue(String.class);
                    String birthday = dataSnapshot.child(x.getKey()).child("birthday").getValue(String.class);
                    String email = dataSnapshot.child(x.getKey()).child("email").getValue(String.class);
                    String phoneNumber = dataSnapshot.child(x.getKey()).child("phoneNumber").getValue(String.class);
                    String imgUrl = dataSnapshot.child(x.getKey()).child("Image_Url").getValue(String.class);
                    String ext = dataSnapshot.child(x.getKey()).child("ext").getValue(String.class);
                    //--vehicule info
                    String cin = dataSnapshot.child(x.getKey()).child("cin").getValue(String.class);
                    String numIimmatriculation = dataSnapshot.child(x.getKey()).child("numIimmatriculation").getValue(String.class);
                    double Latitude = dataSnapshot.child(x.getKey()).child("latitude").getValue(double.class);
                    double Longitude = dataSnapshot.child(x.getKey()).child("longitude").getValue(double.class);
                    possibleDestinations.add(
                            new LatLng(Latitude, Longitude)
                    );


                    user2.add(
                            new UserHelper(fullName, email, x.getKey(), gender, birthday, phoneNumber, cin, numIimmatriculation, ext, imgUrl)
                    );

                }
                FetchDrivers.Drivers.clear();
                FetchDrivers.Drivers = user2;
                //notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_map_box);
        //menu

        String caller = getIntent().getStringExtra("caller");
        callerV = caller;
        if (caller.equals("CapitainLogin")) {
            UserHelper user1 = (UserHelper) getIntent().getSerializableExtra("currentCapitain");
            driver = user1;
        } else if (caller.equals("activity2")) {
            UserHelperCo user1 = (UserHelperCo) getIntent().getSerializableExtra("currentUser");
            user = user1;


        }

        // Hooks Drawer Menu
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        menuIcon = (ImageView) findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        //hooks username


        // Initialize the map view
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(MapBox.this);
        //menu

        //navigation Drawer
        navifationDrawer();
        //vehicule

        Menu menu = navigationView.getMenu();
        if (callerV.equals("CapitainLogin")) {
            menu.findItem(R.id.nav_vehicule).setVisible(true);
        }else{
            menu.findItem(R.id.nav_vehicule).setVisible(false);
        }

    }

    //menu


    private void navifationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        //by default the nav_profile is selected
        navigationView.setCheckedItem(R.id.nav_profile);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username_ = findViewById(R.id.fullname);
                if (callerV.equals("CapitainLogin")) {
                    username_.setText(driver.getUsername());
                } else {
                    username_.setText(user.getUsername());
                }

                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {

                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        animateDrawerActivity();
    }

    private void animateDrawerActivity() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.mapp));

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                username_ = findViewById(R.id.fullname);
                if (callerV.equals("CapitainLogin")) {
                    username_.setText(driver.getUsername());
                } else {
                    username_.setText(user.getUsername());
                }
                final float diff = slideOffset * (1 - 0.7f);
                final float off = 1 - diff;
                contentView.setScaleX(off);
                contentView.setScaleY(off);
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diff / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

        }

    }
    public NavigationView getNavigationView() {
        return navigationView;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_vehicule:

                final Intent intent10 = new Intent(getApplicationContext(), vehicule.class);
                intent10.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent10.putExtra("user",(Serializable) driver);
                startActivity(intent10);
                break;


            case R.id.nav_aide:
                final Intent intent = new Intent(getApplicationContext(), Help.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.nav_profile:
                if (callerV.equals("CapitainLogin")) {
                    final UserHelper Driver = new UserHelper();
                    final String username = driver.getUsername();
                    Driver.setUsername(username);
                    reference = FirebaseDatabase.getInstance().getReference("Driver");
                    final Query checkUser = reference.orderByChild("username").equalTo(username);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String fullName = dataSnapshot.child(username).child("fullName").getValue(String.class);
                            String gender = dataSnapshot.child(username).child("gender").getValue(String.class);
                            String birthday = dataSnapshot.child(username).child("birthday").getValue(String.class);
                            String email = dataSnapshot.child(username).child("email").getValue(String.class);
                            String phoneNumber = dataSnapshot.child(username).child("phoneNumber").getValue(String.class);
                            String password = dataSnapshot.child(username).child("password").getValue(String.class);
                            String imgUrl = dataSnapshot.child(username).child("Image_Url").getValue(String.class);
                            //--vehicule info
                            String cin = dataSnapshot.child(username).child("cin").getValue(String.class);
                            String numIimmatriculation = dataSnapshot.child(username).child("numIimmatriculation").getValue(String.class);


                            Driver.setFullName(fullName);
                            Driver.setGender(gender);
                            Driver.setBirthday(birthday);
                            Driver.setEmail(email);
                            Driver.setPassword(password);
                            Driver.setUsername(username);
                            Driver.setPhoneNumber(phoneNumber);
                            Driver.setCin(cin);
                            Driver.setNumIimmatriculation(numIimmatriculation);
                            Driver.setImage_URL(imgUrl);

                            //process 4 update
                            img = findViewById(R.id.photo);
                            TextView nomAnim = findViewById(R.id.fullname);
                            //process 4 update
                            Pair[] pairs = new Pair[2];
                            pairs[0] = new Pair<View, String>(img, "photo_animate");
                            pairs[1] = new Pair<View, String>(nomAnim, "fullname_anime");
                            Intent intent1 = new Intent(getApplicationContext(), Profile.class);
                            intent1.putExtra("caller", "capitainLogin");
                            intent1.putExtra("driver", (Serializable) Driver);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MapBox.this, pairs);
                                startActivity(intent1, options.toBundle());
                            } else {
                                startActivity(intent1);

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    final UserHelperCo User = new UserHelperCo();
                    final String username = user.getUsername();
                    User.setUsername(username);
                    reference = FirebaseDatabase.getInstance().getReference("Costumers");
                    final Query checkUser = reference.orderByChild("username").equalTo(username);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String fullName = dataSnapshot.child(username).child("fullName").getValue(String.class);
                            String gender = dataSnapshot.child(username).child("gender").getValue(String.class);
                            String birthday = dataSnapshot.child(username).child("birthday").getValue(String.class);
                            String email = dataSnapshot.child(username).child("email").getValue(String.class);
                            String phoneNumber = dataSnapshot.child(username).child("phoneNumber").getValue(String.class);
                            String password = dataSnapshot.child(username).child("password").getValue(String.class);
                            String imgUrl = dataSnapshot.child(username).child("Image_Url").getValue(String.class);


                            //--vehicule info

                            User.setImage_URL(imgUrl);
                            User.setFullName(fullName);
                            User.setGender(gender);
                            User.setBirthday(birthday);
                            User.setEmail(email);
                            User.setPassword(password);
                            User.setUsername(username);
                            User.setPhoneNumber(phoneNumber);

                            img = findViewById(R.id.photo);
                            TextView nomAnim = findViewById(R.id.fullname);
                            //process 4 update
                            Pair[] pairs = new Pair[2];
                            pairs[0] = new Pair<View, String>(img, "photo_animate");
                            pairs[1] = new Pair<View, String>(nomAnim, "fullname_anime");
                            Intent intent1 = new Intent(getApplicationContext(), Profile.class);
                            intent1.putExtra("caller", "activity2");
                            intent1.putExtra("user", (Serializable) User);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MapBox.this, pairs);
                                startActivity(intent1, options.toBundle());
                            } else {
                                startActivity(intent1);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                break;
            case R.id.nav_logout:
                if (callerV.equals("CapitainLogin")) {
                    Intent intent1 = new Intent(getApplicationContext(), CapitainLogin.class);
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Driver");
                    String username = driver.getUsername();
                    reference.child(username).child("auth").setValue(false);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.putExtra("caller", "menu");
                    startActivity(intent1);
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), activity2.class);
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Costumers");
                    String username = user.getUsername();
                    reference.child(username).child("auth").setValue(false);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.putExtra("caller", "menu");
                    startActivity(intent1);
                }
                break;


        }
        return true;
    }


    //fin menu


    private static final Point origine = Point.fromLngLat(-7.647326, 33.518172);

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        MapBox.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(new Style.Builder().fromUri(Style.DARK)
                // Set up the image, source, and layer for the person icon,
                // which is where all of the routes will start from

                .withSource(new GeoJsonSource(PERSON_SOURCE_ID,
                        Feature.fromGeometry(origine)))
                .withLayer(new SymbolLayer(PERSON_LAYER_ID, PERSON_SOURCE_ID).withProperties(
                        iconSize(2f),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true)
                ))

                // Set up the image, source, and layer for the potential destination markers
                .withImage(SYMBOL_ICON_ID, BitmapFactory.decodeResource(
                        this.getResources(), R.drawable.truck_map))
                .withSource(new GeoJsonSource(MARKER_SOURCE_ID, initDestinationFeatureCollection()))
                .withLayer(new SymbolLayer(LAYER_ID, MARKER_SOURCE_ID).withProperties(
                        iconImage(SYMBOL_ICON_ID),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true),
                        iconOffset(new Float[]{0f, -4f})
                ))

                // Set up the source and layer for the direction route LineLayer
                .withSource(new GeoJsonSource(DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID))
                .withLayerBelow(
                        new LineLayer(DASHED_DIRECTIONS_LINE_LAYER_ID, DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID)
                                .withProperties(
                                        lineWidth(7f),
                                        lineJoin(LINE_JOIN_ROUND),
                                        lineColor(Color.parseColor("#FF835C"))
                                ), PERSON_LAYER_ID), new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                getRoutesToAllPoints();
                initRecyclerView();

            }
        });
    }

    public void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }


    //current position
    @SuppressWarnings({"MissingPermission"})
    //permision
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }
    //fin current position

    /**
     * Loop through the possible destination list of LatLng locations and get
     * the route for each destination.
     */
    private void getRoutesToAllPoints() {
        for (LatLng singleLatLng : possibleDestinations) {
            getRoute(Point.fromLngLat(singleLatLng.getLongitude(), singleLatLng.getLatitude()));
        }
    }

    /**
     * Make a call to the Mapbox Directions API to get the route from the person location icon
     * to the marker's location and then add the route to the route list.
     *
     * @param destination the marker associated with the recyclerview card that was tapped on.
     */

    @SuppressWarnings({"MissingPermission"})
    private void getRoute(Point destination) {

        MapboxDirections client = MapboxDirections.builder()
                .origin(origine)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getString(R.string.access_token))
                .build();
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null) {
                    Log.d(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Log.d(TAG, "No routes found");
                    return;
                }
                // Add the route to the list.
                directionsRouteList.add(response.body().routes().get(0));
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.d(TAG, "Error: " + throwable.getMessage());
                if (!throwable.getMessage().equals("Coordinate is invalid: 0,0")) {
                    Toast.makeText(MapBox.this,
                            "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Update the GeoJSON data for the direction route LineLayer.
     *
     * @param route The route to be drawn in the map's LineLayer that was set up above.
     */
    private void drawNavigationPolylineRoute(final DirectionsRoute route) {
        if (mapboxMap != null) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    List<Feature> directionsRouteFeatureList = new ArrayList<>();
                    LineString lineString = LineString.fromPolyline(route.geometry(), PRECISION_6);
                    List<Point> lineStringCoordinates = lineString.coordinates();
                    for (int i = 0; i < lineStringCoordinates.size(); i++) {
                        directionsRouteFeatureList.add(Feature.fromGeometry(
                                LineString.fromLngLats(lineStringCoordinates)));
                    }
                    dashedLineDirectionsFeatureCollection =
                            FeatureCollection.fromFeatures(directionsRouteFeatureList);
                    GeoJsonSource source = style.getSourceAs(DASHED_DIRECTIONS_LINE_LAYER_SOURCE_ID);
                    if (source != null) {
                        source.setGeoJson(dashedLineDirectionsFeatureCollection);
                    }
                }
            });
        }
    }

    /**
     * Create a FeatureCollection to display the possible destination markers.
     *
     * @return a {@link FeatureCollection}, which represents the possible destinations.
     */
    private FeatureCollection initDestinationFeatureCollection() {
        List<Feature> featureList = new ArrayList<>();
        for (LatLng latLng : possibleDestinations) {
            featureList.add(Feature.fromGeometry(
                    Point.fromLngLat(latLng.getLongitude(), latLng.getLatitude())));
        }
        return FeatureCollection.fromFeatures(featureList);
    }

    /**
     * Set up the RecyclerView.
     */
    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_on_top_of_map);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new LocationRecyclerViewAdapter(this,
                createRecyclerViewLocations(), mapboxMap));
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
    }

    /**
     * Create data fro the RecyclerView.
     *
     * @return a list of {@link SingleRecyclerViewLocation} objects for the RecyclerView.
     */
    @SuppressLint("StringFormatInvalid")
    private List<SingleRecyclerViewLocation> createRecyclerViewLocations() {

        ArrayList<SingleRecyclerViewLocation> locationList = new ArrayList<>();
        for (int x = 0; x < possibleDestinations.size(); x++) {
            //###
            SingleRecyclerViewLocation singleLocation = new SingleRecyclerViewLocation();
            singleLocation.setName(FetchDrivers.Drivers.get(x).getUsername());
            singleLocation.setAvailableTables(FetchDrivers.Drivers.get(x).getPhoneNumber() + "\n" + FetchDrivers.Drivers.get(x).getEmail());
            locationList.add(singleLocation);
        }
        return locationList;
    }


    /**
     * POJO model class for a single location in the RecyclerView.
     */
    class SingleRecyclerViewLocation {

        private String name;
        private String description;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvailableTables() {
            return description;
        }

        public void setAvailableTables(String availableTables) {
            this.description = availableTables;
        }

    }

    class LocationRecyclerViewAdapter extends
            RecyclerView.Adapter<LocationRecyclerViewAdapter.MyViewHolder> {

        private List<SingleRecyclerViewLocation> locationList;
        private MapboxMap map;
        private WeakReference<MapBox> weakReference;


        public LocationRecyclerViewAdapter(MapBox activity,
                                           List<SingleRecyclerViewLocation> locationList,
                                           MapboxMap mapBoxMap) {
            this.locationList = locationList;
            this.map = mapBoxMap;
            this.weakReference = new WeakReference<>(activity);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_directions_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            SingleRecyclerViewLocation singleRecyclerViewLocation = locationList.get(position);
            holder.name.setText(singleRecyclerViewLocation.getName());
            if (singleRecyclerViewLocation.getName().equals("test")) {
                //holder.img.setImageResource(R.drawable.driver);
                Glide.with(MapBox.this)
                        .load(R.drawable.user)
                        .fallback(R.drawable.user)
                        .into(holder.img);
            }
            holder.img.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapBox.this, Profile_card.class);
                    Pair pairs;
                    pairs = new Pair<View, String>(holder.img, "photo_animate");


                    for (UserHelper user : FetchDrivers.Drivers) {
                        if (user.getUsername().equals(singleRecyclerViewLocation.getName())) {
                            intent.putExtra("driver", user);
                            break;
                        }
                    }
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MapBox.this, pairs);
                    startActivity(intent, options.toBundle());
                }
            });
            holder.numOfAvailableTables.setText(singleRecyclerViewLocation.getAvailableTables());
            holder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    weakReference.get()
                            .drawNavigationPolylineRoute(weakReference.get().directionsRouteList.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return locationList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView name;
            TextView numOfAvailableTables;
            ImageView img;
            CardView singleCard;
            ItemClickListener clickListener;

            MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.location_title_tv);
                numOfAvailableTables = view.findViewById(R.id.location_num_of_beds_tv);
                singleCard = view.findViewById(R.id.single_location_cardview);
                singleCard.setOnClickListener(this);
                img = view.findViewById(R.id.img_card);


            }

            public void setClickListener(ItemClickListener itemClickListener) {
                this.clickListener = itemClickListener;
            }

            @Override
            public void onClick(View view) {
                clickListener.onClick(view, getLayoutPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Prevent leaks
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Set the LocationComponent activation options
            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .useDefaultLocationEngine(false)
                            .build();

            // Activate with the LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            initLocationEngine();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(this);

        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }


    private static class MainActivityLocationCallback
            implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<MapBox> activityWeakReference;

        MainActivityLocationCallback(MapBox activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location has changed.
         *
         * @param result the LocationEngineResult object which has the last known location within it.
         */
        @Override
        public void onSuccess(LocationEngineResult result) {
            MapBox activity = activityWeakReference.get();

            if (activity != null) {
                Location location = result.getLastLocation();

                if (location == null) {
                    return;
                }

                double latitude = (double) result.getLastLocation().getLatitude();
                double longitude = (double) result.getLastLocation().getLongitude();
                MapBox.thread.run(latitude, longitude);
                //like a Thread----
                // Pass the new location to the Maps SDK's LocationComponent
                if (activity.mapboxMap != null && result.getLastLocation() != null) {
                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
                }
            }
        }

        /**
         * The LocationEngineCallback interface's method which fires when the device's location can not be captured
         *
         * @param exception the exception message
         */
        @Override
        public void onFailure(@NonNull Exception exception) {
            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
            MapBox activity = activityWeakReference.get();
            if (activity != null) {
                Toast.makeText(activity, exception.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }

    }


    public static class MyThread implements Runnable {
        public void run(double latitude, double longitude) {
            if (MapBox.callerV.equals("CapitainLogin")) {
                DatabaseReference firebase = FirebaseDatabase.getInstance().getReference("Driver");
                firebase.child(MapBox.driver.getUsername()).child("latitude").setValue(latitude);
                firebase.child(MapBox.driver.getUsername()).child("longitude").setValue(longitude);
                driver.setLongitude(longitude);
                driver.setLatitude(latitude);
            } else {
                DatabaseReference firebase = FirebaseDatabase.getInstance().getReference("Costumers");
                user.setLatitude(latitude);
                user.setLongitude(longitude);
                firebase.child(MapBox.user.getUsername()).child("latitude").setValue(latitude);
                firebase.child(MapBox.user.getUsername()).child("longitude").setValue(longitude);
            }

        }

        @Override
        public void run() {

        }
    }


}