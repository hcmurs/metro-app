package org.com.hcmurs.ui.screens.metro.route

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import org.com.hcmurs.R
import org.com.hcmurs.model.station
import org.com.hcmurs.ui.components.common.CommonTopBar
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import androidx.core.graphics.toColorInt
import androidx.core.graphics.drawable.toDrawable
import org.com.hcmurs.utils.initialView

@Composable
fun RouteScreen(
    navController: NavController,
    metroStationViewModel: MetroStationViewModel = hiltViewModel<MetroStationViewModel>()
) {

    val isLoading by metroStationViewModel.isLoading.collectAsState()
    val error by metroStationViewModel.error.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var hasLocationPermission by remember { mutableStateOf(false) }
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var currentCenter by remember { mutableStateOf<GeoPoint?>(null) }

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

        metroStationViewModel.getMetroStations()
    }

    Scaffold(
        topBar = {
            CommonTopBar(navController, "Hành trình")
        }
    ) { paddingValues ->

        // Show loading indicator if needed
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        // Show error if needed
        error?.let {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: $it")
            }
        }

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
                        controller.setZoom(14.0)

                        // Center the map on a default location
                        //val startPoint = GeoPoint(10.763032, 106.682397) // Ho Chi Minh City
                        controller.setCenter(initialView)

                        // Add a marker at the center
                        val marker = Marker(this)
                        marker.position = initialView
                        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        marker.title = "Ho Chi Minh City"
                        overlays.add(marker)

                        // Store reference for FAB
                        mapView = this

                        // Add map event listener to track current center
                        addMapListener(object : MapListener {
                            override fun onScroll(event: ScrollEvent?): Boolean {
                                currentCenter = mapCenter as GeoPoint
                                Log.d("RouteScreen", "Current Center - Lat: ${currentCenter?.latitude}, Lng: ${currentCenter?.longitude}")
                                return true
                            }

                            override fun onZoom(event: ZoomEvent?): Boolean {
                                currentCenter = mapCenter as GeoPoint
                                Log.d("RouteScreen", "Current Center - Lat: ${currentCenter?.latitude}, Lng: ${currentCenter?.longitude}, Zoom: ${event?.zoomLevel}")
                                return true
                            }
                        })
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { mapView ->
                    // This block will be called on recomposition
                    val currentZoom = mapView.zoomLevelDouble

                    // Clear old overlays before drawing new ones
                    mapView.overlays.clear()

                    // Add metro line polyline first (so it appears under markers)
                    val polyline = Polyline().apply {
                        station.forEach { addPoint(it) }
                        outlinePaint.strokeWidth = when {
                            currentZoom < 13 -> 8f
                            currentZoom < 16 -> 12f
                            else -> 16f
                        }
                        outlinePaint.color = "#FF1976D2".toColorInt() // Material Blue
                        outlinePaint.strokeCap = Paint.Cap.ROUND
                        outlinePaint.isAntiAlias = true
                    }
                    mapView.overlays.add(polyline)

                    // Add metro stations with larger, more prominent icons
                    station.forEachIndexed { index, stationPoint ->
                        val metroMarker = Marker(mapView).apply {
                            position = stationPoint
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                            title = "Metro Station ${index + 1}"

                            // Use a bitmap-based approach for icon creation
                            val metroIcon = ContextCompat.getDrawable(context, R.drawable.ic_metro)?.mutate()
                            metroIcon?.let { drawable ->
                                // Determine size based on zoom
                                val iconSize = when {
                                    currentZoom < 12 -> 64
                                    currentZoom < 15 -> 96
                                    currentZoom < 18 -> 128
                                    else -> 160
                                }

                                // Create bitmap with proper dimensions
                                val bitmap = android.graphics.Bitmap.createBitmap(
                                    iconSize, iconSize, android.graphics.Bitmap.Config.ARGB_8888
                                )
                                val canvas = android.graphics.Canvas(bitmap)

                                // Set bounds to fill the bitmap
                                drawable.setBounds(0, 0, iconSize, iconSize)
                                drawable.draw(canvas)

                                // Use bitmap as icon
                                icon = bitmap.toDrawable(context.resources)
                            }

                            // Metro stations are always visible and clickable
                            setOnMarkerClickListener { marker, mapView ->
                                // Handle metro station click
                                true
                            }
                        }
                        mapView.overlays.add(metroMarker)
                    }

                    // Add user location marker if permission is granted
                    if (hasLocationPermission) {
                        // You can add user location logic here
                        // val userLocationMarker = ...
                    }

                    // Refresh the map
                    mapView.invalidate()
                }
            )

            // Floating Action Button to reset map to initial view
            FloatingActionButton(
                onClick = {
                    mapView?.let { map ->
                        val initialPoint = initialView
                        map.controller.animateTo(initialPoint, 14.0, 1000L)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Reset to initial view",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
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
fun RouteScreenPreview() {
    // This preview will not show the map, but it allows you to see the layout structure
    RouteScreen(navController = NavController(LocalContext.current))
}