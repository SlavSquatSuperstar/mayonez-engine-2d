#type vertex
#version 330 core

// Circles with 4 vertices and a color

layout (location=0) in vec3 aPosition;
layout (location=1) in vec2 aLocalPosition;
layout (location=2) in vec4 aColor;
layout (location=3) in float aInnerRadius;

uniform mat4 uViewProjection;

out vec2 fLocalPosition;
out vec4 fColor;
out float fInnerRadius;

void main()
{
    fLocalPosition = aLocalPosition;
    fColor = aColor;
    fInnerRadius = aInnerRadius;

    gl_Position = uViewProjection * vec4(aPosition, 1.0);
}

#type fragment
#version 330 core

in vec2 fLocalPosition;
in vec4 fColor;
in float fInnerRadius;

out vec4 color;

void main()
{
    // Outer radius is always circle radius
    float outerRadiusSq = 1.0;

    // Circle/Annulus Equation: r_i^2 ≤ x^2 + y^2 ≤ r_o^2
    float distSq = dot(fLocalPosition, fLocalPosition);

    // Check pixel is between outer and inner radii
    if (distSq > outerRadiusSq || distSq < fInnerRadius * fInnerRadius) {
        discard;
    }

    // Set output color
    color = fColor;
}