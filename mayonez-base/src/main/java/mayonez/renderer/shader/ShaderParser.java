package mayonez.renderer.shader;

import mayonez.assets.*;
import mayonez.assets.text.*;

import java.io.IOException;
import java.util.*;

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
//        return source.splitWithDelimiters("(#type)( )+", 0);
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

}
