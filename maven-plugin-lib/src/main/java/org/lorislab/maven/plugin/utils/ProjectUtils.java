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

import java.util.Iterator;
import java.util.Properties;

/**
 * Utility class for MAVEN project data.
 *
 * @author Andrej Petras <andrej@ajka-andrej.com>
 */
public class ProjectUtils {

    /**
     * Check for packing type of project.
     * Char '*' is equals for all packages
     *
     * @param packaging of project
     * @param packagings list of packaging
     * @return <code>true</code> if the packing type of project is in the packings array.
     */
    public static boolean isPackaging(String packaging, String[] packagings) {
        boolean result = false;
        for (int i = 0; i<packagings.length && !result; i++) {
            result = result || packaging.equals(packagings[i]) || "*".equals(packagings[i]) ;
        }
        return result;
    }

    /**
     * Compare two properties for activation the plugin. Return <code>true</code>
     * if the are rules for activiting the plugin.
     *
     * @param plugin properties for activation.
     * @param project properties.
     * @return <code>true</code> for activation else <code>false</code>
     */
    public static boolean isActivation(Properties plugin, Properties project ) {
        boolean result = true;
        for(Iterator<Object> itr = plugin.keySet().iterator(); itr.hasNext() && result; ) {
           String key = (String)itr.next();
           if (key.startsWith("!")) {
               result = result && (project.getProperty(key.substring(1)) != null);
           } else {
               String property1 = project.getProperty(key);
               String property2 =plugin.getProperty(key);
               if (property1 != null && property2 != null) {
                   result = result && property1.equals(property2);
               } else {
                   result = result && (property1 == null && property2 == null);
               }
           }
        }

        return result;
    }
}
