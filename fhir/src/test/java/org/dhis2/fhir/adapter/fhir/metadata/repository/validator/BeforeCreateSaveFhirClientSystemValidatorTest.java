package org.dhis2.fhir.adapter.fhir.metadata.repository.validator;

/*
 * Copyright (c) 2004-2019, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.apache.commons.lang3.StringUtils;
import org.dhis2.fhir.adapter.fhir.AbstractJpaRepositoryTest;
import org.dhis2.fhir.adapter.fhir.metadata.model.FhirClient;
import org.dhis2.fhir.adapter.fhir.metadata.model.FhirClientSystem;
import org.dhis2.fhir.adapter.fhir.metadata.model.FhirResourceType;
import org.dhis2.fhir.adapter.fhir.metadata.model.System;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Validator tests for the corresponding repository.
 *
 * @author volsch
 */
public class BeforeCreateSaveFhirClientSystemValidatorTest extends AbstractJpaRepositoryTest
{
    public static final String RESOURCE_PATH = "/api/fhirClientSystems";

    public static final String AUTHORIZATION_HEADER_VALUE = CODE_MAPPING_AUTHORIZATION_HEADER_VALUE;

    private System system;

    private FhirClient fhirClient;

    private FhirClientSystem entity;

    @Before
    public void before()
    {
        system = entityManager.createQuery( "SELECT e FROM System e WHERE e.code=:code", System.class )
            .setParameter( "code", "SYSTEM_SL_LOCATION" ).getSingleResult();
        fhirClient = entityManager.createQuery( "SELECT e FROM FhirClient e WHERE e.code=:code", FhirClient.class )
            .setParameter( "code", "DEFAULT_SUBSCRIPTION" ).getSingleResult();

        entity = new FhirClientSystem();
        entity.setFhirResourceType( FhirResourceType.DIAGNOSTIC_REPORT );
        entity.setCodePrefix( createUnique() );
    }

    @Test
    public void testFhirClientNull() throws Exception
    {
        mockMvc.perform( post( RESOURCE_PATH ).header( AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE )
            .contentType( MediaType.APPLICATION_JSON ).content( replaceJsonEntityReferences( entity,
                JsonEntityValue.create( "system", "systems", system.getId().toString() ) ) ) )
            .andExpect( status().isBadRequest() ).andExpect( jsonPath( "errors[0].property", Matchers.is( "fhirClient" ) ) );
    }

    @Test
    public void testReservedId() throws Exception
    {
        mockMvc.perform( patch( RESOURCE_PATH + "/47fd1b28-7558-429b-85ab-c5f5fcc3d00f" )
            .header( AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE )
            .contentType( MediaType.parseMediaType( "application/merge-patch+json" ) ).content( "{}" ) )
            .andExpect( status().isBadRequest() ).andExpect( jsonPath( "errors[0].property", Matchers.is( "fhirResourceType" ) ) );
    }

    @Test
    public void testSystemNull() throws Exception
    {
        mockMvc.perform( post( RESOURCE_PATH ).header( AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE )
            .contentType( MediaType.APPLICATION_JSON ).content( replaceJsonEntityReferences( entity,
                JsonEntityValue.create( "fhirClient", "fhirClients", fhirClient.getId().toString() ) ) ) )
            .andExpect( status().isBadRequest() ).andExpect( jsonPath( "errors[0].property", Matchers.is( "system" ) ) );
    }

    @Test
    public void testResourceTypeNull() throws Exception
    {
        entity.setFhirResourceType( null );
        mockMvc.perform( post( RESOURCE_PATH ).header( AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE )
            .contentType( MediaType.APPLICATION_JSON ).content( replaceJsonEntityReferences( entity,
                JsonEntityValue.create( "fhirClient", "fhirClients", fhirClient.getId().toString() ),
                JsonEntityValue.create( "system", "systems", system.getId().toString() ) ) ) )
            .andExpect( status().isBadRequest() ).andExpect( jsonPath( "errors[0].property", Matchers.is( "fhirResourceType" ) ) );
    }

    @Test
    public void testCodePrefixLength() throws Exception
    {
        entity.setCodePrefix( StringUtils.repeat( 'a', FhirClientSystem.MAX_CODE_PREFIX_LENGTH + 1 ) );
        mockMvc.perform( post( RESOURCE_PATH ).header( AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE )
            .contentType( MediaType.APPLICATION_JSON ).content( replaceJsonEntityReferences( entity,
                JsonEntityValue.create( "fhirClient", "fhirClients", fhirClient.getId().toString() ),
                JsonEntityValue.create( "system", "systems", system.getId().toString() ) ) ) )
            .andExpect( status().isBadRequest() ).andExpect( jsonPath( "errors[0].property", Matchers.is( "codePrefix" ) ) );
    }

    @Test
    public void testDefaultValueLength() throws Exception
    {
        entity.setDefaultValue( StringUtils.repeat( 'a', FhirClientSystem.MAX_DEFAULT_VALUE_LENGTH + 1 ) );
        mockMvc.perform( post( RESOURCE_PATH ).header( AUTHORIZATION_HEADER_NAME, AUTHORIZATION_HEADER_VALUE )
            .contentType( MediaType.APPLICATION_JSON ).content( replaceJsonEntityReferences( entity,
                JsonEntityValue.create( "fhirClient", "fhirClients", fhirClient.getId().toString() ),
                JsonEntityValue.create( "system", "systems", system.getId().toString() ) ) ) )
            .andExpect( status().isBadRequest() ).andExpect( jsonPath( "errors[0].property", Matchers.is( "defaultValue" ) ) );
    }
}