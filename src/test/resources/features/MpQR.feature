Feature: MpQR feature
  Scenario: Get the temporary qr code image link
    When the client calls mp-qr
    Then the client receives status code of 200
    And the client receives the json

  Scenario: Get the Access Token
    When the mp-service calls weixin's access token endpoint
    Then the mp-service gets the access token
