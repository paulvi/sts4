/*******************************************************************************
 * Copyright (c) 2016, 2017 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.boot.java;

import java.io.IOException;

import org.springframework.ide.vscode.boot.BootLanguageServer;
import org.springframework.ide.vscode.boot.BootLanguageServerParams;
import org.springframework.ide.vscode.commons.languageserver.LaunguageServerApp;
import org.springframework.ide.vscode.commons.languageserver.composable.ComposableLanguageServer;
import org.springframework.ide.vscode.commons.util.LogRedirect;

/**
 * Starts up Language Server process
 *
 * @author Martin Lippert
 */
public class Main {

	public static void main(String[] args) throws IOException, InterruptedException {
		String serverName = "boot-java-language-server";
		if (!System.getProperty(LaunguageServerApp.STANDALONE_STARTUP, "false").equals("true")) {
			LogRedirect.redirectToFile(serverName);
		}
		LaunguageServerApp.start(serverName, () -> {
			ComposableLanguageServer<BootJavaLanguageServerComponents> s = BootLanguageServer.createJava(
					BootLanguageServerParams.createDefault()
			);
			return s.getServer();
		});
	}

}
