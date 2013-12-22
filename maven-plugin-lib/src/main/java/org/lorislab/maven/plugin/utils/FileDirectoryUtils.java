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
package org.lorislab.maven.plugin.utils;

import java.io.File;
import java.io.IOException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Utility class for files and directories.
 * 
 * @author Andrej Petras <andrej@ajka-andrej.com>
 */
public class FileDirectoryUtils {

    /**
     * Copy file <code>source</code> to the new file <code>destination</code>
     *
     * @param source file with should be copy.
     * @param destination file.
     * @return <code>true</code> if the new file whas created or updated.
     * @throws MojoExecutionException unabled to copy the file.
     */
    public static boolean copyFileIfChanged(File source, File destination) throws IOException {
        if (source == null || destination == null) {
            return false;
        }

        boolean result = false;
        try {
            result = FileUtils.copyFileIfModified(source, destination);
            if (!result && source.length() != destination.length()) {
                FileUtils.copyFile(source, destination);
                result = true;
            }
        } catch (Exception ex) {
            throw new IOException("Unabled to copy file: " + source + " to file: " + destination, ex);
        }
        return result;
    }
}
