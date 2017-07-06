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

package de.d3adspace.phaedra.parser;

import de.d3adspace.phaedra.exception.ArgumentException;
import de.d3adspace.phaedra.meta.FieldMeta;
import java.util.Arrays;
import java.util.List;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class ValueBasedParameterParser implements ParameterParser {
	
	@Override
	public void parseParameter(String[] args, int currentIndex, FieldMeta fieldMeta,
		Object providerInstance) {
		if (currentIndex == args.length - 1) {
			throw new ArgumentException(
				"Needing one more argument but i am at the end of the arguments.");
		}
		
		String nextArgument = args[currentIndex + 1];
		
		if (nextArgument.startsWith("-")) {
			throw new ArgumentException(
				"I was searching for another argument value but I found a new key.");
		}
		
		if (fieldMeta.getField().getType().isAssignableFrom(List.class)) {
			String[] listElements = args[currentIndex + 1].split(",");
			try {
				fieldMeta.getField().set(providerInstance, Arrays.asList(listElements));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return;
		}
		
		try {
			fieldMeta.getField().set(providerInstance, args[currentIndex + 1]);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
