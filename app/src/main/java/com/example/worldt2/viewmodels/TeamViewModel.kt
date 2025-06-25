package com.example.worldt2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.worldt2.model.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.worldt2.repository.TeamRepository

class TeamViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = TeamRepository(application.applicationContext)

    private val _teams = MutableStateFlow<List<Team>>(emptyList())
    val teams: StateFlow<List<Team>> = _teams

    fun loadTeams() {
        _teams.value = repository.loadTeams()
    }

    fun toggleSelection(clickedTeam: Team) {
        val newList = _teams.value.map { team ->
            when {
                team.name == clickedTeam.name -> {
                    if (team.isSelected) team.copy(isSelected = false)
                    else {
                        val selectedCount = _teams.value.count { it.isSelected }
                        if (selectedCount >= 2) team else team.copy(isSelected = true)
                    }
                }
                else -> team
            }
        }
        _teams.value = newList
    }


    fun getSelectedTeams(): List<Team> = _teams.value.filter { it.isSelected }
}