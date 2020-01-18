Feature: General

  Scenario: It should not be possible to get all users without auth
    When non-authenticated user try to get user list
    Then the authenticated action 'FAILS'

  Scenario: It should be possible to get all users with auth
    When authenticated user try to get user list
    Then the authenticated action 'IS SUCCESSFUL'

  Scenario: It should not be possible to get a user account information
    Given the user id to get
    | userId |
    | edin.mujkanovic@heig-vd.ch |
    When authenticated user try to get user information
    Then the authenticated action 'IS SUCCESSFUL'

  Scenario: It should not be possible to get a user account information
    Given the user id to get
      | userId |
      | edin.mujkanovic@heig-vd.ch |
    When non-authenticated user try to get user information
    Then the authenticated action 'FAILS'

  Scenario: It should not be possible to update another account
    Given the user informations to update
    | email | firstName | lastName | password |
    | edin.mujkanovic@heig-vd.ch | Edin2 | Mujkanovic2 | #Welcome123 |
    When authenticated user try to update an account
    Then the authenticated action 'FAILS'

  Scenario: It should be possible to update our own account
    Given the user informations to update
      | email | firstName | lastName | password |
      | test@test.com | Edin2 | Mujkanovic2 | #Welcome123 |
    When authenticated user try to update an account
    Then the authenticated action 'FAILS'

  Scenario: It should not be possible to delete another account
    Given the user id to delete
    | userId |
    | edin.mujkanovic@heig-vd.ch |
    When authenticated user try to delete an account
    Then the authenticated action 'FAILS'

  Scenario: It should be possible to delete its own account
    Given the user id to delete
      | userId |
      | test@test.test |
    When authenticated user try to delete an account
    Then the authenticated action 'IS SUCCESSFUL'

    

