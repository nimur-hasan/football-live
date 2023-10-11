package com.nehalappstudio.footballlive.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.nehalappstudio.footballlive.R
import com.nehalappstudio.footballlive.adapter.LeagueAdapter
import com.nehalappstudio.footballlive.adapter.LeagueSectionAdapter
import com.nehalappstudio.footballlive.adapter.TeamAdapter
import com.nehalappstudio.footballlive.models.LeagueCardModel
import com.nehalappstudio.footballlive.models.TeamCardModel

class Team : Fragment() {


    private lateinit var dataList:ArrayList<TeamCardModel>
    private lateinit var leagueList:ArrayList<LeagueCardModel>
    private lateinit var rvTeam: RecyclerView
    private lateinit var rvLeagueSection: RecyclerView
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var teamAdapter: TeamAdapter
    private lateinit var leagueSectionAdapter: LeagueSectionAdapter
    private lateinit var searchEdtTxt: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataList = ArrayList()
        leagueList = ArrayList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_team, container, false);

        rvTeam = view.findViewById(R.id.rvTeam);
        rvLeagueSection = view.findViewById(R.id.rvLeaguesSection)

        lottieAnimationView = view.findViewById(R.id.lottieAnimationView)

        rvTeam.layoutManager = LinearLayoutManager(requireContext())
        rvLeagueSection.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        teamAdapter = TeamAdapter(requireContext(), dataList)
        leagueSectionAdapter = LeagueSectionAdapter(requireContext(), leagueList, onItemClick)

        rvTeam.adapter = teamAdapter
        rvLeagueSection.adapter = leagueSectionAdapter

        dataList.add(TeamCardModel("https://media-4.api-sports.io/football/teams/19042.png", "b", "c", "d"))
        dataList.add(TeamCardModel("https://media-4.api-sports.io/football/teams/19042.png", "b", "c", "d"))
        dataList.add(TeamCardModel("https://media-4.api-sports.io/football/teams/19042.png", "b", "c", "d"))
        dataList.add(TeamCardModel("https://media-4.api-sports.io/football/teams/19042.png", "b", "c", "d"))
        dataList.add(TeamCardModel("https://media-4.api-sports.io/football/teams/19042.png", "b", "c", "d"))
        fetchTeamsData("39")
        fetchLeaguesData("2023", "")

        searchEdtTxt = view.findViewById(R.id.searchEdtTxt)
        searchEdtTxt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                fetchLeaguesData("2023", s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}

        })
        return view
    }

    val onItemClick: (Int) -> Unit = { leagueID ->
        fetchTeamsData(leagueID.toString())
    }

    fun fetchLeaguesData(session: String, search: String) {

//        lottieAnimationView.visibility = View.VISIBLE
//        rvLeague.visibility = View.GONE

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        var apiUrl = "https://v3.football.api-sports.io/leagues?season=2023"

        if(search.isEmpty()){
            apiUrl = "https://v3.football.api-sports.io/leagues?season="+session
        }else{
            apiUrl = "https://v3.football.api-sports.io/leagues?search="+search
        }

        val stringRequest = object : JsonObjectRequest(
            Request.Method.GET,
            apiUrl,
            null,
            Response.Listener { response ->
                println("API Response: $response")

                leagueList.clear();
//                Toast.makeText(requireContext(), "Api Request Successful", Toast.LENGTH_LONG).show()

                val responseArr = response.getJSONArray("response")

                for (i in 0 until responseArr.length()) {
                    val leagueFullObj = responseArr.getJSONObject(i)

                    val leagueObj = leagueFullObj.getJSONObject("league")
                    val seasonsObj = leagueFullObj.getJSONArray("seasons").getJSONObject(0)

                    val venue = leagueFullObj.getJSONObject("country").getString("name")
                    val sessionYear = seasonsObj.getString("year")
                    val sessionStart = seasonsObj.getString("start")
                    val sessionEnd = seasonsObj.getString("end")

                    val leagueId = leagueObj.getString("id")
                    val leagueLogo = leagueObj.getString("logo")
                    val leagueName = leagueObj.getString("name")


                    val leagueCardData = LeagueCardModel(leagueId, leagueLogo, leagueName, "todo",venue, sessionYear, sessionStart, sessionEnd)


                    leagueList.add(leagueCardData)
                }

//                lottieAnimationView.visibility = View.GONE
//                rvLeague.visibility = View.VISIBLE
                leagueSectionAdapter.notifyDataSetChanged()


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
    private fun fetchTeamsData(leagueID:String) {

        lottieAnimationView.visibility = View.VISIBLE
        rvTeam.visibility = View.GONE

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        var apiUrl = "https://v3.football.api-sports.io/teams?season=2023&league="+leagueID

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
                    val teamFullObj = responseArr.getJSONObject(i)

                    val teamObj = teamFullObj.getJSONObject("team")
                    val venueObj = teamFullObj.getJSONObject("venue")

                    val name = teamObj.getString("name")
                    val country = teamObj.getString("country")
                    val founded = teamObj.getString("founded")
                    val logo = teamObj.getString("logo")
                    val venue = venueObj.getString("name")



                    val teamCardModel = TeamCardModel(logo, name, venue, country + " - "+founded)

                    dataList.add(teamCardModel)
                }

                lottieAnimationView.visibility = View.GONE
                rvTeam.visibility = View.VISIBLE
                teamAdapter.notifyDataSetChanged()


            },
            Response.ErrorListener { error ->
//                Toast.makeText(requireContext(), "Api Request Failed.", Toast.LENGTH_LONG).show()
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
}