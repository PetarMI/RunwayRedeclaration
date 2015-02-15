Feature: Operator Importing Information

  Scenario: As an Operator I want to import the obstacle information I receive from the ground
  crew staff into the system using an XML file so that I can quickly provide the input and continue
  with my calculations for that case.

    Given Operator Tyrone wants to import information in the system
    When Ground crew staff sends information about obstacle: position = 3, height = 5, runway = 1
    Then System can import XML files containing input successfully