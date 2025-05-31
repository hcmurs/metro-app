package org.com.hcmurs.ui.screens.metro.maps

import android.Manifest
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun MapScreen(navController: NavController) {

    //Ben thanh 10.770696325149563, 106.69754740398896

    var station = listOf<GeoPoint>(
        GeoPoint(10.770696325149563, 106.69754740398896), // Ben Thanh
        GeoPoint(10.774692602241206, 106.7022005846861), // Ga Nha Hat Thanh Pho
        GeoPoint(10.781671962644449, 106.70840954773367), // Ba Son
        GeoPoint(10.796761528709158, 106.71677316492446), // Cong Vien Van Thanh
        GeoPoint(10.79893614957232, 106.72410295882501),  // Tan Cang
        GeoPoint(10.800635526123163, 106.73447068211519), // Thao Dien
        GeoPoint(10.802432989593624, 106.74275024588567), // An Phu
        GeoPoint(10.808621477482305, 106.7557946347932), // Rach Chiec
        GeoPoint(10.82165609486406, 106.75933605616969), // Ga Phuoc Long
        GeoPoint(10.832473464905071, 106.76444401173823), // Ga Binh Thai
        GeoPoint(10.846639523705255, 106.7720542821162),  // Ga Thu Duc
    )

    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var hasLocationPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    LaunchedEffect(Unit) {
        hasLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { context ->
                    // Initialize OSMdroid configuration
                    Configuration.getInstance().load(
                        context,
                        PreferenceManager.getDefaultSharedPreferences(context)
                    )

                    // Create and configure the map view
                    MapView(context).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(15.0)

                        // Center the map on a default location
                        val startPoint = GeoPoint(10.763032, 106.682397) // Ho Chi Minh City
                        controller.setCenter(startPoint)

                        // Add a marker at the center
                        val marker = Marker(this)
                        marker.position = startPoint
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        marker.title = "Ho Chi Minh City"
                        overlays.add(marker)

                        // Create a polyline
                        val line = Polyline().apply {
                            // Add points to the line (varying longitude)
                            station.forEach { point ->
                                addPoint(point)
                            }

                            // Customize line appearance
                            outlinePaint.strokeWidth = 10f
                            outlinePaint.color = Color.RED
                            outlinePaint.strokeCap = Paint.Cap.ROUND
                            outlinePaint.isAntiAlias = true
                        }

                        // Add the line to map overlays
                        overlays.add(line)
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { mapView ->
                    // This block will be called on recomposition
                }
            )
        }
    }

    // Handle lifecycle events for the map view
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    Configuration.getInstance().load(
                        context,
                        PreferenceManager.getDefaultSharedPreferences(context)
                    )
                }

                Lifecycle.Event.ON_PAUSE -> {
                    // Save the map state if needed
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MapScreenPreview() {
    // This preview will not show the map, but it allows you to see the layout structure
    MapScreen(navController = NavController(LocalContext.current))
}