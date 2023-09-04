package com.codingwithmitch.composegooglemaps

import android.location.Location
import com.codingwithmitch.composegooglemaps.clusters.ZoneClusterItem
import com.google.android.gms.maps.model.LatLng

data class MapState(
    val lastKnownLocation: Location?,
    val apiResponse: ApiResponse,
    val latLng: LatLng,
)
