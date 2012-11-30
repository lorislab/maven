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
package com.ajkaandrej.maven.plugin.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;

/**
 * The utility class for the filtering process.
 *
 * @author Andrej Petras <andrej@ajka-andrej.com>
 */
public final class FilterUtils {

    /**
     * Filter source file and save to the destination file.
     *
     * @param project the maven project.
     * @param source the source file.
     * @param dest the destination file.
     *
     * @throws java.io.IOException
     * @throws org.apache.maven.plugin.MojoExecutionException
     */
    public static void filter( MavenProject project, File source, File dest ) throws IOException, MojoExecutionException {
        Properties filterProperties = new Properties( System.getProperties() );
        filterProperties.putAll( project.getProperties() );
        filter(filterProperties,source,dest);
    }

    /**
    * Filter source file and save to the destination file.
    *
    * @param filterProperties the properties.
    * @param source the source file.
    * @param dest the destination file.
    *
    * @throws java.io.IOException
    * @throws org.apache.maven.plugin.MojoExecutionException
    */
    public static void filter( Properties filterProperties, File source, File dest ) throws IOException, MojoExecutionException {

        String rawContents = readFile(source);
        Reader reader  = new BufferedReader(new StringReader(rawContents));

        // support ${token}
        reader = new InterpolationFilterReader( reader, filterProperties, "${", "}" );

        // support @token@
        reader = new InterpolationFilterReader( reader, filterProperties, "@", "@" );

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter( dest ); // platform encoding
            IOUtil.copy( reader, fileWriter );
        }
        finally {
            IOUtil.close( fileWriter );
        }
    }

    /**
     * Read file content, using platform encoding.
     *
     * @param source the file to read
     * @return the file content, read with platform encoding
     */
    private static String readFile( File source ) throws IOException {
        FileReader fileReader = null;
        StringWriter contentWriter = new StringWriter();

        try {
            fileReader = new FileReader( source ); // platform encoding
            IOUtil.copy( fileReader, contentWriter );
        } finally {
            IOUtil.close( fileReader );
        }
        return contentWriter.toString();
    }
}
