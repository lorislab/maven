/*
 * Copyright 2012 Andrej Petras <andrej@ajka-andrej.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lorislab.maven.messagebundles;

import org.lorislab.maven.plugin.utils.FileDirectoryUtils;
import org.lorislab.maven.plugin.utils.ProjectUtils;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.codehaus.plexus.util.DirectoryScanner;

/**
 * The message bundle to language plug-in.
 * 
 * @author Andrej Petras <andrej@ajka-andrej.com>
 */
@Mojo(name = "language",
        defaultPhase = LifecyclePhase.PROCESS_RESOURCES,
        requiresProject = true,
        threadSafe = true)
@Execute(goal = "language", phase = LifecyclePhase.PROCESS_RESOURCES)
public class MessageBundlesToLanguage extends MessageBundlesPlugin {

    /**
     * Plug-in main method.
     *
     * @throws org.apache.maven.plugin.MojoExecutionException
     * @throws org.apache.maven.plugin.MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (!ProjectUtils.isPackaging(packaging, packagings)) {
            getLog().info("No packaging for copy MessageBundles.");
            return;
        }

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(sourceDirectory);
        scanner.setIncludes(createIncludesFilesWithoutLanguage());
        scanner.addDefaultExcludes();

        try {
            scanner.scan();
        } catch (IllegalStateException ex) {
            getLog().error("Error by scanning the directory: " + sourceDirectory, ex);
        }

        String[] paths = scanner.getIncludedFiles();
        if (paths == null) {
            getLog().info("No MessageBundle files to copy.");
            return;
        }

        for (String item : paths) {
            if (checkLanguage(item)) {
                File file = getFileFromDirectory(sourceDirectory, item);
                File result = getFileFromDirectory(outputDirectory, addLanguage(item));
                try {
                    if (FileDirectoryUtils.copyFileIfChanged(file, result)) {
                        getLog().info("File: " + result.toString());
                    } else {
                        getLog().info("No changies found in the file: " + file);
                    }
                } catch (IOException ex) {
                    throw new MojoExecutionException("Error by copy the file.", ex);
                }
            }
        }
    }

    /**
     * Check the langauge in the file name.
     *
     * @param fileName the name of the file.
     * @return returns <code>true</code> for files without the language
     * else <code>false</code>
     */
    private boolean checkLanguage(String fileName) {
        int index = fileName.lastIndexOf("_");
        if (index != -1) {
            int index2 = fileName.lastIndexOf(".");
            if (index2 != -1) {
                String lang = fileName.substring(index + 1, index2);
                if (lang.length() == 2) {
                    Locale loc = new Locale(lang);
                    if (!lang.equals(loc.getDisplayLanguage())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
