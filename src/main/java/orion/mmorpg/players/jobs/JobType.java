package orion.mmorpg.players.jobs;

import lombok.Getter;

public enum JobType {
    SMITHING(Smithing.class),
    MINING(Mining.class);

    @Getter
    Class<? extends AbstractJob> jobClass;

    JobType(Class<? extends AbstractJob> jobClass) {
        this.jobClass = jobClass;
    }
}
