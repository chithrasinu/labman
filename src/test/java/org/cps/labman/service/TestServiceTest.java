package org.cps.labman.service;

import org.cps.labman.dto.TestDto;
import org.cps.labman.exception.DataNotFoundException;
import org.cps.labman.persistence.model.Test;
import org.cps.labman.persistence.repo.TestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    private TestService testService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        testService = new TestService(testRepository);
    }

    @org.junit.jupiter.api.Test
    void testCreate() {
        TestDto testDto = new TestDto();
        testDto.setId(1L);
        testDto.setTestName("Test A");

        Test test = new Test();
        test.setId(testDto.getId());
        test.setTestName(testDto.getTestName());

        Mockito.when(testRepository.save(Mockito.any(Test.class))).thenReturn(test);

        testService.create(testDto);

        Mockito.verify(testRepository, Mockito.times(1)).save(Mockito.any(Test.class));
    }

    @org.junit.jupiter.api.Test
    void testEdit() throws DataNotFoundException {
        Long testId = 1L;

        Test test = new Test();
        test.setId(testId);
        test.setTestName("Test A");

        Mockito.when(testRepository.findById(testId)).thenReturn(Optional.of(test));

        TestDto testDto = testService.edit(testId);

        Assertions.assertEquals(test.getId(), testDto.getId());
        Assertions.assertEquals(test.getTestName(), testDto.getTestName());
    }

    @org.junit.jupiter.api.Test
    void testFindAll() {
        Iterable<Test> tests = Mockito.mock(Iterable.class);
        Mockito.when(testRepository.findAll()).thenReturn(tests);

        Iterable<Test> result = testService.findAll();

        Assertions.assertEquals(tests, result);
    }

    @org.junit.jupiter.api.Test
    void testFindTest() throws DataNotFoundException {
        Long testId = 1L;

        Test test = new Test();
        test.setId(testId);
        test.setTestName("Test A");

        Mockito.when(testRepository.findById(testId)).thenReturn(Optional.of(test));

        Test result = testService.findTest(testId);

        Assertions.assertEquals(test, result);
    }

    @org.junit.jupiter.api.Test
    void testFindTestNotFound() {
        Long testId = 1L;

        Mockito.when(testRepository.findById(testId)).thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            testService.findTest(testId);
        });
    }

    @org.junit.jupiter.api.Test
    void testDelete() throws DataNotFoundException {
        Long testId = 1L;

        Test test = new Test();
        test.setId(testId);
        test.setTestName("Test A");

        Mockito.when(testRepository.findById(testId)).thenReturn(Optional.of(test));

        testService.delete(testId);

        Mockito.verify(testRepository, Mockito.times(1)).deleteById(testId);
    }

    @org.junit.jupiter.api.Test
    void testDeleteNotFound() {
        Long testId = 1L;

        Mockito.when(testRepository.findById(testId)).thenReturn(Optional.empty());

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            testService.delete(testId);
        });
    }
}
