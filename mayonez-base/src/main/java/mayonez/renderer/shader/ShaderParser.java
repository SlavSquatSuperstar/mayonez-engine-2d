package mayonez.renderer.shader;

final class ShaderParser {

    private ShaderParser() {
    }

    // Split program source into stage sources
    static String[] splitShaderSource(String source) {
        // Shaders indicated by "#type <shader_type>"
        // Must match whitespace exactly (for now)
        // This is not valid a GLSL directive, just a convention
        return source.split("(#type)( )+");
//        return source.splitWithDelimiters("(#type)( )+", 0);
    }

    // Get stage from source code, null if empty
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
