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

    static String[] splitShaderSource2(String source) {
        // Shaders indicated by "#type <shader_type>"
        // Must match whitespace exactly (for now)
        // This is not valid a GLSL directive, just a convention
        var split = source.splitWithDelimiters("#type", 0);
        // TODO Read first vs read all
        // TODO Read without splitting
        var sources = new String[split.length / 2];
        for (int i = 0; i < sources.length; i++) {
            sources[i] = split[1 + (2 * i)] + " " + split[1 + (2 * i + 1)];
        }
        return sources;
    }

    // Get all stages from source codes
    static List<ShaderStage> parseShaderStages(String[] stageSources) throws ShaderException {
        return Arrays.stream(stageSources)
                .map(ShaderParser::parseShaderStage)
                .filter(Objects::nonNull)
                .toList();
    }

    // Get one stage from source code, null if empty
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
        var stripped = stageSource.strip();
        if (stripped.isEmpty()) return null;

         /*
          * Look for header "\n # type <shader_type> \n"
          * All spaces optional except between type and <shader_type>
          * First newline optional
          * Use multiline mode so ^ and $ mean line boundaries
          */
        var headerPat = Pattern.compile("^\\s*#\\s*type\\s+(\\w+)\\s*$", Pattern.MULTILINE);
        var matcher = headerPat.matcher(stripped);
        if (!matcher.find()) {
            System.out.println("No match");
            throw new ShaderException("No #type directive at shader start");
        }

        var typeName = matcher.group(1); // Query the (\w+) capture group
        var shaderType = ShaderType.findWithName(typeName);
        var body = stripped.substring(matcher.end() + 1);
        return new ShaderStage(body, shaderType);
    }

}
