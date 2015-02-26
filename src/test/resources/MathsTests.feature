Feature: Testing calculations done by the maths module

  #Scenario 1
  Scenario: Scenario 1 12m tall obstacle, on the centreline, 50m before the 09L threshold, i.e. to the west of the
  threshold. The same obstacle is 3646m from the 27R threshold.

    #    VALUES: TORA TODA ASDA LDA Orientation Position Threshold
    Given The operator has the values for the runway 09L/27R
    And The values1 for 09L are 3902, 3902, 3902, 3595, 09, L and 306
    And The values2 for 27R are 3884, 3962, 3884, 3884, 27, R and 0
    When He adds an obstacle -50 m from the left and 3646 m from the right, of height 12 and 0 above the centreline
    And with blast allowance 300
    Then The recalculated values for 09L should be 3346, 3346, 3346, 2985
    And For 27R should be 2986,2986, 2986, 3346


  Scenario: Scenario 2 25m tall obstacle, 20m below of the centerline, 500m from the 27L threshold and 2853m from
  09R threshold.

    Given The operator has the values for the runway 09R/27L
    And The values1 for 09R are 3660, 3660, 3660, 3393, 09, R and 307
    And The values2 for 27L are 3660, 3660, 3660, 3660, 27, L and 0
    When He adds an obstacle 2853 m from the left and 500 m from the right, of height 25 and 20 above the centreline
    And with blast allowance 300
    Then The recalculated values for 09R should be 1850, 1850, 1850, 2553
    And For 27L should be 2860,2860, 2860, 1850


  Scenario: Scenario 3 15m tall obstacle, 60m above of centreline, 150m from 09R threshold and 3203m from 27L
  threshold.

    Given The operator has the values for the runway 09R/27L
    And The values1 for 09R are 3660, 3660, 3660, 3353, 09, R and 307
    And The values2 for 27L are 3660, 3660, 3660, 3660, 27, L and 0
    When He adds an obstacle 150 m from the left and 3203 m from the right, of height 15 and 60 above the centreline
    And with blast allowance 300
    Then The recalculated values for 09R should be 2903, 2903, 2903, 2393
    And For 27L should be 2393,2393, 2393, 2903


  Scenario: 20m tall obstacle, 20m above of centreline, 50m from 27R threshold and 3546m
  from 09L threshold.

    Given The operator has the values for the runway 09L/27R
    And The values1 for 09L are 3902, 3902, 3902, 3595, 09, L and 306
    And The values2 for 27R are 3884, 3962, 3884, 3884, 27, R and 0
    When He adds an obstacle 3546 m from the left and 50 m from the right, of height 20 and 20 above the centreline
    And with blast allowance 300
    Then The recalculated values for 09L should be 2792, 2792, 2792, 3246
    And For 27R should be 3534,3612, 3534, 2774

  Scenario: 12m height 20m above centreline 106m from left, 1544 from right

    Given The operator has the values for the runway 02/20
    And The values1 for 02 are 1723, 1831, 1723, 1650, 02, _ and 73
    And The values2 for 20 are 1650, 1805, 1650, 1605, 20, _ and 45
    When He adds an obstacle 106 m from the left and 1544 m from the right, of height 12 and 20 above the centreline
    And with blast allowance 300
    Then The recalculated values for 02 should be 1244, 1352, 1244, 884
    And For 20 should be 929,929, 929, 1244
