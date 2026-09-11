/* Ellipse fragment shader */

#type vertex
#version 330 core

layout (location = 0) in vec3 vPosition;      // Global position
layout (location = 1) in vec2 vLocalPosition; // Position on unit circle
layout (location = 2) in vec4 vColor;         // Color
layout (location = 3) in vec2 vInnerRadius;   // Relative to outer axes

uniform mat4 uTransform; // Model-view-projection transform

out vec2 fLocalPosition; // Position on unit circle
out vec4 fColor;         // Color
out vec2 fInnerRadius;   // Relative to outer axes

void main()
{
    fLocalPosition = vLocalPosition;
    fColor = vColor;
    fInnerRadius = vInnerRadius;

    gl_Position = uTransform * vec4(vPosition, 1.0);
}

/* Ellipse vertex shader */

#type fragment
#version 330 core

in vec2 fLocalPosition; // Position on unit circle
in vec4 fColor;         // Color
in vec2 fInnerRadius;   // Relative to outer axes

out vec4 color; // Output to framebuffer

void main()
{
    // Use semi axes instead of radius
    // Inner axes are percents of outer axes
    const float radiusSq = 1.0;

    // Ellipse Equations:
    // (x / a_i)^2 + (y / b_i)^2 ≥ 1
    // (x / a_o)^2 + (y / b_o)^2 ≤ 1
    float outerDistSq = dot(fLocalPosition, fLocalPosition);
    vec2 innerPosition = fLocalPosition / fInnerRadius;
    float innerDistSq = dot(innerPosition, innerPosition);

    // Check pixel is between outer and inner radii
    if (outerDistSq > radiusSq || innerDistSq < radiusSq) {
        discard;
    }

    color = fColor;
}