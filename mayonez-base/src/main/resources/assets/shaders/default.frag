/* Default sprite fragment shader */

#version 330 core

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
     */
    switch (texIdx) {
        case 0: color = fColor * texture(uTextures[0], fTexCoords);
            break;
        case 1: color = fColor * texture(uTextures[1], fTexCoords);
            break;
        case 2: color = fColor * texture(uTextures[2], fTexCoords);
            break;
        case 3: color = fColor * texture(uTextures[3], fTexCoords);
            break;
        case 4: color = fColor * texture(uTextures[4], fTexCoords);
            break;
        case 5: color = fColor * texture(uTextures[5], fTexCoords);
            break;
        case 6: color = fColor * texture(uTextures[6], fTexCoords);
            break;
        case 7: color = fColor * texture(uTextures[7], fTexCoords);
            break;
        default : color = fColor;
    }
}