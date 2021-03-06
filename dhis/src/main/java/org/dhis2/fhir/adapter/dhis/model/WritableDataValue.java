package org.dhis2.fhir.adapter.dhis.model;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Writable implementation of {@link DataValue} that can also be used for
 * JSON serialization and deserialization.
 *
 * @author volsch
 */
public class WritableDataValue implements DataValue, Serializable
{
    private static final long serialVersionUID = 1337075669483267688L;

    @JsonProperty( "dataElement" )
    private String dataElementId;

    private Object value;

    private boolean providedElsewhere;

    @JsonInclude( JsonInclude.Include.NON_NULL )
    private ZonedDateTime lastUpdated;

    @JsonInclude( JsonInclude.Include.NON_NULL )
    private String storedBy;

    @JsonIgnore
    private boolean modified;

    @JsonIgnore
    private boolean newResource;

    public WritableDataValue()
    {
        super();
    }

    public WritableDataValue( @Nonnull String dataElementId, boolean newResource )
    {
        this.dataElementId = dataElementId;
        this.newResource = this.modified = newResource;
    }

    public WritableDataValue( @Nonnull String dataElementId, Object value )
    {
        this.dataElementId = dataElementId;
        this.value = value;
    }

    public String getDataElementId()
    {
        return dataElementId;
    }

    public void setDataElementId( String dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue( Object value )
    {
        this.value = value;
    }

    public boolean isProvidedElsewhere()
    {
        return providedElsewhere;
    }

    public void setProvidedElsewhere( boolean providedElsewhere )
    {
        this.providedElsewhere = providedElsewhere;
    }

    @Override
    public ZonedDateTime getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated( ZonedDateTime lastUpdated )
    {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String getStoredBy()
    {
        return storedBy;
    }

    public void setStoredBy( String storedBy )
    {
        this.storedBy = storedBy;
    }

    public boolean isNewResource()
    {
        return newResource;
    }

    public void setNewResource( boolean newResource )
    {
        this.newResource = newResource;
    }

    @Override
    public boolean isModified()
    {
        return modified;
    }

    public void setModified()
    {
        this.modified = true;
    }

    public void resetModified()
    {
        this.modified = false;
    }
}
