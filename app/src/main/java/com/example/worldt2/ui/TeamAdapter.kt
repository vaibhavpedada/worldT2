package com.example.worldt2.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.worldt2.model.Team
import com.example.worldt2.databinding.ItemTeamBinding

class TeamAdapter(
    private val onTeamClick: (Team) -> Unit
) : RecyclerView.Adapter<TeamAdapter.TeamViewHolder>() {

    private val teams = mutableListOf<Team>()

    fun submitList(list: List<Team>) {
        teams.clear()
        teams.addAll(list)
        notifyDataSetChanged()
    }

    inner class TeamViewHolder(private val binding: ItemTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(team: Team) {
            binding.teamName.text = team.name
            //change 1

            binding.root.setBackgroundColor(
                if (team.isSelected) Color.GRAY else Color.WHITE
            )

            binding.flagImage.load(team.flag)

            binding.root.setOnClickListener {
                onTeamClick(team)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding = ItemTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamViewHolder(binding)
    }

    override fun getItemCount(): Int = teams.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(teams[position])
    }
}