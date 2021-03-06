/*******************************************************************************
 * Copyright (c) 2017 Pivotal, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Pivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.vscode.commons.util;

import java.util.List;
import java.util.function.Consumer;

/**
 * Object able to add/remove {@link FileListener} objects and fire events via these listeners
 * 
 * @author Alex Boyko
 *
 */
public interface FileObserver {
	
	String onFileCreated(List<String> globPattern, Consumer<String> handler);
	
	String onFileChanged(List<String> globPattern, Consumer<String> handler);
	
	String onFileDeleted(List<String> globPattern, Consumer<String> handler);
	
	boolean unsubscribe(String subscriptionId);
	
}
