// Simple vertex shader with whitespace

#version 330 core

layout (location = 0) in vec3 vPosition;
layout (location = 1) in vec4 vColor;
layout (location = 2) in vec2 vTexCoords;

// Parse uniform
 uniform
mat4  uTransform  ;
uniform
 mat4  uTransformMultiplier  ;

out vec4 fColor;
out vec2 fTexCoords;

void main()
{
    fColor = vColor;
    fTexCoords = vTexCoords;
    gl_Position = uTransformMultiplier * uTransform * vec4(vPosition, 1.0);
}
