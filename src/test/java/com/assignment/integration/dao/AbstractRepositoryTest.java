package com.assignment.integration.dao;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.assignment.integration.BookingApplicationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.JpaRepository;

abstract class AbstractRepositoryTest extends BookingApplicationTest {

  private JpaRepository<?, Long> repository;

  public abstract JpaRepository getRepository();

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    repository = getRepository();
  }

  @Test
  public void repositoryInitialized() {
    assertThat("Repository initializing failed", repository, notNullValue());
  }

  @Test
  public void repositoryLoaded() {
    assertThat("Test data loading failed", repository.count(), greaterThan(0L));
  }
}
