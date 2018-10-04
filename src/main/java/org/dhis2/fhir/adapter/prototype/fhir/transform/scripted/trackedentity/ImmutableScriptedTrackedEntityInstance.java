package org.dhis2.fhir.adapter.prototype.fhir.transform.scripted.trackedentity;

/*
 *  Copyright (c) 2004-2018, University of Oslo
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  Neither the name of the HISP project nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.dhis2.fhir.adapter.prototype.fhir.transform.TransformException;
import org.dhis2.fhir.adapter.prototype.geo.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ImmutableScriptedTrackedEntityInstance implements ScriptedTrackedEntityInstance
{
    private final ScriptedTrackedEntityInstance delegate;

    public ImmutableScriptedTrackedEntityInstance( @Nonnull ScriptedTrackedEntityInstance delegate )
    {
        this.delegate = delegate;
    }

    @Override
    public boolean isNewResource()
    {
        return delegate.isNewResource();
    }

    @Override
    @Nullable
    public String getId()
    {
        return delegate.getId();
    }

    @Override
    @Nonnull
    public String getTypeId()
    {
        return delegate.getTypeId();
    }

    @Nullable
    @Override
    public String getOrganizationUnitId()
    {
        return delegate.getOrganizationUnitId();
    }

    @Nullable
    @Override
    public Location getCoordinates()
    {
        return delegate.getCoordinates();
    }

    @Override
    @Nullable
    public Object getValueByName( @Nonnull String typeAttrName )
    {
        return delegate.getValueByName( typeAttrName );
    }

    @Override
    public void validate() throws TransformException
    {
        delegate.validate();
    }
}