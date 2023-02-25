Feature: Generate OTP Rest API

  Background:
    * url 'http://localhost:8897/key_generator/api/v1'
    * header Accept = 'application/json'
    * def generateSecretKeyRequest = read('request/GenerateSecretKeyRequest.json')
    * def expectedOutput = read('response/GenerateSecretKeyResponse.json')

  Scenario:
    Given path '/generate_secret_key'
    And request generateSecretKeyRequest
    When method POST
    Then status 200
    And print response
    And match response.status == "OK"
    And match response == expectedOutput