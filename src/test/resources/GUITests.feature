Feature: Testing the GUI for input error

  Scenario: If the values inputted in the calculus frame are not integers program shouldn't crash

    Given The operator is on the calculus page
    When he inputs potato on all of the fields
    Then The application should not crash if Calculate button pressed

  Scenario: If the height and width inputted in the obstacle frame are not integers s program shouldn't crash

    Given The operator is on the new obstacle page
    When he inputs potato on height and width
    Then The application should not crash if add button pressed