package com.nehalappstudio.footballlive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.imageview.ShapeableImageView
import com.nehalappstudio.footballlive.adapter.MatchAdapter
import com.nehalappstudio.footballlive.models.MatchCardModel
import com.nehalappstudio.footballlive.models.SingleTeam
import com.nehalappstudio.footballlive.models.TeamCardModel
import com.nehalappstudio.footballlive.models.TeamModel
import com.nehalappstudio.footballlive.models.VenueModel
import com.squareup.picasso.Picasso
import java.util.Calendar

class LeagueDetails : AppCompatActivity() {

    lateinit var rvMatches: RecyclerView
    lateinit var lottieAnimationView: LottieAnimationView
    lateinit var matchAdapter: MatchAdapter
    lateinit var dataList: ArrayList<MatchCardModel>
    lateinit var imgLeague: ShapeableImageView
    lateinit var txtLeagueName: TextView
    lateinit var txtLeagueStart: TextView
    lateinit var txtLeagueEnd: TextView
    lateinit var txtLeagueVenue: TextView
    lateinit var txtLeagueSession: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_league_details)

        imgLeague = findViewById(R.id.imgLeague)
        txtLeagueName = findViewById(R.id.txtLeagueDetailsName)
        txtLeagueVenue = findViewById(R.id.txtLeagueVenue)
        txtLeagueStart = findViewById(R.id.txtLeagueStart)
        txtLeagueEnd = findViewById(R.id.txtLeagueEnd)
        txtLeagueSession = findViewById(R.id.txtLeagueSession)

        lottieAnimationView = findViewById(R.id.lottieAnimationView)
        rvMatches = findViewById(R.id.rvMatches)
        rvMatches.layoutManager = LinearLayoutManager(this)
        dataList = ArrayList()

        matchAdapter = MatchAdapter(this, dataList)
        rvMatches.adapter = matchAdapter


        val id = intent.getStringExtra("leagueID")
        val name = intent.getStringExtra("name")
        val img = intent.getStringExtra("img")
        val venue = intent.getStringExtra("venue")
        val start = intent.getStringExtra("start")
        val end = intent.getStringExtra("end")

        if(name != null) { txtLeagueName.text = name}
        if(img != null) {
            Picasso.get().load(img).into(imgLeague)
        }
        if(venue != null) { txtLeagueVenue.text = venue}
        if(start != null) { txtLeagueStart.text = start}
        if(end != null) { txtLeagueEnd.text = end}


        if (id != null) {

            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)

            txtLeagueSession.text = currentYear.toString()
            fetchDayMatches(id, currentYear.toString())
        }

    }

    private fun fetchDayMatches(leagueID:String, session:String) {

        lottieAnimationView.visibility = View.VISIBLE
        rvMatches.visibility = View.GONE

        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val apiUrl = "https://v3.football.api-sports.io/fixtures?league="+leagueID+"&season="+session

        val stringRequest = object : JsonObjectRequest(
            Request.Method.GET,
            apiUrl,
            null,
            Response.Listener { response ->
                println("API Response: $response")

                dataList.clear();
//                Toast.makeText(requireContext(), "Api Request Successful", Toast.LENGTH_LONG).show()

                val responseArr = response.getJSONArray("response")

                for (i in 0 until responseArr.length()) {
                    val fixtureFullObj = responseArr.getJSONObject(i)

                    val fixtureObj = fixtureFullObj.getJSONObject("fixture")
                    val leagueObj = fixtureFullObj.getJSONObject("league")
                    val teamsObj = fixtureFullObj.getJSONObject("teams")
                    val goalsObj = fixtureFullObj.getJSONObject("goals")
                    val scoreObj = fixtureFullObj.getJSONObject("score")

                    val fixtureID = fixtureObj.getString("id")
                    val date = fixtureObj.getString("date")
                    val timestamp = fixtureObj.getString("timestamp")
                    val status = fixtureObj.getJSONObject("status").getString("short")
                    val duration = fixtureObj.getJSONObject("status").getString("elapsed")
                    val venueId = fixtureObj.getJSONObject("venue").getString("id")
                    val venueName = fixtureObj.getJSONObject("venue").getString("name")
                    val venueCity = fixtureObj.getJSONObject("venue").getString("city")
                    val league = leagueObj.getString("name")

                    val homeId = teamsObj.getJSONObject("home").getString("id")
                    val homeName = teamsObj.getJSONObject("home").getString("name")
                    val homeLogo = teamsObj.getJSONObject("home").getString("logo")
                    val homeWinner = false

                    val awayId = teamsObj.getJSONObject("away").getString("id")
                    val awayName = teamsObj.getJSONObject("away").getString("name")
                    val awayLogo = teamsObj.getJSONObject("away").getString("logo")
                    val awayWinner = false

                    val homeGoals = goalsObj.getString("home")
                    val awayGoals = goalsObj.getString("away")

                    val homeTeam = SingleTeam(homeId, homeName, homeLogo, homeWinner, homeGoals)
                    val awayTeam = SingleTeam(awayId, awayName, awayLogo, awayWinner, awayGoals)
                    val team = TeamModel(homeTeam, awayTeam)

                    val venue = VenueModel(venueId, venueName, venueCity)

                    val matchCardData = MatchCardModel(fixtureID, date, timestamp, status, team, venue, duration, league)

                    Log.d("homeName", homeName)

                    dataList.add(matchCardData)
                }

                lottieAnimationView.visibility = View.GONE
                rvMatches.visibility = View.VISIBLE
                matchAdapter.notifyDataSetChanged()


            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Api Request Failed.", Toast.LENGTH_LONG).show()
                println("API Request Failed: ${error.message}")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["x-rapidapi-key"] = "9934587b22930a733e2774cb3b1f3e1d"
                headers["x-rapidapi-host"] = "v3.football.api-sports.io"
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }

    fun getDateByChangeDay(beforeAfter:Int): String {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, beforeAfter)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val formattedDate = "$year-${String.format("%02d", month)}-${String.format("%02d", day)}"

        return formattedDate
    }
}