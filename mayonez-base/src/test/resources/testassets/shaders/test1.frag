// Simple fragment shader

#version 330 core

in vec4 fColor;
in vec2 fTexCoords;

// Parse uniforms
uniform sampler2D uTexture;
uniform bool uUseTexture;

out vec4 color;

void main()
{
    if (uUseTexture)
    {
        color = fColor * texture(uTexture, fTexCoords);
    }
    else
    {
        color = fColor;
    }
}
