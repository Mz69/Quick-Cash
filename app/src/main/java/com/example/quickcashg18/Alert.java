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

        // creating an employee landing object to store the notifications for the employee
        EmployeeLanding employeeNotifs = new EmployeeLanding();
        // checking if the users preferences match with those of the posted job
        if (matchesPreference(job)) {
            // sending the user a notification about the job posting
            employeeNotifs.addNotification("New job posting for you: " + job.getJobName());
        }

    }

    // method checks if the user should receive a notification or not depending on their set preferences
    // compared to the details of the new job posting
    public boolean matchesPreference (Job job) {
        // create a employee profile to compare preferences
        EmployeeProfile employee = new EmployeeProfile();

        // if the hourly pay is at least the employees minimum preferred pay then proceed
        if (job.getTotalPay() >= Integer.parseInt(employee.getEnteredMinHourlyWage()) ) {
            // checking if the location of the job matches with the location the employee prefers
            if (job.getLocation().equals(employee.getEnteredJobLocation())) {
                // comparing if the job time frame is within the employees max and min hour preference
                if (job.getDuration() <= Integer.parseInt(employee.getEnteredMaxHours()) &&
                        job.getDuration() >= Integer.parseInt(employee.getEnteredMinHours())) {
                    return true;
                }
            }
        }
        // if any of the job's details aren't within the users preferences then return false
        return false;
    }

    // check if a job has been accepted so we know if the person who posted the job should be alerted
    public boolean acceptedJob() {
        return false;
    }


}
