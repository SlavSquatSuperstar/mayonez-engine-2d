package mayonez.renderer.shader;

import mayonez.assets.*;
import mayonez.assets.text.*;

import java.io.IOException;
import java.util.*;
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
     * Parses multiple shaders stage from the given GLSL source code.
     *
     * @param source the shader source code
     * @return the shader stages
     * @throws ShaderException if no stages were found
     */
    static List<ShaderStage> parseShaderStages(String source) throws ShaderException {
        List<ShaderStage> stages = new ArrayList<>();

        var matcher = getHeaderMatcher(source);
        var lastMatch = matcher.find();
        while (lastMatch) {
            var typeName = matcher.group(1); // Query the (\w+) capture group
            var shaderType = ShaderType.findWithName(typeName);

            var bodyStart = matcher.end() + 1;
            lastMatch = matcher.find(); // Check if there is another stage
            var bodyEnd = lastMatch
                    ? matcher.start() // Another stage
                    : source.length(); // No more stages

            var body = source.substring(bodyStart, bodyEnd).strip();
            stages.add(new ShaderStage(body, shaderType));
        }

        if (stages.isEmpty()) {
            throw new ShaderException("No shaders found in source file");
        }
        return stages;
    }

    /**
     * Parses one shader stage from the given GLSL source code.
     *
     * @param stageSource the stage source code
     * @return the shader stage
     * @throws ShaderException if no stage was found
     */
    static ShaderStage parseShaderStage(String stageSource) throws ShaderException {
        var matcher = getHeaderMatcher(stageSource);
        if (!matcher.find()) {
            throw new ShaderException("No #type directive at shader start");
        }

        var typeName = matcher.group(1); // Query the (\w+) capture group
        var shaderType = ShaderType.findWithName(typeName);
        var body = stageSource.substring(matcher.end() + 1).strip();
        return new ShaderStage(body, shaderType);
    }

    private static Matcher getHeaderMatcher(String stageSource) {
        /*
         * Look for header "\n # type <shader_type> \n"
         * All spaces optional except between type and <shader_type>
         * First newline optional
         * Use multiline mode so ^ and $ mean line boundaries
         */
        var headerPat = Pattern.compile("^\\s*#\\s*type\\s+(\\w+)\\s*$", Pattern.MULTILINE);
        return headerPat.matcher(stageSource);
    }

}
