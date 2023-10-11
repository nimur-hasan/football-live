package com.nehalappstudio.footballlive.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.imageview.ShapeableImageView
import com.nehalappstudio.footballlive.R
import com.nehalappstudio.footballlive.adapter.MatchAdapter
import com.nehalappstudio.footballlive.models.FeaturedLeagueModel
import com.nehalappstudio.footballlive.models.MatchCardModel
import com.nehalappstudio.footballlive.models.SingleTeam
import com.nehalappstudio.footballlive.models.TeamCardModel
import com.nehalappstudio.footballlive.models.TeamModel
import com.nehalappstudio.footballlive.models.VenueModel
import com.squareup.picasso.Picasso
import java.util.Calendar

class Matches : Fragment() {

    private lateinit var dataList: ArrayList<MatchCardModel>
    private lateinit var rvMatch: RecyclerView
    private lateinit var matchAdapter: MatchAdapter
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var featuredLeagueList: ArrayList<FeaturedLeagueModel>

    private lateinit var txt1:TextView;
    private lateinit var txt2:TextView;
    private lateinit var txt3:TextView;
    private lateinit var txt4:TextView;
    private lateinit var txt5:TextView;
    private lateinit var txt6:TextView;
    private lateinit var txt7:TextView;
    private lateinit var txt8:TextView;
    private lateinit var txt9:TextView;
    private lateinit var txt10:TextView;

    private lateinit var featuredLeague1: ShapeableImageView
    private lateinit var featuredLeague2: ShapeableImageView
    private lateinit var featuredLeague3: ShapeableImageView
    private lateinit var featuredLeague4: ShapeableImageView
    private lateinit var featuredLeague5: ShapeableImageView
    private lateinit var featuredLeague6: ShapeableImageView
    private lateinit var featuredLeague7: ShapeableImageView
    private lateinit var featuredLeague8: ShapeableImageView
    private lateinit var featuredLeague9: ShapeableImageView
    private lateinit var featuredLeague10: ShapeableImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataList = ArrayList();
        matchAdapter = MatchAdapter(requireContext(), dataList)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_matches, container, false);

        txt1 = myView.findViewById(R.id.txt1)
        txt2 = myView.findViewById(R.id.txt2)
        txt3 = myView.findViewById(R.id.txt3)
        txt4 = myView.findViewById(R.id.txt4)
        txt5 = myView.findViewById(R.id.txt5)
        txt6 = myView.findViewById(R.id.txt6)
        txt7 = myView.findViewById(R.id.txt7)
        txt8 = myView.findViewById(R.id.txt8)
        txt9 = myView.findViewById(R.id.txt9)
        txt10 = myView.findViewById(R.id.txt10)

        featuredLeagueList = ArrayList()
        featuredLeagueList.add(FeaturedLeagueModel("https://media-4.api-sports.io/football/leagues/39.png", "Premier League","39" ))
        featuredLeagueList.add(FeaturedLeagueModel("https://media-4.api-sports.io/football/leagues/140.png", "La Liga","140" ))
        featuredLeagueList.add(FeaturedLeagueModel("https://media-4.api-sports.io/football/leagues/71.png", "Serie A","71" ))
        featuredLeagueList.add(FeaturedLeagueModel("https://media-4.api-sports.io/football/leagues/2.png", "UEFA Champions League","2" ))
        featuredLeagueList.add(FeaturedLeagueModel("https://media-4.api-sports.io/football/leagues/3.png", "UEFA Europa League","3" ))
        featuredLeagueList.add(FeaturedLeagueModel("https://media-4.api-sports.io/football/leagues/848.png", "UEFA Europa Conference League","848" ))
        featuredLeagueList.add(FeaturedLeagueModel("https://media-4.api-sports.io/football/leagues/253.png", "Major League Soccer","253" ))
