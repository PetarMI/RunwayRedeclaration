Feature: Operator Imports Information via XML

  Scenario: When the program is opened it should read the cached obstacles data from the obstacles.xml file

    Given A list of predefined obstacles exists
    When Operator opens the program
    Then The program should read the data from the XML file

  Scenario: When the program is opened it should read the cached list of airports' runways from the subfolder
  containing xml files with airports

    Given There are some airport xml files cached
    When Operator opens the program
    Then The program should read the runway information for all the airports


   Scenario: The program must be able to remember obstacles when the operator adds new ones to the predefined list

     Given The ground crew report obstacles: block with length 2, width 4, height 7 with irregular shape
     And car with length 5 width 5 height 3 used for maintenance
     When The operator adds them to the predefined list of obstacles
     Then The program should save them correctly

