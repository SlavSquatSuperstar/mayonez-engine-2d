/* Circle vertex shader */

#type vertex
#version 330 core

layout (location = 0) in vec3 vPosition;      // Global position
layout (location = 1) in vec2 vLocalPosition; // Position on unit circle
layout (location = 2) in vec4 vColor;         // Vertex color
layout (location = 3) in float vInnerRadius;  // Relative to outer radius

uniform mat4 uTransform; // Model-view-projection transform

out vec2 fLocalPosition; // Position on unit circle
out vec4 fColor;         // Color
out float fInnerRadius;  // Relative to outer radius

void main()
{
    fLocalPosition = vLocalPosition;
    fColor = vColor;
    fInnerRadius = vInnerRadius;

    gl_Position = uTransform * vec4(vPosition, 1.0);
}

/* Circle fragment shader */

#type fragment
#version 330 core

in vec2 fLocalPosition; // Position on unit circle
in vec4 fColor;         // Color
in float fInnerRadius;  // Relative to outer radius

out vec4 color; // Output to framebuffer

void main()
{
    // Outer radius is always equal to circle radius
    // Inner radius is percent of outer radius
    const float outerRadiusSq = 1.0;

    // Circle/Annulus Equation: r_i^2 ≤ x^2 + y^2 ≤ r_o^2
    float distSq = dot(fLocalPosition, fLocalPosition);

    // Check pixel is between outer and inner radii
    if (distSq > outerRadiusSq || distSq < fInnerRadius * fInnerRadius) {
        discard;
    }

    color = fColor;
}