package com.rapipay.keygeneratorservice.service

import com.rapipay.keygeneratorservice.GenerateOtpApplicationTests
import com.rapipay.keygeneratorservice.dto.GenerateKeyDto
import com.rapipay.keygeneratorservice.repository.GenerateKeyRepository
import com.rapipay.keygeneratorservice.service.impl.GenerateKeyServiceImpl
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Spy
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GenerateOtpServiceTests(

    @Mock
    private val generateKeyRepository: GenerateKeyRepository,

    @Spy
    @InjectMocks
    private val generateKeyService : GenerateKeyService,

    @Spy
    @InjectMocks
    private val generateKeyServiceImpl: GenerateKeyServiceImpl
) : GenerateOtpApplicationTests() {

   @Test
   fun generateOtp(){
       val testDTO  = GenerateKeyDto(
           identifierValue = "89078900",
           identifierName = "PhoneNo",
           generatedBy = "Auth Service"
       )
       val testEntity = generateKeyRepository.findTop1ByIdentifierValueAndGeneratedByOrderByCreatedAtDesc(testDTO.identifierValue,testDTO.generatedBy).toFuture().get()
       if(testEntity == null){
            generateKeyServiceImpl.generateOtpKey(testDTO)
       }

   }


}