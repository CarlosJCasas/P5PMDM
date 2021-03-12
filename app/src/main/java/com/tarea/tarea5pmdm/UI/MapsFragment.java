package com.tarea.tarea5pmdm.UI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tarea.tarea5pmdm.R;

public class MapsFragment extends Fragment {
    private static final float DEFAULT_ZOOM = 12.0f;
    private final LatLng defaultLocation = new LatLng(40.416775, -3.703790);
    public FusedLocationProviderClient fusedLocationProviderClient;
    public LatLng localizacion;
    private Location lastLocation;
    private GoogleMap mMap;
    private double enviarLatitud;
    private double enviarLongitud;
    private Marker marker;
    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMinZoomPreference(2.0f);
            mMap.setMaxZoomPreference(30.0f);

            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            mMap.setMyLocationEnabled(true);

            if (getArguments() != null && (requireActivity() instanceof MostrarActivity || requireActivity() instanceof ModificarActivity)) {
                //Si recibe argumentos es el de modificar
                mMap.setMyLocationEnabled(false);
                double latitud = getArguments().getDouble("latitud");
                double longitud = getArguments().getDouble("longitud");
                localizacion = new LatLng(latitud, longitud);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(localizacion, DEFAULT_ZOOM));
                marker = mMap.addMarker(new MarkerOptions().position(localizacion).title(getString(R.string.yourLocation)));
            } else {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastLocation = task.getResult();
                            if (lastLocation != null) {
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), DEFAULT_ZOOM));
                                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude())).title(getString(R.string.yourLocation)));
                                enviarLatitud = lastLocation.getLatitude();
                                enviarLongitud = lastLocation.getLongitude();
                                if (requireActivity() instanceof AddActivity)
                                    ((AddActivity) requireActivity()).recibirDatosFragment(enviarLatitud, enviarLongitud);
                                if (requireActivity() instanceof ModificarActivity)
                                    ((ModificarActivity) requireActivity()).recibirDatosFragment(enviarLatitud, enviarLongitud);
                            }
                        } else {
                            //Usar por default porque es null
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            marker = mMap.addMarker(new MarkerOptions().position(defaultLocation).title(getString(R.string.yourLocation)));
                        }
                    }
                });
            }
            if (requireActivity() instanceof AddActivity || requireActivity() instanceof ModificarActivity) {
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if (marker != null) {
                            marker.setPosition(latLng);
                        } else {
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.yourLocation)));
                        }
                        enviarLatitud = latLng.latitude;
                        enviarLongitud = latLng.longitude;
                        if (requireActivity() instanceof AddActivity)
                            ((AddActivity) requireActivity()).recibirDatosFragment(enviarLatitud, enviarLongitud);
                        if (requireActivity() instanceof ModificarActivity)
                            ((ModificarActivity) requireActivity()).recibirDatosFragment(enviarLatitud, enviarLongitud);
                    }
                });
            }

        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                            (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {


                        Toast.makeText(requireActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }

}