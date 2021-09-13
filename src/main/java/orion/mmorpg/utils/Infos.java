package orion.mmorpg.utils;

import java.time.LocalDateTime;

public class Infos {
    String playerName;
    LocalDateTime firstPlayed;
    LocalDateTime lastPlayed;
    long playerTime;

    public Infos(String playerName, LocalDateTime firstPlayed, LocalDateTime lastPlayed, long playerTime) {
        this.playerName = playerName;
        this.firstPlayed = firstPlayed;
        this.lastPlayed = lastPlayed;
        this.playerTime = playerTime;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public LocalDateTime getFirstPlayed() {
        return firstPlayed;
    }

    public void setFirstPlayed(LocalDateTime firstPlayed) {
        this.firstPlayed = firstPlayed;
    }

    public LocalDateTime getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(LocalDateTime lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public long getPlayerTime() {
        return playerTime;
    }

    public void setPlayerTime(long playerTime) {
        this.playerTime = playerTime;
    }
}
