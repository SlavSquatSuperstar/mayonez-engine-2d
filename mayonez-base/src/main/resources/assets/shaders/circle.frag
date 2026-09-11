/* Circle fragment shader */

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