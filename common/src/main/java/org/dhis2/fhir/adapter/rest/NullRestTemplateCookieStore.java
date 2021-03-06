package org.dhis2.fhir.adapter.rest;

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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A rest template cookie store that does not store any cookie.
 *
 * @author volsch
 */
public class NullRestTemplateCookieStore implements RestTemplateCookieStore
{
    @Nullable
    @Override
    public String getCookieName()
    {
        // not known
        return null;
    }

    @Override
    public void setCookieName( @Nonnull String cookieName )
    {
// nothing to be done
    }

    @Override
    public void add( @Nonnull String authorizationHeaderValue, @Nonnull String cookieValue )
    {
        // nothing to be done
    }

    @Nullable
    @Override
    public String get( @Nonnull String authorizationHeaderValue )
    {
        // no cookies have been stored
        return null;
    }

    @Override
    public boolean remove( @Nonnull String authorizationHeaderValue, @Nonnull String cookieValue )
    {
        // no cookies to remove
        return false;
    }
}
