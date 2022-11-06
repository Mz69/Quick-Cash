package com.example.quickcashg18;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class JobPostUnitTest {
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
    @BeforeClass
    public static void setup(){
        correctJob=Mockito.mock(PostJob.class);
        Mockito.when(correctJob.getJobName()).thenReturn("knitting Instuctor");
        Mockito.when(correctJob.getLocation()).thenReturn("2121 Shirley St.");
        Mockito.when(correctJob.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(correctJob.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(correctJob.getUrgency()).thenReturn("Urgent");


        missingJobName=Mockito.mock(PostJob.class);
        Mockito.when(missingJobName.getJobName()).thenReturn("");
        Mockito.when(missingJobName.getLocation()).thenReturn("2121 Shirley St.");
        Mockito.when(missingJobName.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(missingJobName.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(missingJobName.getUrgency()).thenReturn("Urgent");

        missingLocation=Mockito.mock(PostJob.class);
        Mockito.when(missingLocation.getJobName()).thenReturn("knitting Instructor");
        Mockito.when(missingLocation.getLocation()).thenReturn("");
        Mockito.when(missingLocation.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(missingLocation.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(missingLocation.getUrgency()).thenReturn("Urgent");

        correctJobMin= Mockito.mock(PostJob.class);
        Mockito.when(correctJobMin.getJobName()).thenReturn("knitting Instuctor");
        Mockito.when(correctJobMin.getLocation()).thenReturn("2121 Shirley St.");
        Mockito.when(correctJobMin.getTimeFrame()).thenReturn("40 minutes");
        Mockito.when(correctJobMin.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(correctJobMin.getUrgency()).thenReturn("Urgent");

        correctJobDays = Mockito.mock(PostJob.class);
        Mockito.when(correctJobDays.getJobName()).thenReturn("knitting Instuctor");
        Mockito.when(correctJobDays.getLocation()).thenReturn("2121 Shirley St.");
        Mockito.when(correctJobDays.getTimeFrame()).thenReturn("4 days");
        Mockito.when(correctJobDays.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(correctJobDays.getUrgency()).thenReturn("Urgent");

        missingTime=Mockito.mock(PostJob.class);
        Mockito.when(missingTime.getJobName()).thenReturn("knitting Instuctor");
        Mockito.when(missingTime.getLocation()).thenReturn("2121 Shirley St.");
        Mockito.when(missingTime.getTimeFrame()).thenReturn("");
        Mockito.when(missingTime.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(missingTime.getUrgency()).thenReturn("Urgent");

        incorrectTime=Mockito.mock(PostJob.class);
        Mockito.when(incorrectTime.getJobName()).thenReturn("knitting Instuctor");
        Mockito.when(incorrectTime.getLocation()).thenReturn("2121 Shirley St.");
        Mockito.when(incorrectTime.getTimeFrame()).thenReturn("abc");
        Mockito.when(incorrectTime.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(incorrectTime.getUrgency()).thenReturn("Urgent");

        missingSalary=Mockito.mock(PostJob.class);
        Mockito.when(missingSalary.getJobName()).thenReturn("knitting Instructor");
        Mockito.when(missingSalary.getLocation()).thenReturn("2121 Shirley St.");
        Mockito.when(missingSalary.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(missingSalary.getSalary()).thenReturn("");
        Mockito.when(missingSalary.getUrgency()).thenReturn("Urgent");

        incorrectSalary=Mockito.mock(PostJob.class);
        Mockito.when(incorrectSalary.getJobName()).thenReturn("knitting Instructor");
        Mockito.when(incorrectSalary.getLocation()).thenReturn("2121 Shirley St.");
        Mockito.when(incorrectSalary.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(incorrectSalary.getSalary()).thenReturn("abc");
        Mockito.when(incorrectSalary.getUrgency()).thenReturn("Urgent");

        missingUrgency=Mockito.mock(PostJob.class);
        Mockito.when(missingUrgency.getJobName()).thenReturn("knitting Instructor");
        Mockito.when(missingUrgency.getLocation()).thenReturn("2121 Shirley St.");
        Mockito.when(missingUrgency.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(missingUrgency.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(missingUrgency.getUrgency()).thenReturn("");

        incorrectUrgency=Mockito.mock(PostJob.class);
        Mockito.when(incorrectUrgency.getJobName()).thenReturn("knitting Instructor");
        Mockito.when(incorrectUrgency.getLocation()).thenReturn("2121 Shirley St.");
        Mockito.when(incorrectUrgency.getTimeFrame()).thenReturn("4 hours");
        Mockito.when(incorrectUrgency.getSalary()).thenReturn(String.valueOf(30));
        Mockito.when(incorrectUrgency.getUrgency()).thenReturn("");

        correctNotUrgent=Mockito.mock(PostJob.class);
        Mockito.when(correctNotUrgent.getJobName()).thenReturn("knitting Instructor");
        Mockito.when(correctNotUrgent.getLocation()).thenReturn("2121 Shirley St.");
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
        assertFalse(missingLocation.isJobValid());
        assertTrue(correctJob.isJobValid());
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
