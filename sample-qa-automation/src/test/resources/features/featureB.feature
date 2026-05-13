Feature: Feature B demo

  Background:
    Given environment is ready
    And user is authenticated

  @test1
  Scenario: Scenario B1
    When I perform a failing operation
    Then the system should skip the test
    And check next test step continues or not
    And further some steps
