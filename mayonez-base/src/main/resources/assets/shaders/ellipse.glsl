// Ellipses with color, radius, and fill

#type vertex
#version 330 core

layout (location=0) in vec3 aPosition;
layout (location=1) in vec2 aLocalPosition;
layout (location=2) in vec4 aColor;
layout (location=3) in vec2 aInnerRadius;

uniform mat4 uTransform;

out vec2 fLocalPosition;
out vec4 fColor;
out vec2 fInnerRadius;

void main()
{
    fLocalPosition = aLocalPosition;
    fColor = aColor;
    fInnerRadius = aInnerRadius;

    gl_Position = uTransform * vec4(aPosition, 1.0);
}

#type fragment
#version 330 core

in vec2 fLocalPosition;
in vec4 fColor;
in vec2 fInnerRadius;

out vec4 color;

void main()
{
    float radiusSq = 1.0;

    // Ellipse Equations:
    // (x/a_i)^2 + (y/b_i)^2 ≥ 1
    // (x/a_o)^2 + (y/b_o)^2 ≤ 1
    float outerDistSq = dot(fLocalPosition, fLocalPosition);
    vec2 innerPosition = fLocalPosition / fInnerRadius;
    float innerDistSq = dot(innerPosition, innerPosition);

    // Check pixel is between outer and inner radii
    if (outerDistSq > radiusSq|| innerDistSq < radiusSq) {
        discard;
    }

    // Set output color
    color = fColor;
}