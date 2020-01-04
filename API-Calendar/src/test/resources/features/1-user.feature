Feature: User

  Scenario: It should be possible to get users
    Given 'VALID' JWT Token
    When user request users
    Then request 'IS SUCCESSFUL'
    
  Scenario: It should not be possible to get users
    Given 'INVALID' JWT Token
    When user request users
    Then request 'FAILS'

