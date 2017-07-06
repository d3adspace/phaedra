/*
 * Copyright (c) 2017 D3adspace
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.d3adspace.phaedra.extractor;

import de.d3adspace.phaedra.SimplePhaedra;
import de.d3adspace.phaedra.annotation.Option;
import de.d3adspace.phaedra.meta.FieldMeta;
import de.d3adspace.phaedra.meta.FieldMetaFactory;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SimpleFieldMetaExtractor implements FieldMetaExtractor {
	
	@Override
	public Map<String, FieldMeta> getFieldMeta(SimplePhaedra phaedra, Class<?> providerClazz) {
		Class<?> provider = phaedra.getOptionProvider();
		
		return Arrays.stream(provider.getDeclaredFields())
			.filter(field -> field.isAnnotationPresent(Option.class))
			.collect(Collectors.toMap(p -> "-" + p.getAnnotation(Option.class).key(),
				FieldMetaFactory::createFieldMeta));
	}
}
