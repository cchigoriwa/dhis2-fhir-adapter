package org.dhis2.fhir.adapter;

/*
 * Copyright (c) 2004-2018, University of Oslo
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

import com.github.tomakehurst.wiremock.WireMockServer;
import org.dhis2.fhir.adapter.fhir.metadata.model.FhirResourceType;
import org.dhis2.fhir.adapter.fhir.metadata.model.SubscriptionType;
import org.dhis2.fhir.adapter.fhir.security.SystemAuthenticationToken;
import org.dhis2.fhir.adapter.lock.LockManager;
import org.dhis2.fhir.adapter.lock.impl.EmbeddedLockManagerImpl;
import org.dhis2.fhir.adapter.setup.Setup;
import org.dhis2.fhir.adapter.setup.SetupResult;
import org.dhis2.fhir.adapter.setup.SetupService;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Configuration that must be executed before any test is run. It setups the
 * database with a basic system setup.
 *
 * @author volsch
 */
@Configuration
@AutoConfigureAfter( HibernateJpaAutoConfiguration.class )
public class TestConfiguration
{
    private final Logger logger = LoggerFactory.getLogger( getClass() );

    public static final String ADAPTER_AUTHORIZATION = "Bearer 836ef9274abc828728746";

    public static final String DHIS2_USERNAME = "fhir_user";

    public static final String DHIS2_PASSWORD = "fhir_user_123";

    public static final String FHIR_SERVICE_HEADER_NAME = "Authorization";

    public static final String FHIR_SERVICE_HEADER_VALUE = "Basic Zmhpcjp0ZXN0";

    public static final String BASE_DSTU3_CONTEXT = "/baseDstu3";

    @Value( "${dhis2.fhir-adapter.endpoint.system-authentication.username}" )
    private String dhis2SystemAuthenticationUsername;

    @Value( "${dhis2.fhir-adapter.endpoint.system-authentication.password}" )
    private String dhis2SystemAuthenticationPassword;

    @Autowired
    private SetupService setupService;

    private WireMockServer fhirMockServer;

    private UUID remoteSubscriptionId;

    private Map<FhirResourceType, UUID> remoteSubscriptionResourceIds;

    @Nonnull
    public UUID getRemoteSubscriptionId()
    {
        return remoteSubscriptionId;
    }

    @Nonnull
    public UUID getRemoteSubscriptionResourceId( @Nonnull FhirResourceType resourceType )
    {
        final UUID resourceId = remoteSubscriptionResourceIds.get( resourceType );
        Assert.assertNotNull( "Remote subscription resource for " + resourceType + " could not be found.", resourceId );
        return resourceId;
    }

    @Nonnull
    public String getDhis2SystemAuthorization()
    {
        return "Basic " + Base64.getEncoder().encodeToString(
            (dhis2SystemAuthenticationUsername + ":" + dhis2SystemAuthenticationPassword).getBytes( StandardCharsets.UTF_8 ) );
    }

    @Nonnull
    public String getDhis2UserAuthorization()
    {
        return "Basic " + Base64.getEncoder().encodeToString(
            (DHIS2_USERNAME + ":" + DHIS2_PASSWORD).getBytes( StandardCharsets.UTF_8 ) );
    }

    @Nonnull
    @Bean
    protected WireMockServer fhirMockServer()
    {
        return fhirMockServer;
    }

    @Nonnull
    @Bean
    @Primary
    protected LockManager embeddedLockManager()
    {
        return new EmbeddedLockManagerImpl();
    }

    @PostConstruct
    protected void postConstruct()
    {
        fhirMockServer = new WireMockServer( wireMockConfig().dynamicPort() );
        fhirMockServer.start();
        logger.info( "Started WireMock server for FHIR requests on port {}.", fhirMockServer.port() );

        final Setup setup = new Setup();

        setup.getRemoteSubscriptionSetup().getAdapterSetup().setBaseUrl( "http://localhost:8081" );
        setup.getRemoteSubscriptionSetup().getAdapterSetup().setAuthorizationHeaderValue( ADAPTER_AUTHORIZATION );

        setup.getRemoteSubscriptionSetup().getDhisSetup().setUsername( DHIS2_USERNAME );
        setup.getRemoteSubscriptionSetup().getDhisSetup().setPassword( DHIS2_PASSWORD );

        setup.getRemoteSubscriptionSetup().getFhirSetup().setBaseUrl( fhirMockServer.baseUrl() + BASE_DSTU3_CONTEXT );
        setup.getRemoteSubscriptionSetup().getFhirSetup().setHeaderName( FHIR_SERVICE_HEADER_NAME );
        setup.getRemoteSubscriptionSetup().getFhirSetup().setHeaderValue( FHIR_SERVICE_HEADER_VALUE );
        setup.getRemoteSubscriptionSetup().getFhirSetup().setSubscriptionType( SubscriptionType.REST_HOOK );
        setup.getRemoteSubscriptionSetup().getFhirSetup().setSupportsRelatedPerson( true );

        setup.getRemoteSubscriptionSetup().getSystemUriSetup().setOrganizationSystemUri( "http://example.sl/organizations" );
        setup.getRemoteSubscriptionSetup().getSystemUriSetup().setOrganizationCodePrefix( "OU_" );
        setup.getRemoteSubscriptionSetup().getSystemUriSetup().setPatientSystemUri( "http://example.sl/patients" );
        setup.getRemoteSubscriptionSetup().getSystemUriSetup().setPatientCodePrefix( "PT_" );

        setup.getOrganizationCodeSetup().setFallback( true );
        setup.getOrganizationCodeSetup().setDefaultDhisCode( "OU_4567" );
        setup.getOrganizationCodeSetup().setMappings( "9876 OU_1234 \n  8765, OU_2345" );

        final SetupResult setupResult;
        SecurityContextHolder.getContext().setAuthentication( new SystemAuthenticationToken() );
        try
        {
            Assert.assertFalse( setupService.hasCompletedSetup() );
            setupResult = setupService.apply( setup );
            Assert.assertTrue( setupService.hasCompletedSetup() );
        }
        finally
        {
            SecurityContextHolder.clearContext();
        }

        this.remoteSubscriptionId = setupResult.getRemoteSubscriptionId();
        this.remoteSubscriptionResourceIds = setupResult.getRemoteSubscriptionResourceIds();
    }

    @PreDestroy
    protected void preDestroy()
    {
        if ( fhirMockServer != null )
        {
            fhirMockServer.stop();
        }
    }
}