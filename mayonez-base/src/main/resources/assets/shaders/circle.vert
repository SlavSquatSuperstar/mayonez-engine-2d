/* Circle vertex shader */

#version 330 core

layout (location = 0) in vec3 vPosition;      // Global position
layout (location = 1) in vec2 vLocalPosition; // Position on unit circle
layout (location = 2) in vec4 vColor;         // Vertex color
layout (location = 3) in float vInnerRadius;  // Relative to outer radius

uniform mat4 uTransform; // Model-view-projection transform

out vec2 fLocalPosition; // Position on unit circle
out vec4 fColor;         // Color
out float fInnerRadius;  // Relative to outer radius

void main()
{
    fLocalPosition = vLocalPosition;
    fColor = vColor;
    fInnerRadius = vInnerRadius;

    gl_Position = uTransform * vec4(vPosition, 1.0);
}