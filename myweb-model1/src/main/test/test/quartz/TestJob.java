package test.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		System.out.println("Job Executed [" + new Date() + "]");
		
	} // execute
	
}
