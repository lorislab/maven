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
import org.lorislab.maven.plugin.AbstractMavenPlugin;

/**
 * Abstract class for message bundle plug-in.
 * 
 * @author Andrej Petras <andrej@ajka-andrej.com>
 */
public abstract class MessageBundlesPlugin extends AbstractMavenPlugin {

    /**
     * Default message bundles language separator "_".
     */
    protected final static String BUNDLES_SEPARATORCHAR = "_";

    /**
     * Default includes directories
     */
    protected final static String DEFAULT_INCLUDES = "**/";

    /**
     * Default file extension separator ".".
     */
    protected final static String DEFAULT_EXTENSION_SEPARATOR = ".";

    /**
     * Default message bundles file extension.
     */
    protected final static String DEFAULT_EXTENSION = DEFAULT_EXTENSION_SEPARATOR + "properties";

    /**
     * Packaging type of project
     *
     * @parameter expression="${project.packaging}"
     * @readonly
     */
    protected String packaging;

    /**
     * Language for message bundles.
     *
     * @parameter default-value="en"
     */
    protected String language;

    /**
     * MessageBundles file without extension.
     * Default packaging value = *Bundle.
     *
     * @parameter
     */
    protected String[] files = new String[] {"*Bundle"};

    /**
     * MessageBundles file extension.
     * Default packaging value = jar, war.
     *
     * @parameter
     */
    protected String[] packagings = new String[] {"jar", "war"};

    /**
     * Source directory.
     *
     * @parameter expression="${project.build.sourceDirectory}"
     */
    protected File sourceDirectory;

    /**
     * Output directory.
     *
     * @parameter expression="${project.build.outputDirectory}"
     */
    protected File outputDirectory;
    
    /**
     * Create new file in the <code>directory</code> with <code>file</code>.
     *
     * @param directory destination directory
     * @param file old file directory with path
     * @return new file from <code>directory</code> and <code>file</code>
     */
    protected File getFileFromDirectory(File directory, String file) {
        try {
           return new File(directory.getCanonicalPath() + File.separatorChar + file);
        } catch (Exception ex) {
            getLog().warn("File " + file + " could not be resolved from directory " + directory, ex);
        }
        return null;
    }

    /**
     * Remove language from <code>name</code>
     * Example: DataMessageBundles_en.properties - DataMessageBundles.properties
     *
     * @param name old name with the language.
     * @return new name without the language.
     */
    protected String removeLanguage(String name) {
        try {
            String result = name.substring(0, name.indexOf(MessageBundlesPlugin.BUNDLES_SEPARATORCHAR));
            return result + MessageBundlesPlugin.DEFAULT_EXTENSION;
        } catch (Exception ex) {
            getLog().error("Error by create new file name.", ex);
        }
        return null;
    }


    /**
     * Add <code>language</code> to the <code>name</code>
     * Example: input "DataMessageBundles.properties", "en" return "DataMessageBundles_en.properties"
     *
     * @param name old name without the language.
     * @return new name with the language.
     */
    protected String addLanguage(String name) {
        try {
            int index = name.indexOf(MessageBundlesPlugin.DEFAULT_EXTENSION_SEPARATOR);
            return name.substring(0,index) + MessageBundlesPlugin.BUNDLES_SEPARATORCHAR +  language + MessageBundlesPlugin.DEFAULT_EXTENSION;
        } catch (Exception ex) {
            getLog().error("Error by create new file name.", ex);
        }
        return null;
    }

    /**
     * Create includes array with the language in the filename.
     *
     * @return new includes array with laguage in the filename.
     */
    protected String[] createIncludesFilesWithLanguage() {
        String[] result = new String[files.length];
        for(int index=0; index<files.length; index++) {
            result[index] = MessageBundlesPlugin.DEFAULT_INCLUDES + files[index] + MessageBundlesPlugin.BUNDLES_SEPARATORCHAR + language + MessageBundlesPlugin.DEFAULT_EXTENSION;
        }
        return result;
    }

    /**
     * Create includes array without the language in the filename.
     *
     * @return new includes array without laguage in the filename.
     */
    protected String[] createIncludesFilesWithoutLanguage() {
        String[] result = new String[files.length];
        for(int index=0; index<files.length; index++) {
            result[index] = MessageBundlesPlugin.DEFAULT_INCLUDES + files[index] + MessageBundlesPlugin.DEFAULT_EXTENSION;
        }
        return result;
    }
}
