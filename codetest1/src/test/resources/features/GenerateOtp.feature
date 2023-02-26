Feature: Generate Otp Test POST

  Background:
    * url 'http://localhost:8897/key_generator/api/v1'
    * header Accept = 'application/json'
    * def generateOtpRequest = read('request/GenerateOtpRequest.json')
    * def expectedOutput = read('response/GenerateOtpResponse.json')

  Scenario: Generate OTP with the given
  Given  path '/generate_otp'
  And request generateOtpRequest
  When method POST
  Then status 200
  And print response
  And match response.status == "OK"
  And match response == expectedOutput
