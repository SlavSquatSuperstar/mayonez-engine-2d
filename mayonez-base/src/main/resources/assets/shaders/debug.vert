/* Debug shape vertex shader */

#version 330 core

layout (location = 0) in vec3 vPosition; // Global position
layout (location = 1) in vec4 vColor;    // Color

uniform mat4 uTransform; // Model-view-projection transform

out vec4 fColor;

void main()
{
    fColor = vColor;
    gl_Position = uTransform * vec4(vPosition, 1.0);
}