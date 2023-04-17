package orion.mmorpg.players.jobs;

public class Mining extends AbstractJob{
    public Mining() {
        super(JobType.MINING);
    }

    public Mining(int level, int experience) {
        super(JobType.MINING, level, experience);
    }
}
