package com.codingwithmitch.composegooglemaps

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {
    private val fakeApiResponse = ApiResponse(
        type = "FeatureCollection",
        features = listOf(
            Feature(
                type = "Feature",
                id = "11540315",
                geometry = Geometry(
                    type = "Point",
                    coordinates = listOf(44.0092659, 36.1914291)
                ),
                properties = Properties(
                    xid = "W34133277",
                    name = "Citadel of Erbil",
                    rate = 7,
                    osm = "way/34133277",
                    wikidata = "Q206236",
                    kinds = "fortifications,historic,archaeology,interesting_places,castles,other_archaeological_sites"
                )
            ),
            Feature(
                type = "Feature",
                id = "11540316",
                geometry = Geometry(
                    type = "Point",
                    coordinates = listOf(44.0093002, 36.1913757)
                ),
                properties = Properties(
                    xid = "W391279970",
                    name = "Citadel of Erbil",
                    rate = 7,
                    osm = "way/391279970",
                    wikidata = "Q206236",
                    kinds = "architecture,historic_architecture,fortifications,historic,archaeology,interesting_places,destroyed_objects,other_fortifications,other_archaeological_sites"
                )
            ),
            Feature(
                type = "Feature",
                id = "5094175",
                geometry = Geometry(
                    type = "Point",
                    coordinates = listOf(44.0191154, 36.2010422)
                ),
                properties = Properties(
                    xid = "N7643376805",
                    name = "Jalil Khayat Mosque",
                    rate = 3,
                    osm = "node/7643376805",
                    wikidata = "Q19960324",
                    kinds = "religion,mosques,interesting_places"
                )
            )
        )
    )

    val state: MutableState<MapState> = mutableStateOf(
        MapState(
            lastKnownLocation = null,
            apiResponse = fakeApiResponse,
            latLng = LatLng(0.0, 0.0)
        )
    )

    private val delta = 0.1

    private fun calculateBoundingBox(location: Location): Map<String, Double> {
        return mapOf(
            "lon_min" to (location.longitude - delta),
            "lon_max" to (location.longitude + delta),
            "lat_min" to (location.latitude - delta),
            "lat_max" to (location.latitude + delta)
        )
    }


    fun addApiResponseToMap(map: GoogleMap) {
        fakeApiResponse.features.forEach { feature ->
            val coordinates = feature.geometry.coordinates
            state.value = state.value.copy(
                apiResponse = fakeApiResponse,
                latLng = LatLng(coordinates[1], coordinates[0])
            )
            map.addMarker(MarkerOptions().position(state.value.latLng))
        }
    }


    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ): LatLng {
        var userLatLng = LatLng(0.0, 0.0)
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    state.value = state.value.copy(lastKnownLocation = task.result)
                    // Navigate to user's current location
                    userLatLng = LatLng(task.result.latitude, task.result.longitude)

                    // Compute the bounding box
                    val boundingBox = calculateBoundingBox(task.result)
                    // Do something with the boundingBox, perhaps make an API call
                }
            }
        } catch (e: SecurityException) {
            // Show error or something
        }
        return userLatLng
    }


}