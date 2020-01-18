Feature: Register

  Scenario: It should be possible to register
    Given user send his informations to register
      | email       | firstName| lastName | password |
      | test@test.test | test | test | test |

    When user register 'WITH ALL REQUIRED FIELDS'
    Then the registration 'IS SUCCESSFUL'

  Scenario: It should not be possible to register with a mail already used
    Given: user send his informations to register
      | email       | firstName| lastName | password |
      | edin.mujkanovic@heig-vd.ch | test | test | test |

    When user register 'WITH ALL REQUIRED FIELDS'
    Then the registration 'FAILS'

  Scenario: It should not be possible to register with a missing value
    Given: user send no informations to register

    When user register 'WITH ALL REQUIRED FIELDS'
    Then the registration 'FAILS'



