package com.example.worldt2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MatchViewModel : ViewModel() {

    private var team1Name: String = "Team 1"
    private var team2Name: String = "Team 2"

    private val _team1Runs = MutableLiveData(0)
    val team1Runs: LiveData<Int> get() = _team1Runs

    private val _team2Runs = MutableLiveData(0)
    val team2Runs: LiveData<Int> get() = _team2Runs

    private val _wickets = MutableLiveData(0)
    val wickets: LiveData<Int> get() = _wickets

    private val _ballsBowled = MutableLiveData(0)
    val ballsBowled: LiveData<Int> get() = _ballsBowled

    private val _currentInnings = MutableLiveData(1)
    val currentInnings: LiveData<Int> get() = _currentInnings

    private val _lastOutcome = MutableLiveData("")
    val lastOutcome: LiveData<String> get() = _lastOutcome

    private val _matchEnded = MutableLiveData(false)
    val matchEnded: LiveData<Boolean> get() = _matchEnded

    private val _matchResult = MutableLiveData("")
    val matchResult: LiveData<String> get() = _matchResult

    // Max 3 wickets and 12 balls per innings
    private val maxWickets = 3
    private val totalBallsPerInnings = 12

    fun setTeams(name1: String, name2: String) {
        team1Name = name1
        team2Name = name2
    }

    fun getTeamNames(): Pair<String, String> = team1Name to team2Name

    fun playNextBall() {
        if (_matchEnded.value == true) return

        val outcome = getRandomOutcome()

        val newWickets = _wickets.value!!
        val newBalls = _ballsBowled.value!!

        if (outcome == "Out") {
            _wickets.value = newWickets + 1
            _lastOutcome.value = "Out"
        } else {
            val runs = outcome.toIntOrNull()


            if (runs == null) {
                _lastOutcome.value = "Invalid Outcome"

                _team1Runs.value = _team1Runs.value!! + 5
            }


            if (_currentInnings.value == 2) {
                _team1Runs.value = _team1Runs.value!! + (runs ?: 0)
            } else {
                _team2Runs.value = _team2Runs.value!! + (runs ?: 0)
            }

            _lastOutcome.value = "${runs ?: "?"} run${if (runs != 1) "s" else ""}"
        }

        _ballsBowled.value = newBalls + 1


        if (_wickets.value == maxWickets || _ballsBowled.value == totalBallsPerInnings) {
            if (_currentInnings.value == 1) {
                _currentInnings.value = 2
                _wickets.value = 0
                _ballsBowled.value = 0


                _lastOutcome.value = "Innings Break"
            } else {
                finishMatch()
            }
        }


        if (_currentInnings.value == 2 && _team2Runs.value!! > _team1Runs.value!!) {
            finishMatch()
        }
    }

    private fun finishMatch() {

        _matchEnded.value = true


        _matchResult.value = when {
            _team2Runs.value!! > _team1Runs.value!! -> "$team2Name wins"
            _team1Runs.value!! > _team2Runs.value!! -> "$team1Name wins"
            _team1Runs.value!! == _team2Runs.value!! -> "$team1Name wins"
            else -> "Match Drawn!" // unreachable
        }
    }

    private fun getRandomOutcome(): String {
        val outcomes = listOf("0", "1", "2", "3", "4", "6", "Out", "Invalid")


        return outcomes.random()
    }

    fun ballsToOvers(balls: Int): String {

        val overs = balls / 6
        val rem = balls % 6
        return "$overs.$rem"
    }
}
