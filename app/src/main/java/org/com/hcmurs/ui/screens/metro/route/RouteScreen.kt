package org.com.hcmurs.ui.screens.metro.route

import androidx.compose.foundation.lazy.items
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Paint
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import org.com.hcmurs.R
import org.com.hcmurs.model.Station
import org.com.hcmurs.ui.components.common.CommonTopBar
import org.com.hcmurs.utils.initialView
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun RouteScreen(
    navController: NavController,
    metroStationViewModel: MetroStationViewModel = hiltViewModel<MetroStationViewModel>()
) {
    val metroStations by metroStationViewModel.stations.collectAsState()
    val stationPoints by metroStationViewModel.stationGeoPoints.collectAsState()

    val isLoading by metroStationViewModel.isLoading.collectAsState()
    var isCalculatingRoute by remember { mutableStateOf(false) }
    val error by metroStationViewModel.error.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    var hasLocationPermission by remember { mutableStateOf(false) }
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var currentCenter by remember { mutableStateOf<GeoPoint?>(null) }

    var isBottomCardExpanded by remember { mutableStateOf(false) }
    var showStartStationDialog by remember { mutableStateOf(false) }
    var showEndStationDialog by remember { mutableStateOf(false) }
    var selectedStartStation by remember { mutableStateOf<Station?>(null) }
    var selectedEndStation by remember { mutableStateOf<Station?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    LaunchedEffect(metroStations) {
        if (metroStations.isNotEmpty()) {

            Log.d("RouteScreen", "Metro stations loaded: ${metroStations.size}")

            // Only set values if they haven't been set by the user
            if (selectedStartStation == null) {
                selectedStartStation = metroStations.first()
            }
            if (selectedEndStation == null) {
                selectedEndStation = metroStations.last()
            }
        }
    }

    LaunchedEffect(Unit) {
        hasLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        metroStationViewModel.stations
    }

    Scaffold(
        topBar = {
            CommonTopBar(navController, "Hành trình")
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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

            // Only recreate overlays when data changes
            val mapUpdateKey by remember(metroStations, selectedStartStation, selectedEndStation) {
                mutableLongStateOf(System.currentTimeMillis())
            }

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
                                Log.d(
                                    "RouteScreen",
                                    "Current Center - Lat: ${currentCenter?.latitude}, Lng: ${currentCenter?.longitude}"
                                )
                                return true
                            }

                            override fun onZoom(event: ZoomEvent?): Boolean {
                                currentCenter = mapCenter as GeoPoint
                                Log.d(
                                    "RouteScreen",
                                    "Current Center - Lat: ${currentCenter?.latitude}, Lng: ${currentCenter?.longitude}, Zoom: ${event?.zoomLevel}"
                                )
                                return true
                            }
                        })
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { mapView ->
                    if (mapUpdateKey > 0) {
                        // This block will be called on recomposition
                        val currentZoom = mapView.zoomLevelDouble

                        // Clear old overlays before drawing new ones
                        mapView.overlays.clear()

                        // Add metro line polyline first (so it appears under markers)
                        val polyline = Polyline().apply {
                            stationPoints.forEach { addPoint(it) }
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
                        stationPoints.forEachIndexed { index, stationPoint ->

                            // Find corresponding MetroStation for this GeoPoint
                            val metroStationAtPoint = metroStations.find { it.location.distanceToAsDouble(stationPoint) < 100 }

                            // Determine if this is start or end station
                            val isStartOrEndStation = metroStationAtPoint?.isTerminal == true

                            val metroMarker = Marker(mapView).apply {
                                position = stationPoint
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                                title = "Metro Station ${index + 1}"

                                val iconRes = if (isStartOrEndStation) R.drawable.ic_metro else R.drawable.ic_point
                                val metroIcon = ContextCompat.getDrawable(context, iconRes)?.mutate()

                                // Use a bitmap-based approach for icon creation
                                // Apply tint based on station type
//                                metroIcon?.setTint(
//                                    when (index) {
////                                        0 -> "#FFFF5722".toColorInt() // Orange for start
////                                        station.size - 1 -> "#FF4CAF50".toColorInt() // Green for end
//                                        else -> "#FF1976D2".toColorInt() // Blue for regular
//                                    }
//                                )

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
                                val metroStationAtPoint = metroStations.find { station ->
                                    GeoPoint(station.latitude, station.longitude).distanceToAsDouble(stationPoint) < 100
                                }

                                setOnMarkerClickListener { marker, mapView ->
                                    metroStationAtPoint?.let { clickedStation ->
                                        // Toggle between setting as start or end
                                        if (selectedStartStation != clickedStation) {
                                            selectedStartStation = clickedStation
                                        } else if (selectedEndStation != clickedStation) {
                                            selectedEndStation = clickedStation
                                        }
                                        mapView.invalidate()
                                    }
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
                }
            )

            // Route Selection Box with floating swap button
            Box(
                modifier = Modifier
                    .width(300.dp)
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier.align(Alignment.TopCenter),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Start station selector
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_home_50),
                                    contentDescription = "Điểm đi",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                FilledTonalButton(
                                    onClick = { showStartStationDialog = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(35.dp), // Reduced height
                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        selectedStartStation?.name ?: "Chọn ga đi",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.Medium,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        // End station selector
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_red_point_50),
                                    contentDescription = "Điểm đến",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                FilledTonalButton(
                                    onClick = { showEndStationDialog = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(35.dp), // Reduced height
                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        selectedEndStation?.name ?: "Chọn ga đến",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = FontWeight.Medium,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }

                // Floating swap button
                FloatingActionButton(
                    onClick = {
                        val temp = selectedStartStation
                        selectedStartStation = selectedEndStation
                        selectedEndStation = temp
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = 16.dp), // Offset to float outside the card
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Đảo ga"
                    )
                }
            }

            // Bottom Time Information Box
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isBottomCardExpanded = !isBottomCardExpanded }
                    .animateContentSize() // Add this for animation
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Thông tin chuyến đi",
                        style = MaterialTheme.typography.titleMedium
                    )

                    if (isCalculatingRoute) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }

                    if (selectedStartStation != null && selectedEndStation != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Thời gian")
                                Text("25 phút", style = MaterialTheme.typography.bodyLarge)
                            }
                            Column {
                                Text("Khoảng cách")
                                Text("12.5 km", style = MaterialTheme.typography.bodyLarge)
                            }
                            Column {
                                Text("Giá vé")
                                Text("10000 VND", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    } else {
                        Text(
                            "Vui lòng chọn ga đi và ga đến để xem thông tin",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }


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
                    .padding(16.dp, 16.dp, 16.dp, 100.dp),
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

        if (showStartStationDialog) {
            AlertDialog(
                onDismissRequest = { showStartStationDialog = false },
                title = { Text("Chọn ga đi") },
                text = {
                    LazyColumn {
                        items(metroStations) { station ->
                            ListItem(
                                headlineContent = { Text(station.name) },
                                modifier = Modifier.clickable {
                                    selectedStartStation = station
                                    showStartStationDialog = false
                                }
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showStartStationDialog = false }) {
                        Text("Hủy")
                    }
                }
            )
        }

        if (showEndStationDialog) {
            AlertDialog(
                onDismissRequest = { showEndStationDialog = false },
                title = { Text("Chọn ga đến") },
                text = {
                    LazyColumn {
                        items(metroStations) { station ->
                            ListItem(
                                headlineContent = { Text(station.name) },
                                modifier = Modifier.clickable {
                                    selectedEndStation = station
                                    showEndStationDialog = false
                                }
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showEndStationDialog = false }) {
                        Text("Hủy")
                    }
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

@Preview(showBackground = true, apiLevel = 35)
@Composable
fun RouteScreenPreview() {
    // This preview will not show the map, but it allows you to see the layout structure
    RouteScreen(
        navController = NavController(LocalContext.current),
        metroStationViewModel = hiltViewModel<MetroStationViewModel>()
    )
}