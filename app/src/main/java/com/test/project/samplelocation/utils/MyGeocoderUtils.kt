package com.test.project.samplelocation.utils


import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.util.*

class MyGeocoderUtils {
    companion object {
        fun getLocationFromAddress(context: Context, strAddress: String): LatLng? {
            val coder = Geocoder(context)
            val address: ArrayList<Address>?
            val location: Address
            var latLng: LatLng? = null
            if (Geocoder.isPresent()) {
                try {
                    address = coder.getFromLocationName(strAddress, 5) as ArrayList<Address>
                    if (address.size > 0) {
                        location = address[0]
                        latLng = LatLng(location.latitude, location.longitude)
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            return latLng
        }

        fun getAddressFromLocation(mContext: Context, lat: Double, lng: Double): String {
            var city = "Start"
            var locality = "locality"
            var subLocality = "subLocality"
            var feature = "feature"
            var addressLoc = "Location"
            val geocoder = Geocoder(mContext)
            val addresses: ArrayList<Address>
            if (Geocoder.isPresent()) {
                try {
                    addresses = geocoder.getFromLocation(lat, lng, 1) as ArrayList<Address>
                    Log.d("ZOHAIB", " " + addresses.size)
                    if (addresses.size > 0) {
                        addressLoc = addresses[0].getAddressLine(0)
                        city = addresses[0].locality
                        locality = addresses[0].locality
                        subLocality = addresses[0].subLocality
                        feature = addresses[0].featureName
                        Log.d("ZOHAIB", "Address :" + addressLoc)
                        Log.d("ZOHAIB", "city :" + city)
                        Log.d("ZOHAIB", "locality :" + locality)
                        Log.d("ZOHAIB", "subLocality :" + subLocality)
                        Log.d("ZOHAIB", "feature :" + feature)
                    }
                } catch (e: Exception) {
                }
            }
            return addressLoc
        }

        fun getCityFromLocation(mContext: Context, lat: Double, lng: Double): String {
            var city = "City"
            val geocoder = Geocoder(mContext)
            val addresses: ArrayList<Address>
            if (Geocoder.isPresent()) {
                try {
                    addresses = geocoder.getFromLocation(lat, lng, 1) as ArrayList<Address>
                    Log.d("ZOHAIB", " " + addresses.size)
                    if (addresses.size > 0) {
                        city = addresses[0].locality
                    }
                } catch (e: Exception) {
                }
            }
            return city
        }

        fun getSubLocalityFromLocation(mContext: Context, lat: Double, lng: Double): String {
            var subLocality = "subLocality"
            val geocoder = Geocoder(mContext)
            val addresses: ArrayList<Address>
            if (Geocoder.isPresent()) {
                try {
                    addresses = geocoder.getFromLocation(lat, lng, 1) as ArrayList<Address>
                    Log.d("ZOHAIB", " " + addresses.size)
                    if (addresses.size > 0) {
                        subLocality = addresses[0].subLocality
                    }
                } catch (e: Exception) {
                }
            }
            return subLocality
        }
    }
}