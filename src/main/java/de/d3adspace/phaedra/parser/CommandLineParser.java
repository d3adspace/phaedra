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

import de.d3adspace.phaedra.SimplePhaedra;
import de.d3adspace.phaedra.annotation.Option;
import de.d3adspace.phaedra.exception.ArgumentException;
import de.d3adspace.phaedra.meta.FieldMeta;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Felix 'SasukeKawaii' Klauke
 */
public class CommandLineParser {
	
	private final SimplePhaedra phaedra;
	
	public CommandLineParser(SimplePhaedra phaedra) {
		this.phaedra = phaedra;
	}
	
	public Object parse(String[] args) {
		Class<?> provider = this.phaedra.getOptionProvider();
		try {
			Object object = provider.newInstance();
			
			Map<String, FieldMeta> optionFields = Arrays.stream(provider.getDeclaredFields())
				.filter(field -> field.isAnnotationPresent(Option.class))
				.collect(Collectors.toMap(p -> "-" + p.getAnnotation(Option.class).key(),
					p -> new FieldMeta(p.getAnnotation(Option.class).key(),
						p.getAnnotation(Option.class), p)));
			
			for (int i = 0; i < args.length; i++) {
				String token = args[i];
				
				if (!token.startsWith("-")) {
					continue;
				}
				
				FieldMeta fieldMeta = optionFields.get(args[i]);
				
				if (fieldMeta == null) {
					continue;
				}
				
				fieldMeta.getField().setAccessible(true);
				
				if (fieldMeta.getOption().needsValue()) {
					if (i == args.length - 1) {
						throw new ArgumentException(
							"Needing one more argument but i am at the end of the arguments.");
					}
					
					String nextArgument = args[i + 1];
					
					if (nextArgument.startsWith("-")) {
						throw new ArgumentException(
							"I was searching for another argument value but I found a new key.");
					}
					
					if (fieldMeta.getField().getType().isAssignableFrom(List.class)) {
						String[] listElements = args[i + 1].split(",");
						fieldMeta.getField().set(object, Arrays.asList(listElements));
						continue;
					}
					
					fieldMeta.getField().set(object, args[i + 1]);
				} else {
					if (fieldMeta.getField().getType().isAssignableFrom(boolean.class)) {
						fieldMeta.getField().set(object, Boolean.TRUE);
					}
				}
			}
			
			return object;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
