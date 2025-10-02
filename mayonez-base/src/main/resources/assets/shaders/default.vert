// Sprites with texture and color (GLSL 3.3/4.0)

#type vertex
#version 330 core

layout (location=0) in vec3 aPosition;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexID;

uniform mat4 uTransform;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexID;

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexID = aTexID;

    gl_Position = uTransform * vec4(aPosition, 1.0);
}
