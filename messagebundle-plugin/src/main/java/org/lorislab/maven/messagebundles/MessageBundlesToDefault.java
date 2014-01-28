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


import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.lorislab.maven.plugin.utils.FileDirectoryUtils;
import org.lorislab.maven.plugin.utils.ProjectUtils;

/**
 * The message bundles to default plug-in.
 * @goal default
 * @author Andrej Petras <andrej@ajka-andrej.com>
 */
public class MessageBundlesToDefault extends MessageBundlesPlugin {

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
        scanner.setIncludes(createIncludesFilesWithLanguage());
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
            File file = getFileFromDirectory(sourceDirectory, item);
            File result = getFileFromDirectory(outputDirectory, removeLanguage(item));
            try {
                if (FileDirectoryUtils.copyFileIfChanged(file, result)) {
                    getLog().info("File: " + result.toString());
                } else {
                    getLog().warn("Unabled to copy file: " + file + " to file: " + result);
                }
            } catch (IOException ex) {
                throw new MojoExecutionException("Error by copy the file.", ex);
            }
        }
    }
}
