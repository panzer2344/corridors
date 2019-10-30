package ru.corridors.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class GameResults implements Serializable {

    private ClientInfo winner;
    private Map<ClientInfo, ClientScore> results;

    public GameResults(ClientInfo winner, Map<ClientInfo, ClientScore> results) {
        this.winner = winner;
        this.results = results;
    }

    public ClientInfo getWinner() {
        return winner;
    }

    /*public void setWinner(ClientInfo winner) {
        this.winner = winner;
    }*/

    public Map<ClientInfo, ClientScore> getResults() {
        return results;
    }

    /*public void setResults(Map<ClientInfo, ClientScore> results) {
        this.results = results;
    }*/

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GameResults that = (GameResults) object;
        return Objects.equals(winner, that.winner) &&
                Objects.equals(results, that.results);
    }

    @Override
    public int hashCode() {
        return Objects.hash(winner, results);
    }

    @Override
    public String toString() {
        return "GameResults{" +
                "winner=" + winner +
                ", results=" + results +
                '}';
    }
}
