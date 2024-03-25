package org.cps.labman.service;

import org.cps.labman.dto.TestDto;
import org.cps.labman.exception.DataNotFoundException;
import org.cps.labman.persistence.model.Test;
import org.cps.labman.persistence.repo.TestRepository;
import org.springframework.stereotype.Service;

/**
 * manages test management
 */
@Service
public class TestService {

    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public void create(TestDto testDto) {
        Test test = new Test();
        test.setId(testDto.getId());
        test.setTestName(testDto.getTestName());
        testRepository.save(test);
    }

    public TestDto edit(Long id) {
        TestDto testDto = new TestDto();
        Test test = findTest(id);
        testDto.setId(test.getId());
        testDto.setTestName(test.getTestName());
        return testDto;
    }

    public Iterable<Test> findAll() {
        return testRepository.findAll();
    }

    public Test findTest(Long id) throws DataNotFoundException {
        return testRepository.findById(id)
                .orElseThrow(DataNotFoundException::new);
    }

    public void delete(Long id) throws DataNotFoundException {
        testRepository.findById(id)
                .orElseThrow(DataNotFoundException::new);
        testRepository.deleteById(id);
    }
}

