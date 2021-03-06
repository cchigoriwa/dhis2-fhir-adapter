package org.dhis2.fhir.adapter.converter;

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

import org.springframework.core.convert.converter.Converter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Implementation of {@linkplain Converter converter} interface that handles <code>null</code> values
 * automatically. It is ensured that the conversion method gets never a <code>null</code> argument value.
 *
 * @param <A> the concrete class from which the value should be converted.
 * @param <B> the concrete class to which the value should be converted.
 * @author volsch
 */
public abstract class TypedConverter<A, B> implements Converter<A, B>
{
    private final Class<A> fromClass;

    private final Class<B> toClass;

    public TypedConverter( @Nonnull Class<A> fromClass, @Nonnull Class<B> toClass )
    {
        this.fromClass = fromClass;
        this.toClass = toClass;
    }

    public Class<A> getFromClass()
    {
        return this.fromClass;
    }

    public Class<B> getToClass()
    {
        return this.toClass;
    }

    @Nullable
    public abstract B doConvert( @Nonnull A source ) throws ConversionException;

    @Nullable
    public B doConvertNull() throws ConversionException
    {
        return null;
    }

    /**
     * Converts the specified object value, which is casted to the required from class type before doing the conversion.
     *
     * @param source the value that should be converted.
     * @return the converted value.
     * @throws ConversionException thrown if the conversion could not be performed.
     */
    @Nullable
    public final B convertCasted( @Nullable Object source ) throws ConversionException
    {
        return convert( fromClass.cast( source ) );
    }

    /**
     * Converts the specified value to the target type. The specified value will never be <code>null</code>.
     *
     * @param source the value that should be converted.
     * @return the converted value.
     * @throws ConversionException thrown if the conversion could not be performed.
     */
    @Nullable
    public final B convert( @Nullable A source ) throws ConversionException
    {
        return (source == null) ? doConvertNull() : doConvert( source );
    }
}
