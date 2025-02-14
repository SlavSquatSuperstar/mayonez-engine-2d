#type vertex
#version 400 core

// UI sprites with 4 vertices and a texture

layout (location=0) in vec3 aPosition;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexID;

uniform mat4 uProjection;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexID;

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexID = aTexID;

    gl_Position = uProjection * vec4(aPosition, 1.0);
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
    // Apply color to texture
    if (fTexID > 0)
    {
        color = fColor * texture(uTextures[int(fTexID) - 1], fTexCoords);
    }
    // Draw plain color
    else
    {
        color = fColor;
    }
}