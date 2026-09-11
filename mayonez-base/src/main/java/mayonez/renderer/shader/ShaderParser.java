package mayonez.renderer.shader;

import mayonez.assets.*;
import mayonez.assets.text.*;

import java.io.IOException;
import java.util.regex.*;

final class ShaderParser {

    private ShaderParser() {
    }

    /**
     * Read the GLSL source code from a shader file.
     *
     * @param shaderFile the shader file path
     * @return the source code
     * @throws ShaderException if the shader file could not be read
     */
    static String readShaderSource(FilePath shaderFile) throws ShaderException {
        try (var input = shaderFile.openInputStream()) {
            return TextIOUtils.readText(input);
        } catch (IOException e) {
            throw new ShaderException("Could not read shader source: \"%s\""
                    .formatted(shaderFile.getFilename()));
        }
    }

    /**
     * Parses one GLSL shader from the given filename.
     *
     * @param filename the stage filename
     * @return the shader stage
     * @throws ShaderException if no stage was found
     */
    static ShaderStage parseShaderStage(String filename) throws ShaderException {
        var source = ShaderParser.readShaderSource(FilePath.fromFilename(filename));

        var matcher = getHeaderMatcher(source);
        if (!matcher.find()) {
            throw new ShaderException("No #type directive at shader start");
        }

        var typeName = matcher.group(1); // Query the (\w+) capture group
        var shaderType = ShaderType.findWithName(typeName);
        var body = source.substring(matcher.end() + 1).strip();
        return new ShaderStage(filename, body, shaderType);
    }

    private static Matcher getHeaderMatcher(String stageSource) {
        /*
         * Look for header " # type <shader_type>"
         * All whitespaces optional except between type and <shader_type>
         * Use multiline mode so ^ and $ mean line boundaries
         */
        var headerPat = Pattern.compile("^\\s*#\\s*type\\s+(\\w+)\\s*$", Pattern.MULTILINE);
        return headerPat.matcher(stageSource);
    }

}
