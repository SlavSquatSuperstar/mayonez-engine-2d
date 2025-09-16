#type vertex
#version 400 core

// Sprites with 4 vertices and a texture (GLSL 4.0)

layout (location=0) in vec3 aPosition;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexID;

uniform mat4 uViewProjection;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexID;

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexID = aTexID;

    gl_Position = uViewProjection * vec4(aPosition, 1.0);
}

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