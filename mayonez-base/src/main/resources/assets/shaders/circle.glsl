#type vertex
#version 400 core

// Circles with 4 vertices and a color

layout (location=0) in vec3 aPosition;
layout (location=1) in vec2 aLocalPosition;
layout (location=2) in vec4 aColor;
layout (location=3) in float aRadius;

uniform mat4 uView;
uniform mat4 uProjection;

out vec4 fColor;
out vec2 fLocalPosition;
out float fRadius;

void main()
{
    fLocalPosition = aLocalPosition;
    fColor = aColor;
    fRadius = aRadius;

    gl_Position = uProjection * uView * vec4(aPosition, 1.0);
}

#type fragment
#version 400 core

in vec2 fLocalPosition;
in vec4 fColor;
in float fRadius;

out vec4 color;

void main()
{
    // Calculate distance
    float rad = 1.0;
    float distSq = dot(fLocalPosition, fLocalPosition);
    if (distSq >= rad) {
        discard;// Pixel is outside of circle
    }

    // Set output color
    color = fColor;
}