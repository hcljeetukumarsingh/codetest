Feature: Verify SecretKey Rest API for Positive Response
  Background:
    * url 'http://localhost:8897/key_generator/api/v1'
    * def expectedOutput = read('response/GenerateSecretKeyResponse.json')
    * def generateSecretKeyRequest = read('request/GenerateSecretKeyRequest.json')
  Scenario: Send SecretKey and verify key
    Given path '/generate_secret_key'
    And request generateSecretKeyRequest
    When method POST
    Then status 200
    And print response
    Then path "/verify_secret_key"
    Given request { identifierValue:#(response.result.identifierValue) , key :#(response.result.key) , generatedBy :#(response.result.generatedBy) }
    When method POST
    Then status 200
    And print response
    Then response.message = "Secret Key verified successfully"