//        featuredLeagueList.add(FeaturedLeagueModel("https://media-4.api-sports.io/football/leagues/39.png", "Premier League","39" ))

        featuredLeague1 = myView.findViewById(R.id.featuredLeague1)
        featuredLeague2 = myView.findViewById(R.id.featuredLeague2)
        featuredLeague3 = myView.findViewById(R.id.featuredLeague3)
        featuredLeague4 = myView.findViewById(R.id.featuredLeague4)
        featuredLeague5 = myView.findViewById(R.id.featuredLeague5)
        featuredLeague6 = myView.findViewById(R.id.featuredLeague6)
        featuredLeague7 = myView.findViewById(R.id.featuredLeague7)
        featuredLeague8 = myView.findViewById(R.id.featuredLeague8)
        featuredLeague9 = myView.findViewById(R.id.featuredLeague9)
        featuredLeague10 = myView.findViewById(R.id.featuredLeague10)

        Picasso.get().load(featuredLeagueList.get(0).logo).into(featuredLeague1)
        Picasso.get().load(featuredLeagueList.get(1).logo).into(featuredLeague2)
        Picasso.get().load(featuredLeagueList.get(2).logo).into(featuredLeague3)
        Picasso.get().load(featuredLeagueList.get(3).logo).into(featuredLeague4)
        Picasso.get().load(featuredLeagueList.get(4).logo).into(featuredLeague5)
        Picasso.get().load(featuredLeagueList.get(5).logo).into(featuredLeague6)
        Picasso.get().load(featuredLeagueList.get(6).logo).into(featuredLeague7)

        featuredLeague1.setOnClickListener { fetchDayMatches("2023", false, featuredLeagueList.get(0).id) }
        featuredLeague2.setOnClickListener { fetchDayMatches("2023", false, featuredLeagueList.get(1).id) }
        featuredLeague3.setOnClickListener { fetchDayMatches("2023", false, featuredLeagueList.get(2).id) }
        featuredLeague4.setOnClickListener { fetchDayMatches("2023", false, featuredLeagueList.get(3).id) }
        featuredLeague5.setOnClickListener { fetchDayMatches("2023", false, featuredLeagueList.get(4).id) }
        featuredLeague6.setOnClickListener { fetchDayMatches("2023", false, featuredLeagueList.get(5).id) }
        featuredLeague7.setOnClickListener { fetchDayMatches("2023", false, featuredLeagueList.get(6).id) }

        lottieAnimationView = myView.findViewById(R.id.lottieAnimationView)
        rvMatch = myView.findViewById<RecyclerView>(R.id.rvMatch)
        rvMatch.layoutManager = LinearLayoutManager(requireContext())
        rvMatch.adapter = matchAdapter;


        var today = getDayOfMonth()
        txt1.text = getDayOfMonth(-1).toString();
        txt2.text = today.toString();
        txt2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primaryDark))
        txt3.text = getDayOfMonth(1).toString();
        txt4.text = getDayOfMonth(2).toString();
        txt5.text = getDayOfMonth(3).toString();
        txt6.text = getDayOfMonth(4).toString();
        txt7.text = getDayOfMonth(5).toString();
        txt8.text = getDayOfMonth(6).toString();
        txt9.text = getDayOfMonth(7).toString();
        txt10.text = getDayOfMonth(8).toString();

        fetchDayMatches(getDateByChangeDay(0), true, "")


        txt1.setOnClickListener { updateNewShortCalender(txt1.text.toString().toInt()) }
        txt2.setOnClickListener { updateNewShortCalender(txt2.text.toString().toInt()) }
        txt3.setOnClickListener { updateNewShortCalender(txt3.text.toString().toInt()) }
        txt4.setOnClickListener { updateNewShortCalender(txt4.text.toString().toInt()) }
        txt5.setOnClickListener { updateNewShortCalender(txt5.text.toString().toInt()) }
        txt6.setOnClickListener { updateNewShortCalender(txt6.text.toString().toInt()) }
        txt7.setOnClickListener { updateNewShortCalender(txt7.text.toString().toInt()) }
        txt8.setOnClickListener { updateNewShortCalender(txt8.text.toString().toInt()) }
        txt9.setOnClickListener { updateNewShortCalender(txt9.text.toString().toInt()) }
        txt10.setOnClickListener { updateNewShortCalender(txt10.text.toString().toInt()) }

        return myView;
    }

    private fun updateNewShortCalender(selectedDay:Int) {

        txt1.text = getDayOfMonthBySetDate(selectedDay-1).toString();
        txt2.text = selectedDay.toString()
        txt3.text = getDayOfMonthBySetDate(selectedDay+1).toString();
        txt4.text = getDayOfMonthBySetDate(selectedDay+2).toString();
        txt5.text = getDayOfMonthBySetDate(selectedDay+3).toString();
        txt6.text = getDayOfMonthBySetDate(selectedDay+4).toString();
        txt7.text = getDayOfMonthBySetDate(selectedDay+5).toString();
        txt8.text = getDayOfMonthBySetDate(selectedDay+6).toString();
        txt9.text = getDayOfMonthBySetDate(selectedDay+7).toString();
        txt10.text = getDayOfMonthBySetDate(selectedDay+8).toString();

        fetchDayMatches("", true, "")
    }

    private fun fetchDayMatches(season:String, live:Boolean, leagueId:String) {

        lottieAnimationView.visibility = View.VISIBLE
        rvMatch.visibility = View.GONE

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        var apiUrl = "https://v3.football.api-sports.io/fixtures?live=all&league="+leagueId

        if(season != ""){
            apiUrl = "https://v3.football.api-sports.io/fixtures?season="+season
        }
        if(live){
            apiUrl = "https://v3.football.api-sports.io/fixtures?live=all"
        }
        if(leagueId != ""){
            apiUrl = "https://v3.football.api-sports.io/fixtures?live=all&league="+leagueId
        }
        if(season != "" && leagueId != ""){
            apiUrl = "https://v3.football.api-sports.io/fixtures?season="+season+"&league="+leagueId
        }

        if(leagueId != "" && live == true){
            apiUrl = "https://v3.football.api-sports.io/fixtures?live=all&league="+leagueId
        }


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
                rvMatch.visibility = View.VISIBLE
                matchAdapter.notifyDataSetChanged()


            },
            Response.ErrorListener { error ->
                Toast.makeText(requireContext(), "Api Request Failed.", Toast.LENGTH_LONG).show()
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

    fun getDayOfMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getDayOfMonth(beforeAfter:Int): Int {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, beforeAfter)
        return calendar.get(Calendar.DAY_OF_MONTH)
    }


    fun getDayOfMonthBySetDate(day:Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return calendar.get(Calendar.DAY_OF_MONTH)
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

    fun getDateBySetDay(day:Int): String {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, day)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val formattedDate = "$year-${String.format("%02d", month)}-${String.format("%02d", day)}"

        return formattedDate
    }


}