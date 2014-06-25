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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;
import org.lorislab.maven.plugin.AbstractMavenPlugin;

/**
 * The deployment task.
 *
 * @author Andrej Petras
 */
@Mojo(name = "deploy",
        defaultPhase = LifecyclePhase.DEPLOY,
        requiresProject = true,
        threadSafe = true)
@Execute(goal = "deploy", phase = LifecyclePhase.INSTALL)
public class DeploymentMojo extends AbstractMavenPlugin {

    /**
     * The deployment directory.
     */
    private static final String DEPLOY_DIR = "deployments";

    /**
     * The JBOSS profile.
     */
    @Parameter(defaultValue = "standalone", property = "org.lorislab.maven.jboss.server.profile")
    private String profile = "standalone";

    /**
     * The path of the file to deploy.
     */
    @Parameter(defaultValue = "${project.build.directory}/${project.build.finalName}.${project.packaging}", required = true)
    protected File deployFile;

    /**
     * The JBOSS server directory.
     */
    @Parameter(property = "org.lorislab.maven.jboss.server.dir")
    private File jbossDir;

    /**
     * The exploded flag.
     */
    @Parameter(defaultValue = "false")
    private boolean exploded = false;

    /**
     * The deploy the file to the server.
     *
     * @throws MojoExecutionException
     * @throws MojoFailureException
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

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
        File deployDir = new File(profileDir, DEPLOY_DIR);

        if (!deployDir.exists()) {
            throw new MojoFailureException("The JBOSS deployment directory does not exists! Path: " + deployDir.getAbsolutePath());
        }

        if (exploded) {
            throw new MojoFailureException("Not supported functionality");
        } else {
            try {
                getLog().info("Deploy the file: " + deployFile.getAbsolutePath() + " to the server: " + deployDir.getAbsolutePath());                
                FileUtils.copyFileToDirectory(deployFile, deployDir);
                getLog().info("Deployed finished!");
            } catch (IOException ex) {
                throw new MojoExecutionException("Error to copy the deploy final " + deployFile.getAbsolutePath(), ex);
            }
        }
    }

}
