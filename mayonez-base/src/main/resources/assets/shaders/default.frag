/* Default sprite fragment shader */

#version 400 core

in vec4 fColor;     // Color
in vec2 fTexCoords; // Texture coordinates (UV)
in float fTexID;    // Texture slot in uTextures[]

uniform sampler2D uTextures[8]; // OpenGL texture IDs

out vec4 color; // Output to framebuffer

void main()
{
    // Map texture slots 1–8 to indices 0–7
    // Map no texture to index -1
    int texIdx = int(fTexID) - 1;

    // Apply color to texture or draw plain color
    /*
     * Note: Indexing sampler2D arrays with non-constant expressions (i.e. a
     * vertex attribute) on GLSL versions 3.3 and earlier is not allowed on
     * some platforms.
     *
     * Known platforms to produce an compilation error are AMD (any OS) and
     * macOS (ARM64). The workaround is to use a if-else/switch statement.
     * Indexing with constants on Windows GLSL 3.3 also seems to only return
     * the first texture.
     */
    if (texIdx >= 0) {
        color = fColor * texture(uTextures[texIdx], fTexCoords);
    } else {
        color = fColor;
    }
}