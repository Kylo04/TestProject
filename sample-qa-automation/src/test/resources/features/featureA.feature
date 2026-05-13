Feature: Feature A demo

  Background:
    Given environment is ready
    And user is authenticated

  @test1
  Scenario: Scenario A1
    When I perform a normal operation
    Then the operation should pass
    And further some steps

  Scenario Outline: Scenario A2
    When I perform operation for "<type>"
    Then the result should "<result>"
    And check next test step continues or not
    And further some steps

    Examples:
      | type | result |
      | EX1  | fail   |
      | EX2  | soft   |

  Scenario: Scenario A3
    When I perform a failing operation
    Then the system should show a warning
    And check next test step continues or not
    And further some steps