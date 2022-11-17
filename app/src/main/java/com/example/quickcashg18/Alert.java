package com.example.quickcashg18;

public class Alert {

    public void notifyEmployer() {
        // send the alerts to the users profile then save them as strings in an array, the user can then click a button
        // which will allow them to view the contents of that array with all the notifications included
        // the notification should display the accepted job or the details of the new job posting


    }

    public void notifyEmployee(Job job) {
        // can possibly pass job through the job class as the params for this method, and when a job is created
        // both save it in firebase and create a new job object which can be referenced
        // the preferences are currently saved in the employee class so call to there for comparing

        // checking if the users preferences match with those of the posted job
        matchesPreference(job);

    }

    // method checks if the user should receive a notification or not depending on their set preferences
    // compared to the details of the new job posting
    public boolean matchesPreference (Job job) {
        // create a employee profile to compare preferences
        EmployeeProfile employee = new EmployeeProfile();

        // if the hourly pay is at least the employees minimum preferred pay then proceed
        if (job.getHourlyPay() >= Integer.parseInt(employee.getEnteredMinHourlyWage()) ) {
            System.out.println("test works");
            // checking if the location of the job matches with the location the employee prefers
            if (job.getLocation().equals(employee.getEnteredJobLocation())) {
                System.out.println("test still working!");
                // comparing if the job time frame is within the employees max and min hour preference
                if (job.getDurationInHours() <= Integer.parseInt(employee.getEnteredMaxHours()) &&
                        job.getDurationInHours() >= Integer.parseInt(employee.getEnteredMinHours())) {
                    System.out.println("final test passes");
                    return true;
                }
            }
        }
        return false;
    }

    // check if a job has been accepted so we know if the person who posted the job should be alerted
    public boolean acceptedJob() {
        return false;
    }


}
