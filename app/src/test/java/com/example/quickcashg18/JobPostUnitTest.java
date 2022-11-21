package com.example.quickcashg18;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.location.Location;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import javax.inject.Inject;

@RunWith(MockitoJUnitRunner.Silent.class)
public class JobPostUnitTest {
    @InjectMocks
    static PostJob correctJob;
    static PostJob missingJobName;
    static PostJob missingLocation;
    static PostJob correctJobMin;
    static PostJob correctJobDays;
    static PostJob missingTime;
    static PostJob incorrectTime;
    static PostJob missingSalary;
    static PostJob incorrectSalary;
    static PostJob missingUrgency;
    static PostJob incorrectUrgency;
    static PostJob correctNotUrgent;
    static Location nonemptyLocation;

    @BeforeClass
    public static void setup(){
        nonemptyLocation = Mockito.mock(Location.class);
        correctJob=Mockito.mock(PostJob.class);

        Mockito.when(correctJob.getJobDBRef()).thenReturn("knitting Instuctor");
        Mockito.when(correctJob.getLocation()).thenReturn(nonemptyLocation);
        Mockito.when(correctJob.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(correctJob.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(correctJob.getUrgency()).thenReturn("Urgent");


        missingJobName=Mockito.mock(PostJob.class);
        Mockito.when(missingJobName.getJobDBRef()).thenReturn("");
        Mockito.when(missingJobName.getLocation()).thenReturn(new Location(""));
        Mockito.when(missingJobName.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(missingJobName.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(missingJobName.getUrgency()).thenReturn("Urgent");

        missingLocation=Mockito.mock(PostJob.class);
        Mockito.when(missingLocation.getJobDBRef()).thenReturn("knitting Instructor");
        Mockito.when(missingLocation.getLocation()).thenReturn(null);
        Mockito.when(missingLocation.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(missingLocation.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(missingLocation.getUrgency()).thenReturn("Urgent");

        correctJobMin= Mockito.mock(PostJob.class);
        Mockito.when(correctJobMin.getJobDBRef()).thenReturn("knitting Instuctor");
        Mockito.when(correctJobMin.getLocation()).thenReturn(nonemptyLocation);
        Mockito.when(correctJobMin.getTimeFrame()).thenReturn("40 minutes");
        Mockito.when(correctJobMin.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(correctJobMin.getUrgency()).thenReturn("Urgent");

        correctJobDays = Mockito.mock(PostJob.class);
        Mockito.when(correctJobDays.getJobDBRef()).thenReturn("knitting Instuctor");
        Mockito.when(correctJobDays.getLocation()).thenReturn(nonemptyLocation);
        Mockito.when(correctJobDays.getTimeFrame()).thenReturn("4 days");
        Mockito.when(correctJobDays.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(correctJobDays.getUrgency()).thenReturn("Urgent");

        missingTime=Mockito.mock(PostJob.class);
        Mockito.when(missingTime.getJobDBRef()).thenReturn("knitting Instuctor");
        Mockito.when(missingTime.getLocation()).thenReturn(nonemptyLocation);
        Mockito.when(missingTime.getTimeFrame()).thenReturn("");
        Mockito.when(missingTime.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(missingTime.getUrgency()).thenReturn("Urgent");

        incorrectTime=Mockito.mock(PostJob.class);
        Mockito.when(incorrectTime.getJobDBRef()).thenReturn("knitting Instuctor");
        Mockito.when(incorrectTime.getLocation()).thenReturn(nonemptyLocation);
        Mockito.when(incorrectTime.getTimeFrame()).thenReturn("abc");
        Mockito.when(incorrectTime.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(incorrectTime.getUrgency()).thenReturn("Urgent");

        missingSalary=Mockito.mock(PostJob.class);
        Mockito.when(missingSalary.getJobDBRef()).thenReturn("knitting Instructor");
        Mockito.when(missingSalary.getLocation()).thenReturn(nonemptyLocation);
        Mockito.when(missingSalary.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(missingSalary.getSalary()).thenReturn("");
        Mockito.when(missingSalary.getUrgency()).thenReturn("Urgent");

        incorrectSalary=Mockito.mock(PostJob.class);
        Mockito.when(incorrectSalary.getJobDBRef()).thenReturn("knitting Instructor");
        Mockito.when(incorrectSalary.getLocation()).thenReturn(nonemptyLocation);
        Mockito.when(incorrectSalary.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(incorrectSalary.getSalary()).thenReturn("abc");
        Mockito.when(incorrectSalary.getUrgency()).thenReturn("Urgent");

        missingUrgency=Mockito.mock(PostJob.class);
        Mockito.when(missingUrgency.getJobDBRef()).thenReturn("knitting Instructor");
        Mockito.when(missingUrgency.getLocation()).thenReturn(nonemptyLocation);
        Mockito.when(missingUrgency.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(missingUrgency.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(missingUrgency.getUrgency()).thenReturn("");

        incorrectUrgency=Mockito.mock(PostJob.class);
        Mockito.when(incorrectUrgency.getJobDBRef()).thenReturn("knitting Instructor");
        Mockito.when(incorrectUrgency.getLocation()).thenReturn(nonemptyLocation);
        Mockito.when(incorrectUrgency.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(incorrectUrgency.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(incorrectUrgency.getUrgency()).thenReturn("");

        correctNotUrgent=Mockito.mock(PostJob.class);
        Mockito.when(correctNotUrgent.getJobDBRef()).thenReturn("knitting Instructor");
        Mockito.when(correctNotUrgent.getLocation()).thenReturn(nonemptyLocation);
        Mockito.when(correctNotUrgent.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(correctNotUrgent.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(correctNotUrgent.getUrgency()).thenReturn("Not Urgent");

    }

    @Test
    public void checkIfJobNameISValid(){
        assertFalse(missingJobName.isJobValid());
        assertTrue(correctJob.isJobValid());
    }
    @Test
    public void checkIfValidLocation(){
        assertFalse("Invalid location marked valid" ,missingLocation.isJobValid());
        assertTrue("Valid location marked invalid", correctJob.isJobValid());
    }
    @Test
    public void checkIfValidTime(){
        assertFalse(missingTime.isJobValid());
        assertFalse(incorrectTime.isJobValid());
        assertTrue(correctJobDays.isJobValid());
        assertTrue(correctJobMin.isJobValid());
        assertEquals("4 hours",correctJob.getTimeFrame());
        assertEquals("40 minutes",correctJobMin.getTimeFrame());
        assertEquals("4 days",correctJob.getTimeFrame());


    }
    @Test
    public void checkIfValidSalary(){
        assertFalse(missingSalary.isJobValid());
        assertFalse(incorrectSalary.isJobValid());
        assertEquals("30",correctJob.getSalary());
    }
    @Test
    public void checkIfValidUrgency(){
        assertFalse(missingUrgency.isJobValid());
        assertTrue(correctNotUrgent.isJobValid());
        assertEquals("Urgent",correctJob.getUrgency());
        assertEquals("Not Urgent",correctNotUrgent.getUrgency());
    }

}
