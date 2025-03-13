#type vertex
#version 400 core

// Circles with 4 vertices and a color

layout (location=0) in vec3 aPosition;
layout (location=1) in vec2 aLocalPosition;
layout (location=2) in vec4 aColor;
layout (location=3) in float aStroke;
layout (location=4) in float aFill;

uniform mat4 uView;
uniform mat4 uProjection;

out vec4 fColor;
out vec2 fLocalPosition;
out float fStroke;
out float fFill;

void main()
{
    fLocalPosition = aLocalPosition;
    fColor = aColor;
    fStroke = aStroke;
    fFill = aFill;

    gl_Position = uProjection * uView * vec4(aPosition, 1.0);
}

#type fragment
#version 400 core

in vec2 fLocalPosition;
in vec4 fColor;
in float fStroke;
in float fFill;

out vec4 color;

void main()
{
    float rad = 1.0;

    if (fFill > 0.5) { // Solid
        float distSq = dot(fLocalPosition, fLocalPosition);
        if (distSq >= rad) {
            discard; // Pixel is outside of circle
        }
    } else { // Outline
        // Handle stroke
        float dist = length(fLocalPosition);
        if (dist >= rad || dist <= rad - fStroke) {
            discard; // Pixel is outside of border
        }
    }

    // Set output color
    color = fColor;
}