package com.example.worldt2.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.worldt2.databinding.ActivityTeamSelectBinding
import com.example.worldt2.viewmodels.TeamViewModel

class TeamSelectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamSelectBinding
    private lateinit var viewModel: TeamViewModel
    private val adapter = TeamAdapter { clickedTeam ->
        viewModel.toggleSelection(clickedTeam)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[TeamViewModel::class.java]

        binding.teamRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.teamRecyclerView.adapter = adapter

        binding.teamRecyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        lifecycleScope.launchWhenStarted {
            viewModel.teams.collect { teamList ->
                adapter.submitList(teamList)
                binding.startMatchButton.isEnabled = teamList.count { it.isSelected } == 2
                if(binding.startMatchButton.isEnabled)
                    binding.startMatchButton.setBackgroundColor(Color.rgb(0, 153, 51))
                else
                    binding.startMatchButton.setBackgroundColor(Color.LTGRAY)
            }
        }

        binding.startMatchButton.setOnClickListener {
            val selected = viewModel.getSelectedTeams()
            val intent = Intent(this, MatchCentreActivity::class.java).apply {
                putExtra("team1", selected[0].name)
                putExtra("team2", selected[1].name)
            }
            startActivity(intent)
        }

        viewModel.loadTeams()
    }
}