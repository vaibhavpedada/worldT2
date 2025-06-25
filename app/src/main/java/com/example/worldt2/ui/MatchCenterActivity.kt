package com.example.worldt2.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.worldt2.R
import com.example.worldt2.viewmodels.MatchViewModel
import androidx.activity.viewModels

class MatchCentreActivity : AppCompatActivity() {

    private lateinit var team1HeaderTv: TextView
    private lateinit var team1ScoreTv: TextView
    private lateinit var team1OversTv: TextView
    private lateinit var team2HeaderTv: TextView
    private lateinit var team2ScoreTv: TextView
    private lateinit var team2OversTv: TextView
    private lateinit var lastBallOutcomeTv: TextView
    private lateinit var nextBallButton: Button

    private val viewModel: MatchViewModel by viewModels()//comment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_centre)

        team1HeaderTv = findViewById(R.id.team1Header)
        team1ScoreTv = findViewById(R.id.team1Score)
        team1OversTv = findViewById(R.id.team1Overs)
        team2HeaderTv = findViewById(R.id.team2Header)
        team2ScoreTv = findViewById(R.id.team2Score)
        team2OversTv = findViewById(R.id.team2Overs)
        lastBallOutcomeTv = findViewById(R.id.lastBallOutcome)
        nextBallButton = findViewById(R.id.nextBallButton)

        val team1 = intent.getStringExtra("team1") ?: "Team 1"
        val team2 = intent.getStringExtra("team2") ?: "Team 2"

        viewModel.setTeams(team1, team2)

        setupObservers()

        nextBallButton.setOnClickListener {
            viewModel.playNextBall()
        }
    }

    private fun setupObservers() {
        viewModel.currentInnings.observe(this) { updateUI() }
        viewModel.ballsBowled.observe(this) { updateUI() }
        viewModel.wickets.observe(this) { updateUI() }
        viewModel.team1Runs.observe(this) { updateUI() }

        // viewModel.team2Runs.observe(this) { updateUI() } // Commented out

        viewModel.lastOutcome.observe(this) {
            lastBallOutcomeTv.text = it
        }

        viewModel.matchEnded.observe(this) { isEnded ->
            if (isEnded) {
                nextBallButton.isEnabled = false
                nextBallButton.text = "Match Over"
            }
        }

        viewModel.matchResult.observe(this) { result ->
            lastBallOutcomeTv.text = result
            lastBallOutcomeTv.textSize = 26f
        }
    }

    private fun updateUI() {
        val (team1, team2) = viewModel.getTeamNames()

        val balls = viewModel.ballsBowled.value ?: 0
        val wickets = viewModel.wickets.value ?: 0
        val innings = viewModel.currentInnings.value ?: 1
        val team1Runs = viewModel.team1Runs.value ?: 0
        val team2Runs = viewModel.team2Runs.value ?: 0

        val overs1 = if (innings == 1) viewModel.ballsToOvers(balls) else "2.0"
        val overs2 = if (innings == 2) viewModel.ballsToOvers(balls) else "0.0"

        if (innings == 1) {
            team1HeaderTv.text = "$team1 (Batting)"
            team2HeaderTv.text = "$team2 (Bowling)"
            team1ScoreTv.text = "Score: $team1Runs/$wickets"
            team2ScoreTv.text = "yet to bat"
            team1OversTv.text = "Overs: $overs1"
            team2OversTv.text = "yet to bat"
        } else {
            team1HeaderTv.text = "$team1 (Bowling)"
            team2HeaderTv.text = "$team2 (Batting)"
            team1ScoreTv.text = "Score: $team1Runs/3"

            team2ScoreTv.text = "Score: $team2Runs/$wickets"
            team1OversTv.text = "Overs: 2.0"
            team2OversTv.text = "Overs: $overs2"
        }
    }
}
