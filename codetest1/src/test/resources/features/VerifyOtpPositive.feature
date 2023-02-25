
Feature: Verify OTP Rest API for Positive Response
  Background:
    * url 'http://localhost:8897/key_generator/api/v1'
    * def expectedOutput = read('response/GenerateOtpResponse.json')
    * def generateOtpRequest = read('request/GenerateOtpRequest.json')
  Scenario: Send OTP to mobile number
    Given path '/generate_otp'
    And request generateOtpRequest
    When method POST
    Then status 200
    And print response
    Then path "/verify_otp"
    Given request { identifierValue:#(response.result.identifierValue) , key :#(response.result.key) , generatedBy :#(response.result.generatedBy) }
    When method POST
    Then status 200
    And print response
    Then response.message = "OTP verified successfully"