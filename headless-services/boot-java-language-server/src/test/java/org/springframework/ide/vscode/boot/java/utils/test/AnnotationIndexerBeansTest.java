/*******************************************************************************
 * Copyright (c) 2017 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.boot.java.utils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.lsp4j.SymbolInformation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ide.vscode.boot.java.beans.BeansSymbolProvider;
import org.springframework.ide.vscode.boot.java.beans.ComponentSymbolProvider;
import org.springframework.ide.vscode.boot.java.beans.Constants;
import org.springframework.ide.vscode.boot.java.handlers.SymbolProvider;
import org.springframework.ide.vscode.boot.java.utils.AnnotationIndexer;
import org.springframework.ide.vscode.commons.languageserver.java.DefaultJavaProjectFinder;
import org.springframework.ide.vscode.commons.languageserver.java.IJavaProjectFinderStrategy;
import org.springframework.ide.vscode.commons.languageserver.java.JavaProjectFinder;
import org.springframework.ide.vscode.commons.maven.MavenCore;
import org.springframework.ide.vscode.commons.maven.MavenProjectFinderStrategy;
import org.springframework.ide.vscode.project.harness.ProjectsHarness;

/**
 * @author Martin Lippert
 */
public class AnnotationIndexerBeansTest {

	private Map<String, SymbolProvider> symbolProviders;
	private JavaProjectFinder projectFinder;

	@Before
	public void setup() throws Exception {
		symbolProviders = new HashMap<>();
		symbolProviders.put(Constants.SPRING_BEAN, new BeansSymbolProvider());
		symbolProviders.put(Constants.SPRING_COMPONENT, new ComponentSymbolProvider());

		projectFinder = new DefaultJavaProjectFinder(new IJavaProjectFinderStrategy[] {new MavenProjectFinderStrategy(MavenCore.getDefault())});
	}

	@Test
	public void testScanSimpleConfigurationClass() throws Exception {
		AnnotationIndexer indexer = new AnnotationIndexer(projectFinder, symbolProviders);
		File directory = new File(ProjectsHarness.class.getResource("/test-projects/test-annotation-indexing-beans/").toURI());
		indexer.scanFiles(directory);

		String uriPrefix = "file://" + directory.getAbsolutePath();
		List<? extends SymbolInformation> symbols = indexer.getSymbols(uriPrefix + "/src/main/java/org/test/SimpleConfiguration.java");
		assertEquals(2, symbols.size());
		assertTrue(containsSymbol(symbols, "@Bean simpleBean org.test.BeanClass", uriPrefix + "/src/main/java/org/test/SimpleConfiguration.java", 8, 1, 8, 8));
	}

	@Test
	public void testScanSimpleComponentClass() throws Exception {
		AnnotationIndexer indexer = new AnnotationIndexer(projectFinder, symbolProviders);
		File directory = new File(ProjectsHarness.class.getResource("/test-projects/test-annotation-indexing-beans/").toURI());
		indexer.scanFiles(directory);

		String uriPrefix = "file://" + directory.getAbsolutePath();
		List<? extends SymbolInformation> symbols = indexer.getSymbols(uriPrefix + "/src/main/java/org/test/SimpleComponent.java");
		assertEquals(1, symbols.size());
		assertTrue(containsSymbol(symbols, "@Bean simpleComponent org.test.SimpleComponent", uriPrefix + "/src/main/java/org/test/SimpleComponent.java", 4, 0, 4, 10));
	}

	private boolean containsSymbol(List<? extends SymbolInformation> symbols, String name, String uri, int startLine, int startCHaracter, int endLine, int endCharacter) {
		for (Iterator<? extends SymbolInformation> iterator = symbols.iterator(); iterator.hasNext();) {
			SymbolInformation symbol = iterator.next();

			if (symbol.getName().equals(name)
					&& symbol.getLocation().getUri().equals(uri)
					&& symbol.getLocation().getRange().getStart().getLine() == startLine
					&& symbol.getLocation().getRange().getStart().getCharacter() == startCHaracter
					&& symbol.getLocation().getRange().getEnd().getLine() == endLine
					&& symbol.getLocation().getRange().getEnd().getCharacter() == endCharacter) {
				return true;
			}
 		}

		return false;
	}

}