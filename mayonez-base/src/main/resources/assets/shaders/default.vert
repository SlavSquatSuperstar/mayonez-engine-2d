/* Default sprite vertex shader */

#version 330 core

layout (location = 0) in vec3 vPosition;  // Global position
layout (location = 1) in vec4 vColor;     // Color
layout (location = 2) in vec2 vTexCoords; // Texture coordinates (UV)
layout (location = 3) in float vTexID;    // Texture slot in uTextures[]

uniform mat4 uTransform; // Model-view-projection transform

out vec4 fColor;
out vec2 fTexCoords;
out float fTexID;

void main()
{
    fColor = vColor;
    fTexCoords = vTexCoords;
    fTexID = vTexID;

    gl_Position = uTransform * vec4(vPosition, 1.0);
}