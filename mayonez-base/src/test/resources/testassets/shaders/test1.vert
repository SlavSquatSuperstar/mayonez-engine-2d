// Simple vertex shader

#version 330 core

layout (location = 0) in vec3 vPosition;
layout (location = 1) in vec4 vColor;
layout (location = 2) in vec2 vTexCoords;

// Parse uniforms
uniform mat4 uTransform;
uniform float uTransformMultiplier;

out vec4 fColor;
out vec2 fTexCoords;

void main()
{
    fColor = vColor;
    fTexCoords = vTexCoords;
    gl_Position = uTransformMultiplier * uTransform * vec4(vPosition, 1.0);
}
