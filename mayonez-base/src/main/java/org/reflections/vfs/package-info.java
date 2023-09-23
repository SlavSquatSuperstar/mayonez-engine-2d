/**
 * A modified version of ronmamo's <a href = https://github.com/ronmamo/reflections>
 * org.reflections</a> library, used for scanning classpath resources inside a jar.
 * Reflections was originally provided under the
 * <a href = http://www.wtfpl.net/about/>WTFPL v2</a> and
 * <a href = https://apache.org/licenses/LICENSE-2.0>Apache v2.0</a> licenses.
 * A copy of both the licenses are available in the source in the resources package
 * {@code org.reflections.vfs}.
 * <br>
 * The changes to the library are as follows:
 * <ul>
 *     <li>Included only the {@code org.reflections.vfs} package.</li>
 *     <li>Removed all UrlTypes except for {@code directory} and {@code jarInput}.</li>
 *     <li>Extracted inner classes to top-level.</li>
 *     <li>Re-licensed copied code under GPLv3 like the rest of the engine</li>
 * </ul>
 * <br>
 * This package is not exported to the public API.
 *
 * @author ronmamo
 * @author SlavSquatSuperstar
 */
package org.reflections.vfs;