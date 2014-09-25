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
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.lorislab.maven.plugin.AbstractMavenPlugin;

/**
 * The abstract server MAVEN plug-in.
 *
 * @author Andrej Petras
 */
public abstract class AbstractServerMavenPlugin extends AbstractMavenPlugin {

    /**
     * The deployment directory.
     */
    protected static final String DEPLOY_DIR = "deployments";

    /**
     * The JBOSS profile.
     */
    @Parameter(defaultValue = "standalone", property = "org.lorislab.maven.jboss.server.profile")
    protected String profile = "standalone";

    /**
     * The path of the file to deploy.
     */
    @Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}.${project.packaging}", required = true)
    protected File deployFile;

    /**
     * The directory to deploy.
     */
    @Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}", required = true)
    protected File deployDir;
        
    /**
     * The directory name in the deploy directory.
     */
    @Parameter(defaultValue = "${project.build.finalName}.${project.packaging}", required = true)
    protected String targetDirName;    
    
    /**
     * The JBOSS server directory.
     */
    @Parameter(property = "org.lorislab.maven.jboss.server.dir")
    protected File jbossDir;

    /**
     * The MAVEN project.
     */
    @Component
    protected MavenProject project;      

    /**
     * Gets the target directory.
     * 
     * @return the target directory.
     * 
     * @throws MojoExecutionException if the method fails.
     * @throws MojoFailureException if the method fails.
     */
    protected File getTargetDir() throws MojoExecutionException, MojoFailureException {

        if (deployFile == null || !deployFile.exists()) {
            throw new MojoFailureException("The build final name does not exists! Path: " + deployFile.getAbsolutePath());
        }

        if (jbossDir == null) {
            throw new MojoFailureException("The JBOSS directory is not defined! property: org.lorislab.maven.jboss.server.dir");
        }

        if (!jbossDir.exists()) {
            throw new MojoFailureException("The JBOSS directory does not exists! Path: " + jbossDir.getAbsolutePath());
        }

        File profileDir = new File(jbossDir, profile);
        File targetDir = new File(profileDir, DEPLOY_DIR);

        if (!targetDir.exists()) {
            throw new MojoFailureException("The JBOSS deployment directory does not exists! Path: " + targetDir.getAbsolutePath());
        }

        return targetDir;
    }
}
