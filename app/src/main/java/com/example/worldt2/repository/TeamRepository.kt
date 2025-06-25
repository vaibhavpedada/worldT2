package com.example.worldt2.repository

import android.content.Context
import com.example.worldt2.model.Team
import org.json.JSONArray

class TeamRepository(private val context: Context) {

    fun loadTeams(): List<Team> {
        val json = context.assets.open("teams.json")
            .bufferedReader().use { it.readText() }

        val jsonArray = JSONArray(json)
        val teams = mutableListOf<Team>()
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val name = obj.getString("name")
            val flag = obj.getString("flag")
            teams.add(Team(name, flag))
        }
        return teams
    }
}
