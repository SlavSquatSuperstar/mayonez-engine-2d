#type vertex
#version 400 core

// Shapes made up of triangles

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec4 aColor;

uniform mat4 uView;
uniform mat4 uProjection;

out vec4 fColor;

void main()
{
    fColor = aColor;
    gl_Position = uProjection * uView * vec4(aPosition, 1.0);
}

#type fragment
#version 400 core

in vec4 fColor;

out vec4 color;

void main()
{
    color = fColor;
}