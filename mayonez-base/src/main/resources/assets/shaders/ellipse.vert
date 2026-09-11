/* Ellipse vertex shader */

#version 330 core

layout (location = 0) in vec3 vPosition;      // Global position
layout (location = 1) in vec2 vLocalPosition; // Position on unit circle
layout (location = 2) in vec4 vColor;         // Color
layout (location = 3) in vec2 vInnerRadius;   // Relative to outer axes

uniform mat4 uTransform; // Model-view-projection transform

out vec2 fLocalPosition; // Position on unit circle
out vec4 fColor;         // Color
out vec2 fInnerRadius;   // Relative to outer axes

void main()
{
    fLocalPosition = vLocalPosition;
    fColor = vColor;
    fInnerRadius = vInnerRadius;

    gl_Position = uTransform * vec4(vPosition, 1.0);
}