/* Ellipse fragment shader */

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