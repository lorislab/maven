/*
 * Copyright 2014 Andrej Petras.
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
package org.lorislab.maven.jboss.server;

import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

/**
 * The synchronise web task.
 *
 * @author Andrej Petras
 */
@Mojo(name = "sync",
        defaultPhase = LifecyclePhase.DEPLOY,
        requiresProject = true,
        threadSafe = true)
public class SynchronizeWebModuleMojo extends AbstractServerMavenPlugin {

    /**
     * The EAR extension.
     */
    private static final String EAR_EXT = ".ear";
        
    /**
     * The EAR file name.
     */
    @Parameter(defaultValue = "")
    private String ear;

    /**
     * The synchronise the web directory.
     *
     * @throws MojoExecutionException if the method fails.
     * @throws MojoFailureException if the method fails.
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        File targetDir = getTargetDir();
        if (ear != null) {
            File earTargetDir = new File(targetDir, ear + EAR_EXT );
            targetDir = new File(earTargetDir, targetDirName);
        } 
        File webTargetDir = new File(targetDir, targetDirName);
        getLog().info("Synchronise the directory: " + deployDir + " to the server: " + webTargetDir.getAbsolutePath());    
        
        try {
            FileUtils.copyDirectoryStructureIfModified(deployDir, webTargetDir);
            getLog().info("Synhronise finished!");
        } catch (IOException ex) {
            throw new MojoExecutionException("Error to synchronise the web project. " + deployFile.getAbsolutePath(), ex);
        }
    }

}
