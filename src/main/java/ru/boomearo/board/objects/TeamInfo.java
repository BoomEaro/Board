package ru.boomearo.board.objects;

import org.bukkit.scoreboard.Team;

import ru.boomearo.board.objects.boards.AbstractHolder;
import ru.boomearo.board.objects.boards.HolderResult;

import java.util.Objects;

public class TeamInfo {

    private final Team team;
    private final AbstractHolder holder;
    private final int index;

    public TeamInfo(Team team, AbstractHolder holder, int index) {
        this.team = team;
        this.holder = holder;
        this.index = index;
    }

    public Team getTeam() {
        return this.team;
    }

    public AbstractHolder getHolder() {
        return this.holder;
    }

    public int getIndex() {
        return this.index;
    }

    public void update() {
        HolderResult result = this.holder.getHolderResult();

        this.team.setPrefix(result.getPrefix());
        this.team.setSuffix(result.getSuffix());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamInfo teamInfo = (TeamInfo) o;
        return index == teamInfo.index && Objects.equals(team, teamInfo.team) && Objects.equals(holder, teamInfo.holder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, holder, index);
    }

    @Override
    public String toString() {
        return "TeamInfo{" +
                "team=" + team +
                ", holder=" + holder +
                ", index=" + index +
                '}';
    }
}
