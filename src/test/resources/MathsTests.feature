Feature: Testing calculations done by the maths module

  Scenario: Using the sample values for input, are the calculations giving the
  expected result?

    Given The operator has the values for the runway 09L/27R
#    VALUES: TORA TODA ASDA LDA Orientation Position Threshold
    And The values1 for 09L are 3902, 3902, 3902, 3595, 09, L and 306
    And The values2 for 27R are 3884, 3962, 3884, 3884, 27, R and 0
    When He adds an obstacle -50 m from the left and 3646 m from the left, of height 12 and 0 from the centreline
    And with blast allowance 300
    Then The recalculated values for 09L should be 3346, 3346, 3346, 2986