Feature: Login

  Scenario: It should be possible to get an access token JWT with valid credentials
    Given user send his informations
      | email       | firstName| lastName | password |
      | edin.mujkanovic@heig-vd.ch | Edin | Mujkanovic |#Welcome123 |

    When user login 'WITH ALL REQUIRED FIELDS'
    Then the login 'IS SUCCESSFUL'

  Scenario: It should not be possible to get an access token with invalid email
    Given user send his informations
      | email       | firstName| lastName | password |
      | edin.mujkanovic@heig-vd.ch2 | Edin | Mujkanovic |#Welcome123 |

    When user login 'WITH ALL REQUIRED FIELDS'
    Then the login 'FAILS'

  Scenario:  It should not be possible to get an access token with invalid password
    Given user send his informations
      | email       | firstName| lastName | password |
      | edin.mujkanovic@heig-vd.ch2 | Edin | Mujkanovic |#Welcome1234 |

    When user login 'WITH ALL REQUIRED FIELDS'
    Then the login 'FAILS'

  Scenario: It should not be possible to get an access token with invalid credentials
    Given user send his informations
      | email       | firstName| lastName | password |
      | edin.mujkanovic@heig-vd.ch | Edin | Mujkanovic |#Welcome1234 |

    When user login 'WITH ALL REQUIRED FIELDS'
    Then the login 'FAILS'

