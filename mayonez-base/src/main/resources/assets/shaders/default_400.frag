// Sprites with texture and color (GLSL 4.0)

#type fragment
#version 400 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexID;

uniform sampler2D uTextures[8];

out vec4 color;

void main()
{
    int texIdx = int(fTexID) - 1;

    // Apply color to texture
    if (texIdx >= 0 && texIdx < uTextures.length())
    {
        /*
         * Note: Indexing sampler2D arrays with non-constant expressions on
         * GLSL versions 3.3 and earlier is not allowed on some platforms.
         *
         * Known platforms to give an compilation error are AMD and macOS (ARM64).
         * The workaround is to use a if-else/switch statement.
         */
        color = fColor * texture(uTextures[texIdx], fTexCoords);
    }
    // Draw plain color
    else
    {
        color = fColor;
    }
}