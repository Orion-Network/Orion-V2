package orion.mmorpg.players.jobs;

public abstract class AbstractJob {
    public int experience;
    public int level;

    public AbstractJob(int experience, int level) {
        this.experience = experience;
        this.level = level;
    }

    public final void addExp(int experience) {
        this.experience += experience;
        if(experience >= neededExperienceToLevelup()) {
            this.experience = 0;
            this.level += 1;
        }
    }

    public int neededExperienceToLevelup() {
        return (int) (Math.pow(this.level, 1.2)*25);
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }
}
