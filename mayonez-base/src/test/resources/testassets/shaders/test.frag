// Simple fragment shader

#version 330 core

in vec4 fColor;
in vec2 fTexCoords;

// Parse uniforms
uniform sampler2D uTextures[2];
uniform int uTexID;

out vec4 color;

void main()
{
    if (uTexID == 1)
    {
        color = fColor * texture(uTextures[0], fTexCoords);
    }
    else if (uTexID == 2)
    {
        color = fColor * texture(uTextures[1], fTexCoords);
    }
    else
    {
        color = fColor;
    }
}
