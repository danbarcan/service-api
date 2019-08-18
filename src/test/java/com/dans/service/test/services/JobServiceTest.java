package com.dans.service.test.services;

import com.dans.service.entities.Job;
import com.dans.service.entities.User;
import com.dans.service.messaging.Publisher;
import com.dans.service.payloads.ApiResponse;
import com.dans.service.payloads.JobPayload;
import com.dans.service.repositories.CarRepository;
import com.dans.service.repositories.JobRepository;
import com.dans.service.repositories.UserRepository;
import com.dans.service.services.JobService;
import com.dans.service.services.OfferService;
import org.assertj.core.api.Assertions;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobServiceTest {

    private JobService jobService;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private OfferService offerService;

    @Mock
    private Publisher publisher;

    @Before
    public void setUp() {
        this.jobService = new JobService(jobRepository, userRepository, carRepository, offerService, publisher);
    }

    private User user = User.builder().email("test@test.com")
            .password("password")
            .name("test")
            .username("test")
            .phoneNumber("07test")
            .build();

    private JobPayload jobPayload = new JobPayload(-1L, 1L, 1L, "test", "test", "model", "make", 2000);

    private Job job = Job.createJobFromJobPayload(jobPayload, user);

    @Ignore
    @Test
    public void getAllReturnsListWithOneElement() {
        BDDMockito.given(this.jobRepository.findAllByOrderByTimestampDesc()).willReturn(Arrays.asList(this.job));
        Assert.assertNotNull(jobService.getAllJobs().getBody());
        Assert.assertEquals(1, jobService.getAllJobs().getBody().size());
        Assert.assertTrue(jobService.getAllJobs().getBody().contains(this.job));
    }

    @Ignore
    @Test
    public void getAllReturnsEmptyList() {
        Assert.assertNotNull(jobService.getAllJobs().getBody());
        Assert.assertTrue(jobService.getAllJobs().getBody().isEmpty());
    }

    @Test
    public void saveCarFail() {
        Assert.assertThat(jobService.saveJob(jobPayload), Is.is(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, "UNAUTHORIZED"))));
    }

    @Test
    public void saveCarSuccess() {
        BDDMockito.given(this.userRepository.findById(jobPayload.getUserId())).willReturn(Optional.of(this.user));

        Assert.assertThat(jobService.saveJob(jobPayload), Is.is(ResponseEntity.ok(new ApiResponse(true, "Job successfully saved"))));
    }

    @Test
    public void updateCarFailNotFound() {
        Assert.assertThat(jobService.updateJob(jobPayload), Is.is(ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "Job not found"))));
    }

    @Test
    public void updateCarSuccess() {
        BDDMockito.given(this.jobRepository.findById(BDDMockito.anyLong())).willReturn(Optional.of(this.job));
        BDDMockito.given(this.userRepository.findById(jobPayload.getUserId())).willReturn(Optional.of(this.user));

        Assert.assertThat(jobService.updateJob(jobPayload), Is.is(ResponseEntity.ok(new ApiResponse(true, "Job successfully updated"))));
    }
}
