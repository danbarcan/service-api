package com.dans.service.test.controllers;

import com.dans.service.controllers.JobController;
import com.dans.service.entities.Job;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.JobPayload;
import com.dans.service.payloads.JobResponsePayload;
import com.dans.service.services.JobService;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTest {

    private JobController jobController;

    @Mock
    private JobService jobService;

    @Mock
    private JobPayload jobPayload;

    @Before
    public void setUp() {
        this.jobController = new JobController(jobService);
    }

    @Test
    public void saveJob() {
        BDDMockito.given(this.jobController.saveJob(this.jobPayload)).willReturn(ResponseEntity.ok(Arrays.asList(new JobResponsePayload())));
        Assert.assertThat(jobController.saveJob(jobPayload), Is.is(ResponseEntity.ok(Arrays.asList(new JobResponsePayload()))));
    }

    @Test
    public void updateJob() {
        BDDMockito.given(this.jobController.updateJob(this.jobPayload)).willReturn(ResponseEntity.ok(new ApiResponse(true, "Job successfully updated")));
        Assert.assertThat(jobController.updateJob(jobPayload), Is.is(ResponseEntity.ok(new ApiResponse(true, "Job successfully updated"))));
    }

    @Test
    public void getAllJobs() {
        BDDMockito.given(this.jobController.getAllJobs()).willReturn(ResponseEntity.ok(Arrays.asList(new JobResponsePayload())));
        Assert.assertThat(jobController.getAllJobs(), Is.is(ResponseEntity.ok(Arrays.asList(new JobResponsePayload()))));
    }
}
