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
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * This test class needs to be modified to reflect the
 * new job posting upgrades.
 * Problem: Hard to mock, seemingly because the location cannot be
 * set in Mockito, and thus the location is always invalid and
 * the test job will never be posted.
 */
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

        Mockito.when(correctJob.getJobTitle()).thenReturn("knitting Instuctor");
        Mockito.when(correctJob.getDuration()).thenReturn("4 hours");
        Mockito.when(correctJob.getTotalPay()).thenReturn(String.valueOf(30));
        Mockito.when(correctJob.getUrgency()).thenReturn("Urgent");


        missingJobName=Mockito.mock(PostJob.class);
        Mockito.when(missingJobName.getJobTitle()).thenReturn("");
        Mockito.when(missingJobName.getDuration()).thenReturn("4 hours");
        Mockito.when(missingJobName.getTotalPay()).thenReturn(String.valueOf(30));
        Mockito.when(missingJobName.getUrgency()).thenReturn("Urgent");

        missingLocation=Mockito.mock(PostJob.class);
        Mockito.when(missingLocation.getJobTitle()).thenReturn("knitting Instructor");
        Mockito.when(missingLocation.getDuration()).thenReturn("4 hours");
        Mockito.when(missingLocation.getTotalPay()).thenReturn(String.valueOf(30));
        Mockito.when(missingLocation.getUrgency()).thenReturn("Urgent");

        correctJobMin= Mockito.mock(PostJob.class);
        Mockito.when(correctJobMin.getJobTitle()).thenReturn("knitting Instuctor");
        Mockito.when(correctJobMin.getDuration()).thenReturn("40 minutes");
        Mockito.when(correctJobMin.getTotalPay()).thenReturn(String.valueOf(30));
        Mockito.when(correctJobMin.getUrgency()).thenReturn("Urgent");

        correctJobDays = Mockito.mock(PostJob.class);
        Mockito.when(correctJobDays.getJobTitle()).thenReturn("knitting Instuctor");
        Mockito.when(correctJobDays.getDuration()).thenReturn("4 days");
        Mockito.when(correctJobDays.getTotalPay()).thenReturn(String.valueOf(30));
        Mockito.when(correctJobDays.getUrgency()).thenReturn("Urgent");

        missingTime=Mockito.mock(PostJob.class);
        Mockito.when(missingTime.getJobTitle()).thenReturn("knitting Instuctor");
        Mockito.when(missingTime.getDuration()).thenReturn("");
        Mockito.when(missingTime.getTotalPay()).thenReturn(String.valueOf(30));
        Mockito.when(missingTime.getUrgency()).thenReturn("Urgent");

        incorrectTime=Mockito.mock(PostJob.class);
        Mockito.when(incorrectTime.getJobTitle()).thenReturn("knitting Instuctor");
        Mockito.when(incorrectTime.getDuration()).thenReturn("abc");
        Mockito.when(incorrectTime.getTotalPay()).thenReturn(String.valueOf(30));
        Mockito.when(incorrectTime.getUrgency()).thenReturn("Urgent");

        missingSalary=Mockito.mock(PostJob.class);
        Mockito.when(missingSalary.getJobTitle()).thenReturn("knitting Instructor");
        Mockito.when(missingSalary.getDuration()).thenReturn("4 hours");
        Mockito.when(missingSalary.getTotalPay()).thenReturn("");
        Mockito.when(missingSalary.getUrgency()).thenReturn("Urgent");

        incorrectSalary=Mockito.mock(PostJob.class);
        Mockito.when(incorrectSalary.getJobTitle()).thenReturn("knitting Instructor");
        Mockito.when(incorrectSalary.getDuration()).thenReturn("4 hours");
        Mockito.when(incorrectSalary.getTotalPay()).thenReturn("abc");
        Mockito.when(incorrectSalary.getUrgency()).thenReturn("Urgent");

        missingUrgency=Mockito.mock(PostJob.class);
        Mockito.when(missingUrgency.getJobTitle()).thenReturn("knitting Instructor");
        Mockito.when(missingUrgency.getDuration()).thenReturn("4 hours");
        Mockito.when(missingUrgency.getTotalPay()).thenReturn(String.valueOf(30));
        Mockito.when(missingUrgency.getUrgency()).thenReturn("");

        incorrectUrgency=Mockito.mock(PostJob.class);
        Mockito.when(incorrectUrgency.getJobTitle()).thenReturn("knitting Instructor");
        Mockito.when(incorrectUrgency.getDuration()).thenReturn("4 hours");
        Mockito.when(incorrectUrgency.getTotalPay()).thenReturn(String.valueOf(30));
        Mockito.when(incorrectUrgency.getUrgency()).thenReturn("");

        correctNotUrgent=Mockito.mock(PostJob.class);
        Mockito.when(correctNotUrgent.getJobTitle()).thenReturn("knitting Instructor");
        Mockito.when(correctNotUrgent.getDuration()).thenReturn("4 hours");
        Mockito.when(correctNotUrgent.getTotalPay()).thenReturn(String.valueOf(30));
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
        assertEquals("4 hours",correctJob.getDuration());
        assertEquals("40 minutes",correctJobMin.getDuration());
        assertEquals("4 days",correctJob.getDuration());


    }
    @Test
    public void checkIfValidSalary(){
        assertFalse(missingSalary.isJobValid());
        assertFalse(incorrectSalary.isJobValid());
        assertEquals("30",correctJob.getTotalPay());
    }
    @Test
    public void checkIfValidUrgency(){
        assertFalse(missingUrgency.isJobValid());
        assertTrue(correctNotUrgent.isJobValid());
        assertEquals("Urgent",correctJob.getUrgency());
        assertEquals("Not Urgent",correctNotUrgent.getUrgency());
    }

}
