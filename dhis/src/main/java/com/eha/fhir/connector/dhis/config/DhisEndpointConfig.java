package com.eha.fhir.connector.dhis.config;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import com.eha.fhir.connector.model.UsernamePassword;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties( "dhis2.fhir-adapter.endpoint" )
@Validated
public class DhisEndpointConfig implements Serializable
{
    private static final long serialVersionUID = 8393014237402428126L;

    @NotBlank
    private String url;

    @Pattern( regexp = "\\d+" )
    private String apiVersion;

    @NotNull
    @Valid
    private UsernamePassword systemAuthentication;

    public String getUrl()
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public String getApiVersion()
    {
        return apiVersion;
    }

    public void setApiVersion( String apiVersion )
    {
        this.apiVersion = apiVersion;
    }

    @Nonnull
    public UsernamePassword getSystemAuthentication()
    {
        return systemAuthentication;
    }

    public void setSystemAuthentication( @NotNull @Valid @Nonnull UsernamePassword systemAuthentication )
    {
        this.systemAuthentication = systemAuthentication;
    }
}
