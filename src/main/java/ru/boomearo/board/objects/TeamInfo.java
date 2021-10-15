package ru.boomearo.board.objects;

import org.bukkit.scoreboard.Team;

import ru.boomearo.board.objects.boards.AbstractHolder;

public class TeamInfo {

    private final Team team;
    private final AbstractHolder holder;

    public TeamInfo(Team team, AbstractHolder holder) {
        this.team = team;
        this.holder = holder;
    }

    public Team getTeam() {
        return this.team;
    }

    public AbstractHolder getHolder() {
        return this.holder;
    }
}
