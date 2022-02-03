package com.test.project.samplelocation.utils

class MyConstants {
    companion object {
        var latitude = 0.0
        var longitude = 0.0
        var address = "Address"

        val MY_BROADCAST_ACTION = "MY_BROADCAST_ACTION"
        val DATA_FROM_SERVICE_TO_RECEIVER = "DATA_FROM_SERVICE_TO_RECEIVER"
        val PENDING_INTNET_REQUEST_CODE = 1

        var NOTIFICATION_CHANNEL_ID = "Notification Id"
        var NOTIFICATION_CHANNEL_NAME = "Share Location Channel"
        var NOTIFICATION_CHANNEL_DESC = "This chanel is for live location sharing service notification"

        var ADD_LOCATION_CODE = 1
        var ADD_CONTACT_CODE = 2

        var DATA_FROM_LOCATION = "DATA_FROM_LOCATION"
        var DATA_FROM_CONTACT = "DATA_FROM_CONTACT"

        var REMINDER_EDIT_MODEL_INTENT = "REMINDER_EDIT_MODEL_INTENT"
        var MESSAGE_EDIT_MODEL_INTENT = "MESSAGE_EDIT_MODEL_INTENT"
    }
}