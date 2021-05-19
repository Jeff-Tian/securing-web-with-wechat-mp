Feature: MpQR feature
  Scenario: Get the temporary qr code image link
    When the client calls mp-qr
    Then the client receives status code of 200
    And the client receives the json