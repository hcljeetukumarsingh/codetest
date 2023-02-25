Feature: Verify Otp Test POST

  Background:
    * url 'http://localhost:8897/key_generator/api/v1'
    * header Accept = 'application/json'
    * def verifyOtpRequest = read('request/VerifyOtpRequest.json')
    * def expectedOutput = read('response/VerifyOtpResponse.json')

  Scenario: Verify OTP with the given
    Given  path '/verify_otp'
    And request verifyOtpRequest
    When method POST
    Then status 400
    And print response
    And match response.message == "BAD_REQUEST"
    And match response == expectedOutput