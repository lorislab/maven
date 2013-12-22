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

/**
 * The system utility class.
 *
 * @author Andrej Petras <andrej@ajka-andrej.com>
 */
public final class SystemUtils {

    /**
     * Returns <code>true</code> for MacOs else <code>false</code>.
     *
     * @return <code>true</code> if system is MacOS else <code>false</code>.
     */
    public static boolean isOsX() {
        return System.getProperty( "mrj.version" ) != null;
    }
}
