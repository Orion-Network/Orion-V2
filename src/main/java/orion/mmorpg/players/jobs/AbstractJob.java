package orion.mmorpg.players.jobs;

import lombok.Getter;
import org.bukkit.Bukkit;

public abstract class AbstractJob {

    @Getter
    JobType jobType;
    public int experience;
    public int level;

    public AbstractJob(JobType jobType) {
        this.jobType = jobType;
        this.experience = 0;
        this.level = 1;
    }

    public AbstractJob(JobType jobType, int level, int experience) {
        this.jobType = jobType;
        this.experience = experience;
        this.level = level;
    }

    public final void addExp(int experience) {
        this.experience += experience;
        if (this.experience >= neededExperienceToLevelup()) {
            this.experience = 0;
            this.level += 1;
        }
    }

    public int neededExperienceToLevelup() {
        return (int) (Math.pow(this.level, 1.2) * 25);
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }
}
