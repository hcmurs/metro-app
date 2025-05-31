package org.com.hcmurs.ui.screens.osmap

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
fun OsmdroidMapScreen(navController: NavController) {
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
                            addPoint(GeoPoint(10.763032, 106.682397)) // Starting point
                            addPoint(GeoPoint(10.763032, 106.692397)) // Move east
                            addPoint(GeoPoint(10.763032, 106.702397)) // Continue east
                            addPoint(GeoPoint(10.773032, 106.702397)) // Move north

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
fun OsmdroidMapScreenPreview() {
    // This preview will not show the map, but it allows you to see the layout structure
    OsmdroidMapScreen(navController = NavController(LocalContext.current))
}