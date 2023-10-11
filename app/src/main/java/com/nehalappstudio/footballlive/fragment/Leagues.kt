package com.nehalappstudio.footballlive.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.nehalappstudio.footballlive.R
import com.nehalappstudio.footballlive.adapter.LeagueAdapter
import com.nehalappstudio.footballlive.models.LeagueCardModel

class Leagues : Fragment() {

    lateinit var testImg:ShapeableImageView
    lateinit var rvLeague:RecyclerView
    lateinit var dataList: ArrayList<LeagueCardModel>
    lateinit var leagueAdapter: LeagueAdapter
    lateinit var lottieAnimationView: LottieAnimationView
    lateinit var searchEdtTxt: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataList = ArrayList()
        leagueAdapter = LeagueAdapter(requireContext(), dataList)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_leagues, container, false);

        lottieAnimationView = view.findViewById(R.id.lottieAnimationView)
        rvLeague = view.findViewById(R.id.rvLeagues)
        rvLeague.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        rvLeague.adapter = leagueAdapter
        searchEdtTxt = view.findViewById(R.id.searchEdtTxt)

//        testImg = view.findViewById(R.id.imgTest);
//        Picasso.get()
//            .load("https://example.com")
//            .placeholder(R.drawable.fc_logo)
//            .into(testImg)

        fetchLeaguesData("2023", "")

        searchEdtTxt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                fetchLeaguesData("", s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        return view;
    }

    private fun fetchLeaguesData(session:String, search:String) {

        lottieAnimationView.visibility = View.VISIBLE
        rvLeague.visibility = View.GONE

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        var apiUrl = ""

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

                dataList.clear();
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


                    dataList.add(leagueCardData)
                }

                lottieAnimationView.visibility = View.GONE
                rvLeague.visibility = View.VISIBLE
                leagueAdapter.notifyDataSetChanged()


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

}