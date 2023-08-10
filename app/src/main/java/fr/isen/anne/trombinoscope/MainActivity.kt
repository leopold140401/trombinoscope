package fr.isen.anne.trombinoscope

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), OnItemClickListener {

    data class Person(
        val firstName: String,
        val lastName: String,
        val photoUrl: String,
        val title: String,
        val email: String,
        val cell: String,
        val city: String,
        val state: String,
        val country: String,
        val postcode: String,
        val number: String,
        val name: String,
        val date: String,
        val age: String,
        val nationality: String,
        val username: String,
        val password: String,
        val latitude: String,
        val longitude: String,
        val offset: String,
        val description: String
    )

    private val items = mutableListOf<Person>()
    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.list)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = Adapter(items, this)
        recyclerView.adapter = adapter


        val queue = Volley.newRequestQueue(this)
        val url = "https://randomuser.me/api/?results=25"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val results = response.getJSONArray("results")
                val gson = Gson()
                for (i in 0 until results.length()) {
                    val personJson = results.getJSONObject(i)
                    val personData = gson.fromJson(personJson.toString(), PersonData::class.java)
                    val city = personData.location.city ?: "Unknown City"
                    val state = personData.location.state ?: "Unknown State"
                    val country = personData.location.country ?: "Unknown Country"
                    val postcode = personData.location.postcode ?: "Unknown Postcode"
                    val title = personData.name.title ?: "Unknown Title"
                    val nationality = personData.nat ?: "Unknown Nationality"
                    val username = personData.login.username ?: "Unknown Username"
                    val password = personData.login.password ?: "Unknown Password"
                    val cell = personData.cell ?: "Unknown Cell"
                    val email = personData.email ?: "Unknown Email"
                    val latitude = personData.location.coordinates?.latitude ?: "Unknown Latitude"
                    val longitude = personData.location.coordinates?.longitude ?: "Unknown Longitude"
                    val offset = personData.location.timezone?.offset ?: "Unknown Offset"
                    val description = personData.location.timezone?.description ?: "Unknown Description"

                    val person = Person(
                        personData.name.first,
                        personData.name.last,
                        personData.picture.large,
                        title,
                        email,
                        cell,
                        city,
                        state,
                        country,
                        postcode,
                        personData.location.street.number,
                        personData.location.street.name,
                        personData.dob.date,
                        personData.dob.age,
                        nationality,
                        username,
                        password,
                        latitude,
                        longitude,
                        offset,
                        description
                    )
                    items.add(person)
                }
                adapter.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->
                Log.e(TAG, "Error: ${error.message}")
            })
        queue.add(request)
    }
    override fun onItemClick(position: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        val person = items[position]

        intent.putExtra("user_photo", person.photoUrl)
        intent.putExtra("user_firstname", person.firstName)
        intent.putExtra("user_lastname", person.lastName)
        intent.putExtra("user_title", person.title)
        intent.putExtra("user_email", person.email)
        intent.putExtra("user_cell", person.cell)
        intent.putExtra("user_city", person.city)
        intent.putExtra("user_state", person.state)
        intent.putExtra("user_country", person.country)
        intent.putExtra("user_postcode", person.postcode)
        intent.putExtra("user_number", person.number)
        intent.putExtra("user_name", person.name)
        intent.putExtra("user_date", person.date)
        intent.putExtra("user_age", person.age)
        intent.putExtra("user_nationality", person.nationality)
        intent.putExtra("user_username", person.username)
        intent.putExtra("user_password", person.password)
        intent.putExtra("user_latitude", person.latitude)
        intent.putExtra("user_longitude", person.longitude)
        intent.putExtra("user_offset", person.offset)
        intent.putExtra("user_description", person.description)
        startActivity(intent)
    }


    companion object {
        private const val TAG = "MainActivity"
    }
}


data class PersonData(
    val gender: String,
    val name: NameData,
    val location: LocationData,
    val email: String,
    val login: LoginData,
    val dob: DobData,
    val registered: RegisteredData,
    val phone: String,
    val cell: String,
    val id: IdData,
    val picture: PictureData,
    val nat: String,
    val timezone:TimezoneData
)

data class NameData(
    val title: String,
    val first: String,
    val last: String
)

data class LocationData(
    val street: StreetData,
    val city: String?,
    val state: String?,
    val country: String?,
    val postcode: String?,
    val coordinates: CoordinatesData,
    val timezone: TimezoneData
)

data class StreetData(
    val number: String,
    val name: String
)

data class CoordinatesData(
    val latitude: String,
    val longitude: String
)

data class TimezoneData(
    val offset: String,
    val description: String
)

data class LoginData(
    val uuid: String,
    val username: String?,
    val password: String?,
    val salt: String,
    val md5: String,
    val sha1: String,
    val sha256: String
)

data class DobData(
    val date: String,
    val age: String
)

data class RegisteredData(
    val date: String,
    val age: Int
)

data class IdData(
    val name: String,
    val value: String
)

data class PictureData(
    val large: String,
    val medium: String,
    val thumbnail: String
)