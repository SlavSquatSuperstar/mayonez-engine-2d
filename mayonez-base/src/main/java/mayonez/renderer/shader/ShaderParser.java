package mayonez.renderer.shader;

import mayonez.assets.*;
import mayonez.assets.text.*;

import java.io.IOException;
import java.util.*;
import java.util.regex.*;

final class ShaderParser {

    private ShaderParser() {
    }


    // Read source code from file
    static String readShaderSource(FilePath shaderFile) throws ShaderException {
        try (var input = shaderFile.openInputStream()) {
            return TextIOUtils.readText(input);
        } catch (IOException e) {
            throw new ShaderException("Could not read shader source: \"%s\""
                    .formatted(shaderFile.getFilename()));
        }
    }

    // Split program source into stage sources
    static String[] splitShaderSource(String source) {
        // Shaders indicated by "#type <shader_type>"
        // Must match whitespace exactly (for now)
        // This is not valid a GLSL directive, just a convention
        return source.split("(#type)( )+");
    }

    // Get all stages from source codes
    static List<ShaderStage> parseShaderStages(String[] stageSources) throws ShaderException {
        return Arrays.stream(stageSources)
                .map(ShaderParser::parseShaderStage)
                .filter(Objects::nonNull)
                .toList();
    }

    // Get many stages from source code
    static List<ShaderStage> parseShaderStages2(String source) throws ShaderException {
        var stages = new ArrayList<ShaderStage>();

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

    // Get one stage from source code
    static ShaderStage parseShaderStage(String stageSource) throws ShaderException {
        var stripped = stageSource.strip();
        if (stripped.isEmpty()) return null;

        var firstNewLine = stripped.indexOf("\n");
        var typeName = stripped.substring(0, firstNewLine).trim();

        var programSource = stripped.substring(firstNewLine + 1);
        var shaderType = ShaderType.findWithName(typeName);
        return new ShaderStage(programSource, shaderType);
    }

    static ShaderStage parseShaderStage2(String stageSource) throws ShaderException {
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
