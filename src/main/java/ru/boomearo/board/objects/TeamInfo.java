package ru.boomearo.board.objects;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import ru.boomearo.board.objects.boards.AbstractValueHolder;
import ru.boomearo.board.objects.boards.HolderResult;

import java.util.Objects;

public class TeamInfo {

    private final Scoreboard scoreboard;
    private final Objective objective;
    private final Team team;
    private final AbstractValueHolder holder;
    private final String teamEntryName;
    private final int scoreIndex;

    private HolderResult currentResult = null;

    public TeamInfo(Scoreboard scoreboard, Objective objective, Team team, AbstractValueHolder holder, String teamEntryName, int scoreIndex) {
        this.scoreboard = scoreboard;
        this.objective = objective;
        this.team = team;
        this.holder = holder;
        this.teamEntryName = teamEntryName;
        this.scoreIndex = scoreIndex;
    }

    public void update() {
        HolderResult newResult = this.holder.getHolderResult();

        if (!Objects.equals(newResult, this.currentResult)) {
            applyResult(newResult);
        }
    }

    private void applyResult(HolderResult newResult) {
        HolderResult currentResult = this.currentResult;
        this.currentResult = newResult;

        if (currentResult != null && newResult == null) {
            this.scoreboard.resetScores(this.teamEntryName);
        }
        else if (newResult != null && currentResult == null) {
            this.team.addEntry(this.teamEntryName);
            this.objective.getScore(this.teamEntryName).setScore(this.scoreIndex);
        }

        if (newResult != null) {
            this.team.setPrefix(newResult.getPrefix());
            this.team.setSuffix(newResult.getSuffix());
        }
    }

}
