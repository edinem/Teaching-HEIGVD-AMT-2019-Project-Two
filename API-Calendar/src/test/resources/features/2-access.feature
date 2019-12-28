Feature: access

  Scenario: It should be possible to get access roles
    When user request roles
    Then request 'IS SUCCESSFUL'

  Scenario: It should be possible to list access of a calendar
    Given An accessible calendar
    When user request a calendar permission
    Then request 'IS SUCCESSFUL'

  Scenario: It should be possible to add a permission to a calendar
    Given An accessible calendar
    And an user other than in the token
    When user add a permission
    Then request 'IS SUCCESSFUL'

  Scenario: It should not be possible to add a permission 
    Given an viewer role calendar
    And an user other than in the token
    When user add a permission
    Then request 'FAILS'
