package orion.mmorpg.players.jobs;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Jobs {
    @Getter
    HashMap<JobType, AbstractJob> jobs;

    public Jobs() {
        this.jobs = new HashMap<>();
    }

    public static Jobs getBaseJobs() {
        Jobs jobs = new Jobs();
        JobType[] jobTypes = JobType.values();
        for(JobType jobType : jobTypes) {
            try {
                AbstractJob job = jobType.getJobClass().getDeclaredConstructor().newInstance();
                jobs.addJob(job);
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return jobs;
    }

    public AbstractJob getJob(JobType jobType) {
        return jobs.get(jobType);
    }

    public void addJob(AbstractJob job) {
        jobs.put(job.getJobType(), job);
    }
}
