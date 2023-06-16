package UI

import data.model.Event
import data.model.Location
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object HttpClient {
    private val client = OkHttpClient()
    private val mediaType = "application/json; charset=utf-8".toMediaType()

//    fun getLocationLatAndLong(location: Location): Array<Double> {
//        val request = Request.Builder()
//            .url("http://api.positionstack.com/v1/forward?access_key=2d14e843ebf05b269b46de09e718c900&query=${location.street} ${location.city}")
//            .build()
//        val response = client.newCall(request).execute()
//        val json = JsonObject(response.body!!.string())
//        val latitude = json.toBsonDocument().getArray("data").get(0)!!.asDocument().getDouble("latitude").value
//        val longitude = json.toBsonDocument().getArray("data").get(0)!!.asDocument().getDouble("longitude").value
//        return arrayOf(latitude, longitude)
//    }

    fun insertEventIntoMongo(event: Event) {
       println(event.toJson())
        val request = Request.Builder()
            .url("http://localhost:3001/api/event")
            .post(event.toJson().toRequestBody(mediaType))
            .build()
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            println("The event was successfully saved in the database.")
        } else {
            println("The event was not successfully saved in the database.")
        }

        response.close()
    }

}