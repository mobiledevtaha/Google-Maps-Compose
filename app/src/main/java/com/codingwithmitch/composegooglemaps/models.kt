package com.codingwithmitch.composegooglemaps


data class ApiResponse(
    val type: String,
    val features: List<Feature>
)

data class Feature(
    val type: String,
    val id: String,
    val geometry: Geometry,
    val properties: Properties
)

data class Geometry(
    val type: String,
    val coordinates: List<Double>
)

data class Properties(
    val xid: String,
    val name: String,
    val rate: Int,
    val osm: String,
    val wikidata: String,
    val kinds: String
)
