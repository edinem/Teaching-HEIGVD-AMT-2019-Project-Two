Feature: Calendar

  Scenario: It should be possible to list all calendars
    When user request calendars
    Then request 'IS SUCCESSFUL'

  Scenario: It should be possible to create a calendar
    Given user send calendar name
    | name  |
    | test1234 |
    When user create a new calendar
    Then request 'IS SUCCESSFUL'

