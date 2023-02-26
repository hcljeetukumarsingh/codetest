Feature: Verify SecretKey Test POST

  Background:
    * url 'http://localhost:8897/key_generator/api/v1'
    * header Accept = 'application/json'
    * def verifySecretKeyRequest = read('request/VerifySecretKeyRequest.json')
    * def expectedOutput = read('response/VerifySecretKeyResponse.json')

  Scenario: Verify Secret Key with the given
    Given  path '/verify_secret_key'
    And request verifyOtpRequest
    When method POST
    Then status 400
    And print response
    And match response.message == "BAD_REQUEST"
    And match response == expectedOutput