package orion.mmorpg.players.jobs;

public class Fishing extends AbstractJob {
    public Fishing() {
        super(JobType.FISHING);
    }

    public Fishing(int level, int experience) {
        super(JobType.FISHING, level, experience);
    }
}
