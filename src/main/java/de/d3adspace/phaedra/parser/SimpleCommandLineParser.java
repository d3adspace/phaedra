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
import de.d3adspace.phaedra.container.FieldMetaContainer;
import de.d3adspace.phaedra.container.FieldMetaContainerFactory;
import de.d3adspace.phaedra.meta.FieldMeta;

/**
 * Basic command line parser.
 *
 * @author Felix 'SasukeKawaii' Klauke
 */
public class SimpleCommandLineParser implements CommandLineParser {
	
	/**
	 * Underlying phaedra instance.
	 */
	private final SimplePhaedra phaedra;
	
	/**
	 * Create a new parser by its phaedra instance.
	 *
	 * @param phaedra The phaedra instance.
	 */
	SimpleCommandLineParser(SimplePhaedra phaedra) {
		this.phaedra = phaedra;
	}
	
	@Override
	public Object parse(String[] args) {
		try {
			FieldMetaContainer metaContainer = FieldMetaContainerFactory
				.createFieldMetaContainer(this.phaedra, this.phaedra.getOptionProvider());
			
			Object providerInstance = null;
			try {
				providerInstance = this.phaedra.getOptionProvider().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
			
			for (int i = 0; i < args.length; i++) {
				String token = args[i];
				
				if (!token.startsWith("-")) {
					continue;
				}
				
				FieldMeta fieldMeta = metaContainer.getMeta(args[i]);
				
				if (fieldMeta == null) {
					continue;
				}
				
				fieldMeta.getField().setAccessible(true);
				
				Option option = fieldMeta.getOption();
				
				ParameterParser parameterParser = ParameterParserProvider
					.getParameterParser(option.needsValue());
				parameterParser.parseParameter(args, i, fieldMeta, providerInstance);
			}
			
			return providerInstance;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
