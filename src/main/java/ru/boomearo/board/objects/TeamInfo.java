package ru.boomearo.board.objects;

import org.bukkit.scoreboard.Team;

import ru.boomearo.board.objects.boards.AbstractHolder;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamInfo teamInfo = (TeamInfo) o;
        return Objects.equals(team, teamInfo.team) && Objects.equals(holder, teamInfo.holder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, holder);
    }

    @Override
    public String toString() {
        return "TeamInfo{" +
                "team=" + team +
                ", holder=" + holder +
                '}';
    }
}
