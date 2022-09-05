package orion.mmorpg.players.jobs;

public class Jobs {
    Smithing smithing;

    public Jobs(Smithing smithing) {
        this.smithing = smithing;
    }

    public static Jobs getBaseJobs() {
        return new Jobs(new Smithing(0,1));
    }

    public Smithing getSmithing() {
        return smithing;
    }
}
