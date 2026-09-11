/* Debug shape vertex shader */

#type vertex
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

/* Debug shape fragment shader */

#type fragment
#version 330 core

in vec4 fColor; // Color

out vec4 color; // Output to framebuffer

void main()
{
    color = fColor;
}