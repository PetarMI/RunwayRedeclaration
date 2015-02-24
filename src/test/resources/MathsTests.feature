Feature: Testing calculations done by the maths module

  Scenario Outline: Using the sample values for input, are the calculations giving the
  expected result?

    Given The operator has the values for the runway 09L/27R
    And The values1 for <Strip1> are <TORA1>, <TODA1>, <ASDA1>, <LDA1>, <Orientation1> and <Position1>
    And The values2 for <Strip2> are <TORA2>, <TODA2>, <ASDA2>, <LDA2>, <Orientation2> and <Position2>
    When He adds an obstacle 2500 m from the threshold of height 25 m
    Then The recalculated values for <Strip1> should be <recTORA1>, <recTODA1>, <recASDA1>, <recLDA1>



  Examples: First Strip Original Values
    | Strip1  | TORA1 | TODA1 | ASDA1 | LDA1 | Orientation1 | Position1 |
    | 09L     | 3902  | 3902  | 3902  | 3595 | 09           | L         |
#    | 09R    |3660   | 3660  | 3660  |3353  | 09           | R         |


  Examples: Second Strip Original Values
    |Strip2 | TORA2 | TODA2 | ASDA2 | LDA2 | Orientation2 | Position2 |
    | 27R   |3884   |3962   |3884   |3884  | 27           | R         |
#    | 27L   |3660   | 3660  | 3660  |3660  | 27           | L         |

  Examples: First Strip Original Values
    |Strip1  | TORA1 | TODA1 | ASDA1 | LDA1 | Orientation1 | Position1 |
    | 09L    |3902   | 3902  | 3902  |3595  | 09           | L         |
#    | 09R    |3660   | 3660  | 3660  |3353  | 09           | R         |

    Examples: Recalculated Values for Strip1
    | recTORA1 | recTODA1 | recASDA1 | recLDA1 |
    | 1496     | 1496     | 1496     | 1496    |